package com.project.library_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.library_backend.models.User;
import com.project.library_backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository borrowerRepository;

    private User borrower;
    private User invalidBorrower;
    private User borrower2;

    @BeforeEach
    @Transactional
    void initData() {
        // Clear database and flush to ensure no stale data
        borrowerRepository.deleteAll();
        borrowerRepository.flush();

        // Initialize test data without manual ID
        borrower = User.builder()
                .name("Tien Le")
                .email("tienle1@gmail.com")
                .phone("0987654321")
                .build();

        invalidBorrower = User.builder()
                .name("Tien Le")
                .email("tienle1@gmail.com")
                .phone("0987654321091") // Invalid phone
                .build();

        borrower2 = User.builder()
                .name("Jane Doe")
                .email("jane@gmail.com")
                .phone("0987654322")
                .build();

        // Save test data to H2 database and flush to ensure data is written
        borrower = borrowerRepository.saveAndFlush(borrower);
        borrower2 = borrowerRepository.saveAndFlush(borrower2);

        // Debug: Verify data in database
        log.info("Borrowers in DB after init: {}", borrowerRepository.findAll());
    }

    @Test
    void createBorrower_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        User newBorrower = User.builder()
                .name("John Doe")
                .email("john@gmail.com")
                .phone("0123456789")
                .build();
        String content = objectMapper.writeValueAsString(newBorrower);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.name")
                        .value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.email")
                        .value("john@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.phone")
                        .value("0123456789"));
    }

    @Test
    void getAllBorrowers_success() throws Exception {
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Get list of borrower successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].id")
                        .value(borrower.getId().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].name")
                        .value("Tien Le"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].email")
                        .value("tienle1@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[0].phone")
                        .value("0987654321"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].id")
                        .value(borrower2.getId().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].name")
                        .value("Jane Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].email")
                        .value("jane@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("data[1].phone")
                        .value("0987654322"))
                .andDo(result -> log.info("Response: {}", result.getResponse().getContentAsString()));
    }

    @Test
    void createBorrower_phoneInvalid_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(invalidBorrower);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Phone must be between 10 and 12 characters"));
    }

    @Test
    void getBorrowerById_success() throws Exception {
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrowers/" + borrower.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.id")
                        .value(borrower.getId().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("data.name")
                        .value("Tien Le"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.email")
                        .value("tienle1@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.phone")
                        .value("0987654321"));
    }

    @Test
    void getBorrowerById_notFound_fail() throws Exception {
        // GIVEN
        Long nonExistentId = 999L;

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrowers/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateBorrower_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        User updatedBorrower = User.builder()
                .name("Tien Le Updated")
                .email("tienle.updated@gmail.com")
                .phone("0987654321")
                .build();
        String content = objectMapper.writeValueAsString(updatedBorrower);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/borrowers/" + borrower.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Update borrower successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.id")
                        .value(borrower.getId().intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("data.name")
                        .value("Tien Le Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.email")
                        .value("tienle.updated@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.phone")
                        .value("0987654321"))
                .andDo(result -> log.info("Response: {}", result.getResponse().getContentAsString()));
    }

    @Test
    void updateBorrower_phoneInvalid_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(invalidBorrower);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/borrowers/" + borrower.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Phone must be between 10 and 12 characters"));
    }

    @Test
    void updateBorrower_notFound_fail() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        User updatedBorrower = User.builder()
                .name("Tien Le Updated")
                .email("tienle.updated@gmail.com")
                .phone("0987654321")
                .build();
        String content = objectMapper.writeValueAsString(updatedBorrower);
        Long nonExistentId = 999L;

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/borrowers/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}