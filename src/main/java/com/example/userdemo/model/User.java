package com.example.userdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class User {
    private long id;

    private String email;

    private String firstName;

    private String lastName;

    private Date birthDate;

    private String address;

    private String phoneNumber;
}
