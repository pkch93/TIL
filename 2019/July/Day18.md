# 2019.07.18 Thursday

# DDL 생성 기능

> JPA의 스키마 자동생성 기능에 제약조건 추가하기

필드별 `constraint`를 추가할 수 있다. 단일 제약조건을 주려면 `@Column`어노테이션에 제약조건에 맞는 속성을 설정한다. 보통 @Nullable, @Length등을 사용한다.

`@Table`의 `@UniqueConstraint`를 설정하여 유니크 제약조건을 사용할 수 있다. 

```
@Entity
@Table(name = "Member", uniqueConstrains = { @UniqueConstraints(
	name = "NAME_AGE_CONSTRAINT"
	columnNames = {"NAME", "AGE"}
)})
public class Member {
	@Id
	private String id;
	private String username;
	// ...
}
```

이런 제약 조건들은 JPA를 통해 DDL이 생성될 때 적용된다. 즉, SQL을 직접 사용하여 DDL을 사용할 경우는 저렇게 제약조건을 주지 않아도 된다.

단, 위 같이 사용하면 개발자가 DB를 볼 필요없이 Entity만 보더라도 손쉽게 다양한 제약조건을 파악할 수 있다는 장점이 있다.

# 기본 키 매핑

JPA에서는 기본적으로 DB 기본 키 생성 전략을 다음과 같이 정할 수 있다.

1. 직접 할당
2. 자동 할당

직접 할당의 경우는 기본 키를 애플리케이션에서 직접 할당해 주는 것이고 자동 할당은 대리키를 사용하는 방식이다.

여기서 주로 다룰 방법은 **자동 할당**이다. `SEQUENCE`, `IDENTITY`, `TABLE`등의 생성 전략이 있는 이유는 DB마다 지원하는 방식이 다르기 때문이다.

MySQL에서는 시퀀스를 제공하지 않는 대신 Oracle에서는 지원해주는 것이 대표적인 예이다.

키 생성 전략을 사용하기 위해서는 `persistence.xml`에 `hibernate.id.new_generator_mappings`를 true로 설정해야만 한다. 이는 hibernate가 효과적으로 키 생성 전략을 개발했는데 과거와의 호환성을 유지하기 위해 해당 값을 기본적으로 false로 설정했다. 때문에 기존의 JPA를 사용한 시스템을 유지보수하는 것이 아니라면 해당 프로퍼티를 true로 설정해야한다.

```
<property name="hibernate.id.new_generator_mappings" value="true" />
```

## 직접 할당

직접할당의 경우 `@Id`로 매핑된 필드의 값을 추가한 채로 Persistence Context에 추가하는 방법이다. 이 때, `@Id`즉, 식별자가 없이 추가하면 예외가 발생한다.

## 자동 할당

기본 키를 자동 할당하기 위해서는 `@Id` 필드에 반드시 `@GerneratedValue`가 필요하다. `@GeneratedValue`어노테이션의 `strategy`속성을 통해 키 자동 할당 전략을 바꿀 수 있다.

1. GenerationType.IDENTITY

   주로 MySQL, Postgresql, SQL Server, DB2에서 사용하는 전략이다. MySQL의 `AUTO_INCREMENT`처럼 데이터베이스에 값을 저장하고 나서야 기본 키 값을 구할 수 있을 때 사용하는 전략이다.

   ```
   @Entity
   public class Member {
   	@Id @GerneratedValue(strategy = GenerationType.IDENTITY)
   	private long id;
   	// ...
   }
   ```

   `IDENTITY`전략은 데이터를 DB에 저장해야만 식별자 값을 얻을 수 있다. 때문에 원래는 엔티티의 식별자 값을 할당하기 위해서는 JPA는 추가로 DB를 조회해야한다. JDBC3의 `Statement.getGeneratedKeys()`를 사용하면 저장과 동시에 식별자 값을 가져오므로 DB와 한 번의 통신으로 원하는 결과를 얻을 수 있다.

   단, 엔티티가 영속 상태가 되려면 반드시 식별자가 필요하다. 단, `IDENTITY`전략은 엔티티를 DB에 저장해야 식별자를 구할 수 있기 때문에 `EntityManager.persist()`를 호풀하는 즉시 INSERT SQL이 DB에 전달된다. 때문에 `IDENTITY`전략은 쓰기 지연이 동작하지 않는다.

2. GenerationType.SEQUENCE

> DB 시퀀스란 유일한 값을 순서대로 생성하는 특별한 DB 오브젝트이다.

