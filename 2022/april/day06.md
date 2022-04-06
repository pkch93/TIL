# 2022.04.06 TIL - MultipleBagFetchException

- [2022.04.06 TIL - MultipleBagFetchException](#20220406-til---multiplebagfetchexception)
  - [MultipleBagFetchException 발생 원인](#multiplebagfetchexception-발생-원인)
  - [참고](#참고)

JPA 사용하면서 패치 조인을 사용할 때 OneToMany인 관계의 엔티티를 여러개 패치 조인을 할 경우가 있다.

```java
@Entity
@Table
@Getter
@NoArgConstructor(access = AccessLevel.PROTECTED)
public class Post {
    // ...

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY)
    private List<HashTag> hashTags;

    // ...
}
```

위와 같이 Post 하나에 여러 댓글과 여러 해시태그를 가질 수 있다. 이때 Post를 조회하면서 가지고 있는 댓글들 `comments`과 해시태그 `hashTags` 를 함께 가져와서 사용하고자 한다.

이를 위해 post 조회시 comments와 hashTags를 대상으로 패치조인을 한다.

```java
from(post)
    .join(post.comments, comment).fetchJoin()
    .join(post.hashTags, hashTag).fetchJoin()
    .fetch()
```

> Querydsl로 ToMany 관계의 엔티티 패치조인을 표현함
> 

위 상황에서 Hibernate는 MultipleBagFetchException를 던진다.

```
org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
```

## MultipleBagFetchException 발생 원인

Hibernate는 왜 MultipleBagFetchException를 던질까? 그 원인은 카테지안 곱 `Cartesian Product`을 방지하기 위함이다.

> 카테지안 곱이란 조인하는 두 테이블 사이에 존재하는 테이블의 조건에 맞는 모든 데이터를 전부 결합하여 행을 곱한 만큼의 결과가 반환되는 것을 의미한다. 

따라서 MultipleBagFetchException이 발생한다는 의미는 해당 쿼리가 카테지안 곱이 발생할 수 있다는 의미이다. 카테지안 곱을 엄청 많은 수의 데이터를 조회하는 쿼리가 만들어져 DB 성능에 영향을 미치는 쿼리를 방지하라는 Hibernate의 경고이다.

## 참고

**MultipleBagFetchException 발생시 해결 방법:** [https://jojoldu.tistory.com/457?category=637935](https://jojoldu.tistory.com/457?category=637935)

**The best way to fix the Hibernate MultipleBagFetchException**: [https://vladmihalcea.com/hibernate-multiplebagfetchexception/](https://vladmihalcea.com/hibernate-multiplebagfetchexception/)
