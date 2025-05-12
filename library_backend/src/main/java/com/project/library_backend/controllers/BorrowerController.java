package com.project.library_backend.controllers;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.Borrower;
import com.project.library_backend.responses.ResponseObject;
import com.project.library_backend.services.IBorrowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final IBorrowerService borrowerService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createBorrower(
            @Valid @RequestBody Borrower borrower,
            BindingResult result
    ) throws DataNotFoundException,Exception {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(String.join(";", errorMessages))
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        log.info("controller: create borrower");
        Borrower newBorrower = borrowerService.createBorrower(borrower);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("add borrower successfully")
                .status(HttpStatus.OK)
                .data(newBorrower).build());
    }

    //http://localhost:8088/api/v1/products/6
    @GetMapping("/{id}")
    public ResponseEntity<?> getBorrowerById(
            @PathVariable("id") Long borrowerId
    ) throws Exception {
        Borrower existingBorrower = borrowerService.getBorrowerById(borrowerId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(existingBorrower)
                .message("Get borrower information successfully")
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<?> getBorrowerAll() {
        List<Borrower> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of borrower successfully")
                .status(HttpStatus.OK)
                .data(borrowers)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateBorrower(
            @PathVariable Long id,
            @Valid @RequestBody Borrower borrower,
            BindingResult result
    ) throws Exception{
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    ResponseObject.builder()
                            .message(String.join(";", errorMessages))
                            .status(HttpStatus.BAD_REQUEST)
                            .build());
        }
        Borrower borrower1 = borrowerService.updateBorrower(id,borrower);
        return ResponseEntity.ok(new ResponseObject("Update borrower successfully", HttpStatus.OK, borrower1));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBorrower(@PathVariable Long id) throws DataNotFoundException {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Delete subject with id: "+id+" successfully")
                        .build());
    }

}
