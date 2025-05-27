package org.example.service;

import org.example.dao.UserDaoImpl;
import org.example.entity.User;
import org.example.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDaoImpl userDao;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        testUser = new User("John Doe", "john@example.com", 25, LocalDateTime.now());
        testUser.setId(1);

        lenient().when(sessionFactory.openSession()).thenReturn(session);
        lenient().when(session.beginTransaction()).thenReturn(transaction);
    }

    //Тест для метода save() - успешное сохранение пользователя
    @Test
    void save_ShouldSaveUserSuccessfully() {
        try (MockedStatic<HibernateSessionFactoryUtil> mockedUtil = mockStatic(HibernateSessionFactoryUtil.class)) {
            mockedUtil.when(HibernateSessionFactoryUtil::getSessionFactory).thenReturn(sessionFactory);

            doNothing().when(userDao).saveOrUpdate(session, testUser);

            userService.save(testUser);

            verify(userDao).saveOrUpdate(session, testUser);
            verify(transaction).commit();
            verify(transaction, never()).rollback();
            verify(session).close();
        }
    }

    /**
     * Тест для метода save() - обработка исключения с активной транзакцией
     * Если исключение возникло с активной транзакцией и при сохранении пользователя, то выполнить откат rollback
     */
    @Test
    void save_ShouldRollbackTransactionOnException() {
        try (MockedStatic<HibernateSessionFactoryUtil> mockedUtil = mockStatic(HibernateSessionFactoryUtil.class)) {
            mockedUtil.when(HibernateSessionFactoryUtil::getSessionFactory).thenReturn(sessionFactory);

            doThrow(new RuntimeException("Database error")).when(userDao).saveOrUpdate(session, testUser);
            when(transaction.isActive()).thenReturn(true);

            assertDoesNotThrow(() -> userService.save(testUser));

            verify(userDao).saveOrUpdate(session, testUser);
            verify(transaction).isActive();
            verify(transaction).rollback();
            verify(transaction, never()).commit();
            verify(session).close();
        }
    }

    // deleteById() - успешное удаление
    @Test
    void deleteById_ShouldDeleteUserSuccessfully() {
        try (MockedStatic<HibernateSessionFactoryUtil> mockedUtil = mockStatic(HibernateSessionFactoryUtil.class)) {
            Integer userId = 1;
            mockedUtil.when(HibernateSessionFactoryUtil::getSessionFactory).thenReturn(sessionFactory);

            when(userDao.deleteById(session, userId)).thenReturn(true);

            boolean result = userService.deleteById(userId);

            verify(userDao).deleteById(session, userId);
            verify(transaction).commit();
            verify(transaction, never()).rollback();
            verify(session).close();
            assert result;
        }
    }

    //getAllUsers() - успешное получение всех пользователей
    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        try (MockedStatic<HibernateSessionFactoryUtil> mockedUtil = mockStatic(HibernateSessionFactoryUtil.class)) {
            mockedUtil.when(HibernateSessionFactoryUtil::getSessionFactory).thenReturn(sessionFactory);
            when(userDao.findAll(session)).thenReturn(java.util.List.of(testUser));

            var result = userService.getAllUsers();

            verify(userDao).findAll(session);
            verify(session).close();
            assert result.size() == 1;
            assert result.get(0).equals(testUser);
        }
    }

    //Успешное получение пользователя по ID
    @Test
    void getUserById_ShouldReturnUser() {
        try (MockedStatic<HibernateSessionFactoryUtil> mockedUtil = mockStatic(HibernateSessionFactoryUtil.class)) {
            int userId = 1;
            mockedUtil.when(HibernateSessionFactoryUtil::getSessionFactory).thenReturn(sessionFactory);
            when(userDao.findById(session, userId)).thenReturn(testUser);

            User result = userService.getUserById(userId);

            verify(userDao).findById(session, userId);
            verify(session).close();
            assert result.equals(testUser);
        }
    }
}