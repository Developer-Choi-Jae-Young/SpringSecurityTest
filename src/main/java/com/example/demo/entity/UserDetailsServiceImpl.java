package com.example.demo.entity;

import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        return new UserDetailsImpl(user);
    }
}
