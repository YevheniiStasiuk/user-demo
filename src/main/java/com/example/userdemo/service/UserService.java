package com.example.userdemo.service;

import com.example.userdemo.dto.CreateUserDto;
import com.example.userdemo.dto.PatchUserDto;
import com.example.userdemo.dto.PutUserDto;
import com.example.userdemo.exception.BadRequestException;
import com.example.userdemo.model.User;
import com.example.userdemo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapperDefault;
    private final ModelMapper modelMapperSkipNull;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapperDefault, ModelMapper modelMapperSkipNull) {
        this.userRepository = userRepository;
        this.modelMapperDefault = modelMapperDefault;
        this.modelMapperSkipNull = modelMapperSkipNull;
    }

    public User createUser(CreateUserDto createUserDto) {
        if (isUnderEighteen(createUserDto.getBirthDate())) {
            throw new BadRequestException("User have to be adult");
        }

        final User userToCreate = modelMapperDefault.map(createUserDto, User.class);
        return userRepository.create(userToCreate);
    }

    private boolean isUnderEighteen(Date userBirthDate) {
        final LocalDate birthLocalDate = userBirthDate.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        final LocalDate currentDate = LocalDate.now();

        final Period age = Period.between(birthLocalDate, currentDate);

        return age.getYears() < 18;
    }

    public User partlyUpdateUser(String email, PatchUserDto patchUserDto) {
        final User user = userRepository.getByEmail(email);
        modelMapperSkipNull.map(patchUserDto, user);
        return userRepository.update(user);
    }

    public User fullUpdateUser(String email, PutUserDto putUserDto) {
        final User user = userRepository.getByEmail(email);
        modelMapperDefault.map(putUserDto, user);
        user.setEmail(email);
        return userRepository.update(user);
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    public List<User> getUsersByBirthDateRange(Date from, Date to) {
        if (from.getTime() > to.getTime()) {
            throw new BadRequestException("From bigger than to");
        }

        return userRepository.getUsersByBirthDateRange(from, to);
    }
}
