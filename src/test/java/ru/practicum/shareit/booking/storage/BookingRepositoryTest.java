package ru.practicum.shareit.booking.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional

// Не получается с очисткой БД (причина явно не в очистке).
// Почему-то при прохождении тестов по одному все прекрасно работает, но когда они следуют друг за другом,
// БД перестает что-либо возвращать... То есть на все запросы она ничего не возвращает.


@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryTest {
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private PageRequest pageable;
    @Autowired
    private TestEntityManager em;
    @Autowired
    private BookingRepository repository;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setName("test");
        user1.setEmail("test1@test.ru");


        user2 = new User();
        user2.setName("test");
        user2.setEmail("test2@test.ru");

        item1 = new Item();
        item1.setName("test1");
        item1.setDescription("test1");
        item1.setAvailable(true);
        item1.setOwner(user1);

        item2 = new Item();
        item2.setName("test1");
        item2.setDescription("test1");
        item2.setAvailable(true);
        item2.setOwner(user2);

        booking1 = new Booking();
        booking1.setStart(LocalDateTime.now().plusHours(1));
        booking1.setEnd(LocalDateTime.now().plusHours(2));
        booking1.setItem(item1);
        booking1.setBooker(user2);
        booking1.setStatus(BookingStatus.WAITING);

        booking2 = new Booking();
        booking2.setStart(LocalDateTime.now().minusHours(1));
        booking2.setEnd(LocalDateTime.now().plusHours(2));
        booking2.setItem(item1);
        booking2.setBooker(user2);
        booking2.setStatus(BookingStatus.APPROVED);

        booking3 = new Booking();
        booking3.setStart(LocalDateTime.now().minusHours(2));
        booking3.setEnd(LocalDateTime.now().minusHours(1));
        booking3.setItem(item1);
        booking3.setBooker(user2);
        booking3.setStatus(BookingStatus.WAITING);
        pageable = PageRequest.of(0, 10);

    }


    @Test
    void verifyFindAllCurrentByBookerId() {
        em.persist(user1);
        em.persist(user2);
        em.persist(item1);
        em.persist(booking1);
        em.persist(booking2);
        Page<Booking> result = repository.findAllCurrentByBookerId(2L, LocalDateTime.now(), pageable);
        assertAll(
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(user2, result.getContent().get(0).getBooker()),
                () -> assertEquals(booking2, result.getContent().get(0)),
                () -> assertEquals(booking2.getId(), result.getContent().get(0).getId()),
                () -> assertEquals(booking2.getStart(), result.getContent().get(0).getStart()),
                () -> assertEquals(booking2.getEnd(), result.getContent().get(0).getEnd())
        );


    }

    @Test
    void verifyFindAllPastByBookerId() {
        em.persist(user1);
        em.persist(user2);
        em.persist(item1);
        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);
        Page<Booking> result = repository.findAllPastByBookerId(2L, LocalDateTime.now(), pageable);
        assertAll(
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(user2, result.getContent().get(0).getBooker()),
                () -> assertEquals(booking3, result.getContent().get(0))
        );
    }

    @Test
    void verifyFindAllFutureByItemOwnerId() {
        em.persist(user1);
        em.persist(user2);
        em.persist(item1);
        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);

        Page<Booking> result = repository.findAllFutureByItemOwnerId(1L, LocalDateTime.now(), pageable);
        assertAll(
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(user1, result.getContent().get(0).getItem().getOwner()),
                () -> assertEquals(booking1, result.getContent().get(0))
        );
    }

    @Test
    void verifyFindAllByBookerIdAndStatus() {
        em.persist(user1);
        em.persist(user2);
        em.persist(item1);
        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);

        Page<Booking> result = repository.findAllByBookerIdAndStatus(2L, BookingStatus.APPROVED, pageable);
        assertAll(
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(user2, result.getContent().get(0).getBooker()),
                () -> assertEquals(booking2, result.getContent().get(0))
        );
    }

    @Test
    void verifyFindItemLastBook() {
        em.persist(user1);
        em.persist(user2);
        em.persist(item1);
        em.persist(item2);
        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);

        List<Booking> result = repository.findItemLastBook(1L, LocalDateTime.now());
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(item1, result.get(0).getItem()),
                () -> assertEquals(booking3, result.get(0))
        );

    }
}
