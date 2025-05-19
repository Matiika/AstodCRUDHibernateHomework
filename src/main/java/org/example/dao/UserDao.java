package org.example.dao;

import org.example.entity.User;
import org.example.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class UserDao {
    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
            log.info("Saved user: {}", user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            log.error("Error saving user: {}", user, e);
        }
    }



    public List<User> findAll() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createSelectionQuery("FROM User u ORDER BY u.id", User.class).getResultList();
        }
    }

    public User findById(int id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }


    public void deleteById(Integer id) {
        Transaction transaction = null;
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка: " + e);
        }
    }

}
