package org.example.student;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;



public class Main {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static EntityTransaction tx;

    public static void main(String[] args) {
        try {
            emf = Persistence.createEntityManagerFactory("mysql");
            em = emf.createEntityManager();
            tx = em.getTransaction();

            Scanner scanner = new Scanner(System.in);
            int choice = 0;

            do {
                System.out.println("--------------------------------------------------------");
                System.out.println("------------------        MENU:        -----------------");
                System.out.println("--------------------------------------------------------");
                System.out.println("1. Dodaj studenta do bazy danych");
                System.out.println("2. Usuń studenta z bazy danych");
                System.out.println("3. Zaktualizuj studenta w bazie danych");
                System.out.println("4. Wyświetl wszystkich studentów z bazy danych");
                System.out.println("0. Wyście");
                System.out.println("--------------------------------------------------------");
                System.out.println("--------------------------------------------------------");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Podaj imie: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Podaj nazwisko: ");
                        String lastName = scanner.nextLine();

                        Student student = new Student(firstName, lastName);
                        addStudent(student);
                        break;

                    case 2:
                        System.out.print("Podaj ID studenta do usuniecia: ");
                        long id = scanner.nextLong();

                        deleteStudent(id);
                        break;

                    case 3:
                        System.out.print("Podaj ID studenta do zaktualizowania: ");
                        long idToUpdate = scanner.nextLong();
                        scanner.nextLine();
                        System.out.print("Podaj nowe imie: ");
                        String newFirstName = scanner.nextLine();
                        System.out.print("Podaj nowe nazwisko: ");
                        String newLastName = scanner.nextLine();

                        Student studentToUpdate = new Student(newFirstName, newLastName);
                        updateStudent(idToUpdate, studentToUpdate);
                        break;



                    case 4:
                        List<Student> students = getAllStudents();
                        System.out.println("Lista wszystkich studentów:");
                        for (Student s : students) {
                            System.out.println(s);
                        }
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("Zły wybór");
                        break;
                }
            } while (choice != 0);
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

    private static void addStudent(Student student) {
        tx.begin();
        em.persist(student);
        tx.commit();
        System.out.println("Student zostal dodany do bazy danych");
    }

    private static void deleteStudent(long id) {
        tx.begin();
        Student student = em.find(Student.class, id);
        if (student != null) {
            em.remove(student);
            tx.commit();
            System.out.println("Student zostal usuniety z bazy danych");
        } else {
            System.out.println("Student nie został znaleziony w bazie danych");
        }
    }

    private static void updateStudent(long id, Student updatedStudent) {
        tx.begin();
        Student student = em.find(Student.class, id);
        if (student != null) {
            student.setFirstName(updatedStudent.getFirstName());
            student.setLastName(updatedStudent.getLastName());
            tx.commit();
            System.out.println("Student zaktualizowany");
        } else {
            System.out.println("Student nie został znaleziony w bazie danych");
        }
    }

    private static List<Student> getAllStudents() {
        return em.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }
}