package com.project.library_backend.services;

import com.project.library_backend.dtos.BorrowStatusDTO;
import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Book;
import com.project.library_backend.models.BorrowStatus;
import com.project.library_backend.models.User;
import com.project.library_backend.repositories.BookRepository;
import com.project.library_backend.repositories.BorrowStatusRepository;
import com.project.library_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowStatusService implements IBorrowStatusService{

    private final BorrowStatusRepository borrowStatusRepository;
    private final BookRepository bookRepository;
    private final UserRepository borrowerRepository;

    @Override
    public List<BorrowStatus> getAllBorrowStatus() {
        return borrowStatusRepository.findAll();
    }

    @Override
    public BorrowStatus getBorrowStatusById(Long id) throws Exception {
        return borrowStatusRepository.findById(id).
                orElseThrow(()-> new DataNotFoundException(
                        "Cannot find borrowStatus with id ="+id));
    }

    @Override
    public BorrowStatus createBorrowStatus(BorrowStatusDTO borrowStatusDTO) throws Exception{
        Book existingBook = bookRepository
                .findById(borrowStatusDTO.getBookId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find book with id: "+borrowStatusDTO.getBookId()));
        User existingBorrower = borrowerRepository
                .findById(borrowStatusDTO.getBorrowerId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find borrower with id: "+borrowStatusDTO.getBorrowerId()));
        BorrowStatus newBorrowStatus = BorrowStatus.builder()
                .status(borrowStatusDTO.getStatus())
                .user(existingBorrower)
                .book(existingBook)
                .borrowDate(borrowStatusDTO.getBorrowDate())
                .returnDate(borrowStatusDTO.getReturnDate()).build();
        return borrowStatusRepository.save(newBorrowStatus);
    }

    @Override
    public BorrowStatus updateBorrowStatus(Long id, BorrowStatusDTO borrowStatusDTO) throws Exception {
        BorrowStatus borrowStatus = borrowStatusRepository
                .findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find BorrowStatus with id: "+id));
        Book existingBook = bookRepository
                .findById(borrowStatusDTO.getBookId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find book with id: "+borrowStatusDTO.getBookId()));
        User existingBorrower = borrowerRepository
                .findById(borrowStatusDTO.getBorrowerId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find borrower with id: "+borrowStatusDTO.getBorrowerId()));
        borrowStatus.setStatus(borrowStatusDTO.getStatus());
        borrowStatus.setBorrowDate(borrowStatusDTO.getBorrowDate());
        borrowStatus.setReturnDate(borrowStatusDTO.getReturnDate());
        borrowStatus.setUser(existingBorrower);
        borrowStatus.setBook(existingBook);
        return borrowStatusRepository.save(borrowStatus);
    }

    @Override
    public void deleteBorrowStatus(Long id) {
        borrowStatusRepository.deleteById(id);
    }


}
