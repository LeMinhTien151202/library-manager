package com.project.library_backend.services;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Book;
import com.project.library_backend.models.Borrower;

import java.util.List;

public interface IBorrowerService {
    List<Borrower> getAllBorrowers();
    Borrower getBorrowerById(Long id) throws Exception;
    Borrower createBorrower(Borrower borrower);
    Borrower updateBorrower(Long id, Borrower borrower) throws Exception;
    void deleteBorrower(Long id) throws DataNotFoundException;
}
