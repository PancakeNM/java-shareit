package ru.practicum.shareit.item.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.SavedCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.service.UserMapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface CommentMapper {

    Comment map(SavedCommentDto savedCommentDto);

    Collection<CommentDto> map(Collection<Comment> comments);

    @Mapping(source = "author", target = "authorName", qualifiedByName = "userToName")
    CommentDto map(Comment comment);
}
