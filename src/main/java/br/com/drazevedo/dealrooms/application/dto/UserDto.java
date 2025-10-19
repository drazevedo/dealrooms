package br.com.drazevedo.dealrooms.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {

    private String id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String role;
}
