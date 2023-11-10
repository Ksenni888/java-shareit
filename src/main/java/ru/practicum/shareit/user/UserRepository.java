package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

//    @Query(" select i from User i " +
//            "where (i.email) like upper(concat('%', ?1, '%')) ")

 //   boolean findByEmailLike(String email);

//    @Query(" select i from User i " +
//            "where (i.email) like upper(concat('%', ?1, '%')) and" +
//    "where (i.user_id) like upper" )

 //   boolean findByEmailAndId(String email, long userId);
}
