package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.service.spi.ServiceException;
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
        if (user == null) {
            throw new ServiceException("Нельзя сохранить null-пользователя");
        }

        Transaction transaction = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            userDao.saveOrUpdate(session, user);
            transaction.commit();
            log.info("Сохраненный пользователь: {}", user);
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Ошибка сохранения пользователя: {}", user, e);
            throw new ServiceException("Ошибка сохранения пользователя: " + user, e);
        }

    }

    public boolean deleteById(Integer id) {
        if (id == null || id < 0) {
            throw new ServiceException("Неверный id для удаления: " + id);
        }

        Transaction transaction = null;
        boolean deleted;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            deleted = userDao.deleteById(session, id);
            transaction.commit();
            if (deleted) {
                log.info("Удален пользователь с id={}", id);
            } else {
                log.warn("Пользователь с id={} не найден", id);
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            log.error("Ошибка удаления пользователя id={}", id, e);
            throw new ServiceException("Ошибка удаления пользователя: " + id, e);
        }
        return deleted;
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return userDao.findAll(session);
        } catch (Exception e) {
            log.error("Не удалось получить список пользователей", e);
            throw new ServiceException("Не удалось получить список пользователей", e);
        }
    }

    public User getUserById(int id) {
        if (id < 0) {
            throw new ServiceException("Неверный id для поиска: " + id);
        }

        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            User user = userDao.findById(session, id);
            if (user == null) {
                log.warn("Пользователь с id={} не найден", id);
            }
            return user;
        }
    }

}
