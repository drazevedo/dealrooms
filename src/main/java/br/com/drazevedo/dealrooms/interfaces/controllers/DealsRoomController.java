package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.application.dto.FolderDto;
import br.com.drazevedo.dealrooms.application.service.FolderService;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
import br.com.drazevedo.dealrooms.interfaces.controllers.utils.LoggedUserUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
public class DealsRoomController {

    private final FolderService service;

    public DealsRoomController(FolderService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/dealrooms")
    public List<FolderDto> findAllRooms() {
        User loggedUser = LoggedUserUtils.getLoggedUser();

        // Admins dont need access to view anything
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

    @PostMapping("/api/v1/dealrooms")
    @RolesAllowed("ADMIN")
    public FolderDto create(@RequestBody String roomName) {

        return FolderDto.from(service.createRoom(roomName));
    }

    @PostMapping("/api/v1/dealrooms/{roomId}/viewers")
    @RolesAllowed("ADMIN")
    public FolderDto addViewersToRoom(@PathVariable String roomId, @RequestBody List<String> viewersUsernames ) {

        return service.addVieversToRoom(roomId, viewersUsernames);
    }

    @DeleteMapping("/api/v1/dealrooms/{roomId}/viewers")
    @RolesAllowed("ADMIN")
    public FolderDto deleteViewersFromRoom(@PathVariable String roomId, @RequestBody List<String> viewersUsernames ) {

        return service.deleteViewersFromRoomOrFolder(roomId, viewersUsernames);
    }

    @GetMapping("/api/v1/dealrooms/{roomId}")
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
