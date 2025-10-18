package br.com.drazevedo.dealrooms.application.service;

import br.com.drazevedo.dealrooms.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    UserDetails loadUserByUsername(String username);

    User save(User user);

    List<User> findAll();
}
