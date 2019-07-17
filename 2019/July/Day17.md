# 2019.07.17 Wendesday

# Entity mapping

JPA에서 가장 중요한 작업 중 하나는 엔티티와 테이블을 정확히 매핑하는 것이다.

때문에 JPA가 지원하는 다양한 매핑 어노테이션을 이해할 필요가 있다. 매핑 어노테이션은 크게 4가지로 분류할 수 있다.

1. 객체와 테이블 매핑 : `@Entity` / `@Table`
2. 기본 키 매핑 : `@Id`
3. 필드와 컬럼 매핑 : `@Column`
4. 연관관계 매핑 : `@ManyToOne` / `@JoinColumn`

# @Entity

JPA를 사용하여 테이블과 매핑할 클래스는 `@Entity`를 필수로 붙여야한다. `@Entity`가 붙은 클래스는 JPA가 관리하는 것으로 Entity라고 부른다.

## Entity의 속성

- name : JPA에서 사용할 Entity의 이름을 지정. 기본값으로는 클래스의 이름을 따른다. 이때 Entity 이름은 프로젝트에서 단 하나만 존재해야한다.

## 주의점

1. 파라미터가 아무것도 없는 기본생성자는 필수다.
2. final 클래스, enum, interface, inner 클래스에는 사용할 수 없다.
3. DB에 저장할 필드에는 final을 사용하면 안된다.

# @Table

Entity와 매핑할 테이블을 지정하는 어노테이션이다. 만약 `@Table`이 생략하면 `@Entity`의 이름을 기본적으로 따른다.

## Table의 속성

- name : 매핑할 테이블의 이름. 이때 테이블은 DB에 생성되는 테이블 이름을 의미한다. 기본값으로는 Entity의 값을 따른다.
- catalog : catalog 기능이 있는 DB에서 catalog를 매핑한다.
- schema : schema 기능이 있는 DB에서 schema를 매핑한다.
- uniqueConstraints (DDL) : DDL 생성 시에 unique 제약조건을 만든다. 2개 이상의 복합 unique 제약조건도 만들 수 있다. 단, 이 기능은 스키마 자동 생성 기능으로 DDL을 사용할 떼만 적용된다.

# 다양한 매핑

## @Enumerated

자바의 enum을 타입으로 DB와 매핑하도록 도와주는 어노테이션이다. 이때, EnumType의 value를 줄 수 있는데 EnumType.STRING을 권장한다.

## @Temporal

자바의 날짜 타입을 매핑하기 위해 사용하는 어노테이션

## @Lob

기본적으로 DB의 `VARCHAR` 필드는 최대 255자 제한이 있다. `VARCHAR`이상으로 값을 저장해야한다면 `VARCHAR`가 아닌 `CLOB`이 되야한다. 이를 위해 JPA의 `@Lob`을 사용한다. String 필드에 붙이면 `CLOB`타입이 되고 byte[] 필드에 `@Lob`을 붙이면 `BLOB`형태가 된다.

# DB 스키마 자동 생성

JPA는 DB 스키마 자동 생성 기능을 제공한다. 클래스의 매핑 정보를 보면 어떤 테이블에 어떤 컬럼을 이용하는지 알 수 있다. 이 매핑 정보와 각 DB의 Dialect를 사용하여 스키마를 자동 생성한다.

```xml
<property name="hibernate.hbm2ddl.auto" value="create" />
```

위 속성은 `persistence.xml`에 `properties`태그 하위에 주면 스키마 자동 생성이 활성화된다.

단, 위와 같이 value를 create로 주는 것은 개발환경에서만 사용하거나 매핑이 어떻게되는지 참고하는 정도만 활용하고 운영 환경에서는 지양하는 것이 좋다.

## hibernate.hbm2ddl.auto의 속성

- create

  기존 테이블을 삭제하고 새로 생성하는 속성 (DROP + CREATE)

- create-drop

  create 속성에 추가로 애플리케이션을 종료할 때마다 생성한 DDL을 제거하는 속성 (DROP + CREATE + DROP)

- update

  DB 테이블과 Entity 매핑 정보를 비교하여 변경 사항만 수정하는 속성

- validate

  DB 테이블과 Entity 매핑 정보를 비교하여 차이가 있다면 경고를 남기고 애플리케이션을 실행하지 않는 속성. 이 설정은 DDL을 수정하지 않는다. 또한, 운영환경에서 사용을 권장하는 속성이다. 참고로 운영환경에서는 이미 축적된 DB가 있으므로 스키마를 변경한다면 `flyway`같은 마이그레이션 툴을 사용해야만 한다.

- none

  자동 생성 기능을 사용하지 않음. 참고로 none은 실제로 없는 속성이다. 즉, 위 4개의 속성이 미리 정의된 속성이고 그외 속성은 자동 생성 기능을 제공하지 않는다.
  