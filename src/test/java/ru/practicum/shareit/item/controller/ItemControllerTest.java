package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.validator.RequestIdExistValidator;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.controller.validator.UserIdExistValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    ItemCommentInfoDto itemInDto;
    CommentDto commentDto;
    PageRequest pageable;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private ItemRequestRepository requestRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private UserIdExistValidator userValidator;
    private RequestIdExistValidator requestValidator;

    @BeforeEach
    void setUp() {
        userValidator = new UserIdExistValidator(userRepository);
        requestValidator = new RequestIdExistValidator(requestRepository);

        pageable = PageRequest.of(0, 10);

        itemInDto = new ItemCommentInfoDto();
        itemInDto.setId(1L);
        itemInDto.setName("test");
        itemInDto.setDescription("test");
        itemInDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("text");
        commentDto.setAuthorName("Name");
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void shouldCreateItem() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemService.create(1L, itemInDto)).thenReturn(itemInDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemInDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemInDto.getId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemInDto.getAvailable()), boolean.class));
    }

    @Test
    void shouldReturnBadRequestWhenDataNotRight() throws Exception {
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemInDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        itemInDto.setName("");
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemInDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        itemInDto.setName("test");

        itemInDto.setDescription("");
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemInDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        itemInDto.setDescription("test");

        itemInDto.setAvailable(null);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemInDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        itemInDto.setAvailable(true);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemInDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        when(itemService.getByUser(1L, pageable)).thenReturn(List.of(itemInDto));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "-1")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "0")
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "-2")
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        itemInDto.setRequestId(1L);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemInDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void updateItem() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(itemService.update(1L, 1L, itemInDto)).thenReturn(itemInDto);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemInDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemInDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemInDto.getName()), String.class));
    }

    @Test
    void shouldGetUserItems() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemService.getByUser(1L, pageable)).thenReturn(List.of(itemInDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemInDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemInDto.getDescription()), String.class));

    }

    @Test
    void shouldGetItem() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(itemService.getById(1L, 1L)).thenReturn(itemInDto);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemInDto.getId()), Long.class));
    }

    @Test
    void shouldSearchItem() throws Exception {
        when(itemService.search("text", pageable)).thenReturn(List.of(itemInDto));
        mvc.perform(get("/items/search")
                        .param("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemInDto.getId()), Long.class));

    }

    @Test
    void shouldCreateComment() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(new Item()));
        when(itemService.createComment(1L, 1L, commentDto)).thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName()), String.class))
                .andExpect(jsonPath("$.created").exists());


    }
}