package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.application.dto.DocumentDto;
import br.com.drazevedo.dealrooms.application.dto.FolderDto;
import br.com.drazevedo.dealrooms.application.service.DocumentService;
import br.com.drazevedo.dealrooms.application.service.FolderService;
import br.com.drazevedo.dealrooms.domain.model.Document;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
import br.com.drazevedo.dealrooms.interfaces.controllers.utils.LoggedUserUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@SecurityRequirement(name = "basicAuth")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/dealrooms/folder/{folderId}/document")
    @RolesAllowed({"ADMIN", "BORROWER", "LAWYER"})
    public List<DocumentDto> viewDocumentsOnFolder(@PathVariable String folderId) {
        User loggedUser = LoggedUserUtils.getLoggedUser();

        if (Role.ADMIN.equals(loggedUser.getRole())) {
            return service.getDocumentsOn(folderId);
        }else{
            List<DocumentDto> documents = service.getDocumentsOn(folderId);
            return documents.stream()
                    .filter(doc -> doc.getViewers().contains(loggedUser.getUsername()))
                    .toList();
        }
    }

    @PostMapping(value = "/api/v1/dealrooms/folder/{folderId}/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed({"ADMIN", "BORROWER", "LAWYER"})
    @SneakyThrows
    public List<DocumentDto> createDocument(@PathVariable String folderId, @RequestPart("file") MultipartFile file) {

        return service.uploadDocument(folderId, file);
    }

    @PostMapping("/api/v1/dealrooms/document/{documentId}/viewers")
    @RolesAllowed("ADMIN")
    public DocumentDto addViewersToFolder(@PathVariable String documentId, @RequestBody List<String> viewersUsernames ) {

        return service.addVieversToDocument(documentId, viewersUsernames);
    }

    @DeleteMapping("/api/v1/dealrooms/document/{documentId}/viewers")
    @RolesAllowed("ADMIN")
    public DocumentDto deleteViewersFromFolder(@PathVariable String documentId, @RequestBody List<String> viewersUsernames ) {

        return service.deleteViewersFromDocument(documentId, viewersUsernames);
    }

    @PostMapping("/api/v1/dealrooms/document/{documentId}/review")
    @RolesAllowed("LAWYER")
    public DocumentDto reviewDocument(@PathVariable String documentId) {

        return service.reviewDocument(documentId);
    }
}
