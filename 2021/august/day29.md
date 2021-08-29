# 2021.08.29 TIL - Spring FactoryBean

- [2021.08.29 TIL - Spring FactoryBean](#20210829-til---spring-factorybean)
  - [AbstractFactoryBean](#abstractfactorybean)
  - [사용해보기](#사용해보기)
  - [참고](#참고)

Spring 컨테이너에서 빈을 등록할 때 빈으로 등록할 객체를 설정하는 방식과 FactoryBean을 빈으로 등록하는 방식 2가지를 지원한다. 전자는 프로그래머가 직접 객체를 생성하는 반면 후자는 프레임워크 단에서 팩토리를 사용하여 생성한다.

```java
public interface FactoryBean {
    T getObject() throws Exception;
    Class<?> getObjectType();
    boolean isSingleton();
}
```

FactoryBean 인터페이스는 위와 같다.

## AbstractFactoryBean

Spring에서는 쉽게 FactoryBean을 생성할 수 있도록 템플릿인 AbstractFactoryBean을 지원한다.

```java
public abstract class AbstractFactoryBean<T> implements FactoryBean<T>, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, DisposableBean {

    protected final Log logger = LogFactory.getLog(getClass());

    private boolean singleton = true;

    @Nullable
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    @Nullable
    private BeanFactory beanFactory;

    private boolean initialized = false;

    @Nullable
    private T singletonInstance;

    @Nullable
    private T earlySingletonInstance;

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override
    public void setBeanFactory(@Nullable BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Nullable
    protected BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    protected TypeConverter getBeanTypeConverter() {
        BeanFactory beanFactory = getBeanFactory();
        if (beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) beanFactory).getTypeConverter();
        }
        else {
            return new SimpleTypeConverter();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (isSingleton()) {
            this.initialized = true;
            this.singletonInstance = createInstance();
            this.earlySingletonInstance = null;
        }
    }

    @Override
    public final T getObject() throws Exception {
        if (isSingleton()) {
            return (this.initialized ? this.singletonInstance : getEarlySingletonInstance());
        }
        else {
            return createInstance();
        }
    }

    @SuppressWarnings("unchecked")
    private T getEarlySingletonInstance() throws Exception {
        Class<?>[] ifcs = getEarlySingletonInterfaces();
        if (ifcs == null) {
            throw new FactoryBeanNotInitializedException(
                    getClass().getName() + " does not support circular references");
        }
        if (this.earlySingletonInstance == null) {
            this.earlySingletonInstance = (T) Proxy.newProxyInstance(
                    this.beanClassLoader, ifcs, new EarlySingletonInvocationHandler());
        }
        return this.earlySingletonInstance;
    }

    @Nullable
    private T getSingletonInstance() throws IllegalStateException {
        Assert.state(this.initialized, "Singleton instance not initialized yet");
        return this.singletonInstance;
    }

    @Override
    public void destroy() throws Exception {
        if (isSingleton()) {
            destroyInstance(this.singletonInstance);
        }
    }

    @Override
    @Nullable
    public abstract Class<?> getObjectType();

    protected abstract T createInstance() throws Exception;

    @Nullable
    protected Class<?>[] getEarlySingletonInterfaces() {
        Class<?> type = getObjectType();
        return (type != null && type.isInterface() ? new Class<?>[] {type} : null);
    }

    protected void destroyInstance(@Nullable T instance) throws Exception {
    }

    private class EarlySingletonInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (ReflectionUtils.isEqualsMethod(method)) {
                // Only consider equal when proxies are identical.
                return (proxy == args[0]);
            }
            else if (ReflectionUtils.isHashCodeMethod(method)) {
                // Use hashCode of reference proxy.
                return System.identityHashCode(proxy);
            }
            else if (!initialized && ReflectionUtils.isToStringMethod(method)) {
                return "Early singleton proxy for interfaces " +
                        ObjectUtils.nullSafeToString(getEarlySingletonInterfaces());
            }
            try {
                return method.invoke(getSingletonInstance(), args);
            }
            catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

}
```

## 사용해보기

```java
public class Tool {

    private int id;

    // standard constructors, getters and setters
}

public class ToolFactory implements FactoryBean<Tool> {

    private int factoryId;
    private int toolId;

    @Override
    public Tool getObject() throws Exception {
        return new Tool(toolId);
    }

    @Override
    public Class<?> getObjectType() {
        return Tool.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    // standard setters and getters
}
```

위와 같이 테스트 대상인 Tool과 ToolFactory를 정의한다.

```java
@Configuration
public class FactoryBeanAppConfig {
 
    @Bean(name = "tool")
    public ToolFactory toolFactory() {
        ToolFactory factory = new ToolFactory();
        factory.setFactoryId(7070);
        factory.setToolId(2);
        return factory;
    }

    @Bean
    public Tool tool() throws Exception {
        return toolFactory().getObject();
    }
}
```

이를 기반으로 위와 같이 toolFactory와 tool을 빈으로 등록할 수 있다.

참고로 FactoryBean을 주입하고 싶다면 빈 이름에 prefix로 `&`를 붙여야한다.

```java
@SpringBootTest
public class FactoryBeanAppConfigTest {

    @Resource(name = "&tool")
    private ToolFactory toolFactory;

    @Test
    public void toolFactoryInjectTest() {
        assertThat(toolFactory).isNotNull();
    }
}
```

## 참고

What is FactoryBean?: [https://spring.io/blog/2011/08/09/what-s-a-factorybean](https://spring.io/blog/2011/08/09/what-s-a-factorybean)
Bealdung How to use the Spring FactoryBean?: [https://www.baeldung.com/spring-factorybean](https://www.baeldung.com/spring-factorybean)