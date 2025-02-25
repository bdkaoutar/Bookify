package com.kaoutar.bookify.Bookify.dto;

import com.kaoutar.bookify.Bookify.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class registerUserDto {

    private String username;
    private String email;
    private String password;
    //add role, first name, last name
    private Role role;
    private String firstName;
    private String lastName;
}
