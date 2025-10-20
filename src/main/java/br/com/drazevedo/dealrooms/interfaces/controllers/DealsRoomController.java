package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.application.dto.FolderDto;
import br.com.drazevedo.dealrooms.application.service.FolderService;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
import br.com.drazevedo.dealrooms.interfaces.controllers.utils.LoggedUserUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
public class DealsRoomController {

    @Autowired
    private FolderService service;

    @PostMapping("/api/dealrooms")
    @RolesAllowed("ADMIN")
    public FolderDto create(@RequestBody String roomName) {

        return FolderDto.from(service.createRoom(roomName));
    }

    @GetMapping("/api/dealrooms")
    public List<FolderDto> findAllRooms() {
        User loggedUser = LoggedUserUtils.getLoggedUser();

        if (Role.ADMIN.equals(loggedUser.getRole())) {
            return service.findAllRootFolders().stream()
                    .map(FolderDto::from)
                    .toList();
        }else{
            return service.findAllRootFoldersByUser(loggedUser).stream()
                    .map(FolderDto::from)
                    .toList();
        }
    }

    @PostMapping("/api/dealrooms/{roomId}/viewers")
    @RolesAllowed("ADMIN")
    public FolderDto addViewersToRoom(@PathVariable String roomId, @RequestBody List<String> viewersUsernames ) {

        return service.addVieversToRoom(roomId, viewersUsernames);
    }

    @DeleteMapping("/api/dealrooms/{roomId}/viewers")
    @RolesAllowed("ADMIN")
    public FolderDto deleteViewersFromRoom(@PathVariable String roomId, @RequestBody List<String> viewersUsernames ) {

        return service.deleteViewersFromRoomOrFolder(roomId, viewersUsernames);
    }

    @GetMapping("/api/dealrooms/{roomId}")
    public FolderDto viewRoomInfo(@PathVariable String roomId) {
        User loggedUser = LoggedUserUtils.getLoggedUser();

        if (Role.ADMIN.equals(loggedUser.getRole())) {
            return service.getInfoFromRoomOrFolder(roomId);
        }else{
            FolderDto folderDto = service.getInfoFromRoomOrFolder(roomId);
            return folderDto.getViewers().contains(loggedUser.getUsername()) ? folderDto : null;
        }
    }

}
