# 2021.09.19 TIL - BeanFactory와 ApplicationContext

- [2021.09.19 TIL - BeanFactory와 ApplicationContext](#20210919-til---beanfactory와-applicationcontext)
  - [BeanFactory](#beanfactory)
  - [ApplicationContext](#applicationcontext)
    - [EnvironmentCapable](#environmentcapable)
    - [MessageSource](#messagesource)
    - [ApplicationEventPublisher](#applicationeventpublisher)
    - [ResourcePatternResolver](#resourcepatternresolver)
  - [참고](#참고)

## BeanFactory

스프링의 의존성 주입 컨테이너의 핵심은 바로 BeanFactory 인터페이스이다. 컴포넌트의 라이프사이클 뿐만 아니라 의존성까지 관리한다.

BeanFactory는 스프링에서 의존성 주입의 역할을 담당한다. 따라서 애플리케이션에서 DI 기능만 필요하다면 BeanFactory 인터페이스를 구현한 클래스의 인터페이스를 생성하고 빈과 의존성 정보를 구성해야한다.

```java
public interface BeanFactory {

    String FACTORY_BEAN_PREFIX = "&";

    Object getBean(String name) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    Object getBean(String name, Object... args) throws BeansException;

    <T> T getBean(Class<T> requiredType) throws BeansException;

    <T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

    <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType);

    <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType);

    boolean containsBean(String name);

    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

    boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;

    boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException;

    @Nullable
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    @Nullable
    Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException;

    String[] getAliases(String name);

}
```

> 참고로 BeanFactory 만으로는 Annotation 기반의 Java Config를 사용할 수 없다. ApplicationContext를 사용해야만 Annotation 기반의 Java Config를 사용할 수 있다.

```java
package edu.pkch.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SpringEduApplication {

    public static void main(String[] args) {
        Resource resource = new ClassPathResource("context/bean-factory-beans.xml");
        BeanFactory beanFactory = new XmlBeanFactory(resource);
        System.out.println(beanFactory.getBean("person"));
    }
}
```

위와 같이 XmlBeanFactory를 통해 `context/bean-factory-beans.xml`에 정의된 빈 리소스를 전달하여 빈을 등록한다.

> 참고로 XmlBeanFactory는 Deprecated 되었다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="person" class="edu.pkch.spring.bean.Person">
        <constructor-arg name="name" value="pkch"/>
        <constructor-arg name="age" value="29"/>
    </bean>
</beans>
```

> bean XML-Schema는 [다음 문서](https://docs.spring.io/spring-framework/docs/4.2.x/spring-framework-reference/html/xsd-configuration.html)를 참고한다.

위 XML에 `edu.pkch.spring.bean.Person` POJO의 빈을 생성자 주입을 이용하도록 설정한다.

`edu.pkch.spring.bean.Person`은 다음과 같다.

```java
public class Person {
    private String name;
    private int age;

    public Person() {}

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

위와 같이 정의한 후 SpringEduApplication을 실행해보면 다음과 같이 Person이 주입되어 찍힌다.

```text
> Task :compileJava
> Task :processResources UP-TO-DATE
> Task :classes
> Task :SpringEduApplication.main()
Person{name='pkch', age=29}
```

참고로 위와 같이 XML 설정으로 정의한 Person에 JSR-250에 정의된 `@PostConstruct`나 `@PreDestroy`를 정의하면 동작하지 않는다. 이는 `CommonAnnotationBeanPostProcessor` 에서 위 `@PostConstruct`나 `@PreDestroy`의 동작을 구현하고 있기 때문이다.

다만, InitializingBean과 DisposableBean은 정상으로 동작한다.

## ApplicationContext

ApplicationContext는 스프링 컨테이너로써 다음과 같은 인터페이스이다.

```java
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
		MessageSource, ApplicationEventPublisher, ResourcePatternResolver {

    @Nullable
    String getId();

    String getApplicationName();

    String getDisplayName();

    long getStartupDate();

    @Nullable
    ApplicationContext getParent();

    AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;

}
```

ApplicationContext는 인터페이스로 Java Config를 사용한다면 `AnnotationConfigApplicationContext`를 통해 구현할 수 있다. 만약 Spring을 XML로 설정하고자 한다면 `GenericXmlApplicationnContext`을 구현할 수 있다. 그밖에 `GenericGroovyApplicationContext`도 구현체로 지원한다.

> Spring Java Config, 즉, `AnnotationConfigApplicationContext` 는 Spring 3.0부터 지원한다.

ApplicationContext는 **빈의 생성, 주입 기능을 담당**할 뿐만 아니라 **환경변수**, **메세지 기반 다국어 처리, 이벤트 기능** 등 BeanFactory 보다 더 많은 기능을 담당하는 스프링 컨테이너이다.

ApplicationContext의 상위 인터페이스인 EnvironmentCapable, ListableBeanFactory, MessageSource, ApplicationEventPublisher, ResourcePatternResolver가 ApplicatonContext가 BeanFactory에 비해 더 많은 기능들을 할 수 있도록 도와준다.

### EnvironmentCapable

```java
public interface EnvironmentCapable {

    Environment getEnvironment();

    }
    ```

    Spring 3.1부터 지원하는 인터페이스로 스프링 컨테이너의 환경 설정 값들을 가지는 Environment 객체를 관리하는 역할을 담당한다.

    ### ListableBeanFactory

    ListableBeanFactory는 BeanFactory 인터페이스의 확장으로 다음과 같은 형태를 가진다.

    ```java
    public interface ListableBeanFactory extends BeanFactory {

    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(ResolvableType type);

    String[] getBeanNamesForType(ResolvableType type, boolean includeNonSingletons, boolean allowEagerInit);

    String[] getBeanNamesForType(@Nullable Class<?> type);

    String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit);

    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException;

    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException;

    String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);

    Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException;

    @Nullable
    <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
            throws NoSuchBeanDefinitionException;

}
```

빈을 조회할 때 동일한 조건의 빈을 모두 열거하여 조회할 수 있도록 지원하는 BeanFactory이다. 동일한 조건의 빈을 열거하기 위해서는 빈을 lazy 하게 등록하는 것이 아니라 미리 등록 `preload` 해야만 가능하다.

### MessageSource

i18n을 구현하도록 도와주는 인터페이스. 지역에 따라 지정한 메세지를 보여주도록 지원한다.

```java
public interface MessageSource {

    @Nullable
    String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale);

    String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;

    String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException;

}
```

### ApplicationEventPublisher

애플리케이션에서 이벤트를 발행하고 처리하는 모델을 지원하기 위한 인터페이스이다.

```java
@FunctionalInterface
public interface ApplicationEventPublisher {

    default void publishEvent(ApplicationEvent event) {
        publishEvent((Object) event);
    }

    void publishEvent(Object event);

}
```

### ResourcePatternResolver

파일, 클래스패스, 외부의 리소스 등을 편리하게 조회할 수 있도록 지원한다.

```java
public interface ResourcePatternResolver extends ResourceLoader {

    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    Resource[] getResources(String locationPattern) throws IOException;

}
```

## 참고

전문가를 위한 스프링 5: [http://www.yes24.com/Product/Goods/78568418](http://www.yes24.com/Product/Goods/78568418)

스프링 핵심 원리 기본편: [https://www.inflearn.com/course/스프링-핵심-원리-기본편/lecture/55357](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B8%B0%EB%B3%B8%ED%8E%B8/lecture/55357?tab=note)

BeanFactory: [https://www.geeksforgeeks.org/spring-beanfactory/](https://www.geeksforgeeks.org/spring-beanfactory/)

ApplicationContext와 BeanFactory: [http://wonwoo.ml/index.php/post/1571](http://wonwoo.ml/index.php/post/1571)