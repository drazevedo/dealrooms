package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureDataMongo
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FolderControllerTests {

    private static final Folder ROOT_ROOM = new DealRoom(null, "Root Room", "borrower1", "lawyer1", "borrower3");
    private static final List<Folder> FOLDERS = List.of(
            new Folder(null, "folder 1", "borrower1", "lawyer1"),
            new Folder(null, "folder 2", "borrower1", "lawyer1"),
            new Folder(null, "folder 3 - only lawyer", "lawyer1"));

    private static final List<User> USERS = List.of(
            new User(null, "Admin", "email1@email", "admin", "pass", Role.ADMIN),
            new User(null, "Borrower 1", "email2@email", "borrower1", "pass", Role.BORROWER),
            new User(null, "Borrower 2", "email3@email", "borrower2", "pass", Role.BORROWER),
            new User(null, "Borrower 2", "email3@email", "borrower3", "pass", Role.BORROWER),
            new User(null, "Lawyer 1", "email5@email", "lawyer1", "pass", Role.LAWYER));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @BeforeAll
    void beforeAll() {
        userRepository.saveAll(USERS);
        folderRepository.save(ROOT_ROOM);
        FOLDERS.forEach(folder -> folder.setParentFolderId(ROOT_ROOM.getId()));
        folderRepository.saveAll(FOLDERS);
    }

    @AfterAll
    void afterAll() {
        userRepository.deleteAll();
        folderRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "admin")
    void testAdminViewsFolderInfo() throws Exception {
        for (Folder folder : FOLDERS) {
            ResultActions result = mockMvc.perform(get("/api/v1/dealrooms/folder/" + folder.getId()));
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(folder.getName())));
        }
    }

    @Test
    @WithUserDetails(value = "lawyer1")
    void testLawyerViewsFolderInfo() throws Exception {
        for (Folder folder : FOLDERS) {
            ResultActions result = mockMvc.perform(get("/api/v1/dealrooms/folder/" + folder.getId()));
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(folder.getName())));
        }
    }

    @Test
    @WithUserDetails(value = "borrower1")
    void testBorrowerDoesNotSeeLastFolderInfo() throws Exception {
        mockMvc.perform(get("/api/v1/dealrooms/folder/" + FOLDERS.getFirst().getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("folder 1")));

        mockMvc.perform(get("/api/v1/dealrooms/folder/" + FOLDERS.getLast().getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithUserDetails(value = "borrower2")
    void testBorrowerTwoSeesNothing() throws Exception {
        for (Folder folder : FOLDERS) {
            ResultActions result = mockMvc.perform(get("/api/v1/dealrooms/folder/" + folder.getId()));
            result
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
        }
    }

    @Test
    @WithUserDetails(value = "admin")
    void testAdminCreatesFolderInsideFolder() throws Exception {
        for (Folder folder : FOLDERS) {
            ResultActions result = mockMvc.perform(
                    post("/api/v1/dealrooms/folder/" + folder.getId())
                    .content("New Folder Inside Folder"));
            result
                    .andExpect(status().isOk())
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(folder.getId())))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString("New Folder Inside Folder")));
        }
    }


    @Test
    @WithUserDetails(value = "admin")
    void testAdminAddsUsersToFolders() throws Exception {
        for (Folder folder : FOLDERS) {
            ResultActions result = mockMvc.perform(
                    post("/api/v1/dealrooms/folder/" + folder.getId() + "/viewers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("[\"borrower3\"]"));
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(folder.getName())))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString("borrower3")));
        }
    }

    @Test
    @WithUserDetails(value = "admin")
    void testAdminRemovesUsersFromFolders() throws Exception {

        for (Folder folder : FOLDERS) {
            ResultActions result = mockMvc.perform(
                    delete("/api/v1/dealrooms/folder/" + folder.getId() + "/viewers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("[\"borrower3\"]"));
            result
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(org.hamcrest.Matchers.containsString(folder.getName())))
                    .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("borrower3"))));
        }
    }
}
