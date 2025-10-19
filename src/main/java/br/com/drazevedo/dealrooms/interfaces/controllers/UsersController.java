package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.application.dto.UserDto;
import br.com.drazevedo.dealrooms.application.service.UserService;
import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/users")
    public User create(@RequestBody UserDto user) {
        User userEntity = new User();
        userEntity.setEmail(user.getEmail());
        userEntity.setName(user.getName());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setRole(Role.valueOf(user.getRole()));
        return userService.save(userEntity);
    }

    @GetMapping("/api/users")
    public List<UserDto> getUsers() {
        return userService.findAll().stream().map(user ->
            UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .role(user.getRole().name())
                    .build()
        ).toList();
    }

}
