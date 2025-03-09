package ru.practicum.shareit.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.SavedBookingDto;
import ru.practicum.shareit.exception.ErrorHandler;
import ru.practicum.shareit.user.controller.UserController;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ErrorHandlerTest {
    private final ObjectMapper objectMapper;
    private final BookingController bookingController;
    private final UserController userController;

    @Test
    void handleNotFoundExceptionForItemTest() throws Exception {
        SavedBookingDto savedBookingDto = new SavedBookingDto();
        savedBookingDto.setItemId(555L);
        savedBookingDto.setStart(LocalDateTime.now());
        savedBookingDto.setEnd(LocalDateTime.now().plusMinutes(1));
        String savedBookingDtoJson = objectMapper.writeValueAsString(savedBookingDto);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new ErrorHandler())
                .build();

        mockMvc.perform(post("/bookings")
                        .header(RequestHttpHeaders.USER_ID, String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(savedBookingDtoJson))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        "{\"error\":\"Искомый объект не найден.\", \"message\": \"Предмет с ID 555 не найден.\"}"));
    }

    @Test
    void handlerNotFoundExceptionForUserTest() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ErrorHandler())
                .build();

        mockMvc.perform(get("/users/222")
                        .header(RequestHttpHeaders.USER_ID, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        "{\"error\":\"Искомый объект не найден.\", \"message\": \"Пользователь с ID 222 не найден.\"}"));
    }

    @Test
    void handleAccessDeniedExceptionTest() throws Exception {
        SavedBookingDto savedBookingDto = new SavedBookingDto();
        savedBookingDto.setItemId(10L);
        savedBookingDto.setStart(LocalDateTime.now());
        savedBookingDto.setEnd(LocalDateTime.now().plusMinutes(1));
        String savedBookingDtoJson = objectMapper.writeValueAsString(savedBookingDto);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new ErrorHandler())
                .build();

        mockMvc.perform(post("/bookings")
                        .header(RequestHttpHeaders.USER_ID, String.valueOf(999))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(savedBookingDtoJson))
                .andExpect(status().isForbidden())
                .andExpect(content().json(
                        "{\"error\":\"Доступ запрещен.\", \"message\": \"Пользователь с ID 999 не найден.\"}"));
    }

    @Test
    void handleValidationExceptionTest() throws Exception {
        SavedBookingDto savedBookingDto = new SavedBookingDto();
        savedBookingDto.setItemId(10L);
        savedBookingDto.setStart(LocalDateTime.now());
        savedBookingDto.setEnd(LocalDateTime.now().plusMinutes(1));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setControllerAdvice(new ErrorHandler())
                .build();

        mockMvc.perform(post("/bookings")
                        .header(RequestHttpHeaders.USER_ID, String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\": 50,\"start\":\"2024-12-11T22:27:47\",\"end\":\"2024-12-11T22:27:48\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(
                        "{\"error\":\"Неверный формат данных\", \"message\": \"Item Не доступно для бронирования\"}"));
    }

}