`SEQUENCE` 전략은 DB에서 지원하는 시퀀스를 사용하여 식별자를 생성하는 전략으로 Oracle, PostgreSQL, DB2, H2에서 사용할 수 있다.

```
@Entity
@SequenceGenerator(
	name = "BOARD_SEQ_GENERATOR",
	sequenceName = "BOARD_SEQ",
	initialValue = 1, allocationSize = 1
)
public class Member {
	@Id
	@GerneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
	private long id;
	// ...
}
```

`SEQUENCE`전략을 사용하기 위해서는 DB 시퀀스를 매핑해야한다. 이를 위해 위 예제와 같이 `@SequenceGenerator`로 시퀀스 생성기를 등록해야한다. 그리고 이 생성기를 `@GeneratedValue`의 `generator`속성으로 매핑해주면 된다.

> 참고로 `@SequenceGenerator`는 `@GeneratedValue`와 함께 쓰여도 된다.

`SEQUENCE`전략은 `IDENTITY`와는 다르게 쓰기 지연이 지원된다. 이는 시퀀스를 먼저 조회하여 저장할 데이터의 식별자를 알아낼 수 있기 때문이다. 트랜잭션이 커밋되면 플러시가 일어나는 것이다.

### @SequenceGenerator의 속성

- name : 식별자 생성기 이름, 반드시 등록해야한다.
- sequenceName : DB에 실제 등록된 시퀀스 이름. 기본적으로 `hibernate_sequence`
- initialValue : DDL 생성시에만 사용, 시퀀스 DDL 생성할 때 처음 시작하는 수 지정, 기본적으로 1이다.
- allocationSize : 시퀀스 한 번 호출에 증가하는 수로 기본값은 50이다. 이는 성능 최적화를 위함이다. `SEQUENCE` 전략에서는 기본적으로 두번의 DB 조회가 필요하다. 때문에 DB 조회 횟수를 줄이기 위해 50이 기본적으로 설정되어있다. 단, 이경우 한번에 시퀀스 값이 너무 많이 증가할 수 있다는 문제가 있으므로 `INSERT`가 중요하지 않다면 1로 설정해야한다.
- catalog, schema

3. GenerationType.TABLE

`TABLE` 전략은 키 생성 전략용 테이블을 만들어 DB의 시퀀스를 흉내내는 전략이다. 테이블을 사용하는 전략이기 때문에 모든 DB에서 사용할 수 있다.

이 전략을 사용하기 위해서 먼저 시퀀스 대용 테이블을 만들어 줘야한다.

```
CREATE TABLE MY_SEQUENCES (
	sequence_name VARCHAR(255) not null,
	next_val bigint,
	primary key (sequence_name)
);
```

위와 같은 테이블로 시퀀스를 대신한다. `sequence_name`은 시퀀스 이름으로 사용할 컬럼, `next_val`은 시퀀스 값으로 사용할 컬럼이다.

```
@Entity
@TableGenerator(
	name = "BOARD_SEQ_GENERATOR",
	table = "MY_SEQUENCES",
	pkColumnValue = "BOARD_SEQ", allocationSize = 1
)
public class Member {
	@Id
	@GerneratedValue(strategy = GenerationType.TABLE, generator = "BOARD_SEQ_GENERATOR")
	private long id;
	// ...
}
```

위와 같이 설정하면 `TABLE`방식으로 기본키를 생성할 수 있다.

### @TableGenerator

- name : 생성기의 이름
- table : 시퀀스 생성기용 테이블 이름
- pkColumnName : table에 저장되는 시퀀스 컬럼명. 기본적으로 `hibernate_sequences`이다.
- valueColumnValue : 시퀀스의 값으로 사용되는 컬럼명. 기본적으로 `next_val`이다.
- pkColumnValue : 키로 사용할 값 이름. 기본적으로 엔티티이름을 사용하며 `pkColumnName`으로 설정한 컬럼에 `pkColumnValue`로 저장된다.
- initialValue : 시퀀스의 초기 값, 기본적으로 1이다.
- allocationSize : 시퀀스 한번에 증가하는 수, 기본적으로 50
- catalog, schema
- uniqueConstraints

4. GenerationType.AUTO

식별자 자동생성의 기본값이다. 선택한 DB에 따라 `IDENTITY`, `SEQUENCE`, `TABLE` 전략 중 하나를 선택한다.

이때, `SEQUENCE`나 `TABLE`전략이 선택되는 경우 미리 DB 시퀀스나 시퀀스용 테이블을 만들어놔야한다.