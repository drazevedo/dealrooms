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
public class FolderController {

    private final FolderService service;

    public FolderController(FolderService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/dealrooms/folder/{folderId}")
    public FolderDto viewFolderInfo(@PathVariable String folderId) {
        User loggedUser = LoggedUserUtils.getLoggedUser();

        // Admins dont need access to view anything
        if (Role.ADMIN.equals(loggedUser.getRole())) {
            return service.getInfoFromRoomOrFolder(folderId);
        }else{
            FolderDto folderDto = service.getInfoFromRoomOrFolder(folderId);
            return folderDto.getViewers().contains(loggedUser.getUsername()) ? folderDto : null;
        }
    }

    @PostMapping("/api/v1/dealrooms/folder/{folderId}")
    @RolesAllowed("ADMIN")
    public FolderDto createFolder(@PathVariable String folderId, @RequestBody String folderName) {

        return FolderDto.from(service.createFolderOn(folderId, folderName));
    }

    @PostMapping("/api/v1/dealrooms/folder/{folderId}/viewers")
    @RolesAllowed("ADMIN")
    public FolderDto addViewersToFolder(@PathVariable String folderId, @RequestBody List<String> viewersUsernames ) {

        return service.addVieversToFolder(folderId, viewersUsernames);
    }

    @DeleteMapping("/api/v1/dealrooms/folder/{folderId}/viewers")
    @RolesAllowed("ADMIN")
    public FolderDto deleteViewersFromFolder(@PathVariable String folderId, @RequestBody List<String> viewersUsernames ) {

        return service.deleteViewersFromRoomOrFolder(folderId, viewersUsernames);
    }

}
