package org.example;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.User;
import org.example.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();

        UserServiceImpl userServiceImpl = new UserServiceImpl(userDao);

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
                    String name = getName();
                    String email = getEmail();
                    int age = getAge();

                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setEmail(email);
                    newUser.setAge(age);
                    newUser.setCreatedAt(LocalDateTime.now());
                    userServiceImpl.save(newUser);
                    System.out.println(" Пользователь добавлен.");
                }
                case 2 -> {
                    List<User> userList = userServiceImpl.getAllUsers();
                    if (userList.isEmpty()) {
                        System.out.println("Сейчас в базе данных нет пользователей");
                    } else {
                        for (User u : userList) {
                            System.out.println(u);
                        }
                    }

                }
                case 3 -> {
                    System.out.print("Введите ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    User user = userServiceImpl.getUserById(id);
                    if (user != null) {
                        System.out.println(user);
                    } else {
                        System.out.println("Пользователь с ID=" + id + " не найден.");
                    }
                }
                case 4 -> {
                    System.out.print("ID для обновления: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    User user = userServiceImpl.getUserById(id);
                    if (user != null) {
                        System.out.println(user);
                        System.out.println("Какие данные обновить?\n" +
                                "1. Имя\n" +
                                "2. email\n" +
                                "3. возраст\n" +
                                "4. Отмена обновления");

                        while (true) {
                            System.out.print("Выберите число от 1 до 4: ");
                            if (scanner.hasNextInt()) {
                                choice = scanner.nextInt();
                                scanner.nextLine();
                                if (choice >= 1 && choice <= 4) {
                                    updateUser(user, choice, userServiceImpl);
                                    break;
                                } else {
                                    System.out.println("Число должно быть от 1 до 4.");
                                }
                            } else {
                                System.out.println("Введите корректное число.");
                                scanner.nextLine();
                            }
                        }




                    } else {
                        System.out.println("Пользователь не найден.");
                    }
                }
                case 5 -> {
                    System.out.print("Введите ID для удаления: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    User user = userServiceImpl.getUserById(id);
                    if (user != null) {
                        userServiceImpl.deleteById(id);
                        System.out.println("Пользователь удален: " + user);
                    } else {
                        System.out.println("Пользователь с ID=" + id + " не найден.");
                    }
                }
                case 0 -> System.out.println("Выход из программы.");
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        } while (choice != 0);

        scanner.close();
    }

    public static String getName() {
        String name;
        while (true) {
            System.out.print("Имя: ");
            name = scanner.nextLine().trim();

            if (!name.isEmpty() && name.matches(".*[a-zA-Zа-яА-Я].*")) {
                break;
            }
            System.out.println("Имя должно содержать хотя бы одну букву (A-Z или А-Я). Попробуйте снова.");
        }
        return name;
    }

    public static String getEmail() {
        String email;
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine();
            if (email.matches("^.+@.+\\..+$")) {
                break;
            }
            System.out.println("Неверный формат email. Попробуйте снова.");
        }
        return email;
    }

    public static int getAge() {
        int age;
        while (true) {
            System.out.print("Возраст: ");
            if (scanner.hasNextInt()) {
                age = scanner.nextInt();
                scanner.nextLine();
                if (age > 0) {
                    break;
                } else {
                    System.out.println("Возраст должен быть положительным.");
                }
            } else {
                System.out.println("Введите корректное число.");
                scanner.nextLine();
            }
        }
        return age;
    }

    public static void updateUser(User user, int choice, UserServiceImpl userServiceImpl) {

        switch (choice) {
            case 1:
                System.out.println("Вы выбрали изменение имени");
                String name = getName();
                user.setName(name);
                userServiceImpl.save(user);
                System.out.println("Изменение сохранено");
                break;
            case 2:
                System.out.println("Вы выбрали изменение email");
                String email = getEmail();
                user.setEmail(email);
                userServiceImpl.save(user);
                System.out.println("Изменение сохранено");
                break;
            case 3:
                System.out.println("Вы выбрали изменение возраста");
                int age = getAge();
                user.setAge(age);
                userServiceImpl.save(user);
                System.out.println("Изменение сохранено");
                break;
            case 4:
                System.out.println("Вы отменили обновление");
                break;
        }
    }

}
