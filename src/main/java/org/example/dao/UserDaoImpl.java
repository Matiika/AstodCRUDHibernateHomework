package org.example.dao;

import org.example.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import java.util.List;


public class UserDaoImpl implements UserDao{

    @Override
    public void saveOrUpdate(Session session, User user) {
        try {
            session.merge(user);
        } catch (HibernateException e) {
            throw new HibernateException("Не удалось сохранить или обновить пользователя: " + user, e);
        }
    }

    @Override
    public List<User> findAll(Session session) {
        try {
            return session
                    .createSelectionQuery("FROM User u ORDER BY u.id", User.class)
                    .getResultList();
        } catch (HibernateException e) {
            throw new HibernateException("Не удалось получить список пользователей", e);
        }
    }

    @Override
    public User findById(Session session, int id) {
        try {
            return session.get(User.class, id);
        } catch (HibernateException e) {
            throw new HibernateException("Ошибка при поиске пользователя с id=" + id, e);
        }
    }

    @Override
    public boolean deleteById(Session session, Integer id) {
        try {
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                return true;
            }
            return false;
        } catch (HibernateException e) {
            throw new HibernateException("Не удалось удалить пользователя с id=" + id, e);
        }
    }
}
