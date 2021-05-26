# 2019 02.16 Saturday

1. jwt

최근 `다빈치 메이커 페스티벌`에서 개발했던 `청춘어람` 프로젝트를 `koa.js`에서 `express.js`로 바꾸는 작업을 하고있다.

이때 jwt를 통한 인증방식을 사용했는데 최근에 `Authorization Header`를 사용하여 jwt를 전송하는 것을 많이 봐와서 이 두가지 방법의 차이점이 궁금했다.

따라서, 두 가지 방법 중 무슨 방법이 더 적합한지, 왜 그와 같이 사용하는 지 알아보기로 했다.

* jwt in cookie

> cookie의 장단점을 그대로 가짐

_**장점**_

1. HttpOnly : 오직 http 요청/응답 과정에서만 접근 가능하도록 쿠키에서 설정할 수 있으므로 악성 `javascript`에게서 방어가 가능하다. \(`XSS-Crose-Site Script`\)
2. Secure : https에서만 접근가능한 쿠키 생성이 가능하여 암호화되지 않은 채널에게서 정보를 보호할 수 있다.

_**단점**_

1. CSRF : 쿠키는 CSRF 공격에 매우 취약
2. 성능과 확장성 : 쿠키 기반의 인증은 상태를 유지한 상태에서의 인증이 이뤄져야 하므로 서버가 쿠키를 파일이나 DB 형태로 관리하고 있어야한다. 이점에서 성능 / 확장성 문제가 생긴다.
3. jwt in header

> cookie의 장단점이 뒤바뀜

_**장점**_

1. 성능 및 확장성 : token 내부에 metadata와 암호화된 정보가 포함되어있다. 필요한 정보가 token에 포함되어 있으므로 server에서는 상태 유지할 필요가 없다. 이는 성능과 확장성을 높일 수 있는 결과로 이어진다.
2. CSRF : token은 기본적으로 제 3의 웹 어플리케이션\(the third party web application\)으로 전달될 수 없다.

_**단점**_

1. XSS : 브라우저\(`javascript`\)의 local data로 token이 저장되므로 javascript로 접근할 수 있다.
2. 결론

cookie와 token 방식 모두 각각의 장단점이 있으므로 상황에 맞게 적절한 방식을 따르는 것이 바람직하다.

[Store Auth-Token in Cookie or Header? 참고](https://security.stackexchange.com/questions/180357/store-auth-token-in-cookie-or-header)

1. http status 401\(`unauthorized`\) vs 403\(`forbidden`\)

401은 사용자가 `authorization(허가)`보다는 `authentication(인증)`에 어울리는 status code이다. 즉, 유저가 아이디와 패스워드를 가지고 서비스에 인증할 때 만약 잘못된 아이디, 비밀번호인 경우 401 status code가 나타난다.

403은 서비스에서 사용자를 인지하고 있지만 해당 데이터에는 접근할 수 없는 경우\(`forbidden(금지)`\)를 의미한다.

1. mongo shell
2. mongo : mongodb shell 접근
3. use &lt;db&gt; : 해당 db에 접근
4. db : 현재 접근한 db의 이름 출력
5. show users : 현재 db의 user 목록 출력
6. show collections : 현재 db에 존재하는 collection 목록 출력
7. 
[mongo shell 참고](https://docs.mongodb.com/manual/reference/mongo-shell/)

