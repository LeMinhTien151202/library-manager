package com.project.library_backend.services;

import com.project.library_backend.dtos.BorrowStatusDTO;
import com.project.library_backend.models.BorrowStatus;

import java.util.List;

public interface IBorrowStatusService {
    List<BorrowStatus> getAllBorrowStatus();
    BorrowStatus getBorrowStatusById(Long id) throws Exception;
    BorrowStatus createBorrowStatus(BorrowStatusDTO borrowStatusDTO) throws Exception;
    BorrowStatus updateBorrowStatus(Long id, BorrowStatusDTO borrowStatusDTO) throws Exception;
    void deleteBorrowStatus(Long id);

}
