package com.example.userservice.mapper;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UpdateUserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.UserEntity;

/**
 * Маппер для преобразования сущности пользователя и DTO.
 */
public final class UserMapper {
    
    private UserMapper() {
    }

    /**
     * Преобразует DTO создания пользователя в сущность.
     *
     * @param request DTO запроса на создание
     * @return сущность пользователя
     */
    public static UserEntity toEntity(CreateUserRequest request) {
        if (request == null) {
            return null;
        }
        return new UserEntity(
                request.getName(),
                request.getEmail(),
                request.getAge()
        );
    }

    /**
     * Обновляет сущность пользователя значениями из DTO обновления.
     *
     * @param userEntity сущность пользователя
     * @param request DTO обновления
     */
    public static void updateEntity(UserEntity userEntity, UpdateUserRequest request) {
        if (userEntity == null || request == null) {
            return;
        }
        userEntity.setName(request.getName());
        userEntity.setEmail(request.getEmail());
        userEntity.setAge(request.getAge());
    }

    /**
     * Преобразует сущность пользователя в DTO ответа.
     *
     * @param userEntity сущность пользователя
     * @return DTO ответа
     */
    public static UserResponse toResponse(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new UserResponse(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getAge(),
                userEntity.getCreatedAt()
        );
    }
}