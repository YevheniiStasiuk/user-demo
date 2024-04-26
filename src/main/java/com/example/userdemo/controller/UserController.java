package com.example.userdemo.controller;

import com.example.userdemo.dto.CreateUserDto;
import com.example.userdemo.dto.PatchUserDto;
import com.example.userdemo.dto.PutUserDto;
import com.example.userdemo.dto.ResponseUserDto;
import com.example.userdemo.model.User;
import com.example.userdemo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResponseUserDto> postUser(@RequestBody @Validated CreateUserDto createUserDto) {
        final User createdUser = userService.createUser(createUserDto);

        final ResponseUserDto responseUserDto = modelMapper.map(createdUser, ResponseUserDto.class);
        return new ResponseEntity<>(responseUserDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{email}")
    public ResponseEntity<ResponseUserDto> patchUser(@PathVariable String email,
                                                     @RequestBody @Validated PatchUserDto patchUserDto) {
        final User user = userService.partlyUpdateUser(email, patchUserDto);

        final ResponseUserDto responseUserDto = modelMapper.map(user, ResponseUserDto.class);
        return new ResponseEntity<>(responseUserDto, HttpStatus.OK);
    }

    @PutMapping("/{email}")
    public ResponseEntity<ResponseUserDto> putUser(@PathVariable String email,
                                                   @RequestBody @Validated PutUserDto putUserDto) {
        final User user = userService.fullUpdateUser(email, putUserDto);

        final ResponseUserDto responseUserDto = modelMapper.map(user, ResponseUserDto.class);
        return new ResponseEntity<>(responseUserDto, HttpStatus.OK);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDto>> getUsersByBirthDateRange(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date to) {
        final List<User> users = userService.getUsersByBirthDateRange(from, to);

        final List<ResponseUserDto> responseUserDtoList = users.stream()
                .map((user -> modelMapper.map(user, ResponseUserDto.class)))
                .toList();
        return new ResponseEntity<>(responseUserDtoList, HttpStatus.OK);
    }
}
