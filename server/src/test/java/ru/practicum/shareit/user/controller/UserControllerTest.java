package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.user.dto.SavedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private final UserService service;
    private UserDto userExpected;

    @BeforeEach
    void testInit() {
        userExpected = new UserDto();
        userExpected.setId(1L);
        userExpected.setName("User1");
        userExpected.setEmail("test@test.ru");
    }

    @Test
    void createUser() throws Exception {

        long userId = userExpected.getId();
        SavedUserDto userSaveDto = new SavedUserDto();
        userSaveDto.setName(userExpected.getName());
        userSaveDto.setEmail(userExpected.getEmail());
        String userSaveDtoJson = objectMapper.writeValueAsString(userSaveDto);

        when(service.createUser(any(SavedUserDto.class)))
                .thenReturn(userExpected);
        String userDtoExpectedJson = objectMapper.writeValueAsString(userExpected);

        mockMvc.perform(post("/users")
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userSaveDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userDtoExpectedJson));

        verify(service, times(1)).createUser(any(SavedUserDto.class));
    }

    @Test
    void getUserTest() throws Exception {
        long userId = userExpected.getId();
        String path = "/users" + "/" + userId;

        when(service.getUser(eq(userId)))
                .thenReturn(userExpected);
        String userDtoExpectedJson = objectMapper.writeValueAsString(userExpected);

        mockMvc.perform(get(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDtoExpectedJson));

        verify(service, times(1)).getUser(eq(userId));
    }

    @Test
    void updateUserTest() throws Exception {
        long userId = userExpected.getId();
        SavedUserDto userSaveDtoForUpdate = new SavedUserDto();
        String nameUpdated = "test1";
        userSaveDtoForUpdate.setName(nameUpdated);
        String emailUpdated = "test1@test.ru";
        userSaveDtoForUpdate.setEmail(emailUpdated);
        String userSaveDtoForUpdateJson = objectMapper.writeValueAsString(userSaveDtoForUpdate);
        String path = "/users" + "/" + userId;

        when(service.updateUser(eq(userSaveDtoForUpdate), eq(userId)))
                .thenAnswer(invocationOnMock -> {
                    userExpected.setName(nameUpdated);
                    userExpected.setEmail(emailUpdated);
                    return userExpected;
                });
        mockMvc.perform(patch(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userSaveDtoForUpdateJson))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userExpected)));

        verify(service, times(1)).updateUser(eq(userSaveDtoForUpdate), eq(userId));
    }

    @Test
    void deleteUserTest() throws Exception {
        long userId = userExpected.getId();
        String path = "/users" + "/" + userId;
        mockMvc.perform(delete(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteUser(eq(userId));
    }
}
