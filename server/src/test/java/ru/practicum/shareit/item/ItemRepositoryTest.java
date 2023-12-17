package ru.practicum.shareit.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.practicum.shareit.model.TestModels.*;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private CommentRepository commentRepository;

    private final Pageable pageable = PageRequest.of(0, 20, Sort.unsorted());


    @Test
    @DisplayName("Получение всех вещей владельца")
    void findAllByOwnerIdTest() {
        Item item1 = saveItem();
        List<Item> list = itemRepository.findByOwnerId(item1.getOwner().getId(), pageable);

        assertEquals(list.get(0), item1);
    }

    @Test
    @DisplayName("Поиск вещей по имени и описанию")
    void findItemByNameAndDescriptionTest() {
        Item item1 = saveItem();
        List<Item> list = itemRepository.findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                item1.getName().substring(2, 5).toUpperCase(),
                item1.getDescription().substring(1, 4).toUpperCase(),
                pageable);

        assertEquals(list.get(0), item1);
    }

    @Test
    @DisplayName("Получение вещей по запросу")
    void findAllByItemRequestIdTest() {
        Item item1 = saveItem();
        ItemRequest itemRequest1 = saveItemRequest();
        item1.setRequest(itemRequest1);
        itemRepository.save(item1);

        List<Item> list = itemRepository.findByRequest_Id(itemRequest1.getId());

        assertEquals(list.get(0), item1);
    }

    @Test
    @DisplayName("Получение всех комментариев")
    void findAllCommentsByItemIdTest() {
        Item item1 = saveItem();
        Comment comment1 = saveComment(item1);
        List<Comment> comments = commentRepository.findAllByItemId(item1.getId());


        assertEquals(comments.get(0), comment1);
        assertEquals(comments.get(0).getItem(), item1);
    }

    private Item saveItem() {
        User userGet = userRepository.save(user);
        item.setOwner(userGet);
        Item itemNew = itemRepository.save(item);
        item.setOwner(user);
        return itemNew;
    }

    private ItemRequest saveItemRequest() {
        User bookerNew = userRepository.save(booker);
        itemRequest.setRequester(bookerNew);
        ItemRequest itemRequest1 = itemRequestRepository.save(itemRequest);
        itemRequest.setRequester(booker);
        return itemRequestRepository.save(itemRequest1);
    }

    private Comment saveComment(Item item1) {
        User bookerNew = userRepository.save(booker);
        comment.setAuthor(bookerNew);
        comment.setItem(item1);
        Comment commentGet = commentRepository.save(comment);
        comment.setAuthor(booker);
        comment.setItem(item);
        return commentGet;
    }
}