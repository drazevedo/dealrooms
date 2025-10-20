package br.com.drazevedo.dealrooms.application.service;

import br.com.drazevedo.dealrooms.application.dto.DocumentDto;
import br.com.drazevedo.dealrooms.domain.model.Document;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.DocumentStatus;
import br.com.drazevedo.dealrooms.domain.repository.DocumentRepository;
import br.com.drazevedo.dealrooms.domain.repository.FolderRepository;
import br.com.drazevedo.dealrooms.domain.repository.UserRepository;
import br.com.drazevedo.dealrooms.interfaces.controllers.utils.LoggedUserUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Path BASE_PATH = Path.of(System.getProperty("user.dir")).resolve("documents");

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

        User loggedUser = LoggedUserUtils.getLoggedUser();
        Folder parentFolder = folderRepository.findById(folderId).orElseThrow();
        File folderPath = createPath(folderId);
        String fileName = Objects.requireNonNull(file.getOriginalFilename());

        if (fileName.endsWith(".zip")){
            return handleMultipleFiles(file, parentFolder, folderPath, loggedUser);
        }

        file.transferTo( new File(folderPath, fileName));

        Document document = createAndSaveASingleDocument(fileName, folderPath, parentFolder.getId(), loggedUser);
        return List.of(DocumentDto.from(document));
    }

    private List<DocumentDto> handleMultipleFiles(MultipartFile file, Folder parentFolder, File folderPath, User loggedUser) throws IOException, NoSuchAlgorithmException {
        Path tempFile = Files.createTempFile("", file.getOriginalFilename());
        file.transferTo(tempFile);
        ZipInputStream zis = new ZipInputStream(new FileInputStream(tempFile.toFile()));
        ZipEntry zipEntry = zis.getNextEntry();
        List<Document> documents = new ArrayList<>();
        while (zipEntry != null) {
            if (!zipEntry.isDirectory()) {
                if (!zipEntry.getName().startsWith("__MACOSX/")) {
                    File newFile;
                    String[] directories = zipEntry.getName().split(File.separator);
                    File internalFolderPath = folderPath;
                    String internalFolderId = parentFolder.getId();
                    if(directories.length >1){
                        String name = directories[directories.length - 1];
                        directories = ArrayUtils.removeElement(directories, directories[directories.length-1]);
                        internalFolderId = createFoldersForDirectories(directories, parentFolder);
                        internalFolderPath = createPath(internalFolderId);
                        newFile = new File(internalFolderPath, name);
                    }else{
                        newFile = new File(internalFolderPath, zipEntry.getName());
                    }

                    System.out.println(zipEntry.getName());
                    transferZipEntryToFile(newFile, zis);
                    Document document = createAndSaveASingleDocument(newFile.getName(), internalFolderPath, internalFolderId, loggedUser);
                    documents.add(document);
                }

            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
        return documents.stream().map(DocumentDto::from).toList();
    }

    private String createFoldersForDirectories(String[] directories, Folder parentFolder) {
        Folder currentFolder = parentFolder;
        Folder nextFolder = null;
        for (String directory : directories) {

            nextFolder = currentFolder.getFolders().stream()
                    .filter(f -> f.getName().equals(directory))
                    .findFirst().orElse(null);
            if (nextFolder == null) {
                nextFolder = new Folder();
                nextFolder.setName(directory);
                nextFolder.setParentFolderId(currentFolder.getId());
                currentFolder.getFolders().add(nextFolder);
                folderRepository.save(nextFolder);
                currentFolder.getFolders().add(nextFolder);
                folderRepository.save(currentFolder);
            }
            currentFolder = nextFolder;
            nextFolder = null;
        }
        return currentFolder.getId();
    }

    private static void transferZipEntryToFile(File newFile, ZipInputStream zis) throws IOException {
        FileOutputStream fos = new FileOutputStream(newFile);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
    }

    private Document createAndSaveASingleDocument(String fileName, File folderPath, String parentFolderId, User loggedUser) throws IOException, NoSuchAlgorithmException {

        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] array = Files.readAllBytes(folderPath.toPath().resolve(fileName));
        String checksum = Base64.getEncoder().encodeToString(sha256.digest(array));

        Document document = new Document();
        document.setName(fileName);
        document.setPath(folderPath.getCanonicalPath());
        document.setParentFolderId(parentFolderId);
        document.setChecksum(checksum);
        document.setStatus(DocumentStatus.UNCHECKED);
        document.getViewers().add(loggedUser.getUsername());

        documentRepository.save(document);
        return document;
    }


    private File createPath(String folderId) {
        File rootFolder = BASE_PATH.toFile();
        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }
        File folder = BASE_PATH.resolve(folderId).toFile();
        if(!folder.exists()){
            folder.mkdirs();
        }
        return folder;
    }

    @Override
    public DocumentDto addVieversToDocument(String documentId, List<String> viewersUsernames) {
        Document document = documentRepository.findById(documentId).orElseThrow();
        Folder parentFolder = folderRepository.findById(document.getParentFolderId()).orElseThrow();

        List<String> invalidUserNames = viewersUsernames.stream()
                .filter(viewer -> !parentFolder.getViewers().contains(viewer))
                .toList();

        List<String> validUserNames = new ArrayList<>(viewersUsernames);
        validUserNames.removeAll(invalidUserNames);

        List<User> newViewers = userRepository.findAllByUsernameIn(validUserNames);
        document.getViewers().addAll(newViewers.stream().map(User::getUsername).toList());
        documentRepository.save(document);

        if(!invalidUserNames.isEmpty()) {
            throw new RuntimeException("These users don't have permission to view this folder: " + invalidUserNames);
        }
        return DocumentDto.from(document);
    }

    @Override
    public DocumentDto deleteViewersFromDocument(String documentId, List<String> viewersUsernames) {
        Document document = documentRepository.findById(documentId).orElseThrow();
        List<User> newViewers = userRepository.findAllByUsernameIn(viewersUsernames);
        newViewers.stream().map(User::getUsername).forEach(document.getViewers()::remove);
        documentRepository.save(document);

        return DocumentDto.from(document);
    }

    @Override
    public DocumentDto reviewDocument(String documentId) {
        User loggedUser = LoggedUserUtils.getLoggedUser();
        Document document = documentRepository.findById(documentId).orElseThrow();
        
        document.getViewers().stream().filter(username -> username.equals(loggedUser.getUsername())).findFirst().orElseThrow();

        document.setStatus(DocumentStatus.CHECKED);
        documentRepository.save(document);

        return DocumentDto.from(document);
    }
}
