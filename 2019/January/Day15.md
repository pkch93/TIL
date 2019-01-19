# 2019 01.16 Wednesday 

## 1. OOP with Python

```python
class Person:                      #=> 클래스 정의(선언) : 클래스 객체 생성
    name = '홍길동'                  #=> 멤버 변수(데이터 어트리뷰트)
    def greeting(self):            #=> 멤버 메서드(메서드)
        print(f'{self.name}')
iu = Person()       # 인스턴스 객체 생성
daniel = Person()   # 인스턴스 객체 생성
iu.name             # 데이터 어트리뷰트 호출
iu.greeting()       # 메서드 호출
```

- static method / class method

    메서드 호출을 인스턴스가 아닌 클래스가 할 수 있도록 구성할 수 있습니다.

    이때 활용되는게 정적 메서드 혹은 클래스 메서드입니다.

    정적 메소드는 객체가 전달되지 않은 형태이며, 클래스 메서드는 인자로 클래스를 넘겨준다.

    보통 class method는 클래스 변수에 접근하여 로직을 구성할 때 사용한다.

    반면 static method는 해당 클래스의 인스턴스와는 상관없이 로직을 구성할 때 사용한다.

    ```python
    class Dog:
    num_of_dogs = 0
    
    def __init__(self, name, age):
        self.name = name
        self.age = age
        Dog.num_of_dogs += 1
    
    def bark(self):
        print("왈왈왈")
    
    @staticmethod
    def dog_info():
        print("this is 개")
        
    @classmethod
    def count(cls):
        print(cls.num_of_dogs)
    ```

    ```python
    class Calculator:
    @staticmethod
    def add(*args):
        result = 0
        for arg in args:
            result += arg
        return result
    @staticmethod
    def sub(a, b):
        return a - b
    @staticmethod
    def mul(a, b):
        return a*b
    @staticmethod
    def div(a, b):
        return a / b
    ```

    위는 계산기를 static method로 구현한 코드이다. `cls`와 `self`에 접근하지 않으므로 받은 인자로만 값을 구할 수 있도록 구성했다.

    - `__str__`과 `__repr__`과의 차이

        > `__str__`은 informal(developer용), `__repr__`은 formal
        >
        > repr는 인자로 받은 값을 eval의 인자로 사용한 후 eval의 결과값을 리턴한다.
        >
        > 반면 str은 인자를 그대로 str로 바꿔준다.
        >
        > 보통 str은 사람이 보기 좋게 만들어 줄때(개발시), repr은 공식적으로 보여줄때 사용

    ```python
    class Stack():
    
    def __init__(self):
        self.data = []
        self.size = 0
    
    def empty(self):
        if self.size == 0:
            return True
        return False
    
    def top(self):
        if self.empty():
            return None
        return self.data[-1]
    
    def pop(self):
        if self.empty():
            return None
        pop_data = self.data[-1]
        self.size -= 1
        del self.data[-1]
        return pop_data
    
    def push(self, push_data):
        self.size += 1
        self.data.append(push_data)
    
    def __str__(self):
        prints = ""
        for data in self.data[::-1]:
            prints += str(data) + "\n"
        return prints
    def __repr__(self):
        prints = "============ top ============\n"
        for data in self.data[::-1]:
            prints += str(data) + "\n"
        prints += "========== bottom =========="
        return prints
    ```

    다음과 같이 stack에 `__str__`과 `__repr__`이 있다고 생각해보면

    ```python
    stack = Stack()
    stack.push("h1")
    stack.pop()
    print(stack.empty())
    stack.push("h2")
    print(stack.top())
    stack.push("h3")
    stack.push("h1")
    stack.push("h4")
    print(stack.top())
    stack.pop()
    print(stack)
    """
    h1
    h3
    h2
    가 출력
    """
    print(repr(stack))
    """
    ============ top ============
    h1
    h3
    h2
    ========== bottom ==========
    가 출력
    """
    ```
    `print(stack)`을 했을때 기본적으로 `__str__`의 리턴값이 출력된다.

## 2. JWT

> JWT?
> 
> JWT(Json Web Token)은 token 자체에 정보를 심어 정의한 표준 방식(RFC 7519)이다.
> 
> secret 정보를 이용하거나 public/private key를 이용하여 토큰화 할 수 있다.

