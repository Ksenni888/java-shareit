package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.model.JsonModels.*;
import static ru.practicum.shareit.model.TestModels.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    private final String url = "/items";

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Создание вещи")
    public void addItemTest() throws Exception {
        String addItem = createItemDtoJson(itemName, itemDescription, available);
        when(itemService.create(any(), anyInt())).thenReturn(itemDto);
        mockMvc.perform(post(url)
                        .header("X-Sharer-User-Id", item.getOwner().getId())
                        .content(addItem)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemName))
                .andExpect(jsonPath("$.description").value(itemDescription))
                .andExpect(jsonPath("$.available").value(available))
                .andExpect(jsonPath("$.requestId").isEmpty());
    }

    @Test
    @DisplayName("Обновление вещи")
    public void updateItemTest() throws Exception {
        when(itemService.update(any(), anyInt(), anyInt())).thenReturn(ItemMapper.toItemDto(itemUpd));

        String itemUpdS = createItemDtoJson(itemName2, itemDescription2, availableUpd);

        mockMvc.perform(patch(url + "/{itemId}", itemUpd.getId())
                        .header("X-Sharer-User-Id", itemUpd.getOwner().getId())
                        .content(itemUpdS)
                        .contentType(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.name").value(itemName2))
                .andExpect(jsonPath("$.description").value(itemDescription2))
                .andExpect(jsonPath("$.available").value(available))
                .andExpect(jsonPath("$.requestId").isEmpty());

    }

    @Test
    @DisplayName("Удаление вещи")
    public void deleteItemTest() throws Exception {
        mockMvc.perform(delete(url + "/{itemId}", 1)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение списка вещей")
    public void getItemsTest() throws Exception {
        when(itemService.getAllByOwner(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(new ItemBookingDto(1, itemName, itemDescription, user.getId(),
                        true, null, null, new ArrayList<>())));
        mockMvc.perform(get(url)
                        .header("X-Sharer-User-Id", 1
                        )
                        .param("from", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(itemName))
                .andExpect(jsonPath("$[0].description").value(itemDescription));

    }

    @Test
    @DisplayName("Получение вещи по id")
    public void getItemByIdTest() throws Exception {
            when(itemService.getItem(anyInt(), anyInt())).thenReturn(new ItemBookingDto(
                    1, itemName, itemDescription, user.getId(), available, null, null, null)
            );
        mockMvc.perform(get(url + "/{itemId}", 1)
                        .header("X-Sharer-User-Id", user.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value(itemName))
                    .andExpect(jsonPath("$.description").value(itemDescription));

    }

    @Test
    @DisplayName("Поиск вещей по описанию")
    public void searchItemsTest() throws Exception {
        when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(ItemMapper.toItemDtoList(List.of(item, itemUpd)));
        mockMvc.perform(get(url + "/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "дРель")
                        .param("from", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].name").value(itemName))
                .andExpect(jsonPath("$[0].description").value(itemDescription))
                .andExpect(jsonPath("$[1].id").value(itemUpd.getId()))
                .andExpect(jsonPath("$[1].name").value(itemName2))
                .andExpect(jsonPath("$[1].description").value(itemDescription2));
    }

}