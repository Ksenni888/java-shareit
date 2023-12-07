package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RequestControllerTest {
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .build();
    }

    @Test
    public void findRequestByIdTest() throws Exception {
        User user = new User(1L, "name", "mail@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L, "desc", user, LocalDateTime.of(2023, Month.APRIL, 8, 12, 30));
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, LocalDateTime.of(2023, Month.APRIL, 8, 12, 30), "desc", List.of());

        Mockito.when(itemRequestService.findRequestById(anyLong(), anyLong())).thenReturn(itemRequestDto);
        String itemUpdS = createRequestDtoJson(1L, LocalDateTime.of(2023, Month.APRIL, 8, 12, 30), "desc");

        mockMvc.perform(get("/requests" + "/{requestId}", itemRequestDto.getId())
                        .header("X-Sharer-User-Id", 1)
                        .content(itemUpdS)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$..created[1]").value(04))
                .andExpect(jsonPath("$.description").value("desc"));
    }

    public static String createRequestDtoJson(long id, LocalDateTime created, String description) {
        return "{\n" +
                "    \"id\": \"" + id + "\",\n" +
                "    \"created\": \"" + created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\",\n" +
                "    \"description\": \"" + description + "\"\n" +
                "}";
    }


}