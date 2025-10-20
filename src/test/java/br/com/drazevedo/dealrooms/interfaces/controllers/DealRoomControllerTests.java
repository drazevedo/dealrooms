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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureDataMongo
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DealRoomControllerTests {

    private static final List<Folder> ROOMS = List.of(
            new DealRoom(null, "room 1", "borrower1", "lawyer1"),
            new DealRoom(null, "room 2", "borrower2", "lawyer1"),
            new DealRoom(null, "room 3", "borrower3", "lawyer2"));

    private static final List<User> USERS = List.of(
            new User(null, "Admin", "email1@email", "admin", "pass", Role.ADMIN),
            new User(null, "Borrower 1", "email2@email", "borrower1", "pass", Role.BORROWER),
            new User(null, "Borrower 2", "email3@email", "borrower2", "pass", Role.BORROWER),
            new User(null, "Borrower 3", "email4@email", "borrower3", "pass", Role.BORROWER),
            new User(null, "Lawyer 1", "email5@email", "lawyer1", "pass", Role.LAWYER),
            new User(null, "Lawyer 2", "email6@email", "lawyer2", "pass", Role.LAWYER),
            new User(null, "Lawyer 3", "email6@email", "lawyer3", "pass", Role.LAWYER));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @BeforeAll
    void beforeAll() {
        userRepository.saveAll(USERS);
        folderRepository.saveAll(ROOMS);
    }

    @AfterAll
    void afterAll() {
        userRepository.deleteAll();
        folderRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "admin")
    void testAdminSeesAllRooms() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/dealrooms"));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("room 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("room 2")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("room 3")));
    }

    @Test
    @WithUserDetails(value = "lawyer1")
    void testLawyerOneOnlySeesTwoRooms() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/dealrooms"));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("room 1")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("room 2")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("room 3"))));
    }

    @Test
    @WithUserDetails(value = "borrower1")
    void testBorrowerOneOnlySeesOneRoom() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/dealrooms"));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("room 1")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("room 2"))))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("room 3"))));
    }

    @Test
    @WithUserDetails(value = "lawyer3")
    void testLawyerThreeSeesNothing() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/dealrooms"));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("[]"));
    }


}
