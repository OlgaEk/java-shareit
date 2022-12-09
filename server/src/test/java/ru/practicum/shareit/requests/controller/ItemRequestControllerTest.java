package ru.practicum.shareit.requests.controller;

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
import ru.practicum.shareit.item.dto.ItemRequestInfoDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.controller.validator.UserIdExistValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService requestService;
    private ItemRequestDto requestDtoInput;
    private ItemRequestDto requestDtoOutput;
    private ItemRequestInfoDto item;
    private List<ItemRequestInfoDto> items;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ItemRequestRepository requestRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private UserIdExistValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UserIdExistValidator(userRepository);
        item = new ItemRequestInfoDto();
        item.setId(1L);
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);

        items = List.of(item);

        requestDtoInput = new ItemRequestDto();
        requestDtoInput.setDescription("test");

        requestDtoOutput = new ItemRequestDto();
        requestDtoOutput.setId(1L);
        requestDtoOutput.setDescription("test");
        requestDtoOutput.setItems(items);

    }

    @Test
    void shouldCreateRequest() throws Exception {
        when(requestService.create(1L, requestDtoInput)).thenReturn(requestDtoOutput);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDtoOutput.getDescription()), String.class))
                .andExpect(jsonPath("$.items[0]",
                        is(requestDtoOutput.getItems().get(0)), ItemRequestInfoDto.class));
    }

    @Test
    void shouldReturnNotFoundWhenNotFoundUserId() throws Exception {
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenDataNotValid() throws Exception {
        requestDtoInput.setDescription("");
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetRequests() throws Exception {
        when(requestService.getByUser(1L)).thenReturn(List.of(requestDtoOutput));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDtoOutput.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0]",
                        is(requestDtoOutput.getItems().get(0)), ItemRequestInfoDto.class));

        PageRequest pageable = PageRequest.of(0, 10);
        when(requestService.getAll(1L, pageable)).thenReturn(List.of(requestDtoOutput));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDtoOutput.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0]",
                        is(requestDtoOutput.getItems().get(0)), ItemRequestInfoDto.class));

        when(requestService.get(1L)).thenReturn(requestDtoOutput);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(new ItemRequest()));


        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDtoOutput.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDtoOutput.getDescription()), String.class))
                .andExpect(jsonPath("$.items[0]",
                        is(requestDtoOutput.getItems().get(0)), ItemRequestInfoDto.class));
    }

    @Test
    void shouldReturnNotFoundIfParamsWrong() throws Exception {
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "-1")
                        .content(mapper.writeValueAsString(requestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("size", "0")
                        .content(mapper.writeValueAsString(requestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundIfRequestIdIsWrong() throws Exception {
        mvc.perform(get("/requests/0")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}