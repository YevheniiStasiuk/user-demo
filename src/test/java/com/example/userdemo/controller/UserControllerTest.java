package com.example.userdemo.controller;

import com.example.userdemo.dto.CreateUserDto;
import com.example.userdemo.dto.PatchUserDto;
import com.example.userdemo.dto.PutUserDto;
import com.example.userdemo.model.User;
import com.example.userdemo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static String defaultEmail;
    private static User defaultUser;
    private static List<User> defaultUserList;
    private static CreateUserDto defaultCreateUserDto;
    private static CreateUserDto badCreateUserDto;
    private static PatchUserDto defaultPatchUserDto;
    private static CreateUserDto badPatchUserDto;
    private static PutUserDto defaultPutUserDto;
    private static CreateUserDto badPutUserDto;

    @BeforeAll
    public static void setUp() {
        defaultEmail = "test@mail.com";

        defaultUser = new User(1,
                "test@mail.com",
                "Firstname",
                "Lastname",
                new Date(1003179600000L),
                "Test st.",
                "+380123456789");

        defaultUserList = List.of(defaultUser,
                new User(2,
                        "test2@mail.com",
                        "Firstname2",
                        "Lastname2",
                        new Date(1003179600000L),
                        "Test st.",
                        "+380123456789"),
                new User(3,
                        "test3@mail.com",
                        "Firstname3",
                        "Lastname3",
                        new Date(1003179600000L),
                        "Test st.",
                        "+380123456789"));

        defaultCreateUserDto = new CreateUserDto("test@mail.com",
                "Firstname",
                "Lastname",
                new Date(1003179600000L),
                "Test st.",
                "+380123456789");

        badCreateUserDto = new CreateUserDto("bad email",
                "Firstname",
                "Lastname",
                new Date(1003179600000L),
                "Test st.",
                "+380");

        defaultPatchUserDto = new PatchUserDto("Firstname",
                "Lastname",
                new Date(1003179600000L),
                "Test st.",
                "+380123456789");

        badPatchUserDto = new CreateUserDto("bad email",
                "Firstname",
                "Lastname",
                new Date(1003179600000L),
                "Test st.",
                "+380");

        defaultPutUserDto = new PutUserDto("Firstname",
                "Lastname",
                new Date(1003179600000L),
                "Test st.",
                "+380123456789");

        badPutUserDto = new CreateUserDto("bad email",
                "Firstname",
                "Lastname",
                new Date(1003179600000L),
                "Test st.",
                "+380");
    }

    @Test
    public void testPostUserSuccess() throws Exception {
        when(userService.createUser(any(CreateUserDto.class))).thenReturn(defaultUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrapToJson(defaultCreateUserDto)))
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testPostUserBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrapToJson(badCreateUserDto)))
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testPatchUserSuccess() throws Exception {
        when(userService.partlyUpdateUser(any(String.class), any(PatchUserDto.class))).thenReturn(defaultUser);

        mockMvc.perform(MockMvcRequestBuilders.patch("/user/" + defaultEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrapToJson(defaultPatchUserDto)))
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPatchUserBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrapToJson(badPatchUserDto)))
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    
    @Test
    public void testPutUserSuccess() throws Exception {
        when(userService.fullUpdateUser(any(String.class), any(PutUserDto.class))).thenReturn(defaultUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/" + defaultEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrapToJson(defaultPutUserDto)))
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPutUserBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrapToJson(badPutUserDto)))
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetUsersByBirthDateRangeSuccess() throws Exception {
        when(userService.getUsersByBirthDateRange(any(Date.class), any(Date.class))).thenReturn(defaultUserList);

        mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .param("from", "01-01-2000")
                        .param("to", "01-01-2002"))
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetUsersByBirthDateRangeBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .param("from", "01-01")
                        .param("to", "01-01-2002"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private String wrapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
