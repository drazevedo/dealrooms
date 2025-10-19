package br.com.drazevedo.dealrooms.application.dto;

import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
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

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    public User toEntity() {
        User userEntity = new User();
        userEntity.setEmail(this.getEmail());
        userEntity.setName(this.getName());
        userEntity.setUsername(this.getUsername());
        userEntity.setPassword(this.getPassword());
        userEntity.setRole(Role.valueOf(this.getRole()));
        return userEntity;
    }
}
