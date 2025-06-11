package com.project.library_backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.library_backend.dtos.BorrowStatusDTO;
import com.project.library_backend.models.Book;
import com.project.library_backend.models.BorrowStatus;
import com.project.library_backend.models.User;
import com.project.library_backend.repositories.BookRepository;
import com.project.library_backend.repositories.BorrowStatusRepository;
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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/test.properties")
public class BorrowStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BorrowStatusRepository borrowStatusRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository borrowerRepository;

    private Book book;
    private User borrower;
    private BorrowStatus borrowStatus;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        borrowStatusRepository.deleteAll();
        borrowerRepository.deleteAll();
        bookRepository.deleteAll();

        book = bookRepository.save(Book.builder()
                .title("Test Book")
                .author("Author")
                .build());

        borrower = borrowerRepository.save(User.builder()
                .name("Test Borrower")
                .email("test@library.com")
                .phone("0123456789")
                .build());

        borrowStatus = borrowStatusRepository.save(BorrowStatus.builder()
                .book(book)
                .borrower(borrower)
                .borrowDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7))
                .status("BORROWED")
                .build());
    }

    @Test
    void getAllBorrowStatuses_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/borrow_status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Get list of borrowStatus successfully"))
                .andExpect(jsonPath("$.data[0].status").value("BORROWED"));
    }

    @Test
    void getBorrowStatusById_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrow_status/" + borrowStatus.getBorrowId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("BORROWED"))
                .andExpect(jsonPath("$.message").value("Get borrowStatus information successfully"));
    }

    @Test
    void getBorrowStatusById_notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrow_status/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBorrowStatus_success() throws Exception {
        BorrowStatusDTO dto = BorrowStatusDTO.builder()
                .bookId(book.getId())
                .borrowerId(borrower.getId())
                .borrowDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(10))
                .status("BORROWED")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/borrow_status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("BORROWED"))
                .andExpect(jsonPath("$.message").value("add borrowStatus successfully"));
    }

    @Test
    void updateBorrowStatus_success() throws Exception {
        BorrowStatusDTO dto = BorrowStatusDTO.builder()
                .bookId(book.getId())
                .borrowerId(borrower.getId())
                .borrowDate(LocalDate.now().minusDays(1))
                .returnDate(LocalDate.now().plusDays(3))
                .status("RETURNED")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/borrow_status/" + borrowStatus.getBorrowId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("RETURNED"))
                .andExpect(jsonPath("$.message").value("Update borrowStatus successfully"));
    }

    @Test
    void deleteBorrowStatus_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/borrow_status/" + borrowStatus.getBorrowId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Delete subject with id: " + borrowStatus.getBorrowId() + " successfully"));
    }
}
