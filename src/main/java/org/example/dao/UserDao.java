package org.example.dao;

import org.example.entity.User;
import org.hibernate.Session;

import java.util.List;

public interface UserDao {
    public void saveOrUpdate (Session session, User user);
    public List<User> findAll(Session session);
    public User findById(Session session, int id);
    public boolean deleteById(Session session, Integer id);
}
