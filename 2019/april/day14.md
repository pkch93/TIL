# 2019.04.14 Sunday

1. Spring MVC

## - DispatcherServlet

Spring MVC는 여타 웹 프레임워크와 같이 `the front controller pattern`으로 설계되었다. 이때 `front controller`로 사용되는 Servlet이 바로 `DispatcherServlet`이다.

`DispatcherServlet`은 요청`request`에 대해 실제 처리하는 `component`로 위임하는 역할을 한다.

`DispatcherServlet`은 Java Configration이나 web.xml에서 명시해주어야한다.

```java
// java configration
public class MyWebApplicationInitializer implements WebApplicationInitializer {

@Override
public void onStartup(ServletContext servletCxt) {

    // Load Spring web application configuration
    AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
    ac.register(AppConfig.class);
    ac.refresh();

    // Create and register the DispatcherServlet
    DispatcherServlet servlet = new DispatcherServlet(ac);
    ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
    registration.setLoadOnStartup(1);
    registration.addMapping("/app/*");
    }
}
```

> Spring에서 Java Configuration은 3.0 이후 버전에서 사용가능

아래는 xml로 `DispatcherServlet`을 설정하는 방법이다.

```markup
<web-app>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/app-context.xml</param-value>
    </context-param>

    <servlet>
        <servlet-name>app</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value></param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>app</servlet-name>
        <url-pattern>/app/*</url-pattern>
    </servlet-mapping>
</web-app>
```

1. Context Hierarchy

   `DispatcherServlet`은 `WebApplicationContext`마다 각자의 설정을 가진다고 기대한다. `WebApplicationContext`는 연관된 ServletContext와 Servlet에 대한 `link`를 가진다.

   > ServletContext?
   >
   > 하나의 서블릿이 서블릿 컨테이너와 통신하기 위해 사용하는 메서드들을 가진 클래스를 ServletContext라고 일컫는다.
   >
   > 즉, 하나의 애플리케이션에서 다른 모든 서블릿을 관리하며 서로 정보공유 할 수 있도록 도와주는 역할을 ServletContext가 담당한다.

   한편, 다양한 Application들이 하나의 `root WebApplicationContext`를 가지고 자원을 공유하도록 만들 수 있다. 일반적으로 `data repository`와 같은 `bean`을 `root WebApplicationContext`에서 가지고 있으며 연관된 여러 `WebApplicationContext`에서 참조하여 사용할 수 있다.

   따라서, 만약 여러 `Servlet WebApplicationContext`에서 참조하는 `bean`이라면 `Root WebApplicationContext`에 `bean`을 등록하여 모든 ApplicationContext에 일일이 빈을 등록하지 않아도 사용할 수 있다.

   ```java
    public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

        @Override
        protected Class<?>[] getRootConfigClasses() {
            return new Class<?>[] { RootConfig.class };
        }

        @Override
        protected Class<?>[] getServletConfigClasses() {
            return new Class<?>[] { App1Config.class };
        }

        @Override
        protected String[] getServletMappings() {
            return new String[] { "/app1/*" };
        }
    }
   ```

   아래는 계층구조를 만들기 위한 xml 설정이다.

   ```markup
    <web-app>

        <listener>
            <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>

        <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>/WEB-INF/root-context.xml</param-value>
        </context-param>

        <servlet>
            <servlet-name>app1</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <init-param>
                <param-name>contextConfigLocation</param-name>
                <param-value>/WEB-INF/app1-context.xml</param-value>
            </init-param>
            <load-on-startup>1</load-on-startup>
        </servlet>

        <servlet-mapping>
            <servlet-name>app1</servlet-name>
            <url-pattern>/app1/*</url-pattern>
        </servlet-mapping>

    </web-app>
   ```

