package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для обновления существующего пользователя.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неправильный формат email")
    private String email;

    @NotNull(message = "Возраст не может быть пустым")
    @Min(value = 0, message = "Возраст должен быть в пределах от 0 до 150")
    @Max(value = 150, message = "Возраст должен быть в пределах от 0 до 150")
    private Integer age;

}