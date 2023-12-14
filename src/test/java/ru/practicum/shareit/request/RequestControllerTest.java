package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    User user = new User(1L, "userName", "user3@user.ru");
    ItemRequestDto itemRequestDto = new ItemRequestDto(1L, LocalDateTime.of(2023, Month.APRIL, 8, 12, 30), "desc", List.of());

    @Test
    public void findRequestByIdTest() throws Exception {
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

    @Test
    public void addRequestTest() throws Exception {
        ItemRequest itemRequest = new ItemRequest(1L, "desc", user, LocalDateTime.now().minusDays(1));

        Mockito.when(itemRequestService.addRequest(anyLong(), any())).thenReturn(itemRequest);

        String itemUpdS = createRequestDtoJson(1L, LocalDateTime.now().minusDays(1), "desc");

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(itemUpdS)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$..created[2]").value(LocalDateTime.now().minusDays(1).getDayOfMonth()))
                .andExpect(jsonPath("$.description").value("desc"));

        Mockito.verify(itemRequestService, times(1)).addRequest(anyLong(), any());
        Mockito.verifyNoMoreInteractions(itemRequestService);
    }

    @Test
    public void getRequestTest() throws Exception {
        Mockito.when(itemRequestService.getRequest(user.getId())).thenReturn(List.of(itemRequestDto));
        List<ItemRequestDto> result = itemRequestService.getRequest(user.getId());
        String itemUpdS = createRequestDtoJson(1L, LocalDateTime.of(2023, Month.APRIL, 8, 12, 30), "desc");
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(itemUpdS)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]id").value(result.get(0).getId()))
                .andExpect(jsonPath("$..created[2]").value(result.get(0).getCreated().getDayOfMonth()))
                .andExpect(jsonPath("$.[0]description").value(result.get(0).getDescription()));
    }

    @Test
    public void getAllRequestsTest() throws Exception {
        Mockito.when(itemRequestService.getAllRequests(anyLong(), any())).thenReturn(List.of(itemRequestDto));
        List<ItemRequestDto> result = itemRequestService.getAllRequests(user.getId(), PageRequest.of(0, 1, Sort.by("created").descending()));
        String itemUpdS = createRequestDtoJson(1L, LocalDateTime.of(2023, Month.APRIL, 8, 12, 30), "desc");
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0", "size", "1")
                        .content(itemUpdS)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]id").value(result.get(0).getId()))
                .andExpect(jsonPath("$..created[2]").value(result.get(0).getCreated().getDayOfMonth()))
                .andExpect(jsonPath("$.[0]description").value(result.get(0).getDescription()));
    }

    public static String createRequestDtoJson(long id, LocalDateTime created, String description) {
        return "{\n" +
                "    \"id\": \"" + id + "\",\n" +
                "    \"created\": \"" + created + "\",\n" +
                "    \"description\": \"" + description + "\"\n" +
                "}";
    }
}