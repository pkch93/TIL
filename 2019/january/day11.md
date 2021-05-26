# 2019 01.11 Saturday

> 과거에서 교훈을 얻을 수는 있어도, 과거 속에 살 수는 없다.
>
> by 린든 B.존슨 \(미국 제 36대 대통령\)

그동안 `SSAFY` 교육과 `Node.js` 공부에 열중하느라 Spring에 소흘히 한 것 같다.. 그동안 공부한 걸 정리하면서 복습할 필요가 있다는 것을 절실히 느꼈다.

1. Spring Restful API
   * Entity 정의

     Entity 클래스 정의 후 DB 연결이 제대로 이뤄지는지에 대한 테스트를 먼저 진행하였다.

     ```java
       @RunWith(SpringRunner.class)
       @DataJpaTest
       public class DataTodoTest {

           @Autowired
           private TodoRepository todoRepository;

           @Test
           public void createTodo(){

           }
       }
     ```

     다음과 같이 JPA 슬라이싱 테스트를 통해 DB가 제대로 연결되는 지 테스트하였다. 하지만 다음과 같은 오류메시지를 도출하였다.

     ```text
       Illegal attempt to map a non collection as a @OneToMany, @ManyToMany or @CollectionOfElement
     ```

     원인을 분석한 결과 다음의 코드가 문제였다.

     ```java
       @OneToMany(mappedBy = "account")
       private ArrayList<Todo> todoList;
     ```

     위와 같이 인터페이스 타입이 아닌 List 인터페이스의 구현체 중 하나인 ArrayList 변수에 `@OneToMany`어노테이션을 붙여서 생긴 문제였다. 때문에 ArrayList를 List 인터페이스로 바꿔 오류를 해결하였다.

     [참고](https://stackoverflow.com/questions/44258541/illegal-attempt-to-map-a-non-collection-as-a-onetomany-manytomany-or-collec)

   * Spring HATEOAS

     > REST API로써 기능을 하기 위해 LINK 정보를 추가해주는 Spring 프로젝트

     `HATEOAS(Hypermedia As The Engine Of Application State)`란 Rest application 설계의 기본 요소로 Rest api가 다른 network application 설계와 차별성을 부여해준다.

     `hypermedia`를 통해 서버가 클라이언트와 정보를 주고받을때 동적으로 정보를 제공할 수 있도록 만들어준다. 이는 Rest API의 기본 조건인 `hypermedia를 통해 어플리케이션의 상태가 변화 할 수 있다`를 충족시켜준다.

     [HATEOAS 위키백과](https://en.wikipedia.org/wiki/HATEOAS)

     * HATEOAS 구성 요소
       1. Links

          Spring HATEOAS의 Link 정보는 `Atom link definition`을 따르며 `rel`과 `href` 속성으로 구성되어있다. 이 때 `self`와 `next`와 같은 `rel` 속성은 불변성을 지닌다.

          [Atom link definition](https://www.xml.com/pub/a/2004/06/16/dive.html)

       2. Resources

          `Links`를 포함한 실제로 전달하고자 하는 정보를 가진 객체
     * Link 생성하기

       1. ControllerLinkBuilder 사용하기

          > `Controller class`를 기점으로 link를 생성하도록 도와주는 HATEOAS의 API

          ControllerLinkBuilder는 Spring의 ServletUriComponentsBuilder를 사용하여 현재 요청의 기본 uri 정보를 가져온다.

          ```java
           Person person = new Person(1L, "Dave", "Matthews");
           //                 /people                 /     1
           Link link = linkTo(PersonController.class).slash(person.getId()).withSelfRel();
           assertThat(link.getRel(), is(Link.REL_SELF));
           assertThat(link.getHref(), endsWith("/people/1"));
          ```

          위는 [공식문서](https://docs.spring.io/spring-hateoas/docs/1.0.0.BUILD-SNAPSHOT/reference/html/#fundamentals.links)에서 가져온 예시이다.

          `PersonController`가 `/people`으로 매핑되어 있으므로 `linkTo`로 `PersonController`를 주면 `/people`이 주어진다. 이 때 `slash` 메서드로 그 다음 올 uri 정보를 추가로 줄 수 있다. 위 예시에서는 `slash`에 person.getId\(\)`여기서는 1`을 넣어 `/1`을 뒤에 만들어 주었다.

          그 결과로 `{baseuri}/people/1`의 링크가 만들어진다.

       2. method를 기점으로 link 만들기

          `HATEOAS 0.4` 버전 이후로는 method를 기점으로 link를 만들 수 있다.

          * `ControllerLinkBuilder`의 getMethod

            ```java
              Method method = PersonController.class.getMethod("show", Long.class);
              Link link = linkTo(method, 2L).withSelfRel();

              assertThat(link.getHref(), endsWith("/people/2")));
            ```

            위와 같이 `Controller class`의 getMethod를 사용하여 Method 객체를 만들 수 있다. \(getMethod\(method name: string, parameter type\)\)

            이 method 객체와 함께 `linkTo`를 활용하여 link 정보를 만들어 낼 수 있다. 위 예시에서는 `/people/2`라는 uri가 만들어진다.

            위 Method 객체를 만드는 방법 말고도 methodOn을 통해 method를 기점으로 link를 만들 수 있다.

            ```java
              Link link = linkTo(methodOn(PersonController.class).show(2L)).withSelfRel();
              assertThat(link.getHref(), endsWith("/people/2")));
            ```

       [참고](https://docs.spring.io/spring-hateoas/docs/1.0.0.BUILD-SNAPSHOT/reference/html/)

* `@RequestBody`은 POST 메서드일 경우 request 정보를 해당 entity 객체로 반환해준다.

