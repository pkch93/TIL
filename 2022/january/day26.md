# 2022.01.26 TIL - Adapter 패턴

- [2022.01.26 TIL - Adapter 패턴](#20220126-til---adapter-패턴)
  - [학점 계산기 예시](#학점-계산기-예시)
  - [클래스 어댑터와 객체 어댑터](#클래스-어댑터와-객체-어댑터)

어댑터 패턴은 코드를 재사용하기 위한 인터페이스를 처리하고 인터페이스를 활용하여 보정 코드를 작성하는 패턴이다.

## 학점 계산기 예시

만약 어느 대학교 A에서는 4.5를 학점의 만점으로 둔다고 가정한다. 이때 학점을 등급으로 환산하는 GradeCalculator가 있다고 가정한다.

```java
public class GradeCalculator {

    public Grade calculate(double totalCredit) {
        if (totalCredit >= 4.5) {
            return Grade.A_PLUS;
        } else if (totalCredit >= 4) {
            return Grade.A;
        } else if (totalCredit >= 3.5) {
            return Grade.B_PLUS;
        } else if (totalCredit >= 3) {
            return Grade.B;
        } else if (totalCredit >= 2.5) {
            return Grade.C_PLUS;
        } else if (totalCredit >= 2) {
            return Grade.C;
        } else if (totalCredit >= 1.5) {
            return Grade.D_PLUS;
        } else if (totalCredit >= 1.0) {
            return Grade.D;
        }
        return Grade.F;
    }
}
```

Grade는 다음과 같다.

```java
public enum Grade {
    A_PLUS("A+"), A("A"),
    B_PLUS("B+"), B("B"),
    C_PLUS("C+"), C("C"),
    D_PLUS("D+"), D("D"),
    F("F");

    private String grade;

    Grade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }
}
```

이때 만약 4.3 학점을 만점으로 매기는 B 대학교에서 위 계산기를 통해 학점을 계산하려고 한다. 이를 위해서 Adapter를 구성한다.

```java
public class BUniversityGradeCalculateAdapter {
    private static final double CONVERSION_RATE = 4.5 / 4.3;

    private final GradeCalculator gradeCalculator;

    public BUniversityGradeCalculateAdapter(GradeCalculator gradeCalculator) {
        this.gradeCalculator = gradeCalculator;
    }

    public Grade calculate(double totalCredit) {
        double convertedTotalCredit = CONVERSION_RATE * totalCredit;
        return gradeCalculator.calculate(convertedTotalCredit);
    }
}
```

위와 같이 4.3 학점을 만점으로하는 `BUniversityGradeCalculateAdapter`를 구현하여 기존에 존재하는 `GradeCalculator` 로직을 재활용할 수 있다.

## 클래스 어댑터와 객체 어댑터

어댑터 패턴의 구현 방식으로는 상속을 활용한 클래스 어댑터와 조합을 활용한 객체 어댑터가 있다. 앞서 살펴본 `BUniversityGradeCalculateAdapter`은 조합을 활용한 객체 어댑터 방식이다.

상속을 활용한다면 `GradeCalculator`를 상속하는 방법으로 구현해야할 것이다.

```java
public class GradeCalculateClassAdapter extends GradeCalculator {
    private static final double CONVERSION_RATE = 4.5 / 4.3;

    @Override
    public Grade calculate(double totalCredit) {
        double convertedTotalCredit = totalCredit * CONVERSION_RATE;
        return super.calculate(totalCredit);
    }
}
```

위와 같이 `GradeCalculator`를 상속하여 재활용하는 방법이다.

다만, 기존 구현을 상속하는 것은 상위 클래스와 하위 클래스 간의 강한 결합을 형성한다. 그리고 만약에 여러 클래스가 필요한 경우에는 계층적으로 클래스가 상속을 하게 된다.

만약 `GradeCalculateClassAdapter` 를 활용해서 5.0이 만점인 대학교의 학점을 4.3으로 환산한다고 하면 `GradeCalculateClassAdapter`를 상속하여 구현하게 된다. 이렇게 추가적으로 다른 기능을 필요로 한다면 계층적 확장을 통해 구현해야한다.

반면 조합 방식인 객체 어댑터 방식은 기존 클래스를 감싼 새로운 클래스를 생성한다. 이를 통해 느슨한 결합을 형성하여 보다 많은 유연성을 확보할 수 있다.