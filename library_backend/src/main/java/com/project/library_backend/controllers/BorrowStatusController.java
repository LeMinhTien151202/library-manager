package com.project.library_backend.controllers;

import com.project.library_backend.dtos.BorrowStatusDTO;
import com.project.library_backend.models.BorrowStatus;
import com.project.library_backend.responses.ResponseObject;
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
    public ResponseEntity<ResponseObject> getBorrowStatusAll() {
        List<BorrowStatus> borrowStatuses = borrowStatusService.getAllBorrowStatus();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of borrowStatus successfully")
                .status(HttpStatus.OK)
                .data(borrowStatuses)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBorrowStatusById(
            @PathVariable("id") Long borrowStatusId
    ) throws Exception {

        BorrowStatus existingBorrowStatus = borrowStatusService.getBorrowStatusById(borrowStatusId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(existingBorrowStatus)
                .message("Get borrowStatus information successfully")
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createBorrowStatus(
            @Valid @RequestBody BorrowStatusDTO borrowStatusDTO,
            BindingResult result
    ) throws Exception {
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
        BorrowStatus newBorrowStatus = borrowStatusService.createBorrowStatus(borrowStatusDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("add borrowStatus successfully")
                .status(HttpStatus.OK)
                .data(newBorrowStatus).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBorrowStatus(
            @PathVariable Long id,
            @Valid @RequestBody BorrowStatusDTO borrowStatusDTO
    ) throws Exception {

        BorrowStatus borrowStatus = borrowStatusService.updateBorrowStatus(id, borrowStatusDTO);
        return ResponseEntity.ok(
                new ResponseObject("Update borrowStatus successfully", HttpStatus.OK, borrowStatus));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBorrowStatus(@PathVariable Long id) {
        borrowStatusService.deleteBorrowStatus(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Delete subject with id: "+id+" successfully")
                        .build());
    }
}
