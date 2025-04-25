package com.project.library_backend.services;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IBookService {
    List<Book> getAllBooks();
    Book getBookById(Long id) throws Exception;
    Book saveBookWithThumbnail(Book book, MultipartFile thumbnailFile) throws IOException;
    Book updateBook(Long id, Book book) throws Exception;
    void deleteBook(Long id);
}
