package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.SavedUserDto;

@Service
public class UserClient extends BaseClient {
    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/users"))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> createUser(SavedUserDto savedUserDto) {
        return post("", savedUserDto);
    }

    public ResponseEntity<Object> getUser(Long userId) {
        return get(getPath(userId));
    }

    public ResponseEntity<Object> updateUser(Long userId, SavedUserDto savedUserDto) {
        return patch(getPath(userId), savedUserDto);
    }

    public void deleteUser(Long userId) {
        delete(getPath(userId));
    }

    private String getPath(Long userId) {
        return "/" + userId.toString();
    }
}
