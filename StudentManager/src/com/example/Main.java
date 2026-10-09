package com.example;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        StudentRepository repository = new StudentRepository();
        FileManager fileManager = new FileManager();
        Scanner scanner = new Scanner(System.in);

        // Load data on startup
        fileManager.loadFromFile(repository);

        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        addStudent(repository, scanner);
                        break;
                    case 2:
                        showAllStudents(repository);
                        break;
                    case 3:
                        searchStudents(repository, scanner);
                        break;
                    case 4:
                        updateStudent(repository, scanner);
                        break;
                    case 5:
                        removeStudent(repository, scanner);
                        break;
                    case 6:
                        repository.sortBySurname();
                        System.out.println("Список отсортирован по фамилии.");
                        showAllStudents(repository);
                        break;
                    case 7:
                        repository.sortByAverageGrade();
                        System.out.println("Список отсортирован по среднему баллу.");
                        showAllStudents(repository);
                        break;
                    case 8:
                        fileManager.saveToFile(repository.getAllStudents());
                        break;
                    case 9:
                        repository = new StudentRepository(); // reset for reload
                        fileManager.loadFromFile(repository);
                        break;
                    case 10:
                        System.out.println("Программа завершена.");
                        fileManager.saveToFile(repository.getAllStudents()); // auto-save on exit
                        running = false;
                        break;
                    case 11: // Extra: Statistics
                        showStatistics(repository);
                        break;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число.");
                scanner.nextLine(); // clear invalid input
            } catch (Exception e) {
                System.err.println("Произошла ошибка: " + e.getMessage());
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1. Добавить студента");
        System.out.println("2. Показать всех студентов");
        System.out.println("3. Найти студента");
        System.out.println("4. Изменить данные студента");
        System.out.println("5. Удалить студента");
        System.out.println("6. Сортировать по фамилии");
        System.out.println("7. Сортировать по среднему баллу");
        System.out.println("8. Сохранить данные");
        System.out.println("9. Загрузить данные");
        System.out.println("10. Выход");
        System.out.println("11. Показать статистику");
        System.out.print("Выберите пункт: ");
    }

    private static void addStudent(StudentRepository repo, Scanner scanner) {
        try {
            System.out.print("Введите фамилию: ");
            String surname = scanner.nextLine().trim();
            if (surname.isEmpty()) throw new IllegalArgumentException("Фамилия не может быть пустой");

            System.out.print("Введите имя: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) throw new IllegalArgumentException("Имя не может быть пустым");

            System.out.print("Введите группу: ");
            String group = scanner.nextLine().trim();

            System.out.print("Введите средний балл (0-5): ");
            double avg = Double.parseDouble(scanner.nextLine().trim());
            if (avg < 0 || avg > 5) throw new IllegalArgumentException("Средний балл должен быть от 0 до 5");

            Student student = new Student(0, surname, name, group, avg);
            repo.addStudent(student);
            System.out.println("Студент добавлен успешно. ID: " + student.getId());
        } catch (Exception e) {
            System.err.println("Ошибка добавления: " + e.getMessage() + ". Попробуйте снова.");
        }
    }

    private static void showAllStudents(StudentRepository repo) {
        List<Student> students = repo.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("Список студентов пуст.");
            return;
        }
        System.out.println("\nСписок студентов:");
        for (Student s : students) {
            s.printInfo();
        }
    }

    private static void searchStudents(StudentRepository repo, Scanner scanner) {
        System.out.println("1. Поиск по ID");
        System.out.println("2. Поиск по фамилии");
        System.out.println("3. Поиск по группе");
        System.out.println("4. Поиск по диапазону среднего балла");
        System.out.print("Выберите тип поиска: ");
        int type = scanner.nextInt();
        scanner.nextLine();

        switch (type) {
            case 1:
                System.out.print("Введите ID: ");
                int id = scanner.nextInt();
                Student s = repo.findStudentById(id);
                if (s != null) s.printInfo();
                else System.out.println("Студент не найден.");
                break;
            case 2:
                System.out.print("Введите фамилию: ");
                String surname = scanner.nextLine().trim();
                List<Student> bySurname = repo.findStudentBySurname(surname);
                printSearchResults(bySurname);
                break;
            case 3:
                System.out.print("Введите группу: ");
                String group = scanner.nextLine().trim();
                List<Student> byGroup = repo.findStudentsByGroup(group);
                printSearchResults(byGroup);
                break;
            case 4:
                System.out.print("Введите мин. балл: ");
                double min = scanner.nextDouble();
                System.out.print("Введите макс. балл: ");
                double max = scanner.nextDouble();
                List<Student> byRange = repo.findStudentsByGradeRange(min, max);
                printSearchResults(byRange);
                break;
            default:
                System.out.println("Неверный тип поиска.");
        }
    }

    private static void printSearchResults(List<Student> results) {
        if (results.isEmpty()) {
            System.out.println("Ничего не найдено.");
            return;
        }
        System.out.println("Результаты поиска:");
        for (Student s : results) {
            s.printInfo();
        }
    }

    private static void updateStudent(StudentRepository repo, Scanner scanner) {
        System.out.print("Введите ID студента для изменения: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Student existing = repo.findStudentById(id);
        if (existing == null) {
            System.out.println("Студент не найден.");
            return;
        }

        try {
            System.out.print("Новая фамилия (" + existing.getSurname() + "): ");
            String surname = scanner.nextLine().trim();
            if (surname.isEmpty()) surname = existing.getSurname();

            System.out.print("Новое имя (" + existing.getName() + "): ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) name = existing.getName();

            System.out.print("Новая группа (" + existing.getGroup() + "): ");
            String group = scanner.nextLine().trim();
            if (group.isEmpty()) group = existing.getGroup();

            System.out.print("Новый средний балл (" + existing.getAverageGrade() + "): ");
            String avgStr = scanner.nextLine().trim();
            double avg = avgStr.isEmpty() ? existing.getAverageGrade() : Double.parseDouble(avgStr);
            if (avg < 0 || avg > 5) throw new IllegalArgumentException("Средний балл 0-5");

            Student updated = new Student(id, surname, name, group, avg);
            repo.updateStudent(id, updated);
            System.out.println("Данные обновлены.");
        } catch (Exception e) {
            System.err.println("Ошибка обновления: " + e.getMessage());
        }
    }

    private static void removeStudent(StudentRepository repo, Scanner scanner) {
        System.out.print("Введите ID для удаления: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        repo.removeStudent(id);
        System.out.println("Студент удален (если существовал).");
    }

    private static void showStatistics(StudentRepository repo) {
        System.out.println("\n=== Статистика ===");
        System.out.println("Общее количество студентов: " + repo.getTotalStudents());
        System.out.printf("Средний балл по всем: %.2f%n", repo.getAverageGradeAll());
        Student top = repo.getTopStudent();
        if (top != null) System.out.println("Лучший студент: " + top);
        Student bottom = repo.getBottomStudent();
        if (bottom != null) System.out.println("Худший студент: " + bottom);
    }
}
