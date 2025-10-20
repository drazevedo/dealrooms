package br.com.drazevedo.dealrooms.interfaces.controllers;

import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
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
class UserControllerTests {

    private static final List<User> USERS = List.of(new User(null, "Name1", "email1@email", "admin", "pass", Role.ADMIN),
            new User(null, "Name2", "email2@email", "borrower", "pass", Role.BORROWER),
            new User(null, "Name3", "email3@email", "lawyer", "pass", Role.LAWYER));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void beforeAll() {
        userRepository.saveAll(USERS);
    }

    @AfterAll
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    @WithUserDetails(value = "admin")
    void testFindAllUsersOkWithAdminRole() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/users"));
        result
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("admin")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("borrower")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("lawyer")));
    }

    @Test
    @WithUserDetails(value = "lawyer")
    void testFindAllUsersFailWithLawyerRole() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/users"));
        result
                .andExpect(status().isForbidden());
    }

}
