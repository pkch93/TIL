# Day13

## 2019.07.13 Saturday

## 다양한 HandlerMapping

DispatcherServlet이 요청에 적절한 handler를 찾을때 HandlerMapping을 이용한다. 이때 순회하는 순서는 `SimpleUrlHandlerMapping`, `RequestMappingHandlerMapping`, `BeanNameHandlerMapping`, `WelcomePageHandlerMapping` 순으로 HandlerMapping을 이용하여 적절한 Handler를 찾게된다.

> 여기서 Spring을 사용하면서 주로 사용하는 @Controller와 @RequestMapping이 어떻게 Handler로 찾아질 수 있는지 알아보기 위해 `RequestMappingHandlerMapping`을 중심으로 알아볼 예정.

## SimpleUrlHandlerMapping

DispatcherServlet이 가장 먼저 찾아보는 `HandlerMapping`이다.

```java
private final Map<String, Object> urlMap = new LinkedHashMap<>();
```

​

```java
/**
    * Map URL paths to handler bean names.
    * This is the typical way of configuring this HandlerMapping.
    * <p>Supports direct URL matches and Ant-style pattern matches. For syntax
    * details, see the {@link org.springframework.util.AntPathMatcher} javadoc.
    * @param mappings properties with URLs as keys and bean names as values
    * @see #setUrlMap
    */
public void setMappings(Properties mappings) {
    CollectionUtils.mergePropertiesIntoMap(mappings, this.urlMap);
}

/**
    * Set a Map with URL paths as keys and handler beans (or handler bean names)
    * as values. Convenient for population with bean references.
    * <p>Supports direct URL matches and Ant-style pattern matches. For syntax
    * details, see the {@link org.springframework.util.AntPathMatcher} javadoc.
    * @param urlMap map with URLs as keys and beans as values
    * @see #setMappings
    */
public void setUrlMap(Map<String, ?> urlMap) {
    this.urlMap.putAll(urlMap);
}

/**
    * Allow Map access to the URL path mappings, with the option to add or
    * override specific entries.
    * <p>Useful for specifying entries directly, for example via "urlMap[myKey]".
    * This is particularly useful for adding or overriding entries in child
    * bean definitions.
    */
public Map<String, ?> getUrlMap() {
    return this.urlMap;
}
```

`SimpleUrlHandlerMapping`은 위와 같이 urlMap을 변수로 가진다. 이를 이용하여 처음 ApplicationContext가 생성될 때 기본적으로 `/`루트 경로와 `/**`루트 하위 경로에 대한 handler를 등록한다. 자세한건 `registerHandlers`메서드를 참고

```java
@Override
public void initApplicationContext() throws BeansException {
    super.initApplicationContext();
    registerHandlers(this.urlMap);
}

/**
    * Register all handlers specified in the URL map for the corresponding paths.
    * @param urlMap a Map with URL paths as keys and handler beans or bean names as values
    * @throws BeansException if a handler couldn't be registered
    * @throws IllegalStateException if there is a conflicting handler registered
    */
protected void registerHandlers(Map<String, Object> urlMap) throws BeansException {
    if (urlMap.isEmpty()) {
        logger.trace("No patterns in " + formatMappingName());
    }
    else {
        urlMap.forEach((url, handler) -> {
            // Prepend with slash if not already present.
            if (!url.startsWith("/")) {
                url = "/" + url;
            }
            // Remove whitespace from handler bean name.
            if (handler instanceof String) {
                handler = ((String) handler).trim();
            }
            registerHandler(url, handler);
        });
        if (logger.isDebugEnabled()) {
            List<String> patterns = new ArrayList<>();
            if (getRootHandler() != null) {
                patterns.add("/");
            }
            if (getDefaultHandler() != null) {
                patterns.add("/**");
            }
            patterns.addAll(getHandlerMap().keySet());
            logger.debug("Patterns " + patterns + " in " + formatMappingName());
        }
    }
}
```

그럼 urlMap에 사용자가 설정한 url을 넣을 수 있을까? 이를 위해 `setUrlMap`을 호출하는 클래스를 따라가보기로 했다. 그렇게 되면 `DefaultServletHandlerConfiguer` 클래스에서 `setUrlMap`을 사용하는 것을 확인할 수 있다.

```java
// ...
@Nullable
protected SimpleUrlHandlerMapping buildHandlerMapping() {
    if (this.handler == null) {
        return null;
    }

    SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
    handlerMapping.setUrlMap(Collections.singletonMap("/**", this.handler));
    handlerMapping.setOrder(Integer.MAX_VALUE);
    return handlerMapping;
}
```

