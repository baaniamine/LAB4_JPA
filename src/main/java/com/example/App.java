package com.example;

import com.example.model.joined.Developpeur;
import com.example.model.joined.Employe;
import com.example.model.joined.Manager;
import com.example.model.singletable.Moto;
import com.example.model.singletable.Vehicule;
import com.example.model.singletable.Voiture;
import com.example.model.tableperclass.Electronique;
import com.example.model.tableperclass.Livre;
import com.example.model.tableperclass.Produit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Create the EntityManagerFactory
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate-inheritance");

        try {
            // Test SINGLE_TABLE strategy
            System.out.println("\n=== Testing SINGLE_TABLE strategy ===");
            testSingleTable(emf);

            // Test JOINED strategy
            System.out.println("\n=== Testing JOINED strategy ===");
            testJoined(emf);

            // Test TABLE_PER_CLASS strategy
            System.out.println("\n=== Testing TABLE_PER_CLASS strategy ===");
            testTablePerClass(emf);

        } finally {
            // Close the EntityManagerFactory
            emf.close();
        }
    }

    private static void testSingleTable(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Create vehicles
            System.out.println("Creating vehicles...");

            Voiture voiture1 = new Voiture(
                    "Renault", "Clio", LocalDate.of(2020, 5, 15), 15000.0,
                    5, true, "Essence"
            );

            Voiture voiture2 = new Voiture(
                    "Peugeot", "308", LocalDate.of(2019, 3, 10), 18000.0,
                    5, true, "Diesel"
            );

            Moto moto1 = new Moto(
                    "Honda", "CBR", LocalDate.of(2021, 7, 20), 12000.0,
                    600, "Manuelle"
            );

            // Persist vehicles
            em.persist(voiture1);
            em.persist(voiture2);
            em.persist(moto1);

            em.getTransaction().commit();
            System.out.println("Vehicles created successfully!");

            // Polymorphic queries
            em.clear();

            System.out.println("\nFetching all vehicles:");
            List<Vehicule> vehicules = em.createQuery("SELECT v FROM Vehicule v", Vehicule.class)
                    .getResultList();
            vehicules.forEach(System.out::println);

            System.out.println("\nFetching all cars:");
            List<Voiture> voitures = em.createQuery("SELECT v FROM Voiture v", Voiture.class)
                    .getResultList();
            voitures.forEach(System.out::println);

            System.out.println("\nFetching all motorcycles:");
            List<Moto> motos = em.createQuery("SELECT m FROM Moto m", Moto.class)
                    .getResultList();
            motos.forEach(System.out::println);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void testJoined(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Create employees
            System.out.println("Creating employees...");

            Developpeur dev1 = new Developpeur(
                    "Dupont", "Jean", "jean.dupont@example.com", LocalDate.of(2019, 5, 15),
                    "Java", "Backend", 5
            );

            Developpeur dev2 = new Developpeur(
                    "Martin", "Sophie", "sophie.martin@example.com", LocalDate.of(2020, 3, 10),
                    "JavaScript", "Frontend", 3
            );

            Manager manager1 = new Manager(
                    "Dubois", "Pierre", "pierre.dubois@example.com", LocalDate.of(2018, 7, 20),
                    "IT", 10, 5000.0
            );

            // Persist employees
            em.persist(dev1);
            em.persist(dev2);
            em.persist(manager1);

            em.getTransaction().commit();
            System.out.println("Employees created successfully!");

            // Polymorphic queries
            em.clear();

            System.out.println("\nFetching all employees:");
            List<Employe> employes = em.createQuery("SELECT e FROM Employe e", Employe.class)
                    .getResultList();
            employes.forEach(System.out::println);

            System.out.println("\nFetching all developers:");
            List<Developpeur> devs = em.createQuery("SELECT d FROM Developpeur d", Developpeur.class)
                    .getResultList();
            devs.forEach(System.out::println);

            System.out.println("\nFetching all managers:");
            List<Manager> managers = em.createQuery("SELECT m FROM Manager m", Manager.class)
                    .getResultList();
            managers.forEach(System.out::println);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void testTablePerClass(EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Create products
            System.out.println("Creating products...");

            Livre livre1 = new Livre(
                    "Le Seigneur des Anneaux", 25.99, "Un classique de fantasy",
                    "J.R.R. Tolkien", "978-2075134729", 1200, "Pocket"
            );

            Livre livre2 = new Livre(
                    "Harry Potter", 19.99, "Roman fantastique pour jeunes",
                    "J.K. Rowling", "978-2070643028", 800, "Gallimard"
            );

            Electronique elec1 = new Electronique(
                    "Smartphone Galaxy S21", 899.99, "Smartphone haut de gamme",
                    "Samsung", "S21", 24, "Écran 6.2\", 128Go, 8Go RAM"
            );

            // Persist products
            em.persist(livre1);
            em.persist(livre2);
            em.persist(elec1);

            em.getTransaction().commit();
            System.out.println("Products created successfully!");

            // Polymorphic queries
            em.clear();

            System.out.println("\nFetching all products:");
            List<Produit> produits = em.createQuery("SELECT p FROM Produit p", Produit.class)
                    .getResultList();
            produits.forEach(System.out::println);

            System.out.println("\nFetching all books:");
            List<Livre> livres = em.createQuery("SELECT l FROM Livre l", Livre.class)
                    .getResultList();
            livres.forEach(System.out::println);

            System.out.println("\nFetching all electronic products:");
            List<Electronique> elecs = em.createQuery("SELECT e FROM Electronique e", Electronique.class)
                    .getResultList();
            elecs.forEach(System.out::println);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
