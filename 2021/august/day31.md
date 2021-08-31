# 2021.08.31 TIL - Spring ObjectProvider

ObjectProvider는 ObjectFactory의 기능 중 DL `Dependency Lookup` 기능에 좀 더 편의기능을 추가하여 제공하는 인터페이스이다.
ObjectProvider는 Spring 4.3부터 등장한다.

```java
public interface ObjectProvider<T> extends ObjectFactory<T>, Iterable<T> {

    T getObject(Object... args) throws BeansException;

    @Nullable
    T getIfAvailable() throws BeansException;

    default T getIfAvailable(Supplier<T> defaultSupplier) throws BeansException {
        T dependency = getIfAvailable();
        return (dependency != null ? dependency : defaultSupplier.get());
    }

    default void ifAvailable(Consumer<T> dependencyConsumer) throws BeansException {
        T dependency = getIfAvailable();
        if (dependency != null) {
            dependencyConsumer.accept(dependency);
        }
    }

    @Nullable
    T getIfUnique() throws BeansException;

    default T getIfUnique(Supplier<T> defaultSupplier) throws BeansException {
        T dependency = getIfUnique();
        return (dependency != null ? dependency : defaultSupplier.get());
    }

    default void ifUnique(Consumer<T> dependencyConsumer) throws BeansException {
        T dependency = getIfUnique();
        if (dependency != null) {
            dependencyConsumer.accept(dependency);
        }
    }

    @Override
    default Iterator<T> iterator() {
        return stream().iterator();
    }

    default Stream<T> stream() {
        throw new UnsupportedOperationException("Multi element access not supported");
    }

    default Stream<T> orderedStream() {
        throw new UnsupportedOperationException("Ordered element access not supported");
    }

    }
```

Unique, Available 여부에 따라 빈을 사용할지를 결정할 수 있도록 기능을 제공한다. 또한 Iterable을 확장하고 있기 때문에 for문 등으로 반복이 가능하며 stream도 지원한다.

## 참고

프로토타입 빈을 사용시 생기는 문제: [https://chung-develop.tistory.com/63](https://chung-develop.tistory.com/63)

프로토타입 빈을 사용시 생기는 문제2: [](https://chung-develop.tistory.com/63)[https://atoz-develop.tistory.com/entry/Spring-빈의-Scope-싱글톤과-프로토타입](https://atoz-develop.tistory.com/entry/Spring-%EB%B9%88%EC%9D%98-Scope-%EC%8B%B1%EA%B8%80%ED%86%A4%EA%B3%BC-%ED%94%84%EB%A1%9C%ED%86%A0%ED%83%80%EC%9E%85)