# 2019.04.15 Monday

## 1. Spring MVC

오늘은 Spring Web MVC의 기능중 URI와 CORS 처리에 대해 알아보는 것이 목표,,

### - URI Links

Spring MVC에서는 URI 객체를 만드는 여러 방법을 지원해준다.

1. UriComponents

`UriComponentsBuilder`는 변수`variable`를 가진 URI templates에서 URI를 만들어내도록 도와준다.

```java
UriComponents uriComponents = UriComponentsBuilder
        .fromUriString("https://example.com/hotels/{hotel}")
        .queryParam("q", "{q}")
        .encode()
        .build();

URI uri = uriComponents.expand("Westin", "123").toUri();
``` 

`UriComponents`를 사용하면 URI의 틀을 만들어 낼 수 있다. 따라서 만든 uriComponents를 이용하여 변수를 `extend`하여 다양한 URI를 만들어 낼 수 있다.

물론 UriComponents 없이도 URI를 만들어 낼 수 있다. 아래는 위 예시와 같은 URI를 만드는 코드이다.

```java
URI uri = UriComponentsBuilder
        .fromUriString("https://example.com/hotels/{hotel}")
        .queryParam("q", "{q}")
        .encode()
        .buildAndExpand("Westin", "123")
        .toUri();
```

`buildAndExpand`메서드를 통해 Component 객체를 만드는 과정 없이 한번에 URI를 만들 수 있다.

```java
URI uri = UriComponentsBuilder
        .fromUriString("https://example.com/hotels/{hotel}")
        .queryParam("q", "{q}")
        .build("Westin", "123");
```
위 코드는 앞선 `buildAndExpand`메서드를 사용한 코드와 동일한 효과를 가진다. `build` 메서드에 expand할 인자를 넣어 encoding 과정을 함축할 수 있다.

### - UriBuilder

`UriComponentsBuilder`는 `UriBuilder`의 구현체이다. 결과적으로 `UriBuilderFactory`를 가지고 `UriBuilder`를 만들어낼 수 있다.

`UriBuilderFactory`와 `UriBuilder`는 base URL이나 encoding 설정 등의 공유하는 설정을 기반을 URI templates에서 URI를 만들어내도록 도와준다.

```java
String baseUrl = "https://example.com";
DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(baseUrl);

URI uri = uriBuilderFactory.uriString("/hotels/{hotel}")
        .queryParam("q", "{q}")
        .build("Westin", "123");
```
`DefaultUriBuilderFactory`는 `UriBuilderFactory`의 기본 구현체로 내부적으로 UriComponentsBuilder를 사용한다. 위의 예시와 같이 `DefaultUriBuilderFactory`를 통해 URI를 만들어낼 수 있다.

그밖에도 `RestTemplate`나 `WebClient`와 함께 URI를 만들어낼 수도 있다.

> RestTemplate와 WebClient를 잘 몰라서 사용하는 법을 모름
>
> 추후 위 두 개념을 학습 후 실습해볼것

### - URI Encoding

위 URI를 만들때 사용했던 encoding 개념이다.

endode에는 두 가지 옵션이 있다.

1. UriComponentsBuilder의 encode

URI template에 대해 먼저 encode를 해주고 URI variable에 대한 expand 시에 `strictly` encode를 하는 옵션

2. UriComponents의 encode

URI variable이 expand 된 후에 URI Components를 encode 하는 옵션

위 두 옵션은 `non-ASCII`와 부적절한 문자에 대해 `escaped octet`으로 대체한다. 하지만 첫번째 옵션의 경우는 URI variables로 나타나는 예약 문자`reversed meaning`의 문자를 대체한다.

> 아래는 공식문서에 나온 예시
>
> Consider ";", which is legal in a path but has reserved meaning. The first option replaces ";" with "%3B" in URI variables but not in the URI template. By contrast, the second option never replaces ";", since it is a legal character in a path.

