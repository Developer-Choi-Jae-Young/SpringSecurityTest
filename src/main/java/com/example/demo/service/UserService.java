package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UsersRepository usrRepository;

    public void CreateUser(Users usr)
    {
        usrRepository.save(usr);
    }
}
