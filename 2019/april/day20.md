# Day20

## 2019.04.20 Saturday

### 1. gradle

#### Task

Gradle에서 task를 만들때는 아래의 예시와 같이 task Task명으로 선언한다. 그리고 클로저를 통해 해당 task가 수행할 내용을 작성하면 된다.

```groovy
task hello {
    println 'gradle hello'
}
```

위 hello task 'gradle hello'라는 문장을 출력한다. 실행은 `gradle 'task명'`으로 할 수 있다.

* task 실행 순서 제어 \(doLast / doFirst\)

Task 내부에서 doLast와 doFirst를 사용하면 테스크의 순서를 제어할 수 있다.

```groovy
task controlOrder {
    doLast {
        println "둘째"
    }
    doFirst {
        println "첫째"
    }
    doLast {
        println "마지막"
    }
}
```

위 Task를 실행하면 doLast가 먼저 나온다 해도 doFirst가 있는 블록을 먼저 실행한다. 그 이후는 doLast블럭의 순서로 실행된다. 위 예시에서는 첫째 둘째 마지막 순으로 출력된다.

> Gradle 5.0 이전 버전에서는 `<< (leftShift)`를 통해 순서를 제어했다. `<<`는 `doLast` 메서드를 alias한 방법이다. 하지만 5.0부터는 이 방법이 deperated 되었기 때문에 사용하지 않는 것을 권장.

