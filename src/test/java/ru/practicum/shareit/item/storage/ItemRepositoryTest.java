package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryTest {
    @Autowired
    ItemRepository repository;
    @Autowired
    private TestEntityManager em;

    @Test
    void verifyFindByNameAndDescription() {
        User user = new User();
        user.setName("test");
        user.setEmail("test1@test.ru");

        Item item = new Item();
        item.setName("1");
        item.setDescription("test");
        item.setAvailable(true);
        item.setOwner(user);

        Item itemReturnsByName = new Item();
        itemReturnsByName.setName("1A");
        itemReturnsByName.setDescription("test");
        itemReturnsByName.setAvailable(true);
        itemReturnsByName.setOwner(user);

        Item itemReturnsByDesc = new Item();
        itemReturnsByDesc.setName("1");
        itemReturnsByDesc.setDescription("Atest");
        itemReturnsByDesc.setAvailable(true);
        itemReturnsByDesc.setOwner(user);

        PageRequest pageable = PageRequest.of(0, 10);

        em.persist(user);
        em.persist(itemReturnsByName);
        em.persist(itemReturnsByDesc);

        List<Item> result = repository.findByNameAndDescription("a", pageable);
        assertAll(
                () -> assertEquals(2, result.size()),
                () -> assertThat(result).contains(itemReturnsByName),
                () -> assertThat(result).contains(itemReturnsByDesc),
                () -> assertEquals(itemReturnsByName.getId(), result.get(0).getId()),
                () -> assertEquals(itemReturnsByName.getName(), result.get(0).getName()),
                () -> assertEquals(itemReturnsByName.getDescription(), result.get(0).getDescription())

        );


    }


}
