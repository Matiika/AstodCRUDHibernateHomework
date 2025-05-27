package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            userDao.saveOrUpdate(session, user);
            transaction.commit();
            log.info("Saved user: {}", user);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Error saving user: {}", user, e);
        }

    }

    public boolean deleteById(Integer id) {
        boolean deleted = false;
        Transaction transaction = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            deleted = userDao.deleteById(session, id);
            transaction.commit();
            if (deleted) {
                log.info("Deleted user with id={}", id);
            } else {
                log.warn("User with id={} not found, nothing was deleted", id);
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Ошибка удаления пользователя id={}", id, e);
        }
        return deleted;
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return userDao.findAll(session);
        }
    }

    public User getUserById(int id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return userDao.findById(session, id);
        }
    }

}