- Json Web Token을 사용하는 이유

  1. 인증

     JWT를 사용하는 가장 일반적인 상황이 바로 인증상황이다. 한번 로그인 된 후부터는 request에는 JWT 정보가 담겨있다. 로그인 이후 JWT에 담긴 정보를 이용하여 서비스에 접근여부를 결정할 수 있다.

  2. 정보교환

     인증 이외에도 보안성 있는 정보를 전달하는 데에도 사용한다. `signature`를 이용하여 `header`와 `payload`를 토큰화한다. 이를 통해 현재 `content`를 훼손하지 않고 해당 정보를 가져올 수 있다.

- JSON Web Token structure?

JWT는 점(`.`)을 기준으로 세 부분으로 나뉜다.

> 따라서 header.payload(claims).signature 형태로 토큰이 만들어진다.

  1. Header
   
     Header는 일반적으로 2가지 부분으로 나뉜다.
     token의 타입(`JWT`)과 토큰화 할 때 사용한 알고리즘을 담고 있다.

     ```json
     {
        "alg": "HS256",
        "typ": "JWT"
     }
     // Base64Url로 암호화되었음.
     ```

  2. Payload(claims)

     `Payload`에는 전달하고자 하는 정보가 들어있다. 이때 전달하고자 하는 정보들의 일부분(조각)을 `claim`이라고 한다.
     
     예를들어 로그인에 성공한 유저의 id와 권한 등이 두번째 payload 부분에 들어있다.

     이때 claims가 전달하고자 하는 내용인데 이는 3가지 유형이 있다.

     - Registered claims

         등록된 claims로 반드시 지정해야하는 강제성 있는 claim이 아니다. 단, 설정하는 것을 추천하며 대체로 해당 jwt의 설정을 담당하는 claim들이다.

         > `iss`: 토큰 발급자
         >
         > `sub`: 토큰 제목
         >
         > `aud`: 토큰 대상자
         >
         > `exp`: 토큰 만료시간, 시간은 NumericDate 형식이여야하며 현재 시간보다 이후로 설정되어있어야 한다.
         >
         > `nbf`: Not Before을 의미하는 claim. `exp`와 마찬가지로 NumericDate 형식으로 지정한다. 이 날짜가 지나기 전까지는 jwt가 처리되지 않는다.
         >
         > `iat`: issued at으로 토큰이 발급된 시간을 의미하는 claim. 이값으로 토큰이 발급되고 얼마나 지났는지 알 수 있다.
         >
         > `jti`: JWT의 고유식별자로 중복적인 처리를 방지하기 위해 사용. 주로 일회용 토큰에 유용하다.

     - Public claims

         Public claims는 충돌이 방지된 이름을 가지고 있어야만 한다. 보통 충돌을 방지하기 위해 `URI` 형식으로 claim 이름을 지정한다.

     - Private claims

         이해관계자들 간에(보통 서버와 클라이언트) 정보를 공유하기 위해 생성하는 claim. registered와 public과는 달리 이해관계자들 간 동의하에 사용하는 claim
    
  3. Signature 

     Signature는 Header의 인코딩값과 Payload의 인코딩값을 합친 후 SecretKey를 활용하여 해쉬를 생성한다.

     ```
     HMACSHA256(
     base64UrlEncode(header) + "." +
     base64UrlEncode(payload),
     secret)
     ```
    

**※ 참고**

    일반적으로 JWT는 인증시 `Authorization Header`에 `Bearer` 스키마를 활용하여 client에게 토큰을 지급한다. 이렇게 `Authorization Header`를 이용하면 `CORS`시 쿠키를 사용하지 않고도 문제없이 JWT 전송이 가능하다.

[jwt.io 참고](https://jwt.io/introduction/)

## 3. Jekyll Portfolio 사이트 리뉴얼

항상 디자인이 어려운것 같다. 어떻게 해야 나를 잘 표현할 수 있는 레이아웃을 잡을 수 있을까를 고민하고 있지만 좀처럼 답이 나오지 않는다...

일단 사이드 내비바를 상단으로 옮겨 반응형 디자인을 편하게 하도록 만들어야겠다.

그리고 현재 index 페이지에 사진과 함께 내비바만 덩그러니 올려져 있는데 처음 화면에는 나를 요약해서 보여줄 수 있도록 바꿀 예정이다. (간단한 intro, 나의 포부 및 신조, 관심 분야, contact 정보 등을 index 페이지에 요약하여 보여줄 예정)

