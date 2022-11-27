package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.user.controller.validator.UserIdExistValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    User user;
    UserIdExistValidator validator;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Name");
        user.setEmail("test@test.ru");

        validator = new UserIdExistValidator(userRepository);
    }

    @Test
    void ShouldCreateUser() throws  Exception {
        when(userService.create(any(User.class))).thenReturn(user);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName()), String.class));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void shouldReturnNotFoundIfUserNotFound() throws Exception {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(userService.update(anyLong(),any(User.class))).thenReturn(user);
        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(user))
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.email", is(user.getEmail()), String.class));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        when(userService.getAll()).thenReturn(List.of(user));
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(user.getId()), Long.class));
    }

    @Test
    void shouldGetUser() throws Exception {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(userService.getUser(anyLong())).thenReturn(user);
        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class));
    }
}