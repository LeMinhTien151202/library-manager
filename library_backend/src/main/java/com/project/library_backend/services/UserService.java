package com.project.library_backend.services;

import com.project.library_backend.exceptions.DataNotFoundException;
import com.project.library_backend.models.User;
import com.project.library_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) throws Exception{
        return userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: "+id));
    }

    @Override
    public User createUser(User user) {

        log.info("service: create user");

        if (userRepository.existsByPhone(user.getPhone())){
            throw new RuntimeException("phone existed.");
        }

        User newUser = User.builder()
                .phone(user.getPhone())
                .name(user.getName())
                .email(user.getEmail()).build();
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(Long id, User user) throws Exception {
        User user1 = userRepository.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find user with id: "+id));
        user1.setName(user.getName());
        user1.setEmail(user.getEmail());
        user1.setPhone(user.getPhone());
        return userRepository.save(user1);
    }

    @Override
    public void deleteUser(Long id) throws DataNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new DataNotFoundException("Cannot find user with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