위 코드에서는 `DefaultServletHandlerConfigurer`에서 사용하는 handler인 `DefaultServletHttpRequestHandler`를 `/**`경로에 매핑하고 있다. 따라서 `DefaultServletHttpRequestHandler`이 커스텀한 url 핸들러를 등록하도록 구현했는지 살펴보았지만, `DefaultServletHttpRequestHandler`에서는 현재 `ServletContext`의 유형에 따라 요청을 처리하는 작업만 기술하고 있다. 그럼 어디서 처리하는 걸까?

다시 `setUrlMap`으로 돌아간다. 여기서 `ViewControllerRegistry`라는 클래스에서 `setUrlMap`을 사용하고 있다. 이곳에서도 `buildHandlerMapping`이 있는데 이 메서드로 개발자가 커스텀한 url에 handler를 설정해주도록 만든다.

```java
/**
    * Return the {@code HandlerMapping} that contains the registered view
    * controller mappings, or {@code null} for no registrations.
    * @since 4.3.12
    */
@Nullable
protected SimpleUrlHandlerMapping buildHandlerMapping() {
    if (this.registrations.isEmpty() && this.redirectRegistrations.isEmpty()) {
        return null;
    }

    Map<String, Object> urlMap = new LinkedHashMap<>();
    for (ViewControllerRegistration registration : this.registrations) {
        urlMap.put(registration.getUrlPath(), registration.getViewController());
    }
    for (RedirectViewControllerRegistration registration : this.redirectRegistrations) {
        urlMap.put(registration.getUrlPath(), registration.getViewController());
    }

    SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
    handlerMapping.setUrlMap(urlMap);
    handlerMapping.setOrder(this.order);
    return handlerMapping;
}
```

즉, registration으로 등록한 url에 controller를 매핑하여 설정하는 것이다!

마지막으로 `ViewControllerRegistry`의 `buildHandlerMapping`의 호출부를 찾아보면 `WebMvcConfigurationSupport`가 나타난다.

```java
/**
    * Return a handler mapping ordered at 1 to map URL paths directly to
    * view names. To configure view controllers, override
    * {@link #addViewControllers}.
    */
@Bean
@Nullable
public HandlerMapping viewControllerHandlerMapping() {
    ViewControllerRegistry registry = new ViewControllerRegistry(this.applicationContext);
    addViewControllers(registry);

    AbstractHandlerMapping handlerMapping = registry.buildHandlerMapping();
    if (handlerMapping == null) {
        return null;
    }
    handlerMapping.setPathMatcher(mvcPathMatcher());
    handlerMapping.setUrlPathHelper(mvcUrlPathHelper());
    handlerMapping.setInterceptors(getInterceptors());
    handlerMapping.setCorsConfigurations(getCorsConfigurations());
    return handlerMapping;
}
```

결국 위 메서드가 빈으로 등록되면서 `SimpleUrlHandlerMapping`이 설정되는 것을 확인할 수 있다.

> 빈 등록 과정에 대해 학습할 필요가 있어보임,,

### SimpleUrlHandlerMapping에 handler 등록하기

```java
package techcourse.myblog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/helloworld").setViewName("helloworld");
    }
}
```

위와 같이 `WebMvcConfigurer`를 구현하는 클래스를 만들어 `@Configuration` 으로 빈 등록하면 `SimpleUrlHandlerMapping`으로 `UrlMap`에 등록된다. 단, 이경우 위 경로는 가장 낮은 순위로 찾게 되므로 `@RequestMapping`으로 같은 url에 매핑했다면 위 설정이 적용되지 않는다.

```java
// ...
@GetMapping("/helloworld")
public String showHello() {
    return "hello";
}
```

위와 같이 Handler가 설정되어있다면 커스텀한 `SimpleUrlHandlerMapping`보다 `RequestMappingHandlerMapping`이 우선하므로 위의 showHello가 handler로 등록된다.

※ 참고 : Custom `SimpleUrlHandlerMapping`의 우선순위

첫번째 우선순위의 `SimpleUrlHandlerMapping`은 `favicon.ico` 파일을 위한 `FaviconHandlerMapping`, 마지막 순위의 `SimpleUrlHandlerMapping`은 각종 정적 resource를 위한 `ResourceHandlerMapping`이다. 커스텀하게 설정한 `SimpleUrlHandlerMapping`은 `RequestMappingHandlerMapping`의 다음 순위에 배정된다.

### ※ @EnableWebMvc

