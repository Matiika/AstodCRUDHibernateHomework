package org.example;

import org.example.entity.Employee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Test2 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Employee.class)
                .buildSessionFactory();

        try {
            Session session = factory.getCurrentSession();
            session.beginTransaction();

            List<Employee> users = session.createSelectionQuery("from User", Employee.class).getResultList();

            for (Employee user : users) {
                System.out.println(user.toString());
            }

            System.out.println("DONE");

            session.getTransaction().commit();
        } finally {
            factory.close();
        }
    }
}
