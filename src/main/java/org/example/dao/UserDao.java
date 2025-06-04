package org.example.dao;

import org.example.entity.User;
import org.hibernate.Session;

import java.util.List;

public interface UserDao {
    void saveOrUpdate (Session session, User user);
    List<User> findAll(Session session);
    User findById(Session session, int id);
    boolean deleteById(Session session, Integer id);
}
