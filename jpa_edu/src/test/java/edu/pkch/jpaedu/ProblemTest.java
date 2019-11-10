package edu.pkch.jpaedu;

import edu.pkch.jpaedu.domain.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ProblemTest extends JpaTest {

    private EntityManager em;

    @BeforeEach
    void setUp() {
        em = createEntityManager();
        Company company = new Company("hello");
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        em.persist(company);


        transaction.commit();
    }

    @Test
    @DisplayName("N+1 문제")
    void n_plus_one_problem() {
    }
}
