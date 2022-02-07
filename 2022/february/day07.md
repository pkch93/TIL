# 2022.02.07 TIL - Iterator Pattern

- [2022.02.07 TIL - Iterator Pattern](#20220207-til---iterator-pattern)
  - [집합체](#집합체)
  - [반복자](#반복자)
    - [반복자와 집합체의 관계](#반복자와-집합체의-관계)

반복자 패턴이란 내부 구조를 노출하지 않고 집합체를 통해 원소 객체에 순차적으로 접근할 수 있는 방법을 제공하는 패턴이다.

## 집합체

반복자 패턴을 이해하기 위해서는 **집합체와** **집합 개념**을 이해해야한다. 집합은 여러 개의 데이터를 하나로 묶은 것이다. 묶인 데이터 하나를 요소 `Element`라고 하며 반복자 패턴에서는 객체를 하나의 데이터 요소로 처리한다. 그리고 객체로 묶인 요소들을 관리하고 실행한다.

반복자는 객체의 효율적인 집합 관리를 위해 별도의 집합체 `Aggregate`를 가진다. 집합체는 단순 배열과는 달리 복수의 객체를 가진 복합 객체이다. 집합체는 다른 말로 컬렉션 `Collection`이라 한다. 반복자 패턴은 배열을 사용하지 않고 별도의 컬렉션 객체를 사용한다. **컬렉션의 메서드를 활용하여 새로운 객체를 추가하거나 삭제하는 등의 행위를 하도록 만들기 위함이다.**

Java와 같은 언어에서는 컬렉션의 크기를 제한하는 경우가 있다. 이때, 컬렉션을 응용하면 보다 더 유연하게 확장이 가능하다.

```java
public interface Collection<E> extends Iterable<E> {

    int size();

    boolean isEmpty();

    boolean contains(Object o);

    Iterator<E> iterator();

    Object[] toArray();

    <T> T[] toArray(T[] a);

    default <T> T[] toArray(IntFunction<T[]> generator) {
        return toArray(generator.apply(0));
    }

    boolean add(E e);

    boolean remove(Object o);

    boolean containsAll(Collection<?> c);

    boolean addAll(Collection<? extends E> c);

    boolean removeAll(Collection<?> c);

    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    boolean retainAll(Collection<?> c);

    void clear();

    boolean equals(Object o);

    int hashCode();

    @Override
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }

    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
```

> Java에서는 집합체 `컬렉션`을 위와 같이 정의하고 있다. 

위와 같이 컬렉션에서는 컬렉션 내의 요소들을 추가, 삭제, 확인하는 등의 동작을 하는 메서드로 구성되어있다. 즉, 컬렉션 내의 요소들을 다루기 위한 책임이 컬렉션에게 있다.

## 반복자

반복자는 **묶여 있는 객체들에 순차적으로 접근하여 처리할 수 있는 로직**을 제공한다. 또한 컬렉션 안에 있는 객체를 순환 반복하기 위해 외부 반복자를 분리하여 설계한다. 반복자를 설계할 때는 반복 개념을 일반화하여 다형성을 추가하는 것이 중요하다.

```java
public interface Iterator<E> {

    boolean hasNext();

    E next();

    default void remove() {
        throw new UnsupportedOperationException("remove");
    }

    default void forEachRemaining(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (hasNext())
            action.accept(next());
    }
}
```

> Java에서는 위와 같이 Iterator를 제공하고 있다.
> 

컬렉션과는 달리 반복자는 객체들을 순차적으로 접근하는 로직에 초점을 맞춘다. 때문에 `hasNext`와 `next`가 존재한다.

### 반복자와 집합체의 관계

반복자 객체의 생성은 집합체에 의해 이뤄진다. 때문에 반복자는 집합체와 의존 관계를 가진다.

```java
public interface Collection<E> extends Iterable<E> {

    // ...

    Iterator<E> iterator();

    // ...
}
```

앞서 `Collection` 인터페이스에서 `Iterator`를 반환받는 메서드가 존재함을 확인했다. 

> 복합체는 주로 외부 반복자를 사용하며 외부 반복자를 사용하기 위해서 `iterator` 메서드를 제공한다.

반복자 객체는 집합 객체를 의존성으로 주입받는다. 반복자 객체는 컬렉션이 가진 객체를 순환하는 제어부이고 객체를 하나씩 읽어 처리한다.

때문에 집합체와 반복자는 양방향성의 강력한 결합 관계를 가진다.