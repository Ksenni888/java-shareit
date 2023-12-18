package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
@Service
public class ItemClient extends BaseClient{

        private static final String API_PREFIX = "/items";

        @Autowired
        public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
            super(
                    builder
                            .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                            .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                            .build()
            );
        }

        public ResponseEntity<Object> createItem(long userId, ItemDto itemDto) {
            return post("", userId, itemDto);
        }

    public ResponseEntity<Object> updateItem(long itemId, long userId, Item item) {
        return patch("/" + itemId, userId, item);
    }
}