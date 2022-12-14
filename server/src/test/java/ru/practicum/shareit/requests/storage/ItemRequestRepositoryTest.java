package ru.practicum.shareit.requests.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRequestRepository repository;

    @Test
    void verifyFindAllByAnotherUser() {
        User user1 = new User();
        user1.setName("test");
        user1.setEmail("test1@test.ru");
        User user2 = new User();
        user2.setName("test");
        user2.setEmail("test2@test.ru");
        User user3 = new User();
        user3.setName("test");
        user3.setEmail("test3@test.ru");
        ItemRequest requestUser1 = new ItemRequest();
        requestUser1.setDescription("test");
        requestUser1.setRequester(user1);
        requestUser1.setCreated(LocalDateTime.now());
        ItemRequest requestUser2 = new ItemRequest();
        requestUser2.setDescription("test");
        requestUser2.setRequester(user2);
        requestUser2.setCreated(LocalDateTime.now().plusDays(1));
        ItemRequest requestUser3 = new ItemRequest();
        requestUser3.setDescription("test");
        requestUser3.setRequester(user3);
        requestUser3.setCreated(LocalDateTime.now().plusDays(2));

        PageRequest pageable = PageRequest.of(0, 10);

        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(requestUser1);
        em.persist(requestUser2);
        em.persist(requestUser3);


        Page<ItemRequest> result = repository.findAllByRequesterIdNotOrderByCreatedDesc(1L, pageable);
        assertAll(
                () -> assertEquals(2, result.getContent().size()),
                () -> assertEquals(user3.getId(), result.getContent().get(0).getRequester().getId()),
                () -> assertEquals(user2.getId(), result.getContent().get(1).getRequester().getId()),
                () -> assertEquals(3L, result.getContent().get(0).getId()),
                () -> assertEquals(requestUser3.getDescription(), result.getContent().get(0).getDescription()),
                () -> assertEquals(requestUser3.getCreated(), result.getContent().get(0).getCreated()),
                () -> assertEquals(requestUser3.getItemsOnRequest(), result.getContent().get(0).getItemsOnRequest())
        );


    }
}
