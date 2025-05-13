package com.project.library_backend.services;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Borrower;
import com.project.library_backend.repositories.BorrowerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/test.properties")
public class BorrowerServiceTest {
    @Autowired
    private BorrowerService borrowerService;

    @Autowired
    private BorrowerRepository borrowerRepository;

    private Borrower borrower;
    private Borrower borrower2;
    private Borrower invalidBorrower;

    @BeforeEach
    @Transactional
    void initData() {
        // Clear database and flush to ensure no stale data
        borrowerRepository.deleteAll();
        borrowerRepository.flush();

        // Initialize test data
        borrower = Borrower.builder()
                .name("Tien Le")
                .email("tienle1@gmail.com")
                .phone("0987654321")
                .build();

        borrower2 = Borrower.builder()
                .name("Jane Doe")
                .email("jane@gmail.com")
                .phone("0987654322")
                .build();

        invalidBorrower = Borrower.builder()
                .name("Tien Le")
                .email("tienle1@gmail.com")
                .phone("0987654321091") // Invalid phone
                .build();

        // Save test data to H2 database
        borrower = borrowerRepository.saveAndFlush(borrower);
        borrower2 = borrowerRepository.saveAndFlush(borrower2);
    }

    @Test
    void getAllBorrowers_success() {
        // WHEN
        List<Borrower> borrowers = borrowerService.getAllBorrowers();

        // THEN
        assertEquals(2, borrowers.size());
        assertEquals("Tien Le", borrowers.get(0).getName());
        assertEquals("tienle1@gmail.com", borrowers.get(0).getEmail());
        assertEquals("0987654321", borrowers.get(0).getPhone());
        assertEquals("Jane Doe", borrowers.get(1).getName());
        assertEquals("jane@gmail.com", borrowers.get(1).getEmail());
        assertEquals("0987654322", borrowers.get(1).getPhone());
    }

    @Test
    void getAllBorrowers_empty() {
        // GIVEN
        borrowerRepository.deleteAll();
        borrowerRepository.flush();

        // WHEN
        List<Borrower> borrowers = borrowerService.getAllBorrowers();

        // THEN
        assertTrue(borrowers.isEmpty());
    }

    @Test
    void getBorrowerById_success() throws Exception {
        // WHEN
        Borrower result = borrowerService.getBorrowerById(borrower.getId());

        // THEN
        assertNotNull(result);
        assertEquals(borrower.getId(), result.getId());
        assertEquals("Tien Le", result.getName());
        assertEquals("tienle1@gmail.com", result.getEmail());
        assertEquals("0987654321", result.getPhone());
    }

    @Test
    void getBorrowerById_notFound() {
        // GIVEN
        Long nonExistentId = 999L;

        // WHEN, THEN
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> borrowerService.getBorrowerById(nonExistentId));
        assertEquals("Cannot find borrower with id: " + nonExistentId, exception.getMessage());
    }

    @Test
    void createBorrower_success() {
        // GIVEN
        Borrower newBorrower = Borrower.builder()
                .name("John Doe")
                .email("john@gmail.com")
                .phone("0123456789")
                .build();

        // WHEN
        Borrower result = borrowerService.createBorrower(newBorrower);

        // THEN
        assertNotNull(result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@gmail.com", result.getEmail());
        assertEquals("0123456789", result.getPhone());
        assertEquals(3, borrowerRepository.count());
    }

    @Test
    void createBorrower_phoneExists_fail() {
        // GIVEN
        Borrower newBorrower = Borrower.builder()
                .name("John Doe")
                .email("john@gmail.com")
                .phone("0987654321") // Same phone as borrower
                .build();

        // WHEN, THEN
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> borrowerService.createBorrower(newBorrower));
        assertEquals("phone existed.", exception.getMessage());
        assertEquals(2, borrowerRepository.count());
    }

    @Test
    void createBorrower_phoneInvalid_fail() {
        // WHEN, THEN
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> borrowerService.createBorrower(invalidBorrower));
        assertTrue(exception.getMessage().contains("Phone must be between 10 and 12 characters"));
        assertEquals(2, borrowerRepository.count());
    }

    @Test
    void updateBorrower_success() throws Exception {
        // GIVEN
        Borrower updatedBorrower = Borrower.builder()
                .name("Tien Le Updated")
                .email("tienle.updated@gmail.com")
                .phone("0123456789")
                .build();

        // WHEN
        Borrower result = borrowerService.updateBorrower(borrower.getId(), updatedBorrower);

        // THEN
        assertEquals(borrower.getId(), result.getId());
        assertEquals("Tien Le Updated sai", result.getName());
        assertEquals("tienle.updated@gmail.com", result.getEmail());
        assertEquals("0123456789", result.getPhone());
    }

    @Test
    void updateBorrower_notFound() {
        // GIVEN
        Borrower updatedBorrower = Borrower.builder()
                .name("Tien Le Updated")
                .email("tienle.updated@gmail.com")
                .phone("0123456789")
                .build();
        Long nonExistentId = 999L;

        // WHEN, THEN
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> borrowerService.updateBorrower(nonExistentId, updatedBorrower));
        assertEquals("Cannot find borrower with id: " + nonExistentId, exception.getMessage());
    }

    @Test
    void updateBorrower_phoneInvalid_fail() {
        // WHEN, THEN
        TransactionSystemException exception = assertThrows(TransactionSystemException.class,
                () -> borrowerService.updateBorrower(borrower.getId(), invalidBorrower));
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof jakarta.persistence.RollbackException);
        assertTrue(cause.getCause() instanceof ConstraintViolationException);
        assertTrue(cause.getCause().getMessage().contains("Phone must be between 10 and 12 characters"));
    }

    @Test
    void deleteBorrower_success() throws Exception{
        // WHEN
        borrowerService.deleteBorrower(borrower.getId());

        // THEN
        assertEquals(1, borrowerRepository.count());
        assertFalse(borrowerRepository.existsById(borrower.getId()));
    }

    @Test
    void deleteBorrower_notFound() {
        // GIVEN
        Long nonExistentId = 999L;

        // WHEN, THEN
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> borrowerService.deleteBorrower(nonExistentId));
        assertEquals("Cannot find borrower with id: " + nonExistentId, exception.getMessage());
        assertEquals(2, borrowerRepository.count());
    }
}
