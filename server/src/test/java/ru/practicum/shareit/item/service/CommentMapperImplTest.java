package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.SavedCommentDto;
import ru.practicum.shareit.item.model.Comment;

import static org.junit.jupiter.api.Assertions.assertNull;

public class CommentMapperImplTest {
    @Test
    void map() {
        SavedCommentDto commentSaveDto = null;
        CommentMapperImpl commentMapperImpl = new CommentMapperImpl();
        assertNull(commentMapperImpl.map(commentSaveDto));
    }

    @Test
    void testMap() {
        Comment comment = null;
        CommentMapperImpl commentMapperImpl = new CommentMapperImpl();
        assertNull(commentMapperImpl.map(comment));
    }
}
