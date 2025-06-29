package com.project.library_backend.controllers;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Book;
import com.project.library_backend.responses.ResponseObject;
import com.project.library_backend.services.IBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BookController {
    //test thành công github ở 2 máy
    private final IBookService bookService;

    @GetMapping("")
    public ResponseEntity<?> getBookAll() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of books successfully")
                .status(HttpStatus.OK)
                .data(books)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBookById(
            @PathVariable("id") Long bookId
    ) throws Exception {
        Book book = bookService.getBookById(bookId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(book)
                .message("Get book information successfully")
                .status(HttpStatus.OK)
                .build());
    }

    //    @PostMapping("")
//    //Nếu tham số truyền vào là 1 object thì sao ? => Data Transfer Object = Request Object
//    public ResponseEntity<?> insertBook(
//            @Valid @RequestBody Book book,
//            BindingResult result) {
//        if(result.hasErrors()) {
//            List<String> errorMessages = result.getFieldErrors()
//                    .stream()
//                    .map(FieldError::getDefaultMessage)
//                    .toList();
//            return ResponseEntity.badRequest().body(errorMessages);
//        }
//        Book newBook = bookService.createBook(book);
//        return ResponseEntity.ok(newBook);
//
//    }
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseObject> createBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publicationYear") Long publicationYear,
            @RequestParam("genre") String genre,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnailFile) throws IOException {

        // Tạo đối tượng Book
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(publicationYear);
        book.setGenre(genre);

        // Gọi service để lưu book và xử lý thumbnail
        Book savedBook = bookService.saveBookWithThumbnail(book, thumbnailFile);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Create new book successfully")
                        .status(HttpStatus.CREATED)
                        .data(savedBook)
                        .build());
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseObject> updateBook(
            @PathVariable("id") Long bookId,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publicationYear") Long publicationYear,
            @RequestParam("genre") String genre,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnailFile) throws Exception {

       //Book existingBookOpt = bookService.getBookById(id);
        Book existingBookOpt = bookService.getBookById(bookId);

        Book book = existingBookOpt;
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(publicationYear);
        book.setGenre(genre);

        // Gọi service để lưu book và xử lý thumbnail
        Book updatedBook = bookService.saveBookWithThumbnail(book, thumbnailFile);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("Update book successfully")
                        .status(HttpStatus.CREATED)
                        .data(updatedBook)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBook(@PathVariable Long id) throws Exception,DataNotFoundException {
        bookService.deleteBook(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Delete book with id: "+id+" successfully")
                        .build());
    }

}
