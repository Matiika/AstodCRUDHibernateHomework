package org.example.service;

import org.example.entity.User;

import java.util.List;

public interface UserService {
    public void save(User user);
    public boolean deleteById(Integer id);
    public List<User> getAllUsers();
    public User getUserById(int id);
}
