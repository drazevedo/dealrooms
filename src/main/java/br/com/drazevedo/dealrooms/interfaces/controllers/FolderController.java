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

    @PostMapping("/api/dealrooms/folder/{folderId}")
    @RolesAllowed("ADMIN")
    public FolderDto createFolder(@PathVariable String folderId, @RequestBody String folderName) {

        return FolderDto.from(service.createFolderOn(folderId, folderName));
    }

    @PostMapping("/api/dealrooms/folder/{folderId}/viewers")
    @RolesAllowed("ADMIN")
    public FolderDto addViewersToFolder(@PathVariable String folderId, @RequestBody List<String> viewersUsernames ) {

        return service.addVieversToFolder(folderId, viewersUsernames);
    }

    @DeleteMapping("/api/dealrooms/folder/{folderId}/viewers")
    @RolesAllowed("ADMIN")
    public FolderDto deleteViewersFromFolder(@PathVariable String folderId, @RequestBody List<String> viewersUsernames ) {

        return service.deleteViewersFromRoomOrFolder(folderId, viewersUsernames);
    }

    @GetMapping("/api/dealrooms/folder/{folderId}")
    public FolderDto viewFolderInfo(@PathVariable String folderId) {
        User loggedUser = LoggedUserUtils.getLoggedUser();

        if (Role.ADMIN.equals(loggedUser.getRole())) {
            return service.getInfoFromRoomOrFolder(folderId);
        }else{
            FolderDto folderDto = service.getInfoFromRoomOrFolder(folderId);
            return folderDto.getViewers().contains(loggedUser.getUsername()) ? folderDto : null;
        }
    }

}
