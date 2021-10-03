# 2021.09.22 TIL - Spring vs Spring Boot

- [2021.09.22 TIL - Spring vs Spring Boot](#20210922-til---spring-vs-spring-boot)
  - [Spring Boot](#spring-boot)
    - [build.gradle](#buildgradle)
    - [애플리케이션 설정](#애플리케이션-설정)
  - [Spring](#spring)
    - [build.gradle](#buildgradle-1)
    - [애플리케이션 설정](#애플리케이션-설정-1)
  - [참고](#참고)

<img width="574" alt="hello" src="https://user-images.githubusercontent.com/30178507/135743542-3bfe2408-75a4-46b8-a587-f55d52b2a766.png">

루트 `/`에 접근했을때 `hello world!`를 보여주는 html을 반환하게 만드려면 어떻게 해야할까?

> 개발환경인 IntelliJ에서 위 화면을 보기 위한 세팅

## Spring Boot

Spring Boot는 starter 의존성과 auto-configure 기능을 통해 애플리케이션을 빠르고 간단하게 세팅할 수 있도록 지원한다. 또한 전통적인 Spring에서 war를 통해 톰켓에 올려 배포하는 것과 달리 Embedded 서버와 Executable jar 기능으로 애플리케이션을 간단하게 동작할 수 있다.

### build.gradle

```groovy
plugins {
    id 'java'
    id 'org.springframework.boot' version '2.5.3'
    id 'io.spring.dependency-management' version '1.0.11.RELEASE'
}

group = 'edu.pkch'

sourceCompatibility = 11
targetCompatibility = 11

repositories {
    mavenCentral()
}

dependencies {
    implementation "org.springframework.boot:spring-boot-starter-web"
}
```

위와 같이 Spring Boot 플러그인과 `org.springframework.boot:spring-boot-starter-web` 의존성을 추가한다.

### 애플리케이션 설정

이제 설정이 거의 끝났다. 먼저 애플리케이션을 추가한다.

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

여기서 `@SpringBootApplication`을 반드시 추가한다. 이를통해 애플리케이션에 정의한 빈 스캔과 Auto Configure가 동작한다.

이제 `resources/static` 하위에 `index.html`을 추가한다.

> Spring Boot는 jar 패키징을 기본으로 하기 때문에 jsp가 아닌 thymeleaf를 기본 템플릿 엔진으로 활용한다. thymeleaf는 `.html` 확장자를 사용하므로 `index.html`을 추가한다.

또한 `static/index.html`은 웰컴 페이지로 `/` 경로에 접근했을때 기본적으로 서빙한다.
Spring Boot 2.2.x 미만에서는 `resources/index.html`을 기본 페이지로 제공한다.
> 

```html
<!doctype html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>첫 페이지</title>
</head>
<body>
    hello world!
</body>
</html>
```

위와 같이 추가하고 애플리케이션을 실행하여 `http://localhost:8080`에 접근하면 다음과 같은 화면이 나타난다.

<img width="574" alt="spring_boot_hello" src="https://user-images.githubusercontent.com/30178507/135743577-45c90039-c1e7-4432-b013-e261631cf0b2.png">

## Spring

Spring Boot에서는 Executable Jar 기능을 통해 jar로 애플리케이션을 실행할 수 있다. 다만, 기존 Spring에서는 war를 톰켓과 같은 WAS에 올려 실행을 하므로 먼저 톰켓 세팅이 필요하다.

톰켓 9.0.53 다운로드: [https://tomcat.apache.org/download-90.cgi](https://tomcat.apache.org/download-90.cgi)

톰켓을 다운로드한 후 IntelliJ에서 톰켓 설정이 필요하다.

<img width="1321" alt="spring1" src="https://user-images.githubusercontent.com/30178507/135743572-a0ea525e-9719-45e4-a899-73816f32da05.png">

Run/Debug Configuration의 왼쪽 상단에 `+` 버튼을 누르면 Tomcat Server라는게 있다. 여기서 개발환경에서 사용할 톰켓 설정을 하는 것이므로 Local을 선택한다. 그리고 Configure를 통해 다운받은 톰켓 서버의 경로를 설정한다.

그리고 Artifacts 설정이 필요하다. Run/Debug Configuration에서 하위에 Deployment 탭이 있다.

<img width="1321" alt="spring2" src="https://user-images.githubusercontent.com/30178507/135743575-f15d5616-156e-4004-934b-978bcb8d56fe.png">

여기서 위와 같이 `Deploy at the server setup` 하위에 프로젝트의 이름으로 Artifacts가 존재하는 경우도 있지만 없는 경우도 있다. 없는 경우 상단에 `+` 버튼을 눌러 추가한다.

<img width="1321" alt="spring3" src="https://user-images.githubusercontent.com/30178507/135743576-ae4b55e8-8d44-4f4e-a25e-c5f7ba7074d1.png">

그리고 기본적으로 `Output directory`가 `{프로젝트 폴더 경로}/out/artifacts/{프로젝트 이름_WAR}`으로 세팅되어 있는데 이를 `{프로젝트 폴더 경로}/build/libs`로 변경한다. 이는 빌드툴로 gradle을 사용하는데 gradle이 빌드 결과물을 `build/libs`에 놓기 때문이다.

위와 같이 세팅하면 톰켓 설정은 끝난다.

### build.gradle

```groovy
plugins {
    id 'java'
    id 'war'
}

group = 'edu.pkch'

sourceCompatibility = 11
targetCompatibility = 11

repositories {
    mavenCentral()
}

dependencies {
    implementation 'javax.servlet:javax.servlet-api:4.0.1'
    implementation 'org.springframework:spring-core:5.3.10'
    implementation 'org.springframework:spring-context:5.3.10'
    implementation 'org.springframework:spring-webmvc:5.3.10'
    implementation 'com.fasterxml.jackson.core:jackson-core:2.10.2'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.10.2'

    testImplementation 'org.junit.jupiter:junit-jupiter:5.8.0'
    testImplementation 'org.assertj:assertj-core:3.20.0'
}
```

build.gradle은 다음과 같다. war 패키징을 위한 `war` 플러그인과 함께 필요한 의존성을 추가한다.

Spring Boot의 starter 의존성과는 달리 필요한 의존성을 모두 직접 입력해주어야하기 때문에 위와 같이 servlet, spring-core, spring-context, spring-webmvc, jackson 등의 의존성을 설정한다.

> Spring 5.3.10과 Java 11, Gradle 6.8을 사용

이렇게 의존성 설정을 하면 끝나는게 아니다. 애플리케이션 설정이 필요하다.

### 애플리케이션 설정

먼저 Application 초기화를 위한 AppInitializer를 추가한다.

```groovy
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }

@Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[0];
    }
}
```

AppInitializer는 AbstractAnnotationConfigDispatcherServletInitializer를 상속받고 있다.

AbstractAnnotationConfigDispatcherServletInitializer는 Spring 3.2부터 지원하는 Java Config용 Initializer로 이를 통해 간단하게 Spring ApplicationContext 설정을 할 수 있다.

AbstractAnnotationConfigDispatcherServletInitializer는 `org.springframework.web.WebApplicationInitializer`를 구현하고 있는데 이는 Servlet 3.0부터 web.xml을 대체하기 위해 등장한 ServletContainerInitializer을 통해 애플리케이션을 설정하기 위함이다.

```java
@HandlesTypes(WebApplicationInitializer.class)
public class SpringServletContainerInitializer implements ServletContainerInitializer {

	@Override
	public void onStartup(@Nullable Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
			throws ServletException {

		List<WebApplicationInitializer> initializers = Collections.emptyList();

		if (webAppInitializerClasses != null) {
			initializers = new ArrayList<>(webAppInitializerClasses.size());
			for (Class<?> waiClass : webAppInitializerClasses) {
				if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
						WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
					try {
						initializers.add((WebApplicationInitializer)
								ReflectionUtils.accessibleConstructor(waiClass).newInstance());
					}
					catch (Throwable ex) {
						throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
					}
				}
			}
		}

		if (initializers.isEmpty()) {
			servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
			return;
		}

		servletContext.log(initializers.size() + " Spring WebApplicationInitializers detected on classpath");
		AnnotationAwareOrderComparator.sort(initializers);
		for (WebApplicationInitializer initializer : initializers) {
			initializer.onStartup(servletContext);
		}
	}

}
```

Spring webmvc에서 제공하는 SpringServletContainerInitializer이다. SpringServletContainerInitializer는 ServletContainerInitializer를 구현하고 있다. Servlet 3.0 이상의 서버는 `즉, 톰켓은` 서버를 시작할 때 ServletContainerInitializer를 찾아서 `onStartUp`을 실행한다. 때문에 톰켓에 애플리케이션을 올려 실행을 하면 `SpringServletContainerInitializer#onStartUp`이 실행되고 SpringServletContainerInitializer은 `@HandlesTypes(WebApplicationInitializer.class)`를 제공하여 WebApplicationInitializer를 구현한 클래스들을 찾아 초기화한다. 즉, SpringServletContainerInitializer는 WebApplicationInitializer를 찾아 delegate하는 역할을 하는 것이다.

이제 AppInitializer의 `getRootConfigClasses`, `getServletConfigClasses`, `getServletMappings`을 재정의해야한다.

```java
@Configuration
@ComponentScan(
        basePackages = { "edu.pkch.spring" },
        excludeFilters = {
                @ComponentScan.Filter(type= FilterType.ANNOTATION, value= EnableWebMvc.class)
        }
)
public class RootConfig {
}
```

먼저 `getRootConfigClasses`의 설정 클래스로 제공할 RootConfig를 정의한다. Servlet 애플리케이션 상위에 존재하는 컨텍스트 설정이다.

이때 `@EnableWebMvc`가 붙은 `@Configuration` 클래스들은 Servlet 애플리케이션 설정에 사용하기 위해 excludeFilters를 통해 빈스캔에서 제외한다.

```java
@Configuration
@EnableWebMvc
@ComponentScan("edu.pkch.spring.web")
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("WEB-INF/views/");
        resolver.setSuffix(".jsp");

        return resolver;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
```

그리고 WebConfig를 정의한다. `@EnableWebMvc`를 달아서 Spring Web MVC 기능을 사용하도록하며 기본으로 사용할 viewResolver를 설정한다.

> 참고로 InternalResourceViewResolver는 JSP 파일을 위한 ViewResolver이다.
> 

이렇게 정의한 RootConfig와 WebConfig를 가지고 AppInitializer를 수정한다.

```java
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

RootConfig를 getRootConfigClasses에 WebConfig를 getServletConfigClasses에 두고 위 설정한 Servlet 애플리케이션에 접근하기 위한 매핑 경로를 getServletMappings로 지정한다.

다음 컨트롤러 작성한다.

```java
@Controller
public class HomeController {

    @GetMapping
    public String index() {
        return "index";
    }
}
```

간단하게 `/` 루트 경로로 접근하면 `index.jsp`를 뿌려주도록 한다.

이제 `index.jsp` 파일 작성이 필요하다. `src/main/webapp/views` 폴더 하위에 `index.jsp`를 작성한다.

```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>첫 페이지</title>
  </head>
  <body>
    hello world!
  </body>
</html>
```

이제 톰켓을 실행하고 `http://localhost:8080`으로 접근하면 다음과 같은 화면이 나타난다.

<img width="574" alt="spring_hello" src="https://user-images.githubusercontent.com/30178507/135743544-06fd3e13-ee56-4a95-809a-ad9f23d38d45.png">

## 참고

initializr docs: [https://docs.spring.io/initializr/docs/current/reference/html/#user-guide](https://docs.spring.io/initializr/docs/current/reference/html/#user-guide)

[https://gmlwjd9405.github.io/2018/12/24/intellij-tomcat-war-deploy.html](https://gmlwjd9405.github.io/2018/
12/24/intellij-tomcat-war-deploy.html)

[https://akaroice.tistory.com/13](https://akaroice.tistory.com/13)