2. Special Bean Types

   `DispatcherServlet`은 요청을 처리하기 위해서 특정 빈에게 요청 처리를 위임한다. 위의 특정 빈은 Spring에서 관리하는 빈들이다.

   DispatcherServlet이 찾는 특수 빈`special bean`들

   * HandlerMapping

     request를 handler 메서드에 매핑해주는 빈

     `RequestMappingHandlerMapping`과 `SimpleUrlHandlerMapping` 두 가지의 주요 handlermapping이 있다.

   * HandlerAdapter

     request에 mapping된 handler 메서드 호출하는 역할의 빈. HandlerAdapter는 DispatcherServlet이 front controller로써 요청을 분기하는 역할을 수행하도록 만들어 준다.

   * HandlerExceptionResolver

     `Exception`을 해석하기 위한 빈.

     매핑된 Exception을 handler 또는 HTML error view 아니면 다른 방법으로 Exception을 처리한다.

   * ViewResolver

     String 기반의 view 이름을 해석하여 응답으로 처리하는 빈

   * LocaleResolver

     클라이언트의 Locale을 해석하여 클라이언트의 time zone을 사용하도록 만들어주는 Resolver

   * ThemeResolver

     > Theme?
     >
     > 정적 리소스들의 집합으로 application의 화면에 영향을 미치는 stylesheet와 image로 구성된 요소들

     Application이 사용하는 theme를 해석하는 resolver

   * MultipartResolver

     multi-part request를 처리하기 위한 resolver \(file upload 등을 처리\)

   * FlashMapManager

     Flash로 사용하는 input, output FlashMap을 관리하는 빈

3. Web MVC config

Application은 상기 `Special Bean Types`에 해당하는 빈들을 선언한다. `DispatcherServlet`이 위 빈들을 체크하고 만약에 없다면 `DispatcherServlet.properties`에 명시된 내용에 따라 적용한다.

1. Servlet Config

Servlet 3.0 환경에서는 xml base에서 web.xml의 내용을 가져와 프로그래밍적으로 설정할 수 있는 옵션을 제공해준다.

* `WebApplicationInitializer`에서 `XmlApplicationContext`를 생성하여 사용하는 방법.

```java
import org.springframework.web.WebApplicationInitializer;

public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml");

        ServletRegistration.Dynamic registration = container.addServlet("dispatcher", new DispatcherServlet(appContext));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
```

* xml 설정내용 없이 Java-Base로 설정하는 방법

> AbstractAnnotationConfigDispatcherServletInitializer를 상속받아 설정

```java
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { MyWebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
```

* AbstractDispatcherServletInitializer로 xml-based 설정 사용하는 법

```java
public class MyWebAppInitializer extends AbstractDispatcherServletInitializer {

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        XmlWebApplicationContext cxt = new XmlWebApplicationContext();
        cxt.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml");
        return cxt;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
```

1. Processing

DispatcherServlet은 다음과 같은 로직으로 요청을 처리한다.

* `WebApplicationContext`를 찾는다. 그리고 해당 request에 attribute로 요청 처리과정에서 사용할 수 있는 controller나 그 외 요소들을 넣는다. 이때, `DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE` key 값에 따라 바인딩된다.
* LocaleResolver를 request에 바인딩한다. 요청과정을 처리할 때 클라이언트의 Locale에 따라 element를 처리하기 위해 사용된다.
* ThemeResolver가 동작한다.
* 만약 file upload를 위해 MultipartResolver를 이용한다면 request가 MultipartResolver에 의해 MultipartHttpServletRequest로 씌워진다`Wrapped`.
* 요청을 처리할 적절한 Handler를 찾는다. Handler를 찾았다면 관련 실행 로직이 실행된다. \(`preprocessor`, `postprocessor`, `controller`\)
* Model이 return되면 view가 렌더링된다. Model이 return되지 않으면 view도 렌더링되지 않는데 이는 request가 이미 충족될 수 있기 때문이다. \(??\)

> 마지막 request가 이미 충족될 수 있기 때문이 잘 이해되지 않는 부분

DispatcherServlet은 `last-modification-date`를 return 하도록 지원해준다.

