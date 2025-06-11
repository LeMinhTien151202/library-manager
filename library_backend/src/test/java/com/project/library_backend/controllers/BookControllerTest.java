package com.project.library_backend.controllers;

import com.project.library_backend.models.Book;
import com.project.library_backend.repositories.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/test.properties")
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    private Book book;
    private Book invalidBook;
    private Book book2;

    @BeforeEach
    @Transactional
    void initData() {
        // Clear database and flush to ensure no stale data
        bookRepository.deleteAll();
        bookRepository.flush();

        // Initialize test data without manual ID
        book = Book.builder()
                .title("Book 1")
                .author("Author 1")
                .publicationYear(2020L)
                .genre("Fiction")
                .build();

        invalidBook = Book.builder()
                .title("Invalid Book")
                .author("Author 2")
                .publicationYear(2025L) // Assume invalid future year for validation
                .genre("Fiction")
                .build();

        book2 = Book.builder()
                .title("Book 2")
                .author("Author 3")
                .publicationYear(2021L)
                .genre("Non-Fiction")
                .build();

        // Save test data to H2 database and flush to ensure data is written
        book = bookRepository.saveAndFlush(book);
        book2 = bookRepository.saveAndFlush(book2);

        // Debug: Verify data in database
        log.info("Books in DB after init: {}", bookRepository.findAll());
    }

    @Test
    void getBookAll_success() throws Exception {
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("Get list of books successfully"))
                .andExpect(jsonPath("data[0].id").value(book.getId().intValue()))
                .andExpect(jsonPath("data[0].title").value("Book 1"))
                .andExpect(jsonPath("data[0].author").value("Author 1"))
                .andExpect(jsonPath("data[0].publicationYear").value(2020))
                .andExpect(jsonPath("data[0].genre").value("Fiction"))
                .andExpect(jsonPath("data[1].id").value(book2.getId().intValue()))
                .andExpect(jsonPath("data[1].title").value("Book 2"))
                .andExpect(jsonPath("data[1].author").value("Author 3"))
                .andExpect(jsonPath("data[1].publicationYear").value(2021))
                .andExpect(jsonPath("data[1].genre").value("Non-Fiction"))
                .andDo(result -> log.info("Response: {}", result.getResponse().getContentAsString()));
    }

    @Test
    void getBookById_success() throws Exception {
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.id").value(book.getId().intValue()))
                .andExpect(jsonPath("data.title").value("Book 1"))
                .andExpect(jsonPath("data.author").value("Author 1"))
                .andExpect(jsonPath("data.publicationYear").value(2020))
                .andExpect(jsonPath("data.genre").value("Fiction"))
                .andExpect(jsonPath("message").value("Get book information successfully"));
    }

    @Test
    void getBookById_notFound_fail() throws Exception {
        // GIVEN
        Long nonExistentId = 999L;

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBook_success() throws Exception {
        // GIVEN
        MockMultipartFile thumbnailFile = new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", "thumbnail content".getBytes());

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/books")
                        .file(thumbnailFile)
                        .param("title", "New Book")
                        .param("author", "New Author")
                        .param("publicationYear", "2023")
                        .param("genre", "Fiction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("Create new book successfully"))
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("data.title").value("New Book"))
                .andExpect(jsonPath("data.author").value("New Author"))
                .andExpect(jsonPath("data.publicationYear").value(2023))
                .andExpect(jsonPath("data.genre").value("Fiction"))
                .andDo(result -> log.info("Response: {}", result.getResponse().getContentAsString()));
    }

    @Test
    void updateBook_success() throws Exception {
        // GIVEN
        MockMultipartFile thumbnailFile = new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", "thumbnail content".getBytes());

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/books/" + book.getId())
                        .file(thumbnailFile)
                        .with(request -> { request.setMethod("PUT"); return request; })
                        .param("title", "Updated Book")
                        .param("author", "Updated Author")
                        .param("publicationYear", "2022")
                        .param("genre", "Updated Fiction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("Update book successfully"))
                .andExpect(jsonPath("status").value("CREATED"))
                .andExpect(jsonPath("data.id").value(book.getId().intValue()))
                .andExpect(jsonPath("data.title").value("Updated Book"))
                .andExpect(jsonPath("data.author").value("Updated Author"))
                .andExpect(jsonPath("data.publicationYear").value(2022))
                .andExpect(jsonPath("data.genre").value("Updated Fiction"))
                .andDo(result -> log.info("Response: {}", result.getResponse().getContentAsString()));
    }

    @Test
    void updateBook_notFound_fail() throws Exception {
        // GIVEN
        Long nonExistentId = 999L;
        MockMultipartFile thumbnailFile = new MockMultipartFile("thumbnail", "thumbnail.jpg", "image/jpeg", "thumbnail content".getBytes());

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/books/" + nonExistentId)
                        .file(thumbnailFile)
                        .with(request -> { request.setMethod("PUT"); return request; })
                        .param("title", "Updated Book")
                        .param("author", "Updated Author")
                        .param("publicationYear", "2022")
                        .param("genre", "Updated Fiction"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBook_success() throws Exception {
        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("Delete book with id: " + book.getId() + " successfully"))
                .andExpect(jsonPath("status").value("OK"));
    }

    @Test
    void deleteBook_notFound_fail() throws Exception {
        // GIVEN
        Long nonExistentId = 999L;

        // WHEN, THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/books/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
}
