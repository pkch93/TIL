# 2022.01.21 TIL - Builder Pattern

- [2022.01.21 TIL - Builder Pattern](#20220121-til---builder-pattern)
  - [점층적 생성자 패턴](#점층적-생성자-패턴)
  - [자바빈즈 패턴](#자바빈즈-패턴)
  - [빌더 패턴](#빌더-패턴)
  - [참고](#참고)

생성 매개변수가 많을때는 생성자로 대응하기 어려운 점이 있다. 다음과 같이 영양 정보를 표현하는 `NutritionFacts` 클래스가 있다고 가정한다.

```java
public class NutritionFacts {
    private final int servingSize; // 1회 제공량, 필수
    private final int servings; // 총 n회 제공량, 필수
    private final int calories; // 칼로리, 선택
    private final int fat; // 지방, 선택
    private final int sodium; // 나트륨, 선택
    private final int carbohydrate; // 탄수화물, 선택

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
```

1회 제공량 `servingSize`과 총 n회 제공량 `servings`을 제외하고는 나머지는 선택값이라고 가정한다. 이때 기본 값을 0으로 둔다.

그렇다면 `servingSize`와 `servings` 를 제외한 나머지 값을 받을 필요가 없다. 과거에는 이런 클래스 용으로 점층적 생성자 패턴을 즐겨 사용했다.

## 점층적 생성자 패턴

```java
public class NutritionFacts {
    private final int servingSize; // 1회 제공량, 필수
    private final int servings; // 총 n회 제공량, 필수
    private final int calories; // 칼로리, 선택
    private final int fat; // 지방, 선택
    private final int sodium; // 나트륨, 선택
    private final int carbohydrate; // 탄수화물, 선택

    public NutritionFacts(int servingSize, int servings) {
        this(servingSize, servings, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories) {
        this(servingSize, servings, calories, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat) {
        this(servingSize, servings, calories, fat, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium) {
        this(servingSize, servings, calories, fat, sodium, 0);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this(servingSize, servings, calories, fat, sodium, carbohydrate);
    }

    public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
}
```

위 예시와 같이 점층적 생성자 패턴은 코드가 너무 장황해지며 읽기 어려워진다.

## 자바빈즈 패턴

이런 문제 때문에 자바빈즈 패턴을 활용하기도 한다. 자바 빈은 기본 생성자와 getter, setter로 이뤄진 클래스로 자바빈즈 패턴에서는 기본 생성자와 setter를 사용한다.

```java
public class NutritionFacts {
    private final int servingSize; // 1회 제공량, 필수
    private final int servings; // 총 n회 제공량, 필수
    private final int calories; // 칼로리, 선택
    private final int fat; // 지방, 선택
    private final int sodium; // 나트륨, 선택
    private final int carbohydrate; // 탄수화물, 선택

    public NutritionFacts() {}

    // getter, setter...
}
```

```java
NutritionFacts nutritionFacts = new NutritionFacts();
nutritionFacts.setServingSize(240);
nutritionFacts.setServings(8);
nutritionFacts.setCalories(100);
nutritionFacts.setSodium(35);
nutritionFacts.setCarbohydrate(27);
```

자바빈즈패턴으로는 위와 같이 정의할 수 있다. 단, 자바빈즈 패턴은 객체 하나를 만드려면 메서드를 여러개 호출을 해야하고 객체가 완전히 생성되기 전까지 일관성이 무너지게된다. 즉, `NutritionFacts`가 기본생성자로 생성을 하므로 필수값인 `servingSize`와 `servings`가 할당되지 않은 상태의 `NutritionFacts`가 존재할 수 있다는 의미이다. 또한 자바빈즈 패턴은 클래스를 불변으로 만들 수 없으며 스레드 안전성을 얻기 위해서는 프로그래머가 추가 작업을 해야한다.

## 빌더 패턴

위 점층적 생성자 패턴과 자바빈즈 패턴의 장점을 취한 패턴이 있다. 바로 빌더 패턴이다.

```java
public class NutritionFacts {
    private final int servingSize; // 1회 제공량, 필수
    private final int servings; // 총 n회 제공량, 필수
    private final int calories; // 칼로리, 선택
    private final int fat; // 지방, 선택
    private final int sodium; // 나트륨, 선택
    private final int carbohydrate; // 탄수화물, 선택

    private NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.fat = fat;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
    }
    
    public static Builder builder(int servingSize, int servings) {
        return new Builder(servingSize, servings);
    }
    
    public static class Builder {
        private final int servingSize; // 1회 제공량, 필수
        private final int servings; // 총 n회 제공량, 필수
        private int calories = 0; // 칼로리, 선택
        private int fat = 0; // 지방, 선택
        private int sodium = 0; // 나트륨, 선택
        private int carbohydrate = 0; // 탄수화물, 선택
        
        Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }
        
        public Builder calories(int calories) {
            this.calories = calories;
            return this;
        }
        
        public Builder fat(int fat) {
            this.fat = fat;
            return this;
        }
        
        public Builder sodium(int sodium) {
            this.sodium = sodium;
            return this;
        }
        
        public Builder carbohydrate(int carbohydrate) {
            this.carbohydrate = carbohydrate;
            return this;
        }
        
        public NutritionFacts build() {
            return new NutritionFacts(servingSize, servings, calories, fat, sodium, carbohydrate);
        }
    }
}
```

위와 같이 객체를 생성하는 빌더 클래스를 새로 정의하여 본 클래스의 장황함을 줄이고 클라이언트 측에서는 생성에 필요한 값만 할당하고 사용하지 않는 값은 기본값이 할당하도록 만들 수 있다.

## 참고

[이펙티브자바 3/e 2장. 생성자에 매개변수가 많다면 빌더를 고려하자](http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9788966262281)

[쉽게 배워 바로 써먹는 디자인 패턴 5장. 빌더 패턴](http://www.kyobobook.co.kr/product/detailViewKor.laf?ejkGb=KOR&mallGb=KOR&barcode=9791162243404&orderClick=LAG&Kc=)