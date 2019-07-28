package edu.pkch.jpaedu;

import edu.pkch.jpaedu.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaedu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            logic(em);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
    }

    private static void logic(final EntityManager em) {
        String id = "id1";
        User user = new User();
        user.setId(id);
        user.setUsername("지한");
        user.setAge(2);
        //등록
        em.persist(user);

        //수정
        user.setAge(20);

        //한 건 조회
        User findUser = em.find(User.class, id);
        System.out.println("findMember=" + findUser.getUsername() + ", age=" + findUser.getAge());

        //목록 조회
        List<User> users = em.createQuery("select m from User m", User.class).getResultList();
        System.out.println("members.size=" + users.size());

        //삭제
        em.remove(user);
    }
}
