package com.project.library_backend.services;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Book;
import com.project.library_backend.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService{

    private final BookRepository bookRepository;

    @Value("${app.upload-dir}")
    private String uploadDir;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) throws Exception {
        return bookRepository.findById(id).
                orElseThrow(() -> new DataNotFoundException("Cannot find book with id: "+id));
    }

    @Override
    public Book saveBookWithThumbnail(Book book, MultipartFile thumbnailFile) throws IOException {

        // Xử lý upload file ảnh nếu có
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            // Tạo thư mục uploads nếu chưa tồn tại
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // Xóa file ảnh cũ nếu tồn tại
            if (book.getThumbnail() != null && !book.getThumbnail().isEmpty()) {
                File oldFile = new File(book.getThumbnail().substring(1)); // Bỏ dấu "/" đầu tiên
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // Tạo tên file duy nhất để tránh trùng lặp
            String fileName = UUID.randomUUID() + "_" + thumbnailFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // Lưu file vào thư mục uploads
            Files.write(filePath, thumbnailFile.getBytes());

            // Lưu đường dẫn file vào cột thumbnail
            book.setThumbnail("/" + uploadDir + fileName);
        }

        // Lưu book vào cơ sở dữ liệu
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book) throws Exception {
        return null;
    }

    @Override
    public void deleteBook(Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            // Xóa file ảnh nếu tồn tại
            if (book.getThumbnail() != null && !book.getThumbnail().isEmpty()) {
                File file = new File(book.getThumbnail().substring(1));
                if (file.exists()) {
                    file.delete();
                }
            }
            bookRepository.deleteById(id);
        }
    }
}
