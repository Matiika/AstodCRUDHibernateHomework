package org.example;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.service.UserService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();

        int choice;

        do {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Добавить пользователя");
            System.out.println("2 - Показать всех пользователей");
            System.out.println("3 - Найти пользователя по ID");
            System.out.println("4 - Обновить пользователя");
            System.out.println("5 - Удалить пользователя");
            System.out.println("0 - Выход");
            System.out.print("Введите номер действия: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Введите число: ");
                scanner.next();
            }

            choice = scanner.nextInt();
            scanner.nextLine(); // Очистка после ввода числа

            switch (choice) {
                case 1 -> {
                    System.out.print("Имя: ");
                    String name = scanner.nextLine();

                    System.out.print("Email: ");
                    String email = scanner.nextLine();

                    System.out.print("Возраст: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();

                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setEmail(email);
                    newUser.setAge(age);
                    newUser.setCreatedAt(LocalDateTime.now());
                    userService.save(newUser);
                    System.out.println(" Пользователь добавлен.");
                }
                case 2 -> {
                    System.out.println(" Все пользователи:");
                    for (User u : userService.getAllUsers()) {
                        System.out.println(u);
                    }
                }
                case 3 -> {
                    System.out.print("Введите ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    User user = userService.getUserById(id);
                    if (user != null) {
                        System.out.println(user);
                    } else {
                        System.out.println("Пользователь не найден.");
                    }
                }
                case 4 -> {
                    System.out.print("ID для обновления: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    User user = userService.getUserById(id);
                    if (user != null) {
                        System.out.print("Новое имя: ");
                        user.setName(scanner.nextLine());

                        System.out.print("Новый email: ");
                        user.setEmail(scanner.nextLine());

                        System.out.print("Новый возраст: ");
                        user.setAge(scanner.nextInt());
                        scanner.nextLine();

                        userService.save(user);
                        System.out.println("Пользователь обновлён.");
                    } else {
                        System.out.println("Пользователь не найден.");
                    }
                }
                case 5 -> {
                    System.out.print("ID для удаления: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    userService.deleteById(id);
                    System.out.println("Пользователь удалён, если существовал.");
                }
                case 0 -> System.out.println("Выход из программы.");
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
