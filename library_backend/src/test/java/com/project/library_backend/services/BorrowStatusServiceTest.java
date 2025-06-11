package com.project.library_backend.services;

import com.project.library_backend.dtos.BorrowStatusDTO;
import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Book;
import com.project.library_backend.models.BorrowStatus;
import com.project.library_backend.models.User;
import com.project.library_backend.repositories.BookRepository;
import com.project.library_backend.repositories.BorrowStatusRepository;
import com.project.library_backend.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/test.properties")
@Transactional
public class BorrowStatusServiceTest {

    @Autowired
    private BorrowStatusService borrowStatusService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository borrowerRepository;

    @Autowired
    private BorrowStatusRepository borrowStatusRepository;

    private Book savedBook;
    private User savedBorrower;

    @BeforeEach
    void setUp() {
        savedBook = bookRepository.save(Book.builder()
                .title("Test Book")
                .author("Test Author")
                .publicationYear(2023L)
                .genre("Novel")
                .build());

        savedBorrower = borrowerRepository.save(User.builder()
                .name("Test Borrower")
                .email("test@example.com")
                .phone("0123456789")
                .build());
    }

    @Test
    void testCreateBorrowStatus_success() throws Exception {
        BorrowStatusDTO dto = new BorrowStatusDTO();
        dto.setBookId(savedBook.getId());
        dto.setBorrowerId(savedBorrower.getId());
        dto.setStatus("BORROWED");
        dto.setBorrowDate(LocalDate.now());
        dto.setReturnDate(LocalDate.now().plusDays(7));

        BorrowStatus created = borrowStatusService.createBorrowStatus(dto);

        assertNotNull(created.getBorrowId());
        assertEquals("BORROWED", created.getStatus());
        assertEquals(savedBook.getId(), created.getBook().getId());
        assertEquals(savedBorrower.getId(), created.getBorrower().getId());
    }

    @Test
    void testGetAllBorrowStatus_success() throws Exception {
        BorrowStatusDTO dto = new BorrowStatusDTO();
        dto.setBookId(savedBook.getId());
        dto.setBorrowerId(savedBorrower.getId());
        dto.setStatus("BORROWED");
        dto.setBorrowDate(LocalDate.now());
        dto.setReturnDate(LocalDate.now().plusDays(7));
        borrowStatusService.createBorrowStatus(dto);

        List<BorrowStatus> list = borrowStatusService.getAllBorrowStatus();

        assertFalse(list.isEmpty());
    }

    @Test
    void testGetBorrowStatusById_success() throws Exception {
        BorrowStatusDTO dto = new BorrowStatusDTO();
        dto.setBookId(savedBook.getId());
        dto.setBorrowerId(savedBorrower.getId());
        dto.setStatus("RETURNED");
        dto.setBorrowDate(LocalDate.now());
        dto.setReturnDate(LocalDate.now().plusDays(10));
        BorrowStatus saved = borrowStatusService.createBorrowStatus(dto);

        BorrowStatus found = borrowStatusService.getBorrowStatusById(saved.getBorrowId());

        assertEquals("RETURNED", found.getStatus());
    }

    @Test
    void testGetBorrowStatusById_notFound() {
        Long invalidId = 999L;
        Exception exception = assertThrows(DataNotFoundException.class, () -> {
            borrowStatusService.getBorrowStatusById(invalidId);
        });

        assertEquals("Cannot find borrowStatus with id =" + invalidId, exception.getMessage());
    }

    @Test
    void testUpdateBorrowStatus_success() throws Exception {
        BorrowStatusDTO createDTO = new BorrowStatusDTO();
        createDTO.setBookId(savedBook.getId());
        createDTO.setBorrowerId(savedBorrower.getId());
        createDTO.setStatus("BORROWED");
        createDTO.setBorrowDate(LocalDate.now());
        createDTO.setReturnDate(LocalDate.now().plusDays(5));
        BorrowStatus saved = borrowStatusService.createBorrowStatus(createDTO);

        BorrowStatusDTO updateDTO = new BorrowStatusDTO();
        updateDTO.setBookId(savedBook.getId());
        updateDTO.setBorrowerId(savedBorrower.getId());
        updateDTO.setStatus("RETURNED");
        updateDTO.setBorrowDate(LocalDate.now().minusDays(2));
        updateDTO.setReturnDate(LocalDate.now().plusDays(3));

        BorrowStatus updated = borrowStatusService.updateBorrowStatus(saved.getBorrowId(), updateDTO);

        assertEquals("RETURNED", updated.getStatus());
        assertEquals(LocalDate.now().plusDays(3), updated.getReturnDate());
    }

    @Test
    void testDeleteBorrowStatus_success() throws Exception {
        BorrowStatusDTO dto = new BorrowStatusDTO();
        dto.setBookId(savedBook.getId());
        dto.setBorrowerId(savedBorrower.getId());
        dto.setStatus("BORROWED");
        dto.setBorrowDate(LocalDate.now());
        dto.setReturnDate(LocalDate.now().plusDays(5));
        BorrowStatus saved = borrowStatusService.createBorrowStatus(dto);

        borrowStatusService.deleteBorrowStatus(saved.getBorrowId());

        assertFalse(borrowStatusRepository.existsById(saved.getBorrowId()));
    }
    @Test
    void testGetBorrowStatusById_NotFound_ShouldThrowException() {
        Long invalidId = 999L;
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            borrowStatusService.getBorrowStatusById(invalidId);
        });
    }

    @Test
    void testCreateBorrowStatus_BookNotFound_ShouldThrowException() {
        BorrowStatusDTO dto = BorrowStatusDTO.builder()
                .bookId(999L) // book không tồn tại
                .borrowerId(1L) // giả sử tồn tại
                .status("Pending")
                .borrowDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7))
                .build();

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            borrowStatusService.createBorrowStatus(dto);
        });
    }

    @Test
    void testCreateBorrowStatus_BorrowerNotFound_ShouldThrowException() {
        BorrowStatusDTO dto = BorrowStatusDTO.builder()
                .bookId(1L) // giả sử tồn tại
                .borrowerId(999L) // borrower không tồn tại
                .status("Pending")
                .borrowDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(7))
                .build();

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            borrowStatusService.createBorrowStatus(dto);
        });
    }

    @Test
    void testUpdateBorrowStatus_BorrowStatusNotFound_ShouldThrowException() {
        BorrowStatusDTO dto = BorrowStatusDTO.builder()
                .bookId(1L)
                .borrowerId(1L)
                .status("Updated")
                .borrowDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(10))
                .build();

        Long invalidId = 999L;

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            borrowStatusService.updateBorrowStatus(invalidId, dto);
        });
    }


}
