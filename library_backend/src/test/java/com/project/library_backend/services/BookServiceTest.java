package com.project.library_backend.services;
import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Book;
import com.project.library_backend.repositories.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/test.properties")
@Transactional
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @TempDir
    Path tempDir;

    @BeforeEach
    @Transactional
    void setUp() {
        // Làm sạch database trước khi mỗi test
        bookRepository.deleteAll();
        bookRepository.flush();

        // Cấu hình upload dir tạm thời
        System.setProperty("app.upload-dir", tempDir.toString() + "/");
        log.info("Temp upload dir set to: {}", tempDir.toString());
    }

    @Test
    void testSaveBookWithThumbnail_success() throws IOException {
        Book book = Book.builder()
                .title("Test Book")
                .author("Author A")
                .publicationYear(2022L)
                .genre("Science")
                .build();

        MockMultipartFile file = new MockMultipartFile("thumbnail", "image.jpg", "image/jpeg", "dummy image content".getBytes());

        Book savedBook = bookService.saveBookWithThumbnail(book, file);

        assertNotNull(savedBook.getId());
        assertNotNull(savedBook.getThumbnail());
        assertTrue(Files.exists(Path.of(savedBook.getThumbnail().substring(1))), "Thumbnail file should exist");
        log.info("Saved book with thumbnail: {}", savedBook);
    }

    @Test
    void testGetAllBooks_success() throws IOException {
        // Thêm 2 sách
        bookService.saveBookWithThumbnail(Book.builder()
                .title("Book 1")
                .author("Author 1")
                .publicationYear(2020L)
                .genre("Fiction")
                .build(), null);

        bookService.saveBookWithThumbnail(Book.builder()
                .title("Book 2")
                .author("Author 2")
                .publicationYear(2021L)
                .genre("Drama")
                .build(), null);

        List<Book> books = bookService.getAllBooks();

        assertEquals(2, books.size(), "Expected 2 books, but found: " + books.size());
        log.info("Retrieved books: {}", books);
    }

    @Test
    void testGetBookById_success() throws Exception {
        Book book = bookService.saveBookWithThumbnail(Book.builder()
                .title("Book by ID")
                .author("Author B")
                .publicationYear(2019L)
                .genre("Action")
                .build(), null);

        Book found = bookService.getBookById(book.getId());

        assertEquals(book.getTitle(), found.getTitle());
        assertEquals(book.getAuthor(), found.getAuthor());
        log.info("Found book: {}", found);
    }

    @Test
    void testGetBookById_notFound() {
        Long nonExistentId = 999L;
        Exception exception = assertThrows(DataNotFoundException.class, () -> {
            bookService.getBookById(nonExistentId);
        });

        assertEquals("Cannot find book with id: " + nonExistentId, exception.getMessage());
        log.info("Caught expected exception: {}", exception.getMessage());
    }

    @Test
    void testDeleteBook_success() throws Exception {
        Book book = bookService.saveBookWithThumbnail(Book.builder()
                .title("To be deleted")
                .author("Author D")
                .publicationYear(2018L)
                .genre("Fantasy")
                .build(), null);

        bookService.deleteBook(book.getId());

        assertFalse(bookRepository.existsById(book.getId()));
        log.info("Deleted book with id: {}", book.getId());
    }

    @Test
    void testDeleteBook_notFound() {
        Long nonExistentId = 1000L;

        Exception exception = assertThrows(DataNotFoundException.class, () -> {
            bookService.deleteBook(nonExistentId);
        });

        assertEquals("Cannot find book with id: " + nonExistentId, exception.getMessage());
        log.info("Caught expected exception: {}", exception.getMessage());
    }
}
