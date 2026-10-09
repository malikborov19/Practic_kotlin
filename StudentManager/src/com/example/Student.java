package com.example;

public class Student {
    private int id;
    private String surname;
    private String name;
    private String group;
    private double averageGrade;

    public Student() {}

    public Student(int id, String surname, String name, String group, double averageGrade) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.group = group;
        this.averageGrade = averageGrade;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }

    public double getAverageGrade() { return averageGrade; }
    public void setAverageGrade(double averageGrade) { this.averageGrade = averageGrade; }

    public void printInfo() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", group='" + group + '\'' +
                ", averageGrade=" + averageGrade +
                '}';
    }
}
