# 2022.02.15 TIL - Observer Pattern

- [2022.02.15 TIL - Observer Pattern](#20220215-til---observer-pattern)
  - [고려할 점](#고려할-점)
  - [예시](#예시)
  - [참고](#참고)

![출처: [https://johngrib.github.io/wiki/observer-pattern](https://johngrib.github.io/wiki/observer-pattern)](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b0513fe4-7aff-47c5-bed7-0d762686fb1b/Untitled.png)

Observer Pattern은 객체 사이의 일대다 의존관계를 정의해두어 어떤 객체의 상태가 변할 때 그 객체에 의존성을 가진 다른 객체들이 변화를 통지받고 자동으로 갱신할 수 있도록 만들어준다.

Subject에 여러 Observer를 등록`attach`해두고 변화됨을 통지`nofify`하면 등록된 Observer를 순회하면서 각 Observer를 update하는 패턴이다.

이때 Subject와 Observer 간의 느슨한 결합을 갖도록 하는 것이 중요하다. 즉, Observer 등록 순서 등에 특정 로직이 의존하지 않도록 해야한다.

Observer Pattern은 스스로 감시하고 변화하는 것보다는 변화를 통지받고 변화를 알게되는 성격이 강하므로 Publish-Subscribe Pattern이라고도 불린다.

```java
public interface Subject<T> {
    void addObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);
    void notifyObservers();
}

public interface Observer<T> {
    void update(Subject<T> subject, T t);
}
```

## 고려할 점

- Observer는 상태를 갖지 않아도 된다.

    상태는 Subject의 담당이기 때문에 Subject와 Observer의 일대다 관계가 성립할 수 있다.

- notify는 누가 호출?

    GoF에서는 두 가지 방법 중 하나를 선택하라고 한다.

    1. Subject에서 변경이 발생했을시 변경을 저장하는 메서드에서 notify 호출
    2. 사용자가 적절한 시기에 notify 호출

- update 메서드의 인자

    보통 Subject를 인자로 넘겨주어 Subject에서 필요한 값을 얻을 수 있게 하지만 이는 필수조건은 아니다. 상황에 따라 필요한 인자만을 넘겨 처리할 수 있다.

- Observer의 행위가 Subject에 영향을 주는 경우

    Observer의 행위가 Subject에 영향을 준다면 무한루프가 발생할 수 있다.

    1. Subject에서 notify 호출
    2. Observer의 update 호출
    3. update 실행 도중 Subject에 영향을 줌
    4. 1번 반복

위 방식으로 무한루프가 발생할 수 있다. 이런 상황을 회피하기 위해 Observer에 플래그 변수를 하나 두어 update 중인지 아닌지를 관리하도록 만들 수 있다.

## 예시

전세계의 주식거래소에서 주식정보를 받아 보여준다고 가정한다.
이때 Subject `Publisher`는 다음과 같이 구현될 수 있다.

```java
public class StockDataSubject implements Subject<StockData> {
    private final List<Observer<StockData>> stockExchanges;
    private StockData stockData;

        public StockDataSubject() {
        this(new ArrayList<>());
    }

    public StockDataSubject(List<Observer<StockData>> stockExchanges) {
        this.stockExchanges = stockExchanges;
    }

    @Override
    public void addObserver(Observer<StockData> observer) {
        stockExchanges.add(observer);
    }

    @Override
    public void removeObserver(Observer<StockData> observer) {
        stockExchanges.remove(observer);
    }

    @Override
    public void notifyObservers() {
        stockExchanges.forEach(
                stockExchange -> stockExchange.update(this, stockData)
        );
    }

    @Override
    public void update(StockData stockData) {
        this.stockData = stockData;
        notifyObservers();
    }
}
```

위와 같이 주식시장의 데이터를 받아 관찰 `구독`하고 있는 `Observer`에 데이터를 전달하는 `StockDataSubject`를 구현할 수 있다. 참고로 위 `StockDataSubject` 가 가지는 `Observer`는 주식시장 `StockExchange`일 것이다.

> 참고로 위 Subject에는 update가 추가되었다. update로 값을 업데이트했을때 Observer에 값을 통지해주기 위함이다.

```java
public class KoreaStockExchange implements Observer<StockData> {
    private double kospi;
    private double kosdaq;

    @Override
    public void update(Subject<StockData> subject, StockData stockData) {
        this.kospi = stockData.getKospi();
        this.kosdaq = stockData.getKosdaq();

        LocalDateTime now = LocalDateTime.now();
        String currentTimeDisplay = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:dd:ss"));

        System.out.printf("%s: 코스피 - %f / 코스닥 - %f%n", currentTimeDisplay, this.kospi, this.kosdaq);
    }
}

public class AmericaStockExchange implements Observer<StockData> {
    private double kospi;
    private double kosdaq;

    @Override
    public void update(Subject<StockData> subject, StockData stockData) {
        this.kospi = stockData.getKospi();
        this.kosdaq = stockData.getKosdaq();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        String currentTimeDisplay = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:dd:ss"));

        System.out.printf("%s: kospi - %.1f / kosdaq - %.1f%n", currentTimeDisplay, this.kospi, this.kosdaq);
    }
}
```

`StockDataSubject`로부터 값을 받을 `Observer`를 위와 같이 `KoreaStockExchange`, `AmericaStockExchange`를 선언할 수 있다. 각 `StockExchange`들은 `StockDataSubject`가 값을 `update`할 때마다 데이터를 전달받는다.

```java
Observer<StockData> americaStockExchange = new AmericaStockExchange();
Observer<StockData> koreaStockExchange = new KoreaStockExchange();

Subject<StockData> stockDataSubject = new StockDataSubject(Arrays.asList(
        americaStockExchange, koreaStockExchange
));

stockDataSubject.update(new StockData(3000.0, 928.0));
Thread.sleep(1000);
stockDataSubject.update(new StockData(3010.0, 930.0));
Thread.sleep(1000);
stockDataSubject.update(new StockData(3012.0, 910.0));
Thread.sleep(1000);
stockDataSubject.update(new StockData(3015.0, 915.0));
Thread.sleep(1000);
stockDataSubject.update(new StockData(3010.0, 910.0));
```

따라서 위와 같이 `stockDataSubject`에 값을 업데이트할 때마다 다음과 같이 `americaStockExchange`나 `koreaStockExchange`에서도 값을 통지받을 수 있기 때문에 자동으로 값을 처리할 수 있게된다.

## 참고

Observer Pattern: [https://johngrib.github.io/wiki/observer-pattern](https://johngrib.github.io/wiki/observer-pattern)