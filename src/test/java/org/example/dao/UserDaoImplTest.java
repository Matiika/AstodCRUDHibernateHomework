package org.example.dao;

import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDaoImplTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("pass");

    static SessionFactory sessionFactory;
    UserDaoImpl userDao = new UserDaoImpl();
    Session session;

    @BeforeAll
    static void initSessionFactory() {
        var cfg = new Configuration()
                .addAnnotatedClass(User.class)
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.connection.url", postgres.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgres.getUsername())
                .setProperty("hibernate.connection.password", postgres.getPassword());
        sessionFactory = cfg.buildSessionFactory();
    }

    @BeforeEach
    void openSession() {
        // Открытие сессии и начало транзакции перед каждым тестом
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.createMutationQuery("DELETE FROM User").executeUpdate();
        session.getTransaction().commit();
        session.beginTransaction();
    }

    @AfterEach
    void cleanup() {
        // Откат и закрытие сессии после каждого теста
        if (session.getTransaction().isActive()) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @AfterAll
    static void closeFactory() {
        // Закрытие SessionFactory после всех тестов
        sessionFactory.close();
    }

    @Test
    void saveAndFindById_ShouldPersistAndRetrieveUser() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@example.com");

        user = session.merge(user);
        session.getTransaction().commit();
        session.beginTransaction();

        User loaded = userDao.findById(session, user.getId());

        assertNotNull(loaded);
        assertEquals("Alice", loaded.getName());
    }

    @Test
    void findAll_ShouldReturnAllSavedUsers() {
        User u1 = new User();
        u1.setName("Bob");
        u1.setEmail("bob@example.com");

        User u2 = new User();
        u2.setName("Carol");
        u2.setEmail("carol@example.com");

        u1 = session.merge(u1);
        u2 = session.merge(u2);
        session.getTransaction().commit();
        session.beginTransaction();

        List<User> users = userDao.findAll(session);

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> "Bob".equals(u.getName())));
        assertTrue(users.stream().anyMatch(u -> "Carol".equals(u.getName())));
    }

    @Test
    void deleteById_ShouldRemoveExistingUser() {
        User user = new User();
        user.setName("Dave");
        user.setEmail("dave@example.com");

        user = session.merge(user);
        session.getTransaction().commit();
        session.beginTransaction();

        boolean deleted = userDao.deleteById(session, user.getId());
        session.getTransaction().commit();

        assertTrue(deleted);
        session.beginTransaction();
        User missing = userDao.findById(session, user.getId());
        assertNull(missing);
    }
}
