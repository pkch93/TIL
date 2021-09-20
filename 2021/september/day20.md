# 2021.09.20 TIL - @Configuration

- [2021.09.20 TIL - @Configuration](#20210920-til---configuration)
  - [Configuration Enhancements](#configuration-enhancements)
  - [proxyBeanMethods](#proxybeanmethods)
  - [참고](#참고)

`@Configuration`은 `@Component`와 달리 다르게 동작한다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    @AliasFor(annotation = Component.class)
    String value() default "";

    boolean proxyBeanMethods() default true;
}
```

`@Configuration`의 형태는 위와 같다. `@Component`가 붙어있어 ApplicationContext에서 관리되지만 일반 `@Component`들과는 다르게 동작한다.

```java
@Test
void configuration() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    AppConfig appConfig = ac.getBean("appConfig", AppConfig.class);
    System.out.println(appConfig);
}
```

위와 같은 테스트코드로 ApplicationContext에 등록된 `@Configuration` 빈인 AppConfig를 찍어보면 다음과 같이 출력된다.

```
edu.pkch.spring.AppConfig$$EnhancerBySpringCGLIB$$9b87286a@65b154e3
```

즉, CGLIB라는 프록시로 AppConfig가 감싸져 있는 것을 알 수 있다.

이는 ConfigurationClassPostProcessor라는 BeanFactoryPostProcessor가 `@Configuration` 빈을 프록시로 감싸서 ApplicationContext에 등록하도록 만든다.

감쌀때 `@Configuration` 클래스를 EnhancedConfiguration 인터페이스 구현체로 조작한다. EnhancedConfiguration은 BeanFactory를 알아야하기 때문에 BeanFactoryAware를 확장한다. 이를 통해 `@Configuration`에 등록된 빈 팩토리 메서드가 한번만 호출하도록 보장한다. 즉, 빈이 싱글톤임을 보장한다.

```java
public Class<?> enhance(Class<?> configClass, @Nullable ClassLoader classLoader) {
		if (EnhancedConfiguration.class.isAssignableFrom(configClass)) {
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Ignoring request to enhance %s as it has " +
						"already been enhanced. This usually indicates that more than one " +
						"ConfigurationClassPostProcessor has been registered (e.g. via " +
						"<context:annotation-config>). This is harmless, but you may " +
						"want check your configuration and remove one CCPP if possible",
						configClass.getName()));
			}
			return configClass;
		}
		Class<?> enhancedClass = createClass(newEnhancer(configClass, classLoader));
		if (logger.isTraceEnabled()) {
			logger.trace(String.format("Successfully enhanced %s; enhanced class name is: %s",
					configClass.getName(), enhancedClass.getName()));
		}
		return enhancedClass;
	}

	private Enhancer newEnhancer(Class<?> configSuperClass, @Nullable ClassLoader classLoader) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(configSuperClass);
		enhancer.setInterfaces(new Class<?>[] {EnhancedConfiguration.class});
		enhancer.setUseFactory(false);
		enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
		enhancer.setStrategy(new BeanFactoryAwareGeneratorStrategy(classLoader));
		enhancer.setCallbackFilter(CALLBACK_FILTER);
		enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
		return enhancer;
	}

	private Class<?> createClass(Enhancer enhancer) {
		Class<?> subclass = enhancer.createClass();
		// Registering callbacks statically (as opposed to thread-local)
		// is critical for usage in an OSGi environment (SPR-5932)...
		Enhancer.registerStaticCallbacks(subclass, CALLBACKS);
		return subclass;
	}
```

> `@Configuration` 클래스를 EnhancedConfiguration을 구현하도록 조작하는 ConfigurationClassEnhancer

```java
public interface EnhancedConfiguration extends BeanFactoryAware {}
```

> EnhancedConfiguration은 위와 같다.

`@Configuration` 클래스의 `@Bean` 팩토리 메서드는 `ConfigurationClassParser#parse`에 의해서 BeanDefinition으로 등록된다.

이렇게 ConfigurationClassPostProcessor를 통해 `@Configuration` 클래스의 빈들을 BeanDefinition으로 만들고 CGLIB 프록시와 결합하여 해당 빈을 생성할 때 한번만 호출하여 싱글톤 빈으로 만들어지도록 도와준다.

