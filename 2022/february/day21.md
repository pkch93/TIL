# 2022.02.21 TIL - Memento Pattern

- [2022.02.21 TIL - Memento Pattern](#20220221-til---memento-pattern)
  - [Memento Pattern 사용하기](#memento-pattern-사용하기)

메멘토 패턴은 객체의 상태를 저장하여 이전 상태로 복구하는 패턴이다.

객체는 고유한 상태를 가지고 있으며 객체의 상태는 실행 중에도 끊임없이 변경될 수 있다. 이렇게 끊임없이 변경되는 상태를 추적할 수 있도록 다른 객체에 저장했다가 다시 복구할 수 있도록 하는 패턴이 바로 메멘토 패턴이다.

단, 객체 복원 시에 캡슐화를 위반하지 않으면서 복원할 수 있어야한다. 메멘토 패턴은 캡슐화에 영향을 주지 않으면서도 안전하게 객체를 복원할 수 있는 방안을 제시한다.

## Memento Pattern 사용하기

현재 업데이트 버전과 과거 업데이트 버전으로 돌아갈 수 있도록 만드는 기능을 메멘토 패턴으로 구현해본다.

```java
/**
 * Originator of Memento Pattern
 */
public class CurrentUpdate {

    private int version;

    private CurrentUpdate(int version) {
        this.version = version;
    }

    public static CurrentUpdate of(int version) {
        return new CurrentUpdate(version);
    }

    void updateVersionUp() {
        this.version += 1;
    }

    void undoUpdate(Update update) {
        this.version = update.getVersion();
    }
}
```

먼저 현재 업데이트 정보 원본을 가지는 Originator로 CurrentUpdate를 구현한다.

그리고 Mememto를 담당하는 Update를 다음과 같이 정의한다.

```java
public class Update {
    private int version;

    Update(int version) {
        this.version = version;
    }

    int getVersion() {
        return version;
    }
}
```

특이점은 외부에 public으로 공개하지 않고 package-level로 정의하였다는 점이다.

마지막으로 CareTaker 역할을 담당하는 UpdateHistories를 구현한다.

```java
public class UpdateHistories {

    private final Stack<Update> histories;

    UpdateHistories() {
        this.histories = new Stack<>();
    }

    void addUpdateVersionHistory(int version) {
        histories.add(new Update(version));
    }

    void restorePreviousUpdateVersion(CurrentUpdate currentUpdate) {
        Update prevUpdate = histories.pop();
        currentUpdate.undoUpdate(prevUpdate);
    }
}
```

UpdateHistories도 마찬가지로 package-level로 구현되었다. 이를 활용해서 다음과 같이 CurrentUpdate의 버전을 올리고 복원하는 Updater를 구현할 수 있다.

```java
public class Updater {
    private final CurrentUpdate currentUpdate;
    private final UpdateHistories updateHistories;

    public Updater(CurrentUpdate currentUpdate,
                   UpdateHistories updateHistories) {
        this.currentUpdate = currentUpdate;
        this.updateHistories = updateHistories;
    }

    public void update() {
        int nextVersion = currentUpdate.updateVersionUp();
        updateHistories.addUpdateVersionHistory(nextVersion);
    }

    public void restore() {
        updateHistories.restorePreviousUpdateVersion(currentUpdate);
    }
}
```

이를 다음과 같이 사용해볼 수 있다.

```java
public class MementoClient {
    public static void main(String[] args) {
        int initVersion = 1;
        Updater updater = new Updater(CurrentUpdate.of(initVersion), new UpdateHistories(initVersion));

        updater.update();
        updater.restore();
    }
}
```