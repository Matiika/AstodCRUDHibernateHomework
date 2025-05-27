package org.example.dao;

import org.example.entity.User;
import org.hibernate.Session;
import java.util.List;


public class UserDaoImpl implements UserDao{

    public void saveOrUpdate (Session session, User user) {
        session.merge(user);
    }

    public List<User> findAll(Session session) {
        return session
                .createSelectionQuery("FROM User u ORDER BY u.id", User.class)
                .getResultList();
    }

    public User findById(Session session, int id) {
        return session.get(User.class, id);
    }

    public boolean deleteById(Session session, Integer id) {
        User user = session.get(User.class, id);
        if (user != null) {
            session.remove(user);
            return true;
        }
        return false;
    }
}
