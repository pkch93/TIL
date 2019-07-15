# 2019.07.14 Sunday

# JPA?

JPA는 자바 진영의 표준 ORM 프레임워크로 객체와 관계형 데이터베이스 간의 차이를 해결해주는 프레임워크이다. 이를 통해 기존의 테이블 중심 설계에서 객체 중심 설계를 가능하게 해준다.

이런 JPA를 사용하면 다음과 같은 이점이 있다.

1. 데이터 저장 계층에서 작성할 코드가 줄어든다.

   JPA에서 데이터 CRUD에 대한 처리를 해주므로 SQL을 사용할 때보다 적은 코드로 로직을 구현할 수 있다. 즉, SQL은 JPA에 맡기고 비즈니스 로직에만 집중할 수 있다.

2. 객체 중심 개발이 가능해진다.

   때문에 생산성, 유지보수가 좋아지고 테스트 작성이 용이하다는 장점이 있다. 

다만, JPA를 제대로 알고 사용하지 않으면 각종 버그에 시달릴 수 있다. 알아야할 것이 어느정도 있기 때문에 러닝커브가 높은 편에 속하는 프레임워크이다. 하지만 다양한 장점이 있기 때문에 학습하는 것을 권장하고 있다.

## SQL을 직접 다룰때 생길 수 있는 문제점

자바 진영에서는 직접 SQL을 사용할 때 JDBC를 이용한다. 대부분의 자바 서버 개발자는 능숙하게 SQL을 작성하여 JDBC를 활용할 수 있지만 다음과 같은 문제가 있다.

1. 반복

   하나의 도메인을 구현한다고 가정하자. 이때 그 도메인에 대한 DAO를 생성할 것이며 조회, 생성, 수정, 삭제 가능을 각각 SQL작성, 실행, 매핑하는 코드를 짤 것이다. 그리고 만약 객체 구조가 변경된다면 DB와 다른 구조가 되므로 SQL과 JDBC를 사용하여 직접 변환작업을 해주어야한다.

   이런 지루하고 번거로운 작업을 도메인 구현마다 해주어야한다.

2. SQL 의존 개발

   고객의 요구사항은 끊임없이 변한다. 이때 SQL에 의존적인 개발은 번거로움을 유발한다.

   만약 요구사항이 바껴서 테이블 컬럼이 추가된다고 가정하자. 이 경우 조회, 수정, 생성을 담당하는 SQL이 바껴야하는 가능성이 농후하다. 수정의 경우 추가된 컬럼이 수정될 수 있도록 만들어야할 수 있으며 생성의 경우도 마찬가지이다.  또한, 다른 객체와 관계를 맺는 경우는 DAO를 들춰봐서 제대로 조회하고 있는지 확인도 해야한다. 즉, 계층 분리가 되지 않는 문제점이 생긴다.

   즉, 엔티티와 SQL이 밀접한 관계를 맺고 있으므로 엔티티에 필드하나를 추가하더라도 CRUD 코드를 변경해야하는 문제, SQL을 변경해야하는 문제가 생긴다.

이런 문제들을 JPA를 사용하면 해결할 수 있다.

## 패러다임 불일치

데이터를 RDB에 저장하게되면 데이터 중심적으로 구조화하게 된다. 이는 객체가 지원하는 상속, 캡슐화, 다형성, 추상화, 정보은닉 등의 객체지향 패러다임과 어긎난다. 이렇게 데이터 중심의 RDB 데이터를 객체로 만들어 객체지향 프로그래밍을 하고싶다면 개발자가 중간에서 해결해야한다.

하지만 객체와 RDB간의 불일치를 해결하는데 많은 시간과 코드가 소모된다. 때문에 JPA를 사용하면 깔끔히 해결하면서 객체지향적으로 프로그래밍하기 용이하다.

# JPA Environment Setting

JPA는 Hibernate를 기반으로 구현하고 있다. 때문에 Hibernate의 설정 파일인 persistence.xml이 필요하다.

## persistence.xml

JPA의 설정파일인 `Persistence.xml`은 JPA 사용에 필요한 정보를 관리하는 파일로 `classpath:META-INF/persistence.xml` 경로에 존재한다면 별도의 설정 없이도 JPA를 사용할 수 있다.

## persistence

`persistence.xml`는 persistence로 시작한다. 이곳에 xml 네임스페이스와 version을 명시하면 된다. 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">

	<!-- ... -->

</persistence>
```

위 설정으로 `persistence.xml`을 작성하는데 필요한 각종 네임스페이스를 사용할 수 있다.

> 최신버전은 2.2

## persistence-unit

JPA 설정은 persistence-unit에서 시작한다. 일반적으로 연결하려는 데이테베이스당 하나의 persistence-unit을 설정한다. 이때, 각 persistence-unit에 고유한 이름을 붙여야한다.

```xml
<persistence-unit name="jpaedu">
	<!-- ... -->
