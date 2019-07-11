# 2019.07.11 Thursday

# 1. DispatcherServlet

DispatcherServlet은 Spring에서 Controller에 요청을 전달하기 전 모든 요청을 받는 Front Controller의 역할을 한다. 먼저 Spring 서버로 들어오는 요청을 DispatcherServlet이 가로챈 후 각 컨트롤러로 해당 요청을 매핑해준다.

front-controller-pattern 참고 : [https://nesoy.github.io/articles/2017-02/Front-Controller](https://nesoy.github.io/articles/2017-02/Front-Controller)

## strategies

DispatcherServlet에서 어떤 HandlerMapping 전략을 어떻게 줄 것인지, HandlerAdapter는 어떤걸 사용할 것인지, view resolver는 어떤 것을 사용할 것인지 등의 전략...

기본적으로 DispatcherServlet의 getDefaultStrategies 메서드를 통해 각각의 전략을 설정한다. 이 전략들은 처음에 WebApplicationContext가 초기화 될 때 onRefresh() 메서드를 통해 초기 설정된다.

참고 : org.springframework.web.servlet.FrameworkServlet

![](img/strategies-cda46b47-83b3-409a-a02d-7018e9dde22a.png)

DispatcherServlet은 FrameworkServlet 추상 메서드를 상속받는다. 이때 FrameworkServlet에서 

initWebApplicationContext()를 통해 onRefresh를 호출하여 각종 전략을 설정한다. 위 initStrategies에서 호출하는 initXX 메서드들이 각 전략을 설정하는 요소들이다. 참고로  `DispatchServlet.properties` 파일에서 설정한 내용으로 strategy를 설정한다.

![](img/dispatcherservletproperties-be25057c-386e-47d9-a2d6-65f980864f51.png)

위와 같이 설정된 내용을 바탕으로 strategy를 설정한다.

## Dispatch

DispatcherServlet답게 Dispatch, 즉, 요청을 컨트롤러에 넘겨주는 작업을 한다. DispatcherServlet의 `doDispatch()` 메서드가 이를 담당한다. 

이때 doDispatch를 호출하기 전, 전처리 작업을 위해 doService()를 호출하여 실행한다.

![](img/doservie-94393eaa-1256-4a04-82a4-f275e86236b1.png)

doService는 FrameworkServlet 추상클래스의 추상메서드이다. doService를 통해 DispatcherServlet에서의 요청처리를 구현한 것이다.

### - HttpServlet의 요청처리

기본적으로 요청 경로로 클라이언트에서 요청이 들어오면 해당 HttpMethod에 따라 doXXX() 메서드가 호출된다. 이는 javax.servlet.http.HttpServlet의 service 메서드를 참고

### - DispatcherServlet의 dispatch 전처리 작업

![](img/doservice_preprocess-8c7a18a8-3afe-4918-b7f0-12144df31d2c.png)

request에 대한 log출력 → request attribute 백업 → framework object를 request에서 사용할 수 있도록 attribute 설정 → flashMap 사용한다면 FlashMap 설정

위 작업 이후 컨트롤러로 요청을 dispatch한다.

참고로 DispatcherServlet은 `/`와 .jsp로 끝나지 않는 파일 경로에 대해서 처리를 할 수 있다. 즉, `/css/index.css` 와 같은 요청을 DispatcherServlet이 처리한다는 의미이다. 이는 `@Controller`로 매핑된 경로를 찾아 연결하는 HandlerMapping과 다른 차이점이다.

### - doDispatch

DispatcherServlet의 doDispatch() 메서드에서 dispatch 관련 작업을 진행한다.  

이때 핸들러 설정, MultipartResolver 사용에 따른 설정을 한다.

handler 설정의 경우 해당 요청에 대한 주요 handler와 이전 처리 (preHandle), 이후 처리 (postHandle)를 HandlerExecutionChain으로 가져온다.

이렇게 가져온 HandlerExecutionChain을 HandlerAdapter를 이용하여 실행한다. 먼저, preHandle이 존재한다면 먼저 preHandle을 실행, 그리고 해당 요청의 로직을 수행한 후 postHandle을 수행한다.

HandlerMapping 우선순위 및 MVC 동작 참고 :  [https://tinkerbellbass.tistory.com/40](https://tinkerbellbass.tistory.com/40)

## HandlerMapping VS HandlerAdapter

DispatcherServlet이 클라이언트에게서 요청받은 경로를 파악하고 handler를 찾을때는 `HandlerMapping`을 사용한다. 그리고 `HandlerMapping`으로 찾은 handler (정확히는 HandlerExcecutionChain)를 실행하기 위해서 `HandlerAdaapter`를 사용한다.

### HandlerMapping이 @Controller와 @RequestMapping을 파악하는 원리

위 `@Controller`와 `@RequestMapping`은 RequestMappingHandlerMapping을 통해 Handler로써 작용할 수 있는지 판단한다.

# 2. JPA

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