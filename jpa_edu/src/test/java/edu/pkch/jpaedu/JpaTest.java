package edu.pkch.jpaedu;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaTest {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaedu");

    protected EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
}
