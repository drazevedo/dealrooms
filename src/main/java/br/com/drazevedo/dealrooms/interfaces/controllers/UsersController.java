package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.application.service.UserService;
import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.User;
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
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("/api/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

}
