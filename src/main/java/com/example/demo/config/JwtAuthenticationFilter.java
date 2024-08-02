package com.example.demo.config;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.UserDetailsImpl;
import com.example.demo.service.JwtProperties;
import com.example.demo.service.JwtToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private  final JwtToken jwtToken;
    private  final AuthenticationManager authenticationManager;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try{
            log.info("Authentication Filter");
            ObjectMapper om = new ObjectMapper();
            UserDto user = om.readValue(request.getInputStream(), UserDto.class);

            String username = user.getEmail();
            String password = user.getPassword();

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,password);

            return authenticationManager.authenticate(authRequest);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserDetailsImpl principalUserDetails = (UserDetailsImpl)authResult.getPrincipal();

        String username = principalUserDetails.getUsername();
        String access = jwtToken.createToken(authResult);

        response.addHeader(JwtProperties.HEADER_ACCESS_STRING, JwtProperties.TOKEN_PREFIX + access);
        setSuccessResponse(response, "로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        String failReason = failed.getMessage();
        setFailResponse(response, failReason);
    }

    private void setSuccessResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", true);
            jsonObject.put("code", 1);
            jsonObject.put("message", message);
            response.getWriter().print(jsonObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void setFailResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", false);
            jsonObject.put("code", -1);
            jsonObject.put("message", message);

            response.getWriter().print(jsonObject);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