</persistence-unit>
```

이렇게 설정한 persistence-unit에는 각종 property를 설정할 수 있다.

persistence-unit는 보통 하나의 데이터베이스를 뜻한다. 따라서 보통은 DB 연결 설정 및 데이터베이스 간 차이를 반영하기 위한 dialect 설정을 한다.

### properties

다음은 h2 DB 관련 property이다.

```xml
<properties>
    <!-- 필수 속성 -->
    <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
    <property name="javax.persistence.jdbc.user" value="sa"/>
    <property name="javax.persistence.jdbc.password" value=""/>
    <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
    <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect" />

    <!-- 옵션 -->
    <property name="hibernate.show_sql" value="true" />
    <!-- 하이버네이트가 실행한 SQL 출력 여부 옵션 -->
    <property name="hibernate.format_sql" value="true" />
    <!-- 하이버네이트가 실행한 SQL을 보기 쉽게 출력 -->
    <property name="hibernate.use_sql_comments" value="true" />
    <!-- 쿼리 출력시 주석도 함께 출력 -->
    <property name="hibernate.id.new_generator_mappings" value="true" />
    <!-- JPA 표준에 맞게 새로운 키 생성 전략 사용 -->

<!-- <property name="hibernate.hbm2ddl.auto" value="create" /> -->
</properties>
```

### provider

경우에 따라 provider를 설정해야한다. 

provider 속성은 어떤 JPA EntityManager를 사용할지 명시하는 속성으로 주의할 점은 persistence-unit의 최상위에 작성해야한다.

```xml
<persistence-unit name="jpaedu">
	<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
	<!-- ... -->
</persistence-unit>
```

※ 참고

이전에는 `org.hibernate.ejb.HibernatePersistence`를 provider로 많이 사용했지만 최근에는 위 처럼 `org.hibernate.jpa.HibernatePersistenceProvider`사용을 권고한다.

### class

JPA에서 사용할 Entity 클래스를 지정하는 속성이다. persistence-unit 하위에 작성한다.

보통은 JPA 구현체가 엔티티 클래스를 자동으로 인식하지만, 환경에 따라 인식하지 못하는 경우가 있다. 때문에 위 `<class></class>` 속성으로 지정해준다. 참고로 Spring이나 J2EE 환경에서는 Entity를 탐색하는 기능을 제공해준다.

```xml
<persistence-unit name="jpaedu">
	<provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
	<class>edu.pkch.jpaedu.Member</class>
	<!-- ... -->
</persistence-unit>
```

그외 추가적인 속성들은 [https://thoughts-on-java.org/jpa-persistence-xml/](https://thoughts-on-java.org/jpa-persistence-xml/) 참고

## 그 외 알게된 것

현재 실습은 `org.hibernate:hibernate-entitymanager:4.3.10.Final`을 사용하고 있다. 현재 hibernate의 stable version은 `5.4.3.Final`이며 entitymanager, validatior 등의 sub module 등이 `hibernate-core`로 합쳐졌다.

# JPA&Hibernate Basic

## EntityManager 설정

### EntityManagerFactory

JPA의 시작은 EntityManager를 생성하는 것이다. 이를 통해 DB와 연동하여 ORM을 사용할 수 있다. EntityManager를 사용하기 위해서는 EntityManagerFactory를 생성해야한다.

이때 Persistence를 사용하여 EntityManagerFactory를 생성하는데 `META-INF/persistence.xml`에서 설정한 persistence-unit을 찾아 EntityManagerFactory를 생성한다. 이때 사용하는 것이 persistence-unit의 name이다. 고유한 name을 가지고 원하는 persistence-unit을 찾아 EntityManagerFactory를 생성하는 것이다.

```java
// ...
EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpaedu");
// ...
```

위와 같이 Persistence 클래스의 팩토리 메서드인 createEntityManagerFactory로 인자 `jpaedu`를 주면 persistence.xml에서 `jpaedu`라는 persistence-unit을 찾아 생성하게 된다.

이때 EntityManagerFactory는 JPA 구현체에 따라 DB 커넥션풀도 생성한다. 때문에 EntityManagerFactory를 생성하는 비용이 아주 크다. 따라서 애플리케이션에서 딱 한번만 생성하고 공유해서 사용하도록 해야한다.

### EntityManager

EntityManagerFactory에서 EntityManager를 만들어 사용한다. JPA 대부분의 기능은 위 EntityManager가 제공한다.

```java
// ...
EntityManager em = emf.createEntityManager();
// ,,,
```

이렇게 생성한 EntityManager를 통해 Entity를 DB에 등록/조회/수정/삭제 (`CRUD`) 할 수 있다.

EntityManager는 DB 커넥션을 유지하면서 DB와 통신한다. 때문에 스레드간에 공유하거나 재사용해서는 안된다.

### close

애플리케이션 종료시에는 반드시 사용하고 있는 EntityManagerFactory와 EntityManager를 종료해야한다.

```java
em.close();
emf.close();
```

### Transaction

JPA 사용시 항상 트랜젝션 내부에서 데이터를 조작해야한다. 그렇지 않으면 예외가 발생한다.

트랜젝션을 시작하기 위해서는 EntityManager에서 트랜젝션 API를 받아야한다.

```java
// ...
EntityTransaction tx = em.getTransaction();

try {
    tx.begin();
    logic(em);
    tx.commit();
} catch (Exception e) {
    tx.rollback();
} finally {
    em.close();
}
// ...
```

위와 같이 `getTransaction`메서드를 활용하여 트랜잭션 API를 가져올 수 있다. try-catch를 활용하여 만약 중간에 오류가 생기면 rollback으로 이전 상태로 돌리는 등 오류에 대처할 수 있다.