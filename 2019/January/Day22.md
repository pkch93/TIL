# 2019 01.23 Wendesday

> 오늘 하루는, 내일보다 두 배의 가치가 있다.
>
> by 벤자민 프랭클린

1. eslint

`eslint`를 이전에 써본적이 있지만 블로그에서 사용하라는 대로 따라 쳐서 사용해 본 경험 밖에 없없다.

따라서, 이번에 express.js와 react.js를 복습하면서 `eslint`를 `global` 설치하여 사용해보았다.

`ESLint`는 `Node.js v6.14` 이상이에야 하며 `npm v3+`에서 지원한다.

```
npm install -g eslint
```

위 명령으로 현재 컴퓨터에서 eslint 명령을 실행할 수 있다.

```
eslint --init
```

위 명령으로 eslint 설정 파일을 만들 수 있다. (각각의 프로젝트 단위로 eslint를 적용하고자 할 때, .eslintrc라는 eslint 설정 파일 생성)

위 명령을 실행하면 각각의 설정 사항을 커스텀하게 설정할 수 있도록 `cmd`에서 질문사항에 답할 수 있다.

```
How would you like to configure ESLint?
Use a popular style guide
Answer questions about your style
Inspect your Javascript file(s)
```

위 세가지 사항중 하나로 eslint 초기 설정 방법을 고를 수 있다. 보통 널리 쓰이면서 빠르게 javascript 문법을 점검하기 위해 Use a popular style guide를 사용한다.

```
How would you like to configure ESLint? Use a popular style guide
Which style guide do you want to follow?
Airbnb (https://github.com/airbnb/javascript)
Standard (https://github.com/standard/standard)
Google (https://github.com/google/eslint-config-google)
```
위 초기 설정 방법을 `Use a popular style guide`로 고르면 어떤 코딩 스타일을 따를지 고르도록 유도한다.

위 3가지 방법을 비교한 블로그 글은 아래를 참고할 수 있다. (보통 Airbnb와 Standard를 선호하는 것으로 판단)

[eslint style 비교 참고](https://medium.com/@uistephen/style-guides-for-linting-ecmascript-2015-eslint-common-google-airbnb-6c25fd3dff0)

```
How would you like to configure ESLint? Use a popular style guide
Which style guide do you want to follow? Airbnb (https://github.com/airbnb/javascript)
Do you use React? (y/N)
```

그 다음 React를 사용하는지 여부를 묻는다. react를 사용한다면 y, 아니면 N을 누른다.

```
How would you like to configure ESLint? Use a popular style guide
Which style guide do you want to follow? Airbnb (https://github.com/airbnb/javascript)
Do you use React? No
What format do you want your config file to be in?
Javascript
YAML
JSON
```
`.eslintrc` 설정파일을 어떤 형식으로 만들지 설정하는 구간이다. 원하는 형식을 고르도록 한다.(보통 js 형식을 많이 쓰는 것 같다.)

이렇게 고르고 나면 `express.js`를 사용할 경우 eslint 설정이 끝난다.

조금씩 사용하면서 `Google`과 `standard` 스타일을 비교해보고 다른 옵션들에 대해서도 탐구해봐야겠다.

[eslint 공식 사이트](https://eslint.org/)

2. express.js

    - Restful api 요청시 304 status code 발생

        해결방법 : [참고](https://stackoverflow.com/questions/18811286/nodejs-express-cache-and-304-status-code)

    - require() 을 통해 json 요청하는 경우

        json을 그대로 js 객체로 읽어들여서 사용할 수 있다.

    - Error [ERR_HTTP_HEADERS_SENT]: Cannot set headers after they are sent to the client
    
        위 오류 때문에 꽤나 시간을 많이 잡아먹었다. 위 오류가 생길 수 있는 상황은 여러가지이지만 내가 이 오류에 빠진 이유는 다음 코드 때문이다.

        ```javascript
        exports.login = (req, res) => {
        const {username, password} = req.body;
            for(let user of users){
                if (user.username === username && user.password === password){
                    res.status(200).json(user);
                }
            }
            // res.writeHead(403);
            res.status(403).json({ error: "403 Authentication Denied", message: "invaild input" });
        };
        ```
        로그인 관련 미들웨어 코드이다. 위 코드에서 for문을 돌면서 username과 password가 일치하면 응답을 보내주는 형식으로 코드를 짰다.

        하지만, http에서는 하나의 request에 하나의 response만 허용되므로 응답을 보낸 후 for문을 돌게 되면 다음과 같은 에러가 발생한 것이다. 따라서 for문 내부에서 해당하는 값을 찾으면 함수(미들웨어)를 종료하도록 `return true`를 해주어야 오류가 나지 않는다.

        [참고](http://b1ix.net/313)
        [stackoverflow 참고](https://stackoverflow.com/questions/7042340/error-cant-set-headers-after-they-are-sent-to-the-client)