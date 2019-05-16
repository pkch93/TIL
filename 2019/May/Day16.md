# 19.05.16 Thursday

## 1. java

- Matcher / Pattern

`Matcher`와 `Pattern` class는 java에서 정규표현식을 사용할 수 있도록 도와주는 class들이다.

Pattern을 통해 찾고자 하는 패턴의 정규표현식을 `setting`한다.

아래는 전화번호를 찾는 정규표현식 Pettern을 만드는 코드이다.
```java
Pettern phonePettern = Pettern.complie("\\d{3}-?\\d{4}-?\\d{4}");
```

위와 같이 Pettern의 static 메서드인 complie을 사용하면 인자로 전달되는 정규표현식의 Pettern 객체가 생성된다.

이렇게 생성된 Pettern으로 문자열을 검사할 수 있다.

`010-1234-5678입니다.`라는 문자열이 있을 때 위 Pettern을 적용하여 010-1234-5678을 찾을 수 있다. 이때, Matcher 객체가 등장한다.

```java
String str = "010-1234-5678입니다.";
Matcher phoneMatcher = phonePettern.matcher(str);
```

위와 같이 Pettern 객체의 matcher 메서드를 사용하면 해당 문자열을 Pettern의 정규표현식으로 검증한 Matcher객체를 얻을 수 있다.

이때 Matcher로 패턴에 일치하는 값들을 찾는 경우, `find()`메서드를 사용해야한다. 패턴이 일치하는 경우 true를 반환하고 매치되는 위치로 이동하는 역할을 한다.

만약 어떤 문자열에서 정규표현식에 부합하는 문자열이 다수 있다면 find() 메서드를 반복 실행하여 해당 문자열로 이동할 수 있다.

`find()`하여 매치되는 문자열을 찾은 뒤에는 `start()`와 `end()`를 사용할 수 있다.
start는 매치되는 문자열의 시작위치, end는 끝위치를 반환한다.

정규표현식에는 괄호`()`를 통해 패턴의 그룹을 표현할 수 있다. `Matcher`에서는 `group()` 메서드를 사용하여 정규표현식에 매치되는 그룹을 반환할 수 있다. 단, group도 find와 마찬가지로 커서를 이동하는 역할을 한다.

> group(0)은 정규표현식 전체를 가리키는 그룹이다. 0번 그룹에 대해서는 groupCount에서 count하지 않는다.
>
> `groupCount()`메서드는 Pettern에서 그룹 지어진 정규식과 일치하는 문자열의 수가 아닌 정규식에서 그룹지어진 패턴의 전체 갯수를 return

마지막으로 Matcher에는 `matches()`메서드를 지원한다. 이는 문자열 전체와 정규표현식이 일치하는지 검사하는 메서드이다.