예약 문자`reserved meaning character`는 URI에서 중요한 의미를 가지고 있으므로 만약 그 의미로 쓰지 않는다면 반드시 퍼센트 인코딩이 필요하다.

> 예약문자는 RFC 3986에 정의되어있다.

[encoding 관련 위키 참고](https://en.wikipedia.org/wiki/Percent-encoding)

Spring에서는 encoding mode를 설정할 수 있다.

- TEMPLATE_AND_VALUES : 1번 옵션의 encoding 방법
- VALUES_ONLY : URI template에 대해서는 encode하지 않음, 대신 Template에 값을 바인딩하기 전에 UriUtils의 encodeUriUriVariables를 통해 URI variable을 encoding
- URI_COMPONENTS : 2번 옵션의 encoding 방법
- NONE : encoding 하지 않음

### - Relative Servlet Requests

`ServletUriComponentsBuilder`를 통해 현재 `request`가 가진 정보를 재활용하여 URI를 만들 수 있다.

```java
ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromRequest(request)
        .replaceQueryParam("accountId", "{id}").build()
        .expand("123")
        .encode();
```

위 예시 에서는 request가 가지는 `host`, `scheme`, `port`, `path`, `query string`을 재활용하여 URI를 만든다.

```java
ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromContextPath(request)
        .path("/accounts").build()
```

`fromContextPath`메서드를 사용하면 `host`, `port`, `context path`를 재활용하여 URI를 만든다.

```java
ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromServletMapping(request)
        .path("/accounts").build()
```

`fromServletMapping`메서드를 사용하면 `fromContextPath`에서 재활용한 정보에 `Servlet prefix` 정보까지 재활용하여 URI를 만든다.

### - Links to Controllers

`MvcUriComponentsBuilder`를 사용하여 해당 controller의 handler 메서드에 해당하는 URI를 만들어 낼 수 있다. 

```java
@Controller
@RequestMapping("/hotels/{hotel}")
public class BookingController {

    @GetMapping("/bookings/{booking}")
    public ModelAndView getBooking(@PathVariable Long booking) {
        // ...
    }
}

UriComponents uriComponents = MvcUriComponentsBuilder
    .fromMethodName(BookingController.class, "getBooking", 21).buildAndExpand(42);

URI uri = uriComponents.encode().toUri();
```

## 2. CORS

보안상의 이유로 브라우저에서는 현재 origin 밖의 자원에 AJAX로 접근하는 것을 금지한다. 단, CORS 표준에 의해서 해당 Origin을 허용한다면 해당 도메인에서는 현재 origin이 가지고 있는 자원에 접근할 수 있게 된다.

[CORS 더 알아보기](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)

Spring MVC에서는 Cors 처리를 위한 다양한 기능을 제공해 준다.

- @CrossOrigin

controller의 handler 메서드에 붙이는 어노테이션으로 해당 메서드의 path로 접근하는 것을 허용한다.

아무 설정도 하지않으면 기본적으로 모든 `Origin`, 모든 `header`, 해당 메서드에 매핑된 HTTP Method를 모두 허용한다.

`@CrossOrigin`은 class level에서도 붙일 수 있다. class level에서는 controller가 가지는 모든 메서드에 대해서 적용한다.

- Global Configuration

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")
            .allowedOrigins("https://domain2.com")
            .allowedMethods("PUT", "DELETE")
            .allowedHeaders("header1", "header2", "header3")
            .exposedHeaders("header1", "header2")
            .allowCredentials(true).maxAge(3600);

        // Add more mappings...
    }
}
```

- CORS Filter

```java
CorsConfiguration config = new CorsConfiguration();

// Possibly...
// config.applyPermitDefaultValues()

config.setAllowCredentials(true);
config.addAllowedOrigin("https://domain1.com");
config.addAllowedHeader("*");
config.addAllowedMethod("*");

UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
source.registerCorsConfiguration("/**", config);

CorsFilter filter = new CorsFilter(source);
```

[참고](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
