package com.project.library_backend.controllers;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.User;
import com.project.library_backend.responses.ResponseObject;
import com.project.library_backend.services.IUserService;
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
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> createUser(
            @Valid @RequestBody User user,
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
        log.info("controller: create user");
        User newUser = userService.createUser(user);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("add user successfully")
                .status(HttpStatus.OK)
                .data(newUser).build());
    }

    //http://localhost:8088/api/v1/products/6
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable("id") Long userId
    ) throws Exception {
        User existingUser = userService.getUserById(userId);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(existingUser)
                .message("Get user information successfully")
                .status(HttpStatus.OK)
                .build());
    }

    @GetMapping("")
    public ResponseEntity<?> getUserAll() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Get list of user successfully")
                .status(HttpStatus.OK)
                .data(users)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User user,
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
        User user1 = userService.updateUser(id,user);
        return ResponseEntity.ok(new ResponseObject("Update user successfully", HttpStatus.OK, user1));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteBorrower(@PathVariable Long id) throws DataNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .status(HttpStatus.OK)
                        .message("Delete user with id: "+id+" successfully")
                        .build());
    }

}
