package com.project.library_backend.services;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    User getUserById(Long id) throws Exception;
    User createUser(User borrower);
    User updateUser(Long id, User borrower) throws Exception;
    void deleteUser(Long id) throws DataNotFoundException;
}
