package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    Comment dtoToComment(CommentDto commentDto);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto commentToDto(Comment comment);

    List<CommentDto> commentsToDto(List<Comment> comments);
}
