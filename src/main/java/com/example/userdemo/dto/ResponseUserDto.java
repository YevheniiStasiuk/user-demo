package com.example.userdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ResponseUserDto {
    private long id;

    private String email;

    private String firstName;

    private String lastName;

    private Date birthDate;

    private String address;

    private String phoneNumber;
}
