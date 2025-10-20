package br.com.drazevedo.dealrooms.interfaces.controllers.utils;

import br.com.drazevedo.dealrooms.domain.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggedUserUtils {

    public static User getLoggedUser() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
