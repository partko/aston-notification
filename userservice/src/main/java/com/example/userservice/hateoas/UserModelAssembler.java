package com.example.userservice.hateoas;

import com.example.userservice.controller.UserController;
import com.example.userservice.dto.UserResponse;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponse, UserResponse> {

    @Override
    public UserResponse toModel(UserResponse user) {

        user.add(linkTo(UserController.class)
                .slash(user.getId())
                .withSelfRel());

        user.add(linkTo(UserController.class)
                .withRel("all-users"));

        user.add(linkTo(UserController.class)
                .slash(user.getId())
                .withRel("update"));

        user.add(linkTo(UserController.class)
                .slash(user.getId())
                .withRel("delete"));

        return user;
    }
}