## Configuration Enhancements

Spring 3.0에서부터 Java-based Config를 지원하기 위해 `@Configuration`이 등장했지만 그 이전부터 훨씬 풍부한 기능을 지원하는 XML-based Config와 비슷한 수준의 지원을 위해서 Spring 3.1에서 `@Configuration`과 함께 다양한 기능을 추가한다.

그중에 하나가 `@Enable` 어노테이션과 함께 기능을 실행하는 것이다.

- @EnableWebMvc
- @EnableAsync
- @EnableTransactionManagement
- @EnableScheduling
- @EnableLoadTimeWeaving

위와 같은 `@Enable` 어노테이션을 `@Configuration`과 함께 사용하면 해당 기능을 활성화할 수 있다.

```java
@Configuration
@EnableAsync
public class AsyncConfig {

}
```

위 AsyncConfig가 `@EnableAsync`를 통해 Spring에서 지원하는 비동기 기능을 사용할 수 있도록 만들어준다.

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AsyncConfigurationSelector.class)
public @interface EnableAsync {
	// ...
}
```

위 `@EnableAsync`는 `@Import(AsyncConfigurationSelector.class)`를 통해 스프링 애플리케이션이 비동기 기능을 지원하도록한다. 다른 `@Enable`도 마찬가지로 필요한 설정을 `@Import`를 통해 지원하도록 구현되어있다.

## proxyBeanMethods

Spring 5.2부터 `@Configuration`에 추가된 설정으로 기본값이 true이다.

proxyBeanMethods는 빈 메서드가 프록시에 의해 인터셉트되고 만들어진 빈을 반환하도록, 즉, 빈 메서드가 한번만 실행되도록 보장한다.

만약 proxyBeanMethods를 false로 설정하게 되면 true일때 프록시로 감싸져서 ApplicationContext에 저장하는 것과 달리 프록시 없이 감싸게 된다.

```text
edu.pkch.spring.AppConfig@366974ef
```

즉, `@Configuration` 클래스를 CGLIB 프록시없이 생성하므로 `@Bean` 팩터리 메서드를 생성할 때 프록시 없이 생성한다. 단, 만약 하나의 빈 메서드를 여러번 사용하여 만들게되면 사용할 때마다 다른 빈 객체가 생성된다. 즉, 빈이 싱글톤이라는 것을 보장하지 못한다. 이부분은 주의해야한다.

> 이를 Lite Mode라고 한다.

```java
@Configuration(proxyBeanMethods = false)
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memoryMemberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(
                memoryMemberRepository(),
                fixDiscountPolicy()
        );
    }

    @Bean
    public MemoryMemberRepositoryImpl memoryMemberRepository() {
        return new MemoryMemberRepositoryImpl();
    }

    @Bean
    public FixDiscountPolicy fixDiscountPolicy() {
        return new FixDiscountPolicy();
    }

    @Bean
    public RateDiscountPolicy rateDiscountPolicy() {
        return new RateDiscountPolicy();
    }
}
```

위와 같이 사용하면 `memberService`, `orderService`에서 호출하는 `memoryMemberRepository` 빈 메서드는 새로운 MemoryMemberRepositoryImpl을 생성하므로 하나의 `memoryMemberRepository` 빈 객체를 보장하지 못한다.

## 참고

@Configuration docs: [https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Configuration.html](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/context/annotation/Configuration.html)

Configuration Enhancements: [https://spring.io/blog/2011/06/10/spring-3-1-m2-configuration-enhancements](https://spring.io/blog/2011/06/10/spring-3-1-m2-configuration-enhancements)

When to set proxyBeanMethods to false in Springs @Configuration? 참고: [https://stackoverflow.com/questions/61266792/when-to-set-proxybeanmethods-to-false-in-springs-configuration](https://stackoverflow.com/questions/61266792/when-to-set-proxybeanmethods-to-false-in-springs-configuration)

@Configuration Lite Mode: [http://wonwoo.ml/index.php/post/2000](http://wonwoo.ml/index.php/post/2000)