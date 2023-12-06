package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private MockMvc mockMvc;
    @Mock
    private ItemServiceImpl itemService;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();
    }

    public static final User user = new User(1, "userName", "email");
    public static final String itemName = "Дрель";
    public static final String itemDescription = "Простая дрель";
    public static final boolean available = true;
    public static final ItemDto itemDto = new ItemDto(1, itemName, itemDescription, available, 0);
    public static final Item item = new Item(1, itemName, itemDescription, true, user, null);
    public static final Item itemUpd = new Item(1, "itemName2", "itemDescription2", true, user, null);
    public static final ItemDto itemUpdDto = new ItemDto(1, "itemName2", "itemDescription2", true, 0);

    @Test
    public void create() throws Exception {

        String addItem = createItemDtoJson(itemName, itemDescription, available);
        when(itemService.create(anyLong(), any())).thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", item.getOwner().getId())
                        .content(addItem)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemName))
                .andExpect(jsonPath("$.description").value(itemDescription))
                .andExpect(jsonPath("$.available").value(available))
                .andExpect(jsonPath("$.requestId").value(0));
    }

    @Test
    public void updateItemTest() throws Exception {
        when(itemService.update(anyLong(), any(), anyLong())).thenReturn(itemUpd);
        Mockito.when(itemMapper.toItemDto(itemUpd)).thenReturn(itemUpdDto);
        String itemUpdS = createItemDtoJson("itemName2", "itemDescription2", true);

        mockMvc.perform(patch("/items" + "/{itemId}", itemUpd.getId())
                        .header("X-Sharer-User-Id", itemUpd.getOwner().getId())
                        .content(itemUpdS)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.name").value("itemName2"))
                .andExpect(jsonPath("$.description").value("itemDescription2"))
                .andExpect(jsonPath("$.available").value(available))
                .andExpect(jsonPath("$.requestId").value(0));

    }

    public static String createItemDtoJson(String name, String description, boolean available) {
        return "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"description\": \"" + description + "\",\n" +
                "    \"available\": " + available + "\n" +
                "}";
    }
}