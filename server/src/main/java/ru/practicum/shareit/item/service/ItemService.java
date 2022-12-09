package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCommentInfoDto;

import java.util.List;

public interface ItemService {
    ItemCommentInfoDto create(Long userId, ItemCommentInfoDto itemCommentInfoDto);

    void delete(Long id);

    ItemCommentInfoDto update(Long userId, Long itemId, ItemCommentInfoDto itemCommentInfoDto);

    ItemCommentInfoDto getById(Long userId, Long itemId);

    List<ItemCommentInfoDto> getByUser(Long id, Pageable pageable);

    List<ItemCommentInfoDto> search(String text, Pageable pageable);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);

    List<CommentDto> getCommentsByItem(Long itemId);

}
