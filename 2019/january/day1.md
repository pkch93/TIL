# 2019 01.02 Wendesday

1. Jupyter Notebook

```text
pip install jupyter
# Jupyter Notebook 설치

jupyter notebook
# Jupyter Notebook 실행
```

* 단축키
  * enter

    해당 셀 편집 모드

  * esc

    해당 셀 명령 모드

  * a

    위쪽으로 셀을 추가

  * b

    아래로 셀을 추가

  * d 더블클릭

    해당 셀 삭제

  * ctrl + enter

    해당 셀을 실행

  * shift + enter

    해당 셀을 실행하고 아래로 셀 커서를 이동 \(단, 아래 셀이 없다면 새로 추가\)

  * alt + enter

    해당 셀을 실행하고 아래에 셀을 추가
* basic python
* 식별자

파이썬에서 식별자는 변수, 함수, 모듈, 클래스 등을 식별하는데 사용되는 이름이다.

파이썬에서는 다음의 조건을 가지는 이름을 식별자로 사용할 수 없다.

1. 식별자의 이름은 `영문알파벳`, `_`, `숫자`로 구성
2. 첫 글자에 숫자는 올 수 없다.
3. 대소문자를 구별
4. 내장함수나 모듈 등의 이름으로도 만들면 안된다.

   이 경우는 정해진 이름의 함수 / 변수를 이름으로 만든다면 함수 호출이 되지 않는다.

5. 아래의 예약어는 사용 불가

```python
import keyword
print(keyword.kwlist)
```

위 명령어를 실행하면

```text
['False', 'None', 'True', 'and', 'as', 'assert', 'break', 'class', 'continue', 'def', 'del', 'elif', 'else', 'except', 'finally', 'for', 'from', 'global', 'if', 'import', 'in', 'is', 'lambda', 'nonlocal', 'not', 'or', 'pass', 'raise', 'return', 'try', 'while', 'with', 'yield']
```

다음과 같은 파이썬 예약어 목록이 나온다. 위 예약어는 변수명으로 사용할 수 없다.

### 기초 문법

* 인코딩 선언

파이썬에서는 인코딩 선언을 따로 하지 않아도 UTF-8로 선언된다. 만약 인코딩 선언을 따로 하려면 아래와 같이 선언한다. 주석으로 보이지만 파이썬 Parser에 의해 읽혀진다.

```text
    # -*- coding: <encoding-name> -*-
```

* 주석

파이썬의 주석은 기본적으로 `#`을 사용한다. `#`은 파이썬의 한 줄 주석이다.

두 줄 이상의 주석을 달기 위해서는 `""" 주석내용 """`을 사용한다.

```python
# 이 줄은 주석입니다.
"""
이것은 여러줄 주석입니다.
"""
```

`""" """`는 **docstring**이라고 부른다. 이유는 함수나 클래스에 docstring으로 설명을 작성하면 함수와 클래스의 `__doc__`으로 설명을 볼 수 있다.

```python
def mysum(a, b):
    """
    이것은 덧셈함수입니다.
    """
    return a + b

mysum.__doc__ # docstring의 값으로 설명을 볼 수 있다.
```

* 코드라인

기본적으로 파이썬은 `;(세미콜론)`을 작성하지 않는다. 다만, 한 줄로 코드를 작성할 경우 세미콜론으로 표기할 수 있다.

```python
print("Hello") print("World!") # error
print("Hello"); print("World!") # correct
```

또한, 줄이 길어 여러 줄로 작성할 때는 역슬래시`\`로 표기할 수 있다.

```python
i = 0
if i \
== 0:
print(True)

# True
```

다만, 세미콜론과 역슬래쉬는 `PEP`에서 추천하지 않는 표기법으로 사용을 자제하는 것이 좋다.

리스트`[]`, 튜플`()`, 딕셔너리`{}`는 역슬래시`\` 없이도 여러줄 작성이 가능하다.

## ※ is 연산자

> int 값이 짧은 경우는 같은 메모리를 사용하기 때문에 is 연산을 하면 True가 나온다. \(캐싱\)
>
> 단, 값이 큰 경우는 다른 메모리를 사용하므로 False가 된다.
>
> String도 마찬가지이다. 짧은 문자열이면 같은 메모리를 사용하지만, 긴 문자열은 다른 메모리를 사용한다.
>
> 참고로 def 함수 내에서는 같은 메모리를 사용한다.

```python
a = 5
b = 5

a is b # True

a = 100000000
b = 100000000

a is b # False (다른 메모리 참조)

a = "hi"
b = "hi"

a is b # True

a = "안녕하세요 감사해요 잘있어요 다시만나요"
b = "안녕하세요 감사해요 잘있어요 다시만나요"

a is b # False (한글은 바이트를 많이 소요, 메모리를 많이 필요로 하므로 짧은 한글이라도 메모리를 많이 차지할 수 있다.)
```

