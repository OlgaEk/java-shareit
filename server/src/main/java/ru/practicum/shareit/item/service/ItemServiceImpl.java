package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessNotAllowed;
import ru.practicum.shareit.exception.NoEntityException;
import ru.practicum.shareit.exception.NotValidRequestException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final CommentMapper commentMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final ItemRequestRepository requestRepository;

    public ItemCommentInfoDto create(Long userId, ItemCommentInfoDto itemCommentInfoDto) {
        Item item = itemMapper.dtoToItem(itemCommentInfoDto);
        item.setOwner(userRepository.findById(userId).orElseThrow(() -> new NoEntityException("User is not found")));
        return itemMapper.itemToDto(itemRepository.save(item));
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    public ItemCommentInfoDto update(Long userId, Long itemId, ItemCommentInfoDto itemCommentInfoDto) {
        Item itemToUpdate = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoEntityException("Item is not found"));
        if (!Objects.equals(itemToUpdate.getOwner().getId(), userId))
            throw new AccessNotAllowed("User id = " + userId + " is not owner");
        setNewData(itemCommentInfoDto, itemToUpdate);
        return itemMapper.itemToDto(itemRepository.saveAndFlush(itemToUpdate));
    }

    public ItemCommentInfoDto getById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoEntityException("Item is not found"));
        if (Objects.equals(item.getOwner().getId(), userId)) return itemMapper.itemToBookInfoDto(item);
        else {
            ItemBookInfoDto itemBookInfoDto = itemMapper.itemToBookInfoDto(item);
            itemBookInfoDto.setLastBooking(null);
            itemBookInfoDto.setNextBooking(null);
            return itemBookInfoDto;
        }
    }

    public List<ItemCommentInfoDto> getByUser(Long id, Pageable pageable) {
        return itemRepository.findByOwnerIdOrderByIdAsc(id, pageable).stream()
                .map(itemMapper::itemToBookInfoDto)
                .collect(Collectors.toList());
    }

    public List<ItemCommentInfoDto> search(String text, Pageable pageable) {
        if (text.isEmpty()) return new ArrayList<>();
        text = text.toLowerCase();
        return itemRepository.findByNameAndDescription(text, pageable).getContent().stream()
                .map(itemMapper::itemToDto)
                .collect(Collectors.toList());

    }

    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        if (bookingRepository.findAllByBookerIdAndItemId(userId, itemId, LocalDateTime.now()).isEmpty())
            throw new NotValidRequestException("User id = " + userId + " didn't get the item id = " + itemId);
        Comment comment = commentMapper.dtoToComment(commentDto);
        comment.setAuthor(userRepository.findById(userId)
                .orElseThrow(() -> new NoEntityException("User is not found")));
        comment.setItem(itemRepository.findById(itemId)
                .orElseThrow(() -> new NoEntityException("Item is not found")));
        comment.setCreated(LocalDateTime.now());
        return commentMapper.commentToDto(commentRepository.save(comment));
    }

    public List<CommentDto> getCommentsByItem(Long itemId) {
        return commentMapper.commentsToDto(commentRepository.findAllByItemId(itemId));
    }

    private void setNewData(ItemCommentInfoDto itemFrom, Item itemTo) {
        if (itemFrom.getName() != null) {
            if (!itemFrom.getName().isBlank())
                itemTo.setName(itemFrom.getName());
        }
        if (itemFrom.getDescription() != null) {
            if (!itemFrom.getDescription().isBlank())
                itemTo.setDescription(itemFrom.getDescription());
        }
        if (itemFrom.getAvailable() != null) {
            itemTo.setAvailable(itemFrom.getAvailable());
        }
    }


}
