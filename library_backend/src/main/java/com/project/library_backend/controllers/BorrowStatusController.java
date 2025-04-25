package com.project.library_backend.controllers;

import com.project.library_backend.dtos.BorrowStatusDTO;
import com.project.library_backend.models.BorrowStatus;
import com.project.library_backend.models.Borrower;
import com.project.library_backend.services.IBorrowStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/borrow_status")
@RequiredArgsConstructor
public class BorrowStatusController {

    private final IBorrowStatusService borrowStatusService;

    @GetMapping("")
    public ResponseEntity<?> getBorrowStatusAll() {
        try {
            List<BorrowStatus> borrowStatuses = borrowStatusService.getAllBorrowStatus();
            return ResponseEntity.ok(borrowStatuses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBorrowStatusById(
            @PathVariable("id") Long borrowStatusId
    ) {
        try {
            BorrowStatus existingBorrowStatus = borrowStatusService.getBorrowStatusById(borrowStatusId);
            return ResponseEntity.ok(existingBorrowStatus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createBorrowStatus(
            @Valid @RequestBody BorrowStatusDTO borrowStatusDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            BorrowStatus newBorrowStatus = borrowStatusService.createBorrowStatus(borrowStatusDTO);
            return ResponseEntity.ok(newBorrowStatus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBorrowStatus(
            @PathVariable Long id,
            @Valid @RequestBody BorrowStatusDTO borrowStatusDTO
    ) {
        try {
            BorrowStatus borrowStatus = borrowStatusService.updateBorrowStatus(id, borrowStatusDTO);
            return ResponseEntity.ok(borrowStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBorrowStatus(@PathVariable Long id) {
        borrowStatusService.deleteBorrowStatus(id);
        return ResponseEntity.ok("Delete subject with id: "+id+" successfully");
    }
}
