package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.SavedCommentDto;
import ru.practicum.shareit.item.dto.SavedItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImplTest {
    private final EntityManager em;
    private final ItemService service;
    private final ItemMapper itemMapper;
    private ItemDto itemAddedExpected;
    private ItemDto itemExistedExpected;
    private CommentDto commentAddedExpected;

    private static final long NON_EXISTENT_ID = 999;

    @BeforeEach
    public void testInt() {
        itemAddedExpected = new ItemDto();
        itemAddedExpected.setId(1L);
        itemAddedExpected.setName("item");
        itemAddedExpected.setDescription("description");
        itemAddedExpected.setAvailable(false);

        commentAddedExpected = new CommentDto();
        commentAddedExpected.setId(1L);
        commentAddedExpected.setText("comment");

        itemExistedExpected = new ItemDto();
        itemExistedExpected.setId(40L);
        itemExistedExpected.setName("item4");
        itemExistedExpected.setDescription("description4");
        itemExistedExpected.setAvailable(true);
        CommentDto comment = new CommentDto();
        comment.setId(40L);
        comment.setText("comment4");
        comment.setAuthorName("user5");
        itemExistedExpected.setComments(List.of(comment));
    }

    @Test
    void addItemTest() {
        long userId = 10;
        SavedItemDto item = new SavedItemDto();
        item.setName(itemAddedExpected.getName());
        item.setDescription(itemAddedExpected.getDescription());
        item.setAvailable(itemAddedExpected.isAvailable());
        long itemAddedExpectedId = itemAddedExpected.getId();

        service.addItem(userId, item);
        TypedQuery<Item> query = em.createQuery("SELECT i from Item AS i where i.id = :itemId", Item.class);
        ItemDto itemAdded = itemMapper.map(query.setParameter("itemId", itemAddedExpectedId).getSingleResult());

        assertThat(itemAdded, allOf(
                hasProperty("id", equalTo(itemAddedExpected.getId())),
                hasProperty("name", equalTo(itemAddedExpected.getName())),
                hasProperty("description", equalTo(itemAddedExpected.getDescription())),
                hasProperty("available", equalTo(itemAddedExpected.isAvailable()))
        ));
    }

    @Test
    void addItemNonExistentTest() {
        SavedItemDto item = new SavedItemDto();
        item.setName(itemAddedExpected.getName());
        item.setDescription(itemAddedExpected.getDescription());
        item.setAvailable(itemAddedExpected.isAvailable());

        assertThrows(NotFoundException.class, () -> service.addItem(NON_EXISTENT_ID, item));
    }

    @Test
    void addCommentTest() {
        long userId = 20;
        long itemId = 50;
        SavedCommentDto comment = new SavedCommentDto();
        comment.setText(commentAddedExpected.getText());

        CommentDto commentAdded = service.addComment(userId, itemId, comment);

        assertThat(commentAdded, allOf(
                hasProperty("id", equalTo(commentAddedExpected.getId())),
                hasProperty("text", equalTo(commentAddedExpected.getText()))
        ));
    }

    @Test
    void addCommentWithoutOwningItemTest() {
        long userId = 10;
        long itemId = 10;
        SavedCommentDto comment = new SavedCommentDto();
        comment.setText(commentAddedExpected.getText());

        assertThrows(ValidationException.class, () -> service.addComment(userId, itemId, comment));
    }

    @Test
    void updateItem() {
        long userId = 10;
        long itemId = 10;
        SavedItemDto itemForUpdate = new SavedItemDto();
        itemForUpdate.setName("item");
        itemForUpdate.setDescription("description");
        itemForUpdate.setAvailable(false);

        service.updateItem(userId, itemId, itemForUpdate);
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item AS i WHERE i.id = :itemId", Item.class);
        ItemDto itemUpdated = itemMapper.map(query.setParameter("itemId", itemId).getSingleResult());

        assertThat(itemUpdated, allOf(
                hasProperty("id", equalTo(itemId)),
                hasProperty("name", equalTo(itemForUpdate.getName())),
                hasProperty("description", equalTo(itemForUpdate.getDescription())),
                hasProperty("available", equalTo(itemForUpdate.getAvailable()))
        ));
    }

    @Test
    void updateItemNotExistTest() {
        long userId = 10;
        SavedItemDto itemForUpdate = new SavedItemDto();
        itemForUpdate.setName("item");
        itemForUpdate.setDescription("description");
        itemForUpdate.setAvailable(false);

        assertThrows(NotFoundException.class, () -> service.updateItem(userId, NON_EXISTENT_ID, itemForUpdate));
    }

    @Test
    void updateItemNotOwnerTest() {
        long userId = 20;
        long itemId = 10;
        SavedItemDto itemForUpdate = new SavedItemDto();
        itemForUpdate.setName("item");
        itemForUpdate.setDescription("description");
        itemForUpdate.setAvailable(false);

        assertThrows(AccessDeniedException.class, () -> service.updateItem(userId, itemId, itemForUpdate));
    }

    @Test
    void getItem() {
        long itemId = itemExistedExpected.getId();
        CommentDto commentExpected = itemExistedExpected.getComments().stream()
                .toList()
                .getFirst();

        ItemDto item = service.getItem(itemId);

        assertThat(item, allOf(
                hasProperty("id", equalTo(itemExistedExpected.getId())),
                hasProperty("name", equalTo(itemExistedExpected.getName())),
                hasProperty("description", equalTo(itemExistedExpected.getDescription())),
                hasProperty("available", equalTo(itemExistedExpected.isAvailable()))
        ));
        List<CommentDto> comments = item.getComments()
                .stream()
                .toList();
        assertEquals(1, comments.size());
        assertThat(comments.getFirst(), allOf(
                hasProperty("id", equalTo(commentExpected.getId())),
                hasProperty("text", equalTo(commentExpected.getText())),
                hasProperty("authorName", equalTo(commentExpected.getAuthorName()))
        ));
    }

    @Test
    void getAllOwnerItems() {
        long userId = 40;

        List<ItemDto> items = service.getAllOwnerItems(userId)
                .stream()
                .toList();

        assertEquals(1, items.size());
        ItemDto item = items.getFirst();
        assertThat(item, allOf(
                hasProperty("id", equalTo(itemExistedExpected.getId())),
                hasProperty("name", equalTo(itemExistedExpected.getName())),
                hasProperty("description", equalTo(itemExistedExpected.getDescription())),
                hasProperty("available", equalTo(itemExistedExpected.isAvailable()))
        ));
    }

    @Test
    void searchItems() {
        String text = "description4";
        List<ItemDto> items = service.searchItems(text)
                .stream()
                .toList();

        assertEquals(1, items.size());
        ItemDto item = items.getFirst();
        assertThat(item, allOf(
                hasProperty("id", equalTo(itemExistedExpected.getId())),
                hasProperty("name", equalTo(itemExistedExpected.getName())),
                hasProperty("description", equalTo(itemExistedExpected.getDescription())),
                hasProperty("available", equalTo(itemExistedExpected.isAvailable()))
        ));
    }

    @Test
    void itemMapperTest() {
        SavedItemDto itemSaveDto = null;
        Item item = itemMapper.map(itemSaveDto);
        assertEquals(itemSaveDto, item);
        ItemDto itemDto = itemMapper.map(item);
        assertEquals(itemDto, item);

    }
}
