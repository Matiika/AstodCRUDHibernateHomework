package org.example.service;

import org.example.entity.User;

import java.util.List;

public interface UserService {
    void save(User user);
    boolean deleteById(Integer id);
    List<User> getAllUsers();
    User getUserById(int id);
}
