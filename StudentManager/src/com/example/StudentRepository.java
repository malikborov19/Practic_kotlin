package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentRepository {
    private List<Student> students = new ArrayList<>();
    private int nextId = 1;

    public void addStudent(Student student) {
        if (student.getId() == 0) {
            student.setId(nextId++);
        }
        students.add(student);
    }

    public void removeStudent(int id) {
        students.removeIf(s -> s.getId() == id);
    }

    public List<Student> findStudentBySurname(String surname) {
        return students.stream()
                .filter(s -> s.getSurname().equalsIgnoreCase(surname))
                .collect(Collectors.toList());
    }

    public Student findStudentById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Student> findStudentsByGroup(String group) {
        return students.stream()
                .filter(s -> s.getGroup().equalsIgnoreCase(group))
                .collect(Collectors.toList());
    }

    public List<Student> findStudentsByGradeRange(double min, double max) {
        return students.stream()
                .filter(s -> s.getAverageGrade() >= min && s.getAverageGrade() <= max)
                .collect(Collectors.toList());
    }

    public void updateStudent(int id, Student updated) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == id) {
                students.set(i, updated);
                break;
            }
        }
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public void sortBySurname() {
        students.sort(Comparator.comparing(Student::getSurname));
    }

    public void sortByAverageGrade() {
        students.sort(Comparator.comparingDouble(Student::getAverageGrade));
    }

    public int getTotalStudents() {
        return students.size();
    }

    public double getAverageGradeAll() {
        if (students.isEmpty()) return 0.0;
        return students.stream().mapToDouble(Student::getAverageGrade).average().orElse(0.0);
    }

    public Student getTopStudent() {
        return students.stream().max(Comparator.comparingDouble(Student::getAverageGrade)).orElse(null);
    }

    public Student getBottomStudent() {
        return students.stream().min(Comparator.comparingDouble(Student::getAverageGrade)).orElse(null);
    }
}
