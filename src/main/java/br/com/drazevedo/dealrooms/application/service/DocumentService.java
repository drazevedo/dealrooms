package br.com.drazevedo.dealrooms.application.service;

import br.com.drazevedo.dealrooms.application.dto.DocumentDto;
import br.com.drazevedo.dealrooms.application.dto.FolderDto;
import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import br.com.drazevedo.dealrooms.domain.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface DocumentService {

    List<DocumentDto> getDocumentsOn(String folderId);

    List<DocumentDto> uploadDocument(String folderId, MultipartFile file) throws IOException, NoSuchAlgorithmException;

    DocumentDto addVieversToDocument(String documentId, List<String> viewersUsernames);

    DocumentDto deleteViewersFromDocument(String documentId, List<String> viewersUsernames);

    DocumentDto reviewDocument(String documentId);
}
