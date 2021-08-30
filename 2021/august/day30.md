# 2021.08.30 TIL - Spring MultiDataSource 환경에서 QuerydslRepositorySupport

Spring JPA를 사용하면서 여러 DataSource, EntityManager를 등록할 수 있다. 그리고 많은 경우에 JPQL을 사용하기보다 Querydsl을 사용하는 경우가 많은데 이때 QuerydslRepositorySupport를 사용하게된다.

```java
@Repository
public abstract class QuerydslRepositorySupport {

    private final PathBuilder<?> builder;

    private @Nullable EntityManager entityManager;
    private @Nullable Querydsl querydsl;

    public QuerydslRepositorySupport(Class<?> domainClass) {

        Assert.notNull(domainClass, "Domain class must not be null!");
        this.builder = new PathBuilderFactory().create(domainClass);
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {

        Assert.notNull(entityManager, "EntityManager must not be null!");
        this.querydsl = new Querydsl(entityManager, builder);
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
    }

    @Nullable
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected JPQLQuery<Object> from(EntityPath<?>... paths) {
        return getRequiredQuerydsl().createQuery(paths);
    }

    protected <T> JPQLQuery<T> from(EntityPath<T> path) {
        return getRequiredQuerydsl().createQuery(path).select(path);
    }

    protected DeleteClause<JPADeleteClause> delete(EntityPath<?> path) {
        return new JPADeleteClause(getRequiredEntityManager(), path);
    }

    protected UpdateClause<JPAUpdateClause> update(EntityPath<?> path) {
        return new JPAUpdateClause(getRequiredEntityManager(), path);
    }

    @SuppressWarnings("unchecked")
    protected <T> PathBuilder<T> getBuilder() {
        return (PathBuilder<T>) builder;
    }

    @Nullable
    protected Querydsl getQuerydsl() {
        return this.querydsl;
    }

    private Querydsl getRequiredQuerydsl() {

        if (querydsl == null) {
            throw new IllegalStateException("Querydsl is null!");
        }

        return querydsl;
    }

    private EntityManager getRequiredEntityManager() {

        if (entityManager == null) {
            throw new IllegalStateException("EntityManager is null!");
        }

        return entityManager;
    }
}
```

위는 Spring Data JPA에서 제공하는 `org.springframework.data.jpa.repository.support.QuerydslRepositorySupport`이다.

여기서 유의해야할 부분은 `setEntityManager`로 EntityManager를 주입받는 부분이다.

```java
@Autowired
public void setEntityManager(EntityManager entityManager) {

    Assert.notNull(entityManager, "EntityManager must not be null!");
    this.querydsl = new Querydsl(entityManager, builder);
    this.entityManager = entityManager;
}
```

위 부분에서 현재 애플리케이션에 등록된 EntityManager 빈을 받아서 주입하게되는데 여러 EntityManager를 사용하는 경우에는 어떤 EntityManager를 사용해야할 지 알 수 없기 때문에 다음과 같은 `UnsatisfiedDependencyException` 에러가 발생한다.

```text
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'testRepositoryCustomImpl': Unsatisfied dependency expressed through method 'setEntityManager' parameter 0; nested exception is org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'javax.persistence.EntityManager' available: expected single matching bean but found 2: org.springframework.orm.jpa.SharedEntityManagerCreator#0,org.springframework.orm.jpa.SharedEntityManagerCreator#1
```

때문에 QuerydslRepositorySupport를 확장하여 setEntityManager를 오버라이딩하는 방식으로 어떤 EntityManager를 사용할지 명시해주어야한다.

```java
public abstract class TestQuerydslRepositorySupport extends QuerydslRepositorySupport {

    public TestQuerydslRepositorySupport(Class<?> domainClass) {
        super(domainClass);
    }

    @Override
    @PersistenceContext(unitName = "default")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }
}
```

위와 같이 `@PersistenceContext`로 PersistenceUnit의 이름을 지정하여 해당 EntityManagerFactory의 EntityManager를 사용하도록 할 수 있다.

> `@PersistenceContext`는 지정한 EntityManagerFactory에서 EntityManager를 생성하여 주입하도록 도와준다.

## 참고

`@PersistenceContext` 동작원리 참고: [https://netframework.tistory.com/entry/Spring에서-PersistenceContext의-동작-원리](https://netframework.tistory.com/entry/Spring%EC%97%90%EC%84%9C-PersistenceContext%EC%9D%98-%EB%8F%99%EC%9E%91-%EC%9B%90%EB%A6%AC)

spring jpa querydsl multiple datasource 참고: [http://wonwoo.ml/index.php/post/804](http://wonwoo.ml/index.php/post/804)