package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comments;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
   List<Comments> findByItemId(long itemId);
}