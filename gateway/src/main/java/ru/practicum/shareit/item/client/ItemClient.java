package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.SavedCommentDto;
import ru.practicum.shareit.item.dto.SavedItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String ADD_COMMENT = "/{item-id}/comment";
    private static final String SEARCH_PATH = "/search?text=";
    private static final String ITEMS_PATH = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ITEMS_PATH))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> addItem(Long userId, SavedItemDto savedItemDto) {
        return post("", userId, savedItemDto);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, SavedCommentDto savedCommentDto) {
        Map<String, Object> uriVariables = Map.of("item-id", itemId);
        return post(ADD_COMMENT, userId, uriVariables, savedCommentDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, SavedItemDto savedItemDto) {
        String path = "/" + itemId.toString();
        return patch(path, userId, savedItemDto);
    }

    public ResponseEntity<Object> getItem(Long itemId) {
        String path = "/" + itemId.toString();
        return get(path);
    }

    public ResponseEntity<Object> getAllOwnerItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItems(String text) {
        return get(SEARCH_PATH + text);
    }
}
