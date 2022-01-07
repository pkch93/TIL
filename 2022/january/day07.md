# 2022.01.07 TIL - Spring Validation 컨트롤러 이외의 빈에서 사용하기

- [2022.01.07 TIL - Spring Validation 컨트롤러 이외의 빈에서 사용하기](#20220107-til---spring-validation-컨트롤러-이외의-빈에서-사용하기)

컨트롤러에서는 검사하고자 하는 파라미터 객체에 `@Valid`나 `@Validated`를 붙이면 Spring Validation의 유효성 검증 기능을 지원받을 수 있었다.

컨트롤러 이외의 빈에서도 위와 같은 유효성 검사 기능을 지원한다. 단, 다음과 같이 클래스 레벨에 `@Validated` 어노테이션을 달아주어야한다.

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class PostRequest {
    @NotNull
    private Long memberNumber;

    @NotNull
    @Size(min = 5, max = 100)
    private String title;

    @NotNull
    private String contents;

    @Builder
    public PostRequest(Long memberNumber, String title, String contents) {
        this.memberNumber = memberNumber;
        this.title = title;
        this.contents = contents;
    }
}


@Slf4j
@Service
@Validated
public class PostService {

    public void createPost(@Valid PostRequest request) {
        log.info("request: {}", request);
    }
}
```

PostRequest와 빈으로 등록된 PostService가 위와 같이 정의되어있다고 가정한다.

위를 Spring 컨테이너를 띄워서 다음과 같이 테스트해본다.

```java
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void createPost() {
        // given
        PostRequest request = PostRequest.builder()
                .memberNumber(1L)
                .title("t") // PostRequest의 title은 5 ~ 100 사이의 길이가 필요하다.
                .contents("contents")
                .build();

        // when & then
        assertThatThrownBy(() -> postService.createPost(request))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
```
앞서 PostRequest의 title은 `@Size(min = 5, max = 100)`으로 정의되어 있으므로 title 문자열의 길이가 5 ~ 100 사이여야한다.

단, 위 테스트에서 title은 `"t"`로 전달하고 있기 때문에 `ConstraintViolationException`가 발생한다.

위와 같이 빈의 클래스 레벨에 `@Validated`를 붙여서 빈 유효성 검사 기능을 지원받을 수 있다.

> 참고로 메서드에 `@Validated`를 붙이면 빈 유효성 검사 기능을 지원받지 못한다.
