# 2021.06.03 TIL - AutoParams Customization

## Customization

앞선 기능들은 대부분 자동으로 데이터를 만들어준다. 다만, 자동으로 만들어 주는 데이터들이 테스트하려는 비즈니스에 맞지 않는 경우도 있을 것이다. 따라서 비즈니스 요구사항에 맞는 제약사항을 반영한 테스트 데이터가 필요할 수 있다.

이를 위해서 AutoParams에서는  `Customizer` 인터페이스와 `@Customization` 어노테이션을 통해서 테스트 데이터에 대한 자동 생성을 지원한다.

```java
public static class PostCustomization implements Customizer {
  @Override
  public ObjectGenerator customize(ObjectGenerator generator) {
    return (query, context) -> query.getType().equals(Post.class)
            ? new ObjectContainer(factory(context))
            : generator.generate(query, context);
  }

  private Post factory(ObjectGenerationContext context) {
    UUID id = (UUID) context.generate(() -> UUID.class);
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime modified = LocalDateTime.now();
    boolean deleted = false;

    String title = generateRandomString(ThreadLocalRandom.current().nextInt(100));
    String content = generateRandomString(ThreadLocalRandom.current().nextInt(1000));

    return new Post(id, title, content, createdAt, modified, deleted);
  }

  private String generateRandomString(int length) {
    byte[] bytes = new byte[length];
    new Random().nextBytes(bytes);

    return new String(bytes, StandardCharsets.UTF_8);
  }
}

@Getter
public static class Post {
  private UUID id;
  private String title;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  private boolean deleted;

  public Post(UUID id, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, boolean deleted) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
    this.deleted = deleted;
  }

  @Override
  public String toString() {
    return "Post{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", createdAt=" + createdAt +
            ", modifiedAt=" + modifiedAt +
            ", deleted=" + deleted +
            '}';
  }
}
```

Post가 기본적으로 `deleted`가 `false`이고 타이틀이 100자 이내, 글 내용이 1000자 이내라고 가정할 때 `org.javaunit.autoparams.customization.Customizer`를 구현한 PostCustomization를 통해 처리할 수 있다.

파라미터가 `Post` 타입이라면 직접 커스텀한 Post 인스턴스 객체를 생성하여 전달하도록 만든다. 이렇게 PostCustomization를 구현한 뒤 다음과 같이 사용하고자 하는 테스트 메서드에 `@Customization`으로 등록하여 적용할 수 있다.

```java
@ParameterizedTest
@AutoSource
@Customization(PostCustomization.class)
void postCustomization(Post post) {
  System.out.println(post);
}
```

위와 같이 `@Customization(PostCustomization.class)`를 달아서 PostCustomization으로 설정한 사항이 적용된다.

### 참고

AutoParams github: [https://github.com/JavaUnit/AutoParams](https://github.com/JavaUnit/AutoParams)