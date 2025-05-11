package com.project.library_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.library_backend.models.Borrower;
import com.project.library_backend.services.BorrowerService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class BorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BorrowerService borrowerService;

    private Borrower borrower;

    private Borrower borrowerData;

    @BeforeEach
    void initData(){
        borrower = Borrower.builder()
                .name("Tien le")
                .email("tienle1@gmail.com")
                .phone("0987654321").build();

        borrowerData = Borrower.builder()
                .name("Tien le")
                .email("tienle1@gmail.com")
                .phone("0987654321091").build();
    }

    @Test
    void createBorrower_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(borrower);

        Mockito.when(borrowerService.createBorrower(ArgumentMatchers.any()))
                .thenReturn(borrower);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("data.name")
                        .value("Tien le"))
                .andExpect(MockMvcResultMatchers.jsonPath("data.phone")
                .value("0987654321"));

    }

    @Test
        //
    void createUser_usernameInvalid_fail() throws Exception {
        // GIVEN
        //request.setUsername("joh");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(borrowerData);

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/borrowers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message")
                        .value("Phone must be between 10 and 12 characters")
                );
    }

}
