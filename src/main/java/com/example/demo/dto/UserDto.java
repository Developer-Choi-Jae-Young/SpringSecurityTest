package com.example.demo.dto;

import com.example.demo.entity.Role;
import com.example.demo.entity.Users;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private String address;
    private String phone;
    private Role role;

    public Users toEntity()
    {
        return Users.builder().email(this.email).password(this.password).address(this.address).phone(this.phone).role(this.role).build();
    }
}
