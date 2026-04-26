package com.example.userservice.controller;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UpdateUserRequest;
import com.example.userservice.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * REST API для управления пользователями.
 */
@RequestMapping("/api/users")
public interface UserController {

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO с данными пользователя
     */
    @Operation(summary = "Получить пользователя по id")
    @ApiResponse(responseCode = "200", description = "Пользователь найден")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @GetMapping("/{id}")
    UserResponse getById(@PathVariable Long id);

    /**
     * Возвращает список всех пользователей.
     *
     * @return список пользователей в виде DTO
     */
    @Operation(summary = "Получить всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен")
    @GetMapping
    CollectionModel<UserResponse> getAll();

    /**
     * Создаёт нового пользователя.
     *
     * @param request DTO с данными для создания пользователя
     * @return DTO созданного пользователя
     */
    @Operation(summary = "Создать пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь создан")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации или email уже занят другим пользователем")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse create(@Valid @RequestBody CreateUserRequest request);

    /**
     * Обновляет существующего пользователя.
     *
     * @param id идентификатор пользователя
     * @param request DTO с новыми данными пользователя
     * @return DTO обновлённого пользователя
     */
    @Operation(summary = "Обновить пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлён")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации или email уже занят другим пользователем")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @PutMapping("/{id}")
    UserResponse update(@PathVariable Long id,
                        @Valid @RequestBody UpdateUserRequest request);

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    @Operation(summary = "Удалить пользователя")
    @ApiResponse(responseCode = "204", description = "Пользователь успешно удалён")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id);
}