[&lt;&lt; 참고](https://stackoverflow.com/questions/26085379/whats-the-operator-double-less-than-in-gradle)

* groovy의 기능을 활용한 task

gradle은 groovy 언어를 활용하므로 groovy의 기능을 활용하여 빌드스크립트를 작성할 수 있다.

1. groovy 객체 메서드 활용

groovy는 JVM에서 동작하는 언어이다. 따라서, java 라이브러리를 groovy에서 사용할 수 있고 그 반대도 가능하다. groovy에서도 java와 마찬가지로 다양한 클래스를 제공해준다. 대표적으로 `String`이 있다.

```groovy
task stringTask {
    String str = 'Hello World!'
    doLast {
        println str.toUpperCase()
        println str.toLowerCase()
    }
}
```

`gradle stringTask` 명령으로 위 task를 실행하면 `HELLO WORLD!`와 `hello world!`가 나타난다.

1. loop

반복을 사용하여 task를 정의할 수 있다.

```groovy
task loop {
    doLast {
        10.times { println "$it" }
    }
}
```

위와 같이 task를 작성했다. `times`는 앞에 나온 숫자만큼 블록에 정의한 내용을 실행한다는 의미이다. 이때 실행되는 실행숫자를 참조할 수 있는데 기본적으로 `it`로 참조할 수 있다.

위 결과는 0~9까지 숫자가 출력된다.

```groovy
3.items { i -> 
    task "execTask$i" {
        doLast {
            println "counter: $i"
        }
    }
}
execTask1.dependsOn 'execTask0','execTask2'
```

위와 같이 task를 생성했다. `3.items` 반복으로 execTask0, execTask1, execTask2를 만든 스크립트이다.

> dependOn은 task에 대해 의존 task를 설정하는 옵션이다. 위 예시에서 execTask1의 의존성으로 execTask0과 execTask2를 설정했다. 따라서 execTask1은 의존성으로 설정된 execTask0과 execTask2가 먼저 수행된 후에 실행된다.

1. map

groovy에서도 array, map과 같은 자료구조를 사용할 수 있다. 이때, map으로 `key`와 `value`를 사용하여 task를 선언할 수 있다.

```groovy
def conf = ['myName': 'pkch', 'myAge': 27]

conf.each(info, value -> 
    task "execTask$info" {
        doLast {
            println value
        }
    }
)
```

groovy에서는 map 자료구조에 each 메서드를 통해 모든 데이터를 조회할 수 있다. 조회 할 때 key, value 값을 가져와서 key를 task name 만들 때 사용하고 value로 해당 task를 실행할 때 출력하는 task이다.

* task property

task를 선언할 때 이름 뒤에 `()`를 통해 property를 지정할 수 있다. 단, task 객체의 property를 지정해야한다. \(임의의 이름 X\)

```groovy
task desc(description: "this is gradle task") {
    println description
}
```

[task properies](https://docs.gradle.org/current/dsl/org.gradle.api.Task.html#N18C50)

* defaultTasks

gradle 명령어로 임의로 task를 실행하지 않아도 빌드 스크립트가 실행되었을때 실행단계에서 실행할 기본 task를 설정하는 방법

```groovy
defaultTasks 'execTask0', 'execTask1'

3.items { i -> 
    task "execTask$i" {
        doLast {
            println "counter: $i"
        }
    }
}
```

위와 같이 defaultTasks로 task 이름을 지정하여 처음 실행할 task를 설정할 수 있다.

* 조건에 따른 빌드
* onlyif
* if
* 실행 순서 제어

실행 순서를 제어하는데는 두가지방법이 있다. `mustRunAfter`와 `shouldRunAfter`가 있다.

두 방법은 공통적으로 task 간 실행되는 순서를 강제적으로 지정하는 방법이다.

task 간 순환참조가 발생하는 경우에는 예외가 발생한다. 반면 `shouldRunAfter`의 경우는 순환참조를 무시하고 `shouldRunAfter`로 지정된 순서대로 실행된다. 그리고 병렬 실행 시 `shouldRunAfter` task와는 별도로 모든 의존성을 충족하면 `shouldRunAfter`를 무시한다.

[mustRunAfter와 shouldRunAfter의 차이](https://stackoverflow.com/questions/36794690/what-is-the-difference-between-mustrunafter-and-shouldrunafter-in-task-ordering)

> 의존관계와 순서 지정 간의 차이
>
> 의존관계는 해당 task가 실행되기 전 관계를 가진 task를 모두 실행한 후 실행한다.
>
> 반면 `mustRunAfter`나 `shouldRunAfter`의 경우는 선후 관계를 가진 task가 모두 실행 task에 있어야 적용된다.

* task graph

> task간의 의존관계를 시각적으로 표현한 그래프 \(`방향성 비순환 그래프`\)

gradle task에서 `dependsOn`으로 의존관계를 지정했다면 해당 task간의 의존성이 생긴다.

따라서, `dependsOn`으로 선언된 task를 먼저 실행해야만 해당 task를 실행할 수 있다. 이때, 선행 task에서 exception이 발생한다면 실행이 중지되는데 만약 `finallzedBy`로 종료시 반드시 실행하는 task를 지정하면 task graph가 바뀌게 된다.

### Gradle Domain Object

#### 1. Project

Project 객체는 `build.gradle`과 대응하여 프로젝트의 환경 구성, 의존관계, task 등의 내용을 제어하고 참고한다.

gradle이 빌드를 실행할 때 Project 객체는 다음과 같이 생성된다. 1. Settings 객체를 생성한다. 2. 만약 settings.gradle이 있다면 Settings 객체를 settings.gradle의 설정사항에 맞춘다. 3. Settings 객체의 설정사항을 사용하여 Project 객체의 계층구조를 만든다. 4. 멀티 프로젝트인 경우 부모 프로젝트에게서 Project 객체를 생성 후 자식 프로젝트의 Project 객체를 생성한다.

[Project 객체 Properties / Methods 참고](https://docs.gradle.org/current/dsl/org.gradle.api.Project.html#N14F03)

#### 2. Task

Task 객체는 Action 객체를 생성하여 구성한다. 내부적으로 execute\(\)를 호출하여 실행한다.

> Action은 gradle에서 작업을 처리하기 위한 하나의 기본 단위

[Task 객체 Properties / Methods 참고](https://docs.gradle.org/current/dsl/org.gradle.api.Task.html#N18C50)

#### 3. Gradle

해당 프로젝트의 gradle 환경을 참조할 수 있는 객체

[Gradle 객체 Properties / Methods 참고](https://docs.gradle.org/current/dsl/org.gradle.api.invocation.Gradle.html)

#### 4. Settings

Settings 객체는 settings.gradle과 연관된 객체이다. 멀티 프로젝트 설정시에 주로 사용된다. 설정과 관련된 객체로 프로젝트 빌드 수행 전에 Settings 객체를 먼저 생성하게된다.

[Settings 객체 Properties / Methods 참고](https://docs.gradle.org/current/dsl/org.gradle.api.initialization.Settings.html#N1918A)

## 2. Spring MVC

### - MVC Config

MVC Config를 위해서 Spring은 자바, XML의 방법을 지원한다.

> 최근에는 Java Configuration을 더 선호

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
    // configuration method,,
}
```

위와 같이 `@EnableWebMvc`으로 Spring의 MVC 기능을 활성화 할 수 있다. 이는 XML 설정에서 `<mvc:annotation-driven>`과 같다.

Java Configuration에서는 `WebMvcConfigurer` 인터페이스를 통해 해당 Spring application의 MVC 설정을 변경 혹은 추가할 수 있다.

#### Type Conversion

기본적으로 Formatter로 `Number`와 `Date`에 대해 지원한다. 이들 Formatter를 사용하려면 `@NumberFormat`이나 `@DateTimeFormat` 어노테이션으로 적용할 수 있다.

> DateFormat의 경우는 Java API인 `Date`나 `LocalDateTime`뿐 아니라 `Joda-Time`까지 지원한다.

만약, custom한 `formatter`를 앱에 등록하고 싶다면 `WebMvcConfigurer`의 `addFormatters` 메서드를 `Override`하여 적용한다.

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry){
        // ..
    }
}
```

#### Validation

classpath에 `Bean Validation`이 등록되어 있다면 해당 Validator가 `LocalValidatorFactoryBean`로써 global Validaor로 등록된다. 등록된 Validator는 `@Valid`나 controller에 `Validated` method arguments를 사용하여 유효성 검사를 할 수 있다.

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public Validator getValidator() {
        // ...
    }
}
```

위는 Validator를 global하게 등록하는 방법이다. 만약 Handler에 지역적으로 적용하기 위해서는 다음과 다음과 같이 등록한다.

```java
@Controller
public class MyController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new FooValidator());
    }

}
```

#### Interceptors

`WebMvcConfigurer`의 `addInterceptors`를 통해 인터셉터를 등록할 수 있다.

> 인터셉터는 요청에 추가적인 처리를 할 때 사용할 수 있다.

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
        registry.addInterceptor(new ThemeChangeInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
        registry.addInterceptor(new SecurityInterceptor()).addPathPatterns("/secure/*");
    }
}
```

#### Content Types

request의 media type에 대해 `MediaType` 타입을 결정할 수 있다.

기본적으로 `json`, `xml`, `rss`, `atom` 등의 URL path extension을 처음 체크한다. 그 다음으로 `Accept` 헤더를 체크한다.

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("json", MediaType.APPLICATION_JSON);
        configurer.mediaType("xml", MediaType.APPLICATION_XML);
    }
}
```

