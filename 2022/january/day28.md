# 2022.01.28 TIL - Composite Pattern

- [2022.01.28 TIL - Composite Pattern](#20220128-til---composite-pattern)
  - [투명성](#투명성)
  - [파일시스템 예시](#파일시스템-예시)

복합체 패턴은 객체들의 관계를 트리 구조로 구성하여 부분-전체를 표현하는 패턴이다. 즉, 하나의 객체가 다른 객체를 포함하는 복합 객체의 구조적 특성을 활용한 패턴이 바로 복합체 패턴이다.

위와 같이 복합체는 트리 구조로 객체 그룹화를 한다. 그룹화를 통해 더 큰 집단의 객체로 확장하는데 이를 집단화라고한다. 이런 집단화의 특징은 객체 결합 모양이 트리형태로 확장된다. 트리에서 마지막을 잎 `Leaf`라고 하며 중간 객체를 노드 `Node` 라고 한다.

이런 트리 형태는 재귀적 구조의 데이터를 표현할 때 자주 사용한다. 파일 시스템, 조직도 등을 표현할 때 주로 사용된다. 즉, 하나의 구조 안에 또 다른 구조를 가진 모델을 설계할 때 사용된다.

## 투명성

복합체 패턴은 Component 인터페이스를 통해서 Component 객체와 Leaf 객체를 따로 구분하지 않고 동일한 동작으로 처리한다. 이를 투명성이라고 하며 복합체 패턴의 가장 큰 특징 중에 하나이다.

따라서 투명성을 구현하기 위해 클래스를 일반화하는 작업이 필요하다. 즉, 복합 객체들의 공통 부분을 추상화하는 것이 필요하다. 일반화된 클래스에서 동일한 메서드의 호출을 보장하기 위해서는 똑같은 인터페이스를 사용해야한다. 인터페이스를 구분하게 된다면 투명성이 결여되기 때문에 투명성이 보장된 클래스에서는 단일 클래스와 복합 클래스를 구별하지 않고도 동일한 방법으로 객체에 접근할 수 있어야한다.

복합체로 구현하기 위해서는 투명성을 부여해야한다. 단, 이를 위해서 특정 객체 설계시 불필요한 메서드가 생길 수 있다. 서로 다른 객체의 투명성으로 인해서 하나의 객체에 2개 이상의 책임이 부여될 수 있기 때문이다. 즉, 객체지향설계의 단일책임원칙 `SRP`에 위배된다.

단일책임원칙에 위배됨에도 설계하는 것은 복합체의 특징인 **투명성**을 보장하기 위함이다. 때문에 복합체 패턴에 적용하기에 적합한 상황인지를 잘 파악해야한다.

## 파일시스템 예시

간단한 파일시스템으로 복합체 `Composite`형태의 폴더 `Folder`와 잎 `Leaf` 형태의 파일 `File`을 다음과 같이 구현해본다. 루트 폴더에서 하위에 가진 복합 객체들의 title을 모두 조회하는 것을 목표로 한다.

```java
public abstract class Resource {
    protected final String title;

    public Resource(String title) {
        this.title = title;
    }

    abstract public void printTitle();
}
```

따라서 `Folder`와 `File` 모두 일반화 할 수 있는 추상클래스 Resource를 위와 같이 구현한다. 그리고 Resource를 기반으로 `Folder`와 `File`을 다음과 같이 구현한다.

```java
public class Folder extends Resource {
    private final List<Resource> resources;

    private Folder(String title, List<Resource> resources) {
        super(title);
        this.resources = resources;
    }

    public static Folder createInitFolder(String title) {
        return new Folder(title, new ArrayList<>());
    }

    @Override
    public void printTitle() {
        System.out.println(this.title);
        resources.forEach(Resource::printTitle);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }
}

public class File extends Resource {
    public File(String title) {
        super(title);
    }

    @Override
    public void printTitle() {
        System.out.println(this.title);
    }
}
```

단, 위 구현에서는 Folder 클래스에만 `addResource`가 구현되어 있다. 즉, 리소스를 추가하기 위해서는 일반화된 `Resource`가 아닌 `Folder` 클래스의 객체라는 것을 클라이언트에서 알아야한다.

```java
Folder folder = Folder.createInitFolder("folder");
folder.addResource(new File("file1"));
folder.addResource(new File("file2"));
folder.addResource(new File("file3"));

Folder folder2 = Folder.createInitFolder("folder2");
folder2.addResource(new File("file4"));
folder2.addResource(new File("file5"));

folder.addResource(folder2);
```

즉, 위와 같은 형태로 `folder1`, `folder2`를 일반화 형태인 `Resource`가 아닌 `Folder`로 정의해야한다. 이는 복합체의 특징인 투명성을 보장하지 못한다.

따라서 다음과 같이 `addResource`를 상위 클래스인 `Resource`에 추가한다.

```java
public abstract class Resource {
    protected final String title;

    public Resource(String title) {
        this.title = title;
    }

    abstract public void printTitle();

    abstract public void addResource(Resource resource);
}

public class Folder extends Resource {
    private final List<Resource> resources;

    private Folder(String title, List<Resource> resources) {
        super(title);
        this.resources = resources;
    }

    public static Folder createInitFolder(String title) {
        return new Folder(title, new ArrayList<>());
    }

    @Override
    public void printTitle() {
        System.out.println(this.title);
        resources.forEach(Resource::printTitle);
    }

    @Override
    public void addResource(Resource resource) {
        resources.add(resource);
    }
}

public class File extends Resource {
    public File(String title) {
        super(title);
    }

    @Override
    public void printTitle() {
        System.out.println(this.title);
    }

    @Override
    public void addResource(Resource resource) {
        throw new UnsupportedOperationException();
    }
}
```

위와 같이 `Resource`에 `addResource`를 추가하고 `File` 클래스에서도 `addResource` 메서드를 재정의한다. 위와 같이 투명성을 보장한다면 클라이언트에서는 다음과 같은 형태로 `Folder` 타입을 알지 못하더라도 사용이 가능하다.

```java
Resource folder = Folder.createInitFolder("folder");
folder.addResource(new File("file1"));
folder.addResource(new File("file2"));
folder.addResource(new File("file3"));

Resource folder2 = Folder.createInitFolder("folder2");
folder2.addResource(new File("file4"));
folder2.addResource(new File("file5"));

folder.addResource(folder2);
```

간단한 파일 시스템의 예시처럼 한 객체에서 사용하지 않는 메서드가 있더라도 투명성을 위해서 일반화가 필요하다. 이런 객체지향설계의 원칙을 위반하는 단점이 있음에도 하나의 구조체가 다른 구조체를 가지면서 트리 형태로 구성할 가능성이 있는 경우에는 사용해봄직 해보인다.