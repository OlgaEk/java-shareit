package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("test");
        user.setEmail("test1@test.ru");
    }

    @Test
    void shouldCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.create(user);
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());

        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void shouldThrowExceptionIfEmailNotUnique() {
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        AlreadyExistException ex = assertThrows(AlreadyExistException.class, () -> userService.create(user));
        assertEquals("Email test1@test.ru is already exist", ex.getMessage());

        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void shouldDeleteUser() {
        userService.delete(1L);
    }

    @Test
    void shouldUpdateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);
        User result = userService.update(1L, user);
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());

        verify(userRepository).findById(anyLong());
        verify(userRepository).saveAndFlush(any(User.class));

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowExceptionWhenUpdateIfUserIdNotFoundOrEmailNotUnique() {
        NoEntityException ex = assertThrows(NoEntityException.class, () -> userService.update(1L, user));
        assertEquals("User not found", ex.getMessage());

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.saveAndFlush(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        AlreadyExistException exExist = assertThrows(AlreadyExistException.class,
                () -> userService.update(1L, user));
        assertEquals("Email test1@test.ru is already exist", exExist.getMessage());

        verify(userRepository, times(2)).findById(anyLong());
        verify(userRepository).saveAndFlush(any(User.class));

        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void shouldGetAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> result = userService.getAll();
        assertNotNull(result);
        assertEquals(user.getId(), result.get(0).getId());
        assertEquals(user.getEmail(), result.get(0).getEmail());

        verify(userRepository).findAll();

        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void shouldGetUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        User result = userService.getUser(1L);
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());

        verify(userRepository).findById(1L);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowExceptionIfUserIdNotFound() {
        NoEntityException ex = assertThrows(NoEntityException.class, () -> userService.getUser(1L));
        assertEquals("User not found", ex.getMessage());
    }
}