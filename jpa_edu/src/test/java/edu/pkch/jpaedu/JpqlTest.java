package edu.pkch.jpaedu;

import edu.pkch.jpaedu.domain.Address;
import edu.pkch.jpaedu.domain.Company;
import edu.pkch.jpaedu.domain.User;
import edu.pkch.jpaedu.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JpqlTest {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaedu");
    EntityManager em;
    User user;
    Company company;

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        user = new User("pkch", "park", 27);
        Address address = new Address("pohang", "samho-ro", "08707");
        company = new Company("woowabros");
        addEntity(company);
        user.setAddress(address);
        addUser(user, company);
    }

    private void addEntity(Object entity) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.persist(entity);
        transaction.commit();
    }

    private void addUser(User user, Company company) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        user.setCompany(company);
        em.persist(user);
        transaction.commit();
    }

    @Test
    @DisplayName("유저 엔티티 조회 jqpl 테스트")
    void queryByJpqlForUserEntity() {
        TypedQuery<User> userQuery = em.createQuery("SELECT u FROM User u", User.class);
        assertEquals(user, userQuery.getSingleResult());
    }

    @Test
    @DisplayName("유저 엔티티의 컬럼 중 이름만 조회 jqpl 테스트")
    void queryByJpqlForUsername() {
        TypedQuery<String> usernameQuery = em.createQuery("SELECT u.username FROM User u", String.class);
        assertEquals(user.getUsername(), usernameQuery.getSingleResult());
    }

    @Test
    @DisplayName("유저 엔티티의 컬럼 중 타입이 다른 이름과 나이 조회 jqpl 테스트 / getResultList")
    void queryByJpqlForUsernameAndAge() {
        Query usernameAndAgeQuery = em.createQuery("SELECT u.username, u.age FROM User u");
        List resultList = usernameAndAgeQuery.getResultList();

        Object[] result = (Object[]) resultList.get(0);

        assertEquals(user.getUsername(), result[0]);
        assertEquals(user.getAge(), result[1]);
    }

    @Test
    @DisplayName("유저 엔티티의 컬럼 중 타입이 다른 이름과 나이 조회 jqpl 테스트 / getSingleResult")
    void queryByJpqlForUsernameAndAgeGetSingleResult() {
        Query usernameAndAgeQuery = em.createQuery("SELECT u.username, u.age FROM User u");
        Object[] result = (Object[]) usernameAndAgeQuery.getSingleResult();

        assertEquals(user.getUsername(), result[0]);
        assertEquals(user.getAge(), result[1]);
    }

    @Test
    @DisplayName("jpql이 두개 이상의 엔티티 객체를 가져올때 예외 테스트")
    void queryByJpqlForMultipleElementsOfGetSingleResult() {
        User other = new User("pkch93", "pkch", 27);
        addEntity(other);

        Query usernameAndAgeQuery = em.createQuery("SELECT u.username, u.age FROM User u");
        assertThrows(NonUniqueResultException.class, usernameAndAgeQuery::getSingleResult);

        removeEntity(other);
    }

    @Test
    @DisplayName("jpql 쿼리 결과가 하나도 없을때 getSingleResult 예외 테스트")
    void queryByJpqlForNoElementException() {
        removeEntity(user);
        Query usernameAndAgeQuery = em.createQuery("SELECT u.username, u.age FROM User u");
        assertThrows(NoResultException.class, usernameAndAgeQuery::getSingleResult);
    }

    @Test
    @DisplayName("이름 기반 파라미터 바인딩 유저 엔티티 조회 jqpl 테스트")
    void queryByJpqlNameParameterForUserEntity() {
        User other = new User("pkch93", "pkch", 27);
        addEntity(other);

        User result = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", other.getUsername())
                .getSingleResult();
        assertEquals(other, result);

        removeEntity(other);
    }

    @Test
    @DisplayName("위치 기반 파라미터 바인딩 유저 엔티티 조회 jqpl 테스트")
    void queryByJpqlPositionParameterForUserEntity() {
        User other = new User("pkch93", "pkch", 27);
        addEntity(other);

        User result = em.createQuery("SELECT u FROM User u WHERE u.username = ?1", User.class)
                .setParameter(1, other.getUsername())
                .getSingleResult();
        assertEquals(other, result);

        removeEntity(other);
    }

    @Test
    @DisplayName("N:1로 맺어진 엔티티 조회 jqpl 테스트")
    void queryByJpqlUsersCompanyEntity() {
        Company result = em.createQuery("SELECT u.company FROM User u", Company.class)
                .getSingleResult();

        assertEquals(company, result);
    }

    @Test
    @DisplayName("User의 값타입 Address 조회 jqpl 테스트")
    void queryByJpqlUsersAddressValueObject() {
        Address result = em.createQuery("SELECT u.address FROM User u", Address.class)
                .getSingleResult();

        Address address = new Address("pohang", "samho-ro", "08707");

        assertEquals(address, result);
    }

    @Test
    @DisplayName("User의 스칼라 타입 username 조회 jqpl 테스트")
    void queryByJpqlUsersName() {
        String result = em.createQuery("SELECT username FROM User u WHERE u.age = :age", String.class)
                .setParameter("age", 27)
                .getSingleResult();

        assertEquals("park", result);
    }

    @Test
    @DisplayName("엔티티 User의 username과 age를 UserDto로 변환하는 jpql 테스트")
    void queryByJpqlForUserConversion() {
        UserDto result = em.createQuery("SELECT new edu.pkch.jpaedu.dto.UserDto(u.username, u.age) FROM User u", UserDto.class)
                .getSingleResult();

        assertEquals("park", result.getUsername());
        assertEquals(27, result.getAge());
    }

    @Test
    @DisplayName("NamedQuery 사용하여 User 정보 조회 테스트")
    void queryByNamedQuery() {
        User result = em.createNamedQuery("User.findByUsername", User.class)
                .setParameter("username", "park")
                .getSingleResult();

        assertEquals(user, result);
    }

    @Test
    @DisplayName("Company fetch join 테스트")
    void queryByFetchJoin() {
        TypedQuery<Company> query = em.createQuery("SELECT c FROM Company c join fetch c.users", Company.class);

        query.getSingleResult();
    }

    @Test
    @DisplayName("Company entityGraph 조회 테스트")
    void queryByEntityGraph() {
        TypedQuery<Company> query = em.createQuery("SELECT c FROM Company c", Company.class)
                .setHint("javax.persistence.fetchgraph", em.getEntityGraph("Company.users"));

        query.getSingleResult();
    }



    @AfterEach
    void tearDown() {
        removeEntity(user);
        removeEntity(company);
        em.close();
    }

    private void removeEntity(Object entity) {
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.remove(entity);
        transaction.commit();
    }
}
