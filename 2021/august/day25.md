# 2021.08.25 TIL - JpaAutoConfiguration

- [2021.08.25 TIL - JpaAutoConfiguration](#20210825-til---jpaautoconfiguration)
  - [DataSourceAutoConfiguration](#datasourceautoconfiguration)
  - [HibernateJpaAutoConfiguration](#hibernatejpaautoconfiguration)
    - [LocalContainerEntityManagerFactoryBean](#localcontainerentitymanagerfactorybean)
    - [LocalContainerEntityManagerFactoryBean를 직접 정의할 때 참고](#localcontainerentitymanagerfactorybean를-직접-정의할-때-참고)
      - [JpaProperties는 적용된다](#jpaproperties는-적용된다)
    - [JpaTransactionManager](#jpatransactionmanager)

아래 순서대로 스프링 환경에서 JPA를 사용하기 위한 설정을 한다.

1. DataSourceAutoConfiguration
2. HibernateJpaAutoConfiguration `HibernateJpaConfiguration`, JpaBaseConfiguration

## DataSourceAutoConfiguration

DataSourceAutoConfiguration는 DataSource와 DataSource를 설정한 애플리케이션의 환경에 따라 Embedded 설정 또는 Pool 설정을 한다.

> HSQL, H2, DERBY인 경우 Embedded 설정을 한다. `org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseCondition 참고`

HikariCP나 DBCP 같은 의존성이 데이터베이스 커넥션 풀 의존성이 존재한다면 Pool 설정을 한다. `org.springframework.boot.autoconfigure.jdbc.PooledDataSourceConfiguration 참고`

Spring Boot 2.0.0 이상부터는 HikariCP를 기본 커넥션 풀로 사용하므로 `DataSourceConfiguration.Hikari`가 동작한다.

## HibernateJpaAutoConfiguration

DataSourceAutoConfiguration이 동작한 후에 HibernateJpaAutoConfiguration가 작동한다. HibernateJpaAutoConfiguration은 따로 설정이 있지는 않고 HibernateJpaConfiguration을 import하고 있다.

HibernateJpaConfiguration는 JpaBaseConfiguration을 확장한 JPA + 하이버네이트 Configuration 클래스이다. 따라서 HibernateJpaConfiguration에서 EntityManager를 생성, JpaProperties 설정, JpaVendorAdapter 생성 등을 한다.

HibernateJpaConfiguration에서 하이버네이트 관련 설정을 위한 프로퍼티인 HibernateProperties `spring.jpa.hibernate.*`를 받고 있으며 DataSource JPA 구현체의 설정인 vendorProperties `HibernateJpaConfiguration에서는 하이버네이트 설정`과 mapping-resource, JTA 여부 등을 가지고 EntityManagerFactory를 생성한다.

> EntityManagerFactory는 `LocalContainerEntityManagerFactoryBean`로 생성한다.

### LocalContainerEntityManagerFactoryBean

`org.springframework.beans.factory.FactoryBean`을 확장한 클래스이다.

JPA 스팩에 EntityManager는 Application-Managed와 Container-Managed 2가지 유형이 존재한다고 소개한다. LocalContainerEntityManagerFactoryBean은 Container-Managed로 JPA를 사용하는 방법으로 정의할 수 있는 방법이다.

> 단, Spring 환경에서는 Application-Managed나 Container-Managed는 큰 차이가 없다고한다.

Spring Boot에서는 LocalContainerEntityManagerFactoryBean을 정의하지 않는다면 JpaBaseConfiguration을 통해 생성한다.

> 기본적으로 persistenceUnit의 이름을 default로 생성한다.

JpaBaseConfiguration에서 LocalContainerEntityManagerFactoryBean 빈 생성부분에 `@ConditionalOnMissingBean({ LocalContainerEntityManagerFactoryBean.class, EntityManagerFactory.class })` 다음과 같이 EntiryManagerFactory 빈이나 LocalContainerEntityManagerFactoryBean 빈이 없는 경우에 생성을 하도록 설정이 되어있기 때문에 직접 정의한다면 `JpaBaseConfiguration#entityManagerFactory`는 동작하지 않는다.

Spring Boot에서는 LocalContainerEntityManagerFactoryBean을 쉽게 정의할 수 있도록 기본 EntityManagerFactoryBuilder를 생성한다. 따라서 직접 정의할때는 EntityManagerFactoryBuilder를 사용한다면 손쉽게 LocalContainerEntityManagerFactoryBean을 정의할 수 있다.

> 단, EntityManagerFactoryBuilder 또한 직접 빈으로 정의한다면 Spring Boot에서는 생성하지 않는다.

LocalContainerEntityManagerFactoryBean와 LocalEntityManagerFactoryBean 차이 참고: [https://stackoverflow.com/questions/6156832/what-is-the-difference-between-localcontainerentitymanagerfactorybean-and-locale](https://stackoverflow.com/questions/6156832/what-is-the-difference-between-localcontainerentitymanagerfactorybean-and-locale)

### LocalContainerEntityManagerFactoryBean를 직접 정의할 때 참고

> Spring Boot가 제공하는 EntityManagerFactoryBuilder로 정의한다고 가정

#### JpaProperties는 적용된다

LocalContainerEntityManagerFactoryBean를 직접 정의하면 JpaProperties `spring.jpa.properties`는 적용이 되는 반면 `spring.jpa.hibernate`와 같이 JPA 구현체 벤더에 특화된 설정은 적용이 되지 않는다.

```java
@Bean
@ConditionalOnMissingBean
public EntityManagerFactoryBuilder entityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
                                                               ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                                               ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers) {
    EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter,
            this.properties.getProperties(), persistenceUnitManager.getIfAvailable());
    customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
    return builder;
}
```

위 코드는 JpaBaseConfiguration에 정의된 EntityManagerFactoryBuilder 빈 생성코드이다. 위 코드를 보면 JpaProperties를 받아 생성하는 것을 볼 수 있다.

> JpaBaseConfiguration에 JpaProperties 타입인 properties가 정의되어 있기 때문에 `this.properties`로 접근이 가능하다.

반면 하이버네이트 같이 벤더에 특화된 설정은 EntityManagerFactory를 생성할 때 정의를 한다.

```java
@Bean
@Primary
@ConditionalOnMissingBean({ LocalContainerEntityManagerFactoryBean.class, EntityManagerFactory.class })
public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factoryBuilder) {
    Map<String, Object> vendorProperties = getVendorProperties();
    customizeVendorProperties(vendorProperties);
    return factoryBuilder.dataSource(this.dataSource).packages(getPackagesToScan()).properties(vendorProperties)
            .mappingResources(getMappingResources()).jta(isJta()).build();
}
```

위 코드는 Spring Boot에서 LocalContainerEntityManagerFactoryBean 타입의 entityManagerFactory 빈을 생성하는 코드이다.

각 벤더의 Configuration에서 구현한 `getVendorProperties` 메서드를 사용하여 벤더에 맞는 설정을 적용한다.

> 위의 경우 JPA 벤더로 하이버네이트를 사용하므로 `HibernateJpaConfiguration#getVendorProperties`를 호출하고 있다.

참고로 `JpaBaseConfiguration#getVendorProperties`는 추상메서드이다.

따라서 LocalContainerEntityManagerFactoryBean를 직접 구현한다면 벤더에 특화된 설정은 직접 구현시에 `EntityManagerFactoryBuilder#properties`를 통해 해야한다.

### JpaTransactionManager

PlatformTransactionManager 빈을 직접 정의하지 않는다면 Spring Boot의 JpaBaseConfiguration에서 JpaTransactionManager를 기본으로 생성한다.

```java
@Bean
@ConditionalOnMissingBean
public PlatformTransactionManager transactionManager(ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManagerCustomizers.ifAvailable((customizers) -> customizers.customize(transactionManager));
    return transactionManager;
}
```
