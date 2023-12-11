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
import ru.practicum.shareit.booking.dto.BookingDto2;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwners;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private BookingRepository bookingRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();
    }

    private User user = new User(1, "userName", "email");
    private boolean available = true;
    private ItemDto itemDto = new ItemDto(1, "itemName", "itemDescription", true, 0);
    private Item item = new Item(1, "itemName", "itemDescription", true, user, null);
    private Item itemUpd = new Item(1, "itemName2", "itemDescription2", true, user, null);
    private ItemDto itemUpdDto = new ItemDto(1, "itemName2", "itemDescription2", true, 0);
    private LocalDateTime start = LocalDateTime.of(2024, Month.APRIL, 8, 12, 30);
    private LocalDateTime end = LocalDateTime.of(2024, Month.APRIL, 12, 12, 30);
    private LocalDateTime start1 = LocalDateTime.of(2023, Month.APRIL, 8, 12, 30);
    private LocalDateTime end1 = LocalDateTime.of(2023, Month.APRIL, 12, 12, 30);
    CommentDto commentDto = new CommentDto(0L, "text", user.getId(), user.getName(), LocalDateTime.of(2023, 12, 10, 11, 30));
    List<CommentDto> comments = List.of(commentDto);
    ItemRequest request = new ItemRequest(1L, "description", user, start);
    BookingDto2 lastBooking = new BookingDto2(1L, start1, end1, user.getId());
    BookingDto2 nextBooking = new BookingDto2(2L, start, end, user.getId());
    ItemDtoForOwners itemDtoForOwners = new ItemDtoForOwners(1L, "name", "description",
            true, request.getId(), lastBooking, nextBooking, comments);
    Booking booking = new Booking(1L, start, end, item, user, BookingStatus.APPROVED);

    @Test
    public void create() throws Exception {

        String addItem = createItemDtoJson("itemName", "itemDescription", available);
        when(itemService.create(anyLong(), any())).thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", item.getOwner().getId())
                        .content(addItem)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("itemName"))
                .andExpect(jsonPath("$.description").value("itemDescription"))
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

    @Test
    public void findById() throws Exception {

        Mockito.when(itemService.findById(anyLong(), anyLong())).thenReturn(itemDtoForOwners);

        String itemForOunersUpdS = createItemForOunersJson("name", "description", lastBooking, nextBooking, true);

        mockMvc.perform(get("/items" + "/{itemId}", itemDtoForOwners.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .content(itemForOunersUpdS)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(available))
                .andExpect(jsonPath("$.lastBooking.id").value(1L))
                .andExpect(jsonPath("$.nextBooking.id").value(2L));
    }

    @Test
    public void getItemsByUserId() throws Exception {
        List<ItemDtoForOwners> allItemsDto = List.of(itemDtoForOwners);
        Mockito.when(itemService.getItemsByUserId(user.getId())).thenReturn(allItemsDto);
        String itemDtoForOwners = createItemForOunersJson("name", "description", lastBooking, nextBooking, true);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(List.of(itemDtoForOwners).toString())
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"))
                .andExpect(jsonPath("$[0].available").value(available))
                .andExpect(jsonPath("$[0].lastBooking.id").value(1L))
                .andExpect(jsonPath("$[0].nextBooking.id").value(2L));
    }

    @Test
    public void findItems() throws Exception {
        String text = "itemName";
        List<Item> items = List.of(item);
        Mockito.when(itemService.findItems(text)).thenReturn(items);
        Mockito.when(itemMapper.toItemDto(any())).thenReturn(itemDto);

        String allItemDto = createItemDtoJson("itemName", "itemDescription", true);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", user.getId())
                        .param("text", "itemName")
                        .content(List.of(allItemDto).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].name").value("itemName"))
                .andExpect(jsonPath("$[0].description").value("itemDescription"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    public static String createItemForOunersJson(String name, String description, BookingDto2 lastBooking, BookingDto2 nextBooking, boolean available) {
        return "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"description\": \"" + description + "\",\n" +
                "    \"lastBooking\": \"" + lastBooking + "\",\n" +
                "    \"nextBooking\": \"" + nextBooking + "\",\n" +
                "    \"available\": " + available + "\n" +
                "}";
    }

    public static String createItemDtoJson(String name, String description, boolean available) {
        return "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"description\": \"" + description + "\",\n" +
                "    \"available\": " + available + "\n" +
                "}";
    }
}