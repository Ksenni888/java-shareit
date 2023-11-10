package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//@Getter
//@Setter
@Data
@Builder
@Entity
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private long id;

    @Column(name="item_name")//, nullable = false)
    private String name;

    @Column(name="description")//, length=512)
    private String description;

    @Column(name="is_available")//, nullable=false)
    private Boolean available;

    @Column(name="owner_id")//, nullable=false)
    private User owner;

    @Column(name="request_id")
    private ItemRequest request;
}
