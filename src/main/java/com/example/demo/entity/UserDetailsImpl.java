package com.example.demo.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final Users user;
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + role.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>(); //List인 이유 : 여러개의 권한을 가질 수 있다
        authorities.add(authority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
