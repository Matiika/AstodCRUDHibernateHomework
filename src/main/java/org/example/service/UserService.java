package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserService {



    private final UserDao userDao = new UserDao();

    public User getUserById(int id) {
        return userDao.findById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public void save(User user) {
        userDao.save(user);
    }

    public void deleteById(Integer id) {
        userDao.deleteById(id);
    }

}
