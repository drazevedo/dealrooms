package br.com.drazevedo.dealrooms.interfaces.controllers.utils;

import br.com.drazevedo.dealrooms.domain.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedUserUtils {

    public static User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