요청 처리시 Handler가 `LastModified` 인터페이스를 구현하고 있는지 확인한다. 만약 구현하고 있다면 `LastModified` 인터페이스의 메서드인 `long getLastModified(request)` 메서드를 호출하여 client에게 `last-modification-date`를 return한다.

1. Interception

모든 HandlerMapping은 특정 요청에 대해 추가적인 기능 적용하고자 할 때 Interceptor를 사용하여 구현할 수 있다.

Interceptor는 반드시 `org.springframework.web.servlet`의 `HandlerInterceptor`를 구현하여 정의할 수 있다.

Interceptor에는 3가지 메서드를 구현해야한다.

* preHandle : 실제 Handler가 실행되기 전에 실행

  preHandle은 boolean 값을 return한다. 이때 true를 return하게되면 Handler에 정의된 로직을 수행하게되고 false를 return하면 로직이 중단된다.

* postHandle : 실제 Handler의 로직이 수행된 후 실행

  참고로 postHandle은 `@RequestBody`와 `@ResponseEntity` handler method와는 큰 효율을 발휘하지 못한다. 이는 postHandle 메서드가 실행되기 전에 이미 response가 쓰여지기 때문이다. 즉, postHandle에서 response 정보를 바꾸는 작업을 하기에는 너무 늦기 때문에 적절하지 못하다는 것을 의미한다.

* afterCompletion : 모든 request가 수행된 후 실행
* Exceptions

request mapping 도중에 exception이 발생한다면 `DispatcherServlet`은 `HandlerExceptionResolver` 빈에게 exception을 해석하여 error response를 전달하는 등의 handing 권한을 위임한다.

1. Locale

`DispatcherServlet`은 자동적으로 클라이언트의 locale에 따라 메세지를 해석한다. 이는 `LocaleResolver`가 있기 때문에 가능하다.

요청이 왔을때 `DispatcherServlet`은 Locale Resolver를 찾는다. 만약 존재한다면 locale을 세팅한다.

`RequestContext`의 `getLocale()` 메서드를 사용하여 locale 정보를 가져올 수 있다.

여기에 Interceptor를 활용하여 특정 환경에 있는 Locale을 변경할수도 있다.

> LocaleResolver와 Interceptor는 org.springframework.web.servlet.i18n 패키지에 정의되어있다.

* Time Zone

Locale Resolver는 클라이언트의 Locale을 얻는 것 이외에도 Time Zone을 알아내는 데도 유용하게 사용할 수 있다. `LocaleContextResolver` 인터페이스는 LocaleResovler를 확장하여 time zone 정보를 포함하도록 만들어준다.

위 `LocaleContextResolver`를 사용하게 되면 `RequestContext.getTimeZone()`메서드를 사용하여 Time Zone 정보를 자동으로 얻을 수 있다. \(Time Zone은 Spring의 ConversionService에 등록된 Date/Time Converter와 Formatter에 의해 자동으로 사용된다.\)

* Header Resolver

`LocaleResolver`의 기능 중 하나로 `accept-language` header를 검사하는 기능을 가진다. 보통은 `accept-language` header는 클라이언트의 운영체제에 등록된 locale을 포함한다.

* Cookie Resolver

클라이언트가 가지고 있는 `Cookie`를 검사하여 `Locale`이나 `TimeZone` 정보가 존재하는지 검사한다. `CookieLocaleResolver`를 통해 cookie의 이름, 최대 수명, path를 지정할 수 있다.

* Session Resolver

유저의 요청과 관련된 Session에서 Locale과 TimeZone을 가져오는 Resolver. `CookieLocaleResolver`아는 달리 Servlet container의 HttpSession에 locale 설정을 가져와 저장한다. 때문에 Session이 종료되면 locale 정보도 사라진다.

* Locale Interceptor

`LocaleChangeResolver`로 현재 locale을 다르게 변경할 수 있다.

[Spring MVC 공식문서](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)

