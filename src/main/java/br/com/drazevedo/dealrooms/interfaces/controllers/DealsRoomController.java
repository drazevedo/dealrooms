package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.application.dto.DealRoomDto;
import br.com.drazevedo.dealrooms.application.service.DealRoomService;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
@RolesAllowed("ADMIN")
public class DealsRoomController {

    @Autowired
    private DealRoomService service;

    @PostMapping("/api/dealrooms")
    public DealRoomDto create(@RequestBody String roomName) {

        return DealRoomDto.from(service.createRoom(roomName));
    }

    @GetMapping("/api/dealrooms")
    public List<DealRoomDto> find() {
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: currently locked to admins by roles allowed on the class

        if (Role.ADMIN.equals(loggedUser.getRole())) {
            return service.findAll().stream()
                    .map(DealRoomDto::from)
                    .toList();
        }else{
            return service.findByUser(loggedUser).stream()
                    .map(DealRoomDto::from)
                    .toList();
        }
    }

}
