package com.example.userdemo.service;

import com.example.userdemo.dto.CreateUserDto;
import com.example.userdemo.dto.PatchUserDto;
import com.example.userdemo.dto.PutUserDto;
import com.example.userdemo.exception.BadRequestException;
import com.example.userdemo.model.User;
import com.example.userdemo.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapperDefault;

    @Mock
    private ModelMapper modelMapperSkipNull;

    private static User defaultUser;
    private static User updatedUser;
    private static List<User> defaultUserList;
    private static CreateUserDto defaultCreateUserDto;
    private static CreateUserDto lowAgeCreateUserDto;
    private static PatchUserDto defaultPatchUserDto;
    private static PutUserDto defaultPutUserDto;
    private static Date from;
    private static Date to;

    @BeforeAll
    public static void setUp() {
        defaultUser = new User(1,
                "test@mail.com",
                "Firstname",
                "Lastname",
                new Date(1003179600000L),
                "Test st.",
                "+380123456789");

        updatedUser = new User(1,
                "test@mail.com",
                "Firstname2",
                "Lastname2",
                new Date(1003179600000L),
                "Test st.",
                "+380123456789");

        defaultUserList = List.of(defaultUser,
                new User(2,
                        "test2@mail.com",
                        "Firstname2",
                        "Lastname2",
                        new Date(1041372000000L),
                        "Test st.",
                        "+380123456789"),
                new User(3,
                        "test3@mail.com",
                        "Firstname3",
                        "Lastname3",
                        new Date(978300000000L),
                        "Test st.",
                        "+380123456789"));

        defaultCreateUserDto = new CreateUserDto("test@mail.com",
                "Firstname",
                "Lastname",
                new Date(1003179600000L),
                "Test st.",
                "+380123456789");

        lowAgeCreateUserDto = new CreateUserDto("test@mail.com",
                "Firstname",
                "Lastname",
                new Date(1577829600000L),
                "Test st.",
                "+380123456789");

        defaultPatchUserDto = new PatchUserDto("Firstname2",
                "Lastname2",
                null,
                null,
                null);

        defaultPutUserDto = new PutUserDto("Firstname2",
                "Lastname2",
                new Date(1003179600000L),
                "Test st.",
                "+380123456789");

        from = new Date(946677600000L);

        to = new Date(1072908000000L);
    }

    @Test
    public void testCreateUserSuccess() {
        when(modelMapperDefault.map(defaultCreateUserDto, User.class)).thenReturn(defaultUser);
        when(userRepository.create(any(User.class))).thenReturn(defaultUser);

        User user = userService.createUser(defaultCreateUserDto);

        assertNotNull(user);
        assertEquals(defaultUser, user);
    }

    @Test
    public void testCreateUserLowAge() {
        assertThrows(BadRequestException.class, () -> userService.createUser(lowAgeCreateUserDto));
    }

    @Test
    public void partlyUpdateUserSuccess() {
        when(userRepository.getByEmail(any(String.class))).thenReturn(updatedUser);
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);

        User user = userService.partlyUpdateUser("test@mail.com", defaultPatchUserDto);

        assertNotNull(user);
        assertEquals(updatedUser, user);
    }

    @Test
    public void fullUpdateUserSuccess() {
        when(userRepository.getByEmail(any(String.class))).thenReturn(updatedUser);
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);

        User user = userService.fullUpdateUser("test@mail.com", defaultPutUserDto);

        assertNotNull(user);
        assertEquals(updatedUser, user);
    }

    @Test
    public void getUsersByBirthDateRangeSuccess() {
        when(userRepository.getUsersByBirthDateRange(from, to)).thenReturn(defaultUserList);

        List<User> users = userService.getUsersByBirthDateRange(from, to);

        assertNotNull(users);
        assertEquals(defaultUserList, users);
    }

    @Test
    public void getUsersByBirthDateRangeFromBiggerThanTo() {
        assertThrows(BadRequestException.class, () -> userService.getUsersByBirthDateRange(to, from));
    }
}
