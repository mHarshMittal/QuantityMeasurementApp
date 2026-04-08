package com.apps.admin.client;

import com.apps.admin.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Component
public class AuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public List<UserDto> getAllUsers() {
        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
            authServiceUrl + "/auth/admin/users",
            HttpMethod.GET, null,
            new ParameterizedTypeReference<List<UserDto>>() {});
        return response.getBody();
    }

    public UserDto getUserById(Long id) {
        return restTemplate.getForObject(authServiceUrl + "/auth/admin/users/" + id, UserDto.class);
    }

    public void deleteUser(Long id) {
        restTemplate.delete(authServiceUrl + "/auth/admin/users/" + id);
    }
}
