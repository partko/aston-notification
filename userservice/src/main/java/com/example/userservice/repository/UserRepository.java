package com.example.userservice.repository;

import com.example.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью UserEntity.
 * Расширяет JpaRepository, предоставляя стандартные CRUD-операции и дополнительно содержит
 * методы для поиска и проверки существования пользователя по адресу электронной почты.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Находит пользователя по адресу электронной почты.
     *
     * @param email адрес электронной почты пользователя
     * @return Optional с найденным пользователем или пустой Optional, если пользователь с таким email не найден
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Проверяет, существует ли пользователь с указанным адресом электронной почты.
     *
     * @param email адрес электронной почты пользователя
     * @return true, если пользователь существует, иначе false
     */
    boolean existsByEmail(String email);
}