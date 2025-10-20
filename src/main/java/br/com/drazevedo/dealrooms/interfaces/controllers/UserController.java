package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.application.dto.UserDto;
import br.com.drazevedo.dealrooms.application.service.UserService;
import br.com.drazevedo.dealrooms.domain.model.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RolesAllowed("ADMIN")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/users")
    public UserDto create(@RequestBody UserDto user) {

        User userEntity = user.toEntity();
        User savedUser = userService.save(userEntity);

        return UserDto.from(savedUser);
    }

    @GetMapping("/api/users")
    public List<UserDto> getUsers() {

        return userService.findAll().stream()
                .map(UserDto::from)
                .toList();
    }

}
