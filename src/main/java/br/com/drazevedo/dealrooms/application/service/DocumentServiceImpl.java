package br.com.drazevedo.dealrooms.application.service;

import br.com.drazevedo.dealrooms.application.dto.DocumentDto;
import br.com.drazevedo.dealrooms.application.dto.FolderDto;
import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.Document;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.DocumentStatus;
import br.com.drazevedo.dealrooms.domain.repository.DocumentRepository;
import br.com.drazevedo.dealrooms.domain.repository.FolderRepository;
import br.com.drazevedo.dealrooms.domain.repository.UserRepository;
import br.com.drazevedo.dealrooms.interfaces.controllers.utils.LoggedUserUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Path BASE_PATH = Path.of(System.getProperty("user.dir"));

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public DocumentServiceImpl(FolderRepository folderRepository, DocumentRepository documentRepository, UserRepository userRepository) {
        this.folderRepository = folderRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<DocumentDto> getDocumentsOn(String folderId) {
        return documentRepository.findByParentFolderId(folderId).stream()
                .map(DocumentDto::from).toList();
    }

    @Override
    public List<DocumentDto> uploadDocument(String folderId, MultipartFile file) throws IOException, NoSuchAlgorithmException {
        if (file.isEmpty()) {
            return Collections.emptyList();
        }
        Folder parent = folderRepository.findById(folderId).orElseThrow();
        if (file.getName().endsWith(".zip")){
            // multiple documents and folders
        }

        User loggedUser = LoggedUserUtils.getLoggedUser();

        String filePath = createPath(folderId);
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        file.transferTo( new File(filePath, fileName));

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] array = Files.readAllBytes(Path.of(filePath).resolve(fileName));
        String checksum = Base64.getEncoder().encodeToString(sha256.digest(array));

        Document document = new Document();
        document.setName(fileName);
        document.setPath(filePath);
        document.setParentFolder(parent);
        document.setChecksum(checksum);
        document.setStatus(DocumentStatus.UNCHECKED);
        document.getViewers().add(loggedUser.getUsername());

        documentRepository.save(document);
        return List.of(DocumentDto.from(document));
    }

    private String createPath(String folderId) {
        File rootFolder = BASE_PATH.toFile();
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        File folder = BASE_PATH.resolve(folderId).toFile();
        if(!folder.exists()){
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    @Override
    public DocumentDto addVieversToDocument(String documentId, List<String> viewersUsernames) {
        return null;
    }

    @Override
    public DocumentDto deleteViewersFromDocument(String documentId, List<String> viewersUsernames) {
        return null;
    }
}
