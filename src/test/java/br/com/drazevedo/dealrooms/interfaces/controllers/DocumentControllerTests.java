package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import br.com.drazevedo.dealrooms.domain.model.Document;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.DocumentStatus;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
import br.com.drazevedo.dealrooms.domain.repository.DocumentRepository;
import br.com.drazevedo.dealrooms.domain.repository.FolderRepository;
import br.com.drazevedo.dealrooms.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.InputStream;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureDataMongo
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DocumentControllerTests {

    private static final Folder ROOT_ROOM = new DealRoom(null, "Root Room", "borrower1", "lawyer1", "borrower2","lawyer2");
    private static final List<Document> DOCUMENTS = List.of(
            new Document("Document 1", "path/to/folder", "1234", "root","borrower1", "lawyer1", "lawyer2"),
            new Document("Document 2", "path/to/folder", "1234", "root","borrower1", "lawyer1", "lawyer2"),
            new Document("Document 3", "path/to/folder", "1234", "root", "lawyer1", "lawyer2"));

    private static final List<User> USERS = List.of(
            new User(null, "Admin", "email1@email", "admin", "pass", Role.ADMIN),
            new User(null, "Borrower 1", "email2@email", "borrower1", "pass", Role.BORROWER),
            new User(null, "Borrower 2", "email3@email", "borrower2", "pass", Role.BORROWER),
            new User(null, "Lawyer 1", "email4@email", "lawyer1", "pass", Role.LAWYER),
            new User(null, "Lawyer 2", "email5@email", "lawyer2", "pass", Role.LAWYER));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeAll
    void beforeAll() {
        userRepository.saveAll(USERS);
        folderRepository.save(ROOT_ROOM);
        DOCUMENTS.forEach(folder -> folder.setParentFolderId(ROOT_ROOM.getId()));
        documentRepository.saveAll(DOCUMENTS);
    }

    @AfterAll
    void afterAll() {
        userRepository.deleteAll();
        folderRepository.deleteAll();
        documentRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "admin")
    void testAdminViewsDocumentsInFolders() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/dealrooms/folder/" + ROOT_ROOM.getId() + "/document" ));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Document 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Document 2")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Document 3")));
    }

    @Test
    @WithUserDetails(value = "lawyer1")
    void testLawyerViewsAllFilesInfo() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/dealrooms/folder/" + ROOT_ROOM.getId() + "/document" ));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Document 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Document 2")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Document 3")));
    }

    @Test
    @WithUserDetails(value = "borrower1")
    void testBorrowerDoesNotSeeLastFileInfo() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/dealrooms/folder/" + ROOT_ROOM.getId() + "/document" ));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Document 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Document 2")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Document 3"))));
    }

    @Test
    @WithUserDetails(value = "admin")
    void testAdminUploadSimpleFile() throws Exception {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream("testfiles/testFile.txt");

        byte[] content = is.readAllBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
            "file",
            "testFile.txt",
            "text/plain",
            content);

        ResultActions result = mockMvc.perform(multipart("/api/v1/dealrooms/folder/" + ROOT_ROOM.getId() + "/document" )
        .file(mockFile));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("testFile.txt")));
    }
    
    @Test
    @WithUserDetails(value = "admin")
    void testAdminUploadZipFileWithFolders() throws Exception {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream("testfiles/testZip.zip");

        byte[] content = is.readAllBytes();
        MockMultipartFile mockFile = new MockMultipartFile(
            "file",
            "testZip.zip",
            "application/zip",
            content);

        ResultActions result = mockMvc.perform(multipart("/api/v1/dealrooms/folder/" + ROOT_ROOM.getId() + "/document" )
        .file(mockFile));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("testFile 2.txt")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("testFile 3.txt")));
    }

    @Test
    @WithUserDetails(value = "admin")
    void testAdminAddsUsersToDocument() throws Exception {
        for (Document document : DOCUMENTS) {
            ResultActions result = mockMvc.perform(
                    post("/api/v1/dealrooms/document/" + document.getId() + "/viewers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("[\"borrower2\"]"));
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(document.getName())))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString("borrower2")));
        }
    }

    @Test
    @WithUserDetails(value = "admin")
    void testAdminRemovesUsersFromDocument() throws Exception {

        for (Document document : DOCUMENTS) {
            ResultActions result = mockMvc.perform(
                    delete("/api/v1/dealrooms/document/" + document.getId() + "/viewers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("[\"lawyer2\"]"));
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(document.getName())))
                    .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("lawyer2"))));
        }
    }

    @Test
    @WithUserDetails(value = "lawyer1")
    void testLawyerReviewsDocument() throws Exception {

        for (Document document : DOCUMENTS) {
            ResultActions result = mockMvc.perform(
                    post("/api/v1/dealrooms/document/" + document.getId() + "/review"));
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(document.getName())))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(DocumentStatus.CHECKED.name())));
        }
    }
}
