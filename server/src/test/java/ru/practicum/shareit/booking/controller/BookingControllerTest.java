package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.SavedBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final BookingService bookingService;

    @MockBean
    private final BookingService service;
    private BookingDto bookingExpected;

    @BeforeEach
    public void testInit() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(1);

        ItemDto item = new ItemDto();
        item.setId(1);
        item.setName("Test");
        item.setDescription("Test Object");
        item.setAvailable(true);

        UserDto booker = new UserDto();
        booker.setId(1L);
        booker.setName("Test Booker");
        booker.setEmail("test@test.ru");

        bookingExpected = new BookingDto();
        bookingExpected.setId(1);
        bookingExpected.setStart(start);
        bookingExpected.setEnd(end);
        bookingExpected.setStatus(BookingStatus.WAITING);
        bookingExpected.setItem(item);
        bookingExpected.setBooker(booker);

    }

    @Test
    void addBookingTest() throws Exception {
        long userId = 1;
        SavedBookingDto savedBookingDto = new SavedBookingDto();
        savedBookingDto.setItemId(1L);
        savedBookingDto.setStart(LocalDateTime.now());
        savedBookingDto.setEnd(LocalDateTime.now().plusMinutes(1));
        String savedBookingDtoJson = objectMapper.writeValueAsString(savedBookingDto);
        String bookingExpectedJson = objectMapper.writeValueAsString(bookingExpected);

        when(service.addBooking(eq(userId), any(SavedBookingDto.class)))
                .thenReturn(bookingExpected);
        mockMvc.perform(post("/bookings")
                        .header(RequestHttpHeaders.USER_ID, String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(savedBookingDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingExpectedJson));

        verify(service, times(1)).addBooking(eq(userId), any(SavedBookingDto.class));
    }

    @Test
    void manageBookingTest() throws Exception {
        long userId = 1;
        long bookingId = 1;
        boolean approved = true;
        String path = "/bookings/" + bookingId;

        when(service.manageBooking(eq(userId), eq(bookingId), eq(approved)))
                .thenAnswer(invocationOnMock -> {
                    bookingExpected.setStatus(BookingStatus.APPROVED);
                    return bookingExpected;
                });

        mockMvc.perform(patch(path)
                        .header(RequestHttpHeaders.USER_ID, String.valueOf(userId))
                        .param("approved", String.valueOf(approved))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(BookingStatus.APPROVED.name()));

        verify(service, times(1)).manageBooking(eq(userId), eq(bookingId), eq(approved));
    }

    @Test
    void getBookingTest() throws Exception {
        long userId = 1;
        long bookingId = 1;
        String path = "/bookings/" + bookingId;
        String bookingExpectedJson = objectMapper.writeValueAsString(bookingExpected);

        when(service.getBooking(eq(userId), eq(bookingId)))
                .thenReturn(bookingExpected);

        mockMvc.perform(get(path)
                        .header(RequestHttpHeaders.USER_ID, String.valueOf(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingExpectedJson));

        verify(service, times(1)).getBooking(eq(userId), eq(bookingId));
    }

    @Test
    void getAllUserBookingsTest() throws Exception {
        long userId = 1;
        BookingState state = BookingState.REJECTED;

        when(service.getAllUserBookings(eq(userId), eq(state)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/bookings")
                        .header(RequestHttpHeaders.USER_ID, String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("state", String.valueOf(state))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).getAllUserBookings(eq(userId), eq(state));
    }

    @Test
    void getAllUserItemsBookingsTest() throws Exception {
        long userId = 1;
        BookingState state = BookingState.WAITING;
        String path = "/bookings" + "/owner";
        List<BookingDto> expectedBookings = List.of(bookingExpected);
        String expectedBookingsJson = objectMapper.writeValueAsString(expectedBookings);

        when(service.getAllUserItemsBookings(eq(userId), eq(state)))
                .thenReturn(expectedBookings);
        mockMvc.perform(get(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .param("state", String.valueOf(state))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(content().json(expectedBookingsJson));

        verify(service, times(1)).getAllUserItemsBookings(eq(userId), eq(state));
    }
}
