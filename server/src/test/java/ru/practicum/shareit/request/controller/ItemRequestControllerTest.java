package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.SavedItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private final ItemRequestService service;
    private ItemRequestDto itemRequestExpected;

    @BeforeEach
    public void testInit() {
        itemRequestExpected = new ItemRequestDto();
        itemRequestExpected.setId(1);
        itemRequestExpected.setDescription("description");
    }

    @Test
    public void createItemRequestTest() throws Exception {
        long userId = 1;
        SavedItemRequestDto itemRequest = new SavedItemRequestDto();
        itemRequest.setDescription(itemRequestExpected.getDescription());
        String itemRequestJson = objectMapper.writeValueAsString(itemRequest);
        String itemRequestExpectedJson = objectMapper.writeValueAsString(itemRequestExpected);

        when(service.createItemRequest(any(Long.class), any(SavedItemRequestDto.class)))
                .thenReturn(itemRequestExpected);

        mockMvc.perform(post("/requests")
                    .header(RequestHttpHeaders.USER_ID, userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(itemRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(itemRequestExpectedJson));

        verify(service, times(1))
                .createItemRequest(any(Long.class), any(SavedItemRequestDto.class));
    }

    @Test
    void getAllUserItemRequestTest() throws Exception {
        long userId = 10;
        Collection<ItemRequestDto> itemRequestsExpected = List.of(itemRequestExpected);
        String itemRequestsExpectedJson = objectMapper.writeValueAsString(itemRequestsExpected);

        when(service.getAllUserItemRequests(eq(userId)))
                .thenReturn(itemRequestsExpected);

        mockMvc.perform(get("/requests")
                .header(RequestHttpHeaders.USER_ID, userId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(itemRequestsExpectedJson));

        verify(service, times(1)).getAllUserItemRequests(eq(userId));
    }

    @Test
    void getAllItemRequestsTest() throws Exception {
        long itemRequestId = itemRequestExpected.getId();
        String path = "/requests" + "/" + itemRequestId;
        String itemRequestExpectedJson = objectMapper.writeValueAsString(itemRequestExpected);

        when(service.getItemRequest(eq(itemRequestId)))
                .thenReturn(itemRequestExpected);

        mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(itemRequestExpectedJson));

        verify(service, times(1)).getItemRequest(eq(itemRequestId));
    }

    @Test
    void getAllTest() throws Exception {
        String path = "/requests" + "/" + "all";
        when(service.getAllItemRequests(10))
                .thenReturn(List.of());

        mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header(RequestHttpHeaders.USER_ID, 10))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).getAllItemRequests(10);
    }
}
