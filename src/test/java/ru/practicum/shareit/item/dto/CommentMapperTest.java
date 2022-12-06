package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class CommentMapperTest {
    private CommentDto commentDto;
    private Comment comment;

    private User user;
    @InjectMocks
    private CommentMapperImpl mapper;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setId(1L);

        commentDto = new CommentDto();
        commentDto.setId(1L);

        user = new User();
    }

    @Test
    void verifyDtoToComment() {
        Comment result = mapper.dtoToComment(commentDto);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(comment.getId(), result.getId())
        );

        assertNull(mapper.dtoToComment(null));
    }

    @Test
    void verifyCommentToDto() {
        CommentDto result = mapper.commentToDto(comment);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(commentDto.getId(), result.getId())
        );

        assertNull(mapper.commentToDto(null));

        comment.setAuthor(user);
        CommentDto resultNullUser = mapper.commentToDto(comment);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(commentDto.getId(), resultNullUser.getId())
        );

        user.setName("Name");
        CommentDto resultUser = mapper.commentToDto(comment);
        assertNotNull(result);
        assertAll(
                () -> assertEquals(commentDto.getId(), resultUser.getId())
        );
    }

    @Test
    void verifyCommentsToDto() {
        List<CommentDto> result = mapper.commentsToDto(List.of(comment));
        assertNotNull(result);
        assertAll(
                () -> assertEquals(1, result.size()),
                () -> assertEquals(commentDto.getId(), result.get(0).getId())
        );

        assertNull(mapper.commentsToDto(null));
    }


}
