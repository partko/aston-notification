package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

/**
 * DTO для возврата данных пользователя во внешний слой.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends RepresentationModel<UserResponse> {

    private Long id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "User { "+ id
                + " | " + name
                + " | " + email
                + " | " + age
                + " | " + createdAt
                + " }";
    }
}