`@EnableWebMvc`는 Spring Web MVC 프로젝트를 사용하기 위해 설정해야하는 어노테이션이다. 이때 스프링에서는 `@Configuration`과 `@EnableWebMvc` 어노테이션이 붙은 설정 클래스를 찾아 WebMvc 설정을 해주는 것으로 보인다. 스프링부트에서는 `WebMvcAutoConfiguration`이라는 자동 설정 클래스가 있어 Web MVC의 기본적인 설정을 해준다.

스프링부트의 `DelegatingWebMvcConfiguration`을 `@EnableWebMvc`가 `@Import`하고 있으므로 스프링 `ApplicationContext`가 생성될 때 자동설정된다.

> 위 내용은 지금까지 학습내용을 바탕으로 추측한 내용... 향후 학습필요

## RequestMappingHandlerMapping

### HandlerMapping이 @Controller와 @RequestMapping을 파악하는 원리

위 `@Controller`와 `@RequestMapping`은 RequestMappingHandlerMapping을 통해 Handler로써 작용할 수 있는지 판단한다.

이를 자세히 이해하기 위해 `AbstractHandlerMethodMapping`에 변수로 선언된 `MappingRegistry`를 이해해야한다.

`MappingRegistry`는 `AbstractHandlerMethodMapping`의 이너클래스로써 애플리케이션의 모든 mapping 정보를 담고 있는 클래스이다. `AbstractHandlerMethodMapping`는 이를 통해 매핑 정보를 관리한다.

```java
class MappingRegistry {

    private final Map<T, MappingRegistration<T>> registry = new HashMap<>();

    private final Map<T, HandlerMethod> mappingLookup = new LinkedHashMap<>();

    private final MultiValueMap<String, T> urlLookup = new LinkedMultiValueMap<>();

    private final Map<String, List<HandlerMethod>> nameLookup = new ConcurrentHashMap<>();

    private final Map<HandlerMethod, CorsConfiguration> corsLookup = new ConcurrentHashMap<>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    // ...
}
```

위 `MappingRegistry`에 m,apping 등록은 애플리케이션이 처음 초기화 될 때 실행한다.

```java
/**
 * Detects handler methods at initialization.
 * @see #initHandlerMethods
 */
@Override
public void afterPropertiesSet() {
    initHandlerMethods();
}

/**
 * Scan beans in the ApplicationContext, detect and register handler methods.
 * @see #getCandidateBeanNames()
 * @see #processCandidateBean
 * @see #handlerMethodsInitialized
 */
protected void initHandlerMethods() {
    for (String beanName : getCandidateBeanNames()) {
        if (!beanName.startsWith(SCOPED_TARGET_NAME_PREFIX)) {
            processCandidateBean(beanName);
        }
    }
    handlerMethodsInitialized(getHandlerMethods());
}

/**
    * Determine the names of candidate beans in the application context.
    * @since 5.1
    * @see #setDetectHandlerMethodsInAncestorContexts
    * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors
    */
protected String[] getCandidateBeanNames() {
    return (this.detectHandlerMethodsInAncestorContexts ?
            BeanFactoryUtils.beanNamesForTypeIncludingAncestors(obtainApplicationContext(), Object.class) :
            obtainApplicationContext().getBeanNamesForType(Object.class));
}
```

위 `afterPropertiesSet`메서드를 보면 빈 초기화 때 Handler를 찾아 등록하는 것을 알 수 있다.

> `afterPropertiesSet`는 `InitializingBean`의 추상 메서드, `AbstractHandlerMethodMapping`은 인터페이스 `InitializingBean`을 구현하고있다.

애플리케이션 초기화시 `initHandlerMethod`와 `getCandidateBeanNames` 메서드가 호출되면서 mapping을 등록한다.

참고로 위 `getCandidateBeanNames`는 `AbstractApplicationContext`의 `getBeanNamesForType`을 호출하여 빈 이름을 가져온다.

```java
@Override
public String[] getBeanNamesForType(ResolvableType type) {
    assertBeanFactoryActive();
    return getBeanFactory().getBeanNamesForType(type);
}

@Override
public String[] getBeanNamesForType(@Nullable Class<?> type) {
    assertBeanFactoryActive();
    return getBeanFactory().getBeanNamesForType(type);
}

@Override
public String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
    assertBeanFactoryActive();
    return getBeanFactory().getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
}
```

결국 위와 같은 과정으로 등록된 매핑을 가져와 현재 요청과 가장 적절한 핸들러를 DispatcherServlet에 전달하여 요청을 수행하도록 해준다.

> 빈 등록 과정에 대한 학습 필요
>
> BeanNameHandlerMapping과 WelcomePageHandlerMapping은 빈등록과 주입 및 @ComponentScan이 빈을 찾는 과정에 대해 학습한 후 필요에 따라 학습할 예정

