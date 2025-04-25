package com.project.library_backend.controllers;

import com.project.library_backend.models.Borrower;
import com.project.library_backend.services.IBorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final IBorrowerService borrowerService;

    @PostMapping("")
    public ResponseEntity<?> createBorrower(
            @Valid @RequestBody Borrower borrower,
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
            Borrower newBorrower = borrowerService.createBorrower(borrower);
            return ResponseEntity.ok(newBorrower);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //http://localhost:8088/api/v1/products/6
    @GetMapping("/{id}")
    public ResponseEntity<?> getBorrowerById(
            @PathVariable("id") Long borrowerId
    ) {
        try {
            Borrower existingBorrower = borrowerService.getBorrowerById(borrowerId);
            return ResponseEntity.ok(existingBorrower);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getBorrowerAll() {
        try {
            List<Borrower> borrowers = borrowerService.getAllBorrowers();
            return ResponseEntity.ok(borrowers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBorrower(
            @PathVariable Long id,
            @Valid @RequestBody Borrower borrower
    ) {
        try {
            Borrower borrower1 = borrowerService.updateBorrower(id,borrower);
            return ResponseEntity.ok(borrower1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.ok("Delete subject with id: "+id+" successfully");
    }

}
