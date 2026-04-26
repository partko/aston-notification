package com.example.userservice.controller.impl;

import com.example.userservice.controller.UserController;
import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UpdateUserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.hateoas.UserModelAssembler;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST-контроллер для управления пользователями.
 */
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final UserModelAssembler assembler;

    @Override
    public UserResponse getById(Long id) {
        UserResponse user = userService.getUserById(id);
        return assembler.toModel(user);
    }

    @Override
    public CollectionModel<UserResponse> getAll() {
        List<UserResponse> users = userService.getAllUsers();
        return CollectionModel.of(
                users.stream()
                        .map(assembler::toModel)
                        .toList(),
                linkTo(methodOn(UserController.class).getAll()).withSelfRel()
        );
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        return assembler.toModel(userService.createUser(request));
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        return assembler.toModel(userService.updateUser(id, request));
    }

    @Override
    public void delete(Long id) {
        userService.deleteUser(id);
    }
}