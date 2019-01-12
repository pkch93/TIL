# 2019 01.08 Tuesday

1. git config 설정 변경

다른 컴퓨터에서 github에 push할 때 이전에 git 설정이 되어있다면 (즉, github login 정보가 있다면) 이전 로그인 정보를 지우고 나의 계정으로 바꿔줘야한다.

```
git credential reject
protocol=https
host=github.com
```
위 명령으로 이전 로그인 정보를 삭제해준다.

이 경우 나의 파일을 push할 때 현재 컴퓨터의 github 로그인 정보가 사라지기 때문에 로그인 창이 뜬다. 이 때, 로그인을 해주면 github 로그인 정보가 남아있게 된다.

위는 현재 컴퓨터와 github 연동을 설정한 것이다.

다음은 global로 설정한 user.name과 user.email을 다시 설정하여야한다.

이를 통해 현재 컴퓨터에서 커밋하는 파일에 대한 기록이 남게된다.

2. Node.js

    - sequelize.js

    개발단계에서 schema를 변경시 즉시 반영시키기 위해서는 `sequelize.sync({force: true})`와 같이 sync 메서드에 force옵션을 주면 된다.

    > production 단계에서는 절대 쓰지말 것!

    production에서는 schema 변경시 sequelize migration을 사용하여 스키마 변경할 것

    [sequelize migration 참고](http://docs.sequelizejs.com/manual/tutorial/migrations.html)

    - passport.js

    Node.js의 인증 middleware

    ```
    npm install passport --save
    ```
    `passport.js`는 인증 요청을 처리하기 위한 용도로 만들어진 middleware이다. passport.js는 passport의 모든 기능을 application에 위임한다. 그렇다고 application과 결합이 어렵지 않다!

    이를 통해 유지보수를 가능하게 하고 깔끔한 코드를 유지할 수 있도록 도와준다.

    Authenticate

    passport의 인증 요청은 `passport.authenticate()`를 통해 쉽게 호출할 수 있다.

    `authenticate()`의 반환값은 express 어플리케이션에서 라우트를 편하게 사용하도록 만들어 주는 `Connect middleware`가 된다.

    [Connect middleware 참고](https://github.com/senchalabs/connect#readme)

    ```javascript
    app.post('/login',
    passport.authenticate('local'),
    function(req, res) {
        // If this function gets called, authentication was successful.
        // `req.user` contains the authenticated user.
        res.redirect('/users/' + req.user.username);
    });
    ```
    위와 같이 express Route 내부에서 `passport.authenticate()` 함수를 불러서 사용할 수 있다.

    만약 `passport.authenticate()`로 인증이 실패한다면 `401 Unauthorized` 상태 코드가 반환된다. 이렇게 되면 후에 나오는 추가적인 router는 불리지 않는다.

    인증이 성공한 경우는 다음 route handler가 불러지며 `req.user` 변수로 인증된 user 정보를 가져올 수 있다.