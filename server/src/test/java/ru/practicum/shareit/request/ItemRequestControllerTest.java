package ru.practicum.shareit.request;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.model.JsonModels.createRequestJson;
import static ru.practicum.shareit.model.JsonModels.requestDescription;
import static ru.practicum.shareit.model.TestModels.itemRequestDto;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemRequestControllerTest {

    private final String url = "/requests";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    @DisplayName("Получение всех запросов")
    public void getItemRequestsAll() throws Exception {
        when(itemRequestService.getAllRequests(anyInt(), anyInt(), anyInt())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get(url + "/all")
                        .header("X-Sharer-User-Id", itemRequestDto.getRequesterId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].requesterId").value(itemRequestDto.getRequesterId()))
                .andExpect(jsonPath("$[0].created").isNotEmpty());
    }

    @Test
    @DisplayName("Получение всех запросов от создавшего запрос")
    public void getItemRequestsForRequester() throws Exception {
        when(itemRequestService.getRequestsByRequester(anyInt())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get(url)
                        .header("X-Sharer-User-Id", itemRequestDto.getRequesterId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].requesterId").value(itemRequestDto.getRequesterId()))
                .andExpect(jsonPath("$[0].created").isNotEmpty());
    }

    @Test
    @DisplayName("Получение запроса по id")
    public void getItemRequestById() throws Exception {
        when(itemRequestService.getRequestById(anyInt(), anyInt())).thenReturn(itemRequestDto);

        mockMvc.perform(get(url + "/{id}", itemRequestDto.getId())
                        .header("X-Sharer-User-Id", itemRequestDto.getRequesterId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.requesterId").value(itemRequestDto.getRequesterId()))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }

    @Test
    @DisplayName("Создание запроса")
    public void addItemRequest() throws Exception {
        String requestJson = createRequestJson(requestDescription);

        when(itemRequestService.addRequest(any(), anyInt())).thenReturn(itemRequestDto);

        mockMvc.perform(post(url)
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", itemRequestDto.getRequesterId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.requesterId").value(itemRequestDto.getRequesterId()))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }

}