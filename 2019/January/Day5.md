# 2019 01.06 Sunday

> 어제를 놓아주는 법을 알게 되었을 때, 그때 오늘의 아름다운 아침을 시작할 수 있다.
>
> by 스티브 마라볼리 / Steve Maraboli

## 1. Node.js

어제까지 Node.js가 제공하는 기능과 내장 모듈에 대해 학습하였다.

특히 라우팅 과정에서 매우 난잡한 코드가 만들어졌다. 이를 해결하기 위한 방법으로 Node에서 웹 서버 개발을 도와주는 `express.js`를 학습하고자 한다.

### - Express.js

> Node.js를 위한 빠르고 개방적이고 간결한 웹 프레임워크
>
> Fast, unopinionated, minimalist web framework for Node.js 
>
> [출처](http://expressjs.com/)

express.js 공식 홈페이지에서 위와 같이 소개하고 있다.

express.js는 http 모듈의 요청 / 응답 객체에 기능을 추가하여 기존에 존재하는 메서드를 계속 사용하면서 코드 작성에 불편한 문제를 개선했다. 때문에 http 모듈로 웹서버를 작성하는 것보다 간결하게 코드를 작성할 수 있으며 기존에 존재하는 내장 모듈이나 `npm`으로 가져온 외장 모듈을 자유롭게 사용할 수 있다.

> express.js를 선택한 이유
>
> Node.js에는 express.js 이외에도 다양한 웹 프레임워크가 있다. (`koa`, `hapi`, `sails` 등...)
>
> 그 중에서 express.js를 선택한 이유는 express.js는 npm에서 가장 많은 인기를 얻고 있다. 때문에 인터넷 웹 상에서도 다양한 자료가 존재하며, 기능 추가 및 유지보수도 활발히 일어난다.
>
> 위의 이유와 더불어 처음 제대로 시작하는 입장에서는 가장 인기있는 프레임워크를 공부하고 익숙해진 상태에서 나에게 맞는 프레임워크를 선택하는 것이 바람직하다고 생각하기에 express.js를 선택하였다.

![npm node.js 서버 프레임워크 다운로드 수 비교](images/node_download.PNG)

위 사진에서도 보듯이 다른 Node.js 웹 서버 프레임워크에 비해 압도적으로 많은 다운로드 수를 보여준다!

[출처](https://www.npmtrends.com/express-vs-hapi-vs-koa-vs-sails)

 - Hello World!

 express.js는 Node.js가 설치되어 있어야 사용가능하다. 설치가 되있다면 프로젝트 폴더에서 다음과 같이 express를 다운받는다.

 ```
 npm install express
 ```

 만약 dependency 옵션에 넣고 싶다면 다음과 같이 express를 설치한다.

 ```
 npm install express --save
 ```

 > ## --save vs --save-dev vs no options
 >
 > 위 `--save`나 `--save-dev` 없이 그냥 `npm install`을 하면 해당 프로젝트 폴더의 `node_modules`에 설치하는 패키지만 설치하기만한다.
 >
 > 반면 `--save`나 `--save-dev`는 Node에서 패키지 의존성을 관리하는 파일인 `package.json`파일을 업데이트 해준다. 각각 `dependencies`와 `devDependencies`에 설치된 패키지의 이름이 key, 버전이 value로 등록된다. 
 >
 > 이 때 `dev`의 차이는 npm install 시 `--production` 옵션에 따라 달라진다. `dependencies`는 어떤 옵션에도 항상 설치되지만 `devDependencies`는 `--dev`옵션을 붙여야지만 설치가 된다. `--production`에서는 설치되지 않는다!
 >
 > [참고](http://ohyecloudy.com/ddiary/2016/09/04/til-npm-install-save-or-save-dev/)

 이렇게 express.js를 설치하면 다음과 같이 웹서버를 실행할 수 있다.

 ```javascript
 var express = require('express'); // express 모듈 가져오기
 var app = express(); // express instance 생성

 app.get('/', function (req, res) {
   res.send('Hello World!');
 }); // index에 진입하면 'Hello World!'가 응답으로 나타난다.

 app.listen(3000, function () {
   console.log('Example app listening on port 3000!');
 }); // 3000번 포트로 express 웹 서버 가동
 ```
 이렇게 가동한 웹 서버는 `http://localhost:3000/`에서 확인 할 수 있다.

- express-generator

 단, 위와 같이 `express`를 사용하려면 프로젝트의 골격을 스스로 짜야하는 번거로움이 있다. 이는 처음 쓰는 초보자에게는 큰 혼란을 줄 수 있다. 이에 `express`는 `express-generator`를 제공하여 프로젝트의 골격을 빠르게 잡아주도록 도와준다.

 `express-generator`는 콘솔 명령어이므로 `-g`옵션으로 전역으로 설치한다.

 ```
 npm i -g express-generator
 ```

 > i는 install의 약자

 이렇게 `express-generator`를 설치하면 `express` 명령어로 express 프로젝트를 만들어 낼 수 있다.

 ```
 express first --view=ejs
 ```

 위 명령어는 first라는 express 프로젝트를 생성하는 명령이다. 이때 --view 옵션으로 ejs 템플릿 엔진을 사용하도록 지정하였다.

 이렇게 만들어진 first 폴더에서 `npm install`을 하면 의존 패키지가 받아지면서 비로소 웹서버를 사용할 수 있게 된다.

 `express-generator`를 통해 생성한 프로젝트 구조는 다음과 같다.

 ![express-generator 프로젝트 구조](images/express-generator.PNG)
 
 `express-generator`에서 어플리케이션을 구동하는 핵심적인 파일은 `app.js`이다.

 ```javascript
 var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
 ```

 위는 `express-generator`로 생성한 프로젝트의 `app.js` 파일이다.
 위 코드에서 `app.set()`을 통해 `express` 어플리케이션이 동작하는데 필요한 설정 정보를 등록하고 있다.
 
 > app.set(key: string, value: ?)
 >
 > app.set 메서드는 `key:value` 형태로 개발자가 원하는 value를 애플리케이션에 등록하는데 사용한다. 단, 일부 `key`는 어플리케이션에 필요한 설정 정보를 다루는데 사용된다.
 >
 > 이렇게 등록한 값은 app.get(key: string)으로 가져올 수 있다.

 ```javascript
 app.set('title', 'My Site');
 app.get('title'); // "My Site"
 ```
 이 중에서도 `env`, `views`, `view engine`등은 어플리케이션 필요한 설정 정보를 다루는데 사용된다.

 ```javascript
 app.set('views', path.join(__dirname, 'views')); // 어플리케이션의 view를 제공하기 위해 폴더 이름이나 폴더 배열을 setting하는 부분 (key는 views)
 app.set('view engine', 'ejs'); // 템플릿 엔진을 설정하는 부분 (key는 view engine)
 ```
 [더 많은 정보 참고](https://expressjs.com/ko/4x/api.html#app.set)

 그리고 `app.use()`를 통해 수많은 미들웨어를 등록하고 있다.

 > ## 미들웨어?
 >
 > Node.js에서 요청과 응답 사이의 주기에서 그 다음의 미들웨어 함수를 엑세스하는 권한을 갖는 함수이다.
 >
 > 미들웨어는 다음과 같이 수행할 수 있다.
 >
 > - 모든 코드 실행
 > - 요청 및 응답에 대한 작업 수행
 > - 요청-응답 주기 종료
 > - 스택 내부에 다음 미들 미들웨어 호출
 >
 > 미들웨어는 요청-응답 주기 동안 선언한 순서대로 미들웨어가 실행된다.
 >
 > 따라서, 실행하기 위해 꼭 선언해야하는 미들웨어가 있다면 해당 미들웨어의 앞에 선언해주어야 제대로 동작할 수 있다.
 >
 > 현재의 미들웨어 함수가 요청-응답 주기를 종료하지 않는다면 반드시 next()를 호출하여 그 다음 미들웨어 함수를 불러줘야한다. 그렇지 않으면 요청이 정지되어 방치된다.

 위 `app.js` 에서는 다양한 미들웨어를 사용하고 있다.

 ```javascript
  var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
 
 // ...

 app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});
 ```

 위와 같이 `express`에 내장된 미들웨어 (ex. `unlencoded`, `json`, `static`)를 등록하고 있고 외장 모듈인 `morgan`과 `cookie-parser`를 가져와 미들웨어로 등록해주고 있다.
 
 또한, Router 미들웨어를 만들어 `app`의 라우팅을 모듈화하고 있다. (라우팅은 아래에서 더 다룰 예정)

 > express 내장 미들웨어
 >
 > - unlencoded (~4.16.0)
 >  
 >    `body-parser`를 기반으로 인코딩되지 않은 요청 정보를 인코딩 하는 미들웨어
 >
 >    [unlencoded 참고](https://expressjs.com/ko/4x/api.html#express.urlencoded) 
 >
 > - json (~4.16.0)
 >
 >    마찬가지로 `body-parser`를 활용하여 JSON으로 들어오는 요청을 파싱하는 미들웨어
 >
 > [json 참고](https://expressjs.com/ko/4x/api.html#express.json)
 >
 > - static
 >
 >    `server-static`을 기반으로 정적 파일을 제공하도록 도와주는 미들웨어
 >
 > [static 참고](https://expressjs.com/ko/4x/api.html#express.static)
 >
 > ※ Router도 있지만 아래에서 다름

 express-generator 구조를 보면 특이한 폴더와 파일이 있다.
 
 바로 `./bin/www`파일이다.

 `express 3`에서는 필요한 미들웨어를 처음 세팅하는데 `app.configure()`이나 `app.use()`를 사용했다.

 하지만 `express 4`에서부터 독립적으로 관리하기 위하여 `static`을 제외한 모든 미들웨어들이 `app.js`에서 제거되었고 제거 될 예정이다. 따라서, 처음 어플리케이션이 시작할 때 설정을 해주기 위해 `./bin/www`를 이용하여 정의한다.

 > `./bin/www`은 http 모듈에 express를 연결하고 포트를 지정하는 역할을 한다.

 [./bin/www 참고](https://stackoverflow.com/questions/23169941/what-does-bin-www-do-in-express-4-x)

 - 라우팅

 > 라우팅은 어플리케이션의 endpoint (URI)를 정의하고 URI가 클라이언트 요청에 응답하는 방식을 의미한다.
 
 ```javascript
 var express = require('express');
 var app = express();

 app.get('/', function(req, res) {
   res.send('hello world');
 });
 ```
 위는 공식문서에서 제공하는 기본적인 라우팅 예시이다.

 get method로 `/` url이 오면 `hello world`가 응답하도록 만들었다.

 이렇게 `express` instance에 HTTP Method에 해당하는 routing method를 사용하여 url과 http 요청을 구분하여 처리할 수 있다.

 > HTTP Method
 >
 > HTTP는 주어진 리소스에 필요한 액션이 수행되게 하는 요청 메서드 집합을 정의한다.
 >
 > 주로 GET, POST, PUT, PATCH, DELETE를 주로 사용하며 그 외에 HEAD, CONNECT, OPTIONS, TRACE 등이 있다.
 >
 > - GET : 특정 리소스를 요청, 오직 데이터를 받기만 하는 용도
 >
 > - POST : 특정 리소스를 생성할 때 주로 사용
 >
 > - PUT : 목적 리소스에 대한 데이터를 전부 바꿀때 사용
 >
 > - PATCH : 목적 리소스에 일부분만 수정할 때 사용
 >
 > - DELETE : 특정 리소스를 삭제할 때 사용
 >
 > [HTTP Method 참고](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods)

 ```javascript
 // GET method route
 app.get('/', function (req, res) {
   res.send('GET request to the homepage');
 });

 // POST method route
 app.post('/', function (req, res) {
   res.send('POST request to the homepage');
 });
 ```
 
 위와 같이 GET, POST Method 요청으로 `/`에 해당하는 요청을 따로 처리할 수 있다.
 
 만약 모든 요청 메서드에서 동일한 처리를 하는 경우 `all`을 사용할 수도 있다.

 ```javascript
 app.all('/', function(req, res, next){
     console.log('this url get all method request');
     next();
 });
 ```

 > all은 HTTP Mehtod가 아니다! express에서 해당 경로의 모든 요청 메서드에 대해 미들웨어 함수를 로드하는데 사용한다.
 
 위에 정의한 라우팅 경로는 일반적으로 `문자열`을 사용하지만 `문자열 패턴`, `정규표현식`도 가능하다.

 ```javascript
 // 문자열 방식
 app.get('/hello', (req, res) => {
     res.end('hello');
 }); 
 
 // 문자열 패턴 방식
 app.get('/hel+o', (req, res) => {
     res.end('hel+o');
 }); // '/helo' or '/hello' or '/hellllllo and so on...
 // +는 앞선 문자가 0개 이상 있을 수 있다.

 app.get('/hell?o', (req, res) => {
     res.end('hel?o');
 }); // '/helo' or '/hello'
 // ?는 앞선 문자가 없거나 있거나
 
 app.get('/he(ll)?o', (req, res) => {
     res.end('he(ll)o');
 }); // '/heo' or '/hello'
 // 괄호를 넣으면 괄호안 문자를 한 문자 덩어리로 인식하게 할 수 있다.

 app.get('/h*o', (req, res) => {
     res.end('h*o');
 }); // '/ho' or 'hello' or '/hfjwfjowekfo' and so on...
 // *는 아무 문자나 나타날 수 있다.

 // 정규표현식
 app.get(/hello/, (req, res) => {
     res.end('hello');
 }); // 정규표현식으로 hello가 포함된 url에 일치
 ```

 `미들웨어`와 비슷하게 작동하는 여러 콜백 함수로 요청을 처리할 수도 있다. (라우트 핸들러)
 
 이는 라우트에 대한 사전 조건을 지정하여 현재의 라우트를 계속할 이유가 없다면 후속 라우트로 `next`를 이용하여 전달할 수 있다.

 > 하나의 콜백 함수는 하나의 라우트를 처리할 수 있다. 
 
 ```javascript
 app.get('/example/b', function (req, res, next) {
  console.log('the response will be sent by the next function ...');
  next();
 }, function (req, res) {
  res.send('Hello from B!');
 });

 var cb0 = function (req, res, next) {
  console.log('CB0');
  next();
 }
 
 var cb1 = function (req, res, next) {
  console.log('CB1');
  next();
 }
 var cb2 = function (req, res) {
  res.send('Hello from C!');
 }
 app.get('/example/c', [cb0, cb1, cb2]);

 app.get('/example/d', [cb0, cb1], function (req, res, next) {
  console.log('the response will be sent by the next function ...');
  next();
 }, function (req, res) {
  res.send('Hello from D!');
 });
 ```
 `/example/b`에서는 url 문자열 뒤에 콜백을 2개 줘서 해당 요청을 처리했다. 이 때, 마지막 콜백을 제외하고는 반드시 `next`를 지정해야한다! 그렇지 않으면 다음 콜백으로 넘어가지 않는다.

 위 `/example/c`와 `/example/d`에서의 예시같이 라우팅 메서드의 두번째 인자에 callback 함수 배열을 넣어 해당 요청에 대하여 처리할 콜백을 넣을 수 있다. `/example/d`에서처럼 callback 배열과 함께 여러가지 콜백을 직접 인자로 줄 수도 있다.
 
 > 응답 메서드
 >
 > ![express 응답 메서드들](images/express-response.PNG)
 >
 > [express 라우팅 공식문서 참고](http://expressjs.com/ko/guide/routing.html)

 단, 위와 같이 라우트 핸들러를 작성하면 `next` object를 지정해야하는 점, 여러 HTTP Method를 사용해야하는 경우 번거로운 경우가 생길 수 있다. 이 떄 `app.route()`로 해결할 수 있다.

 ```javascript
 app.route('/user')
  .get(function(req, res) {
    res.send('Get user list');
  })
  .post(function(req, res) {
    res.send('join user');
  })
  .put(function(req, res) {
    res.send('Update the user data');
  });
 ```
 
 위와 같이 `route()`로 한 url에 대해 다양한 HTTP Method를 처리할 수 있다. 또한 모듈식으로 작성가능하므로 중복과 오타도 줄일 수 있다.

 마지막으로 express의 `Router` 클래스로 모듈식으로 마운팅 가능한 핸들러를 작성할 수 있다.

 ```javascript
 // hello.js
 var express = require('express');
 var router = express.Router();

 router.use(function log(req, res, next){
     console.log('Time: ' + Date.now());
     next(); 
 }); // 다음과 같이 router에 미들웨어를 제공할 수 있다.

 router.get('/', function(req, res){
     res.end('hello world!');
 });

 router.get('/ko', function(req, res){
     res.end('안녕 세계!');
 });

 module.exports = router;
 ```
 위와 같이 설정한 라우트를 express 앱에서 미들웨어로 등록하면 라우팅이 적용된다.

 ```javascript
 // app.js
 var express = require('express');
 var hello = require('./hello');

 var app = express();
 app.use('/hello', hello);
 ```
 위와 같이 use로 위 예시에 설정한 `hello`라우터를 등록하면 `/hello`를 baseurl로 요청을 처리할 수 있다. (위 예시에서는 `/hello`와 `/hello/ko`에 대해 요청처리)

 [express 공식문서 참고](http://expressjs.com/ko/guide/routing.html)

### - Sequelize.js

`Sequelize.js`는 `Node.js`에서 `ORM`을 지원해주는 라이브러리이다.

```
npm install --save sequelize

# And one of the following:
npm install --save pg pg-hstore // postgresql
npm install --save mysql2 // mysql
npm install --save sqlite3 // sqlite3
npm install --save tedious // MSSQL
```

위와 같이 `sequelize`와 원하는 db를 연결하기 위한 의존성을 추가하면 준비는 완료됐다.

 - DB 연결하기

 Sequelize는 초기 생성시 커넥션 풀을 설정한다.

 > 커넥션 풀 설정시 유의사항
 >
 > single process의 경우는 `database`당 하나의 `instance`가 생성되는 것이 가장 이상적이다. (이 경우 커넥션 풀은 `pool`에 설정한 갯수만큼 생성)
 >
 > multiple processes의 경우는 전체 프로세스 instance가 커넥션 풀을 비율대로 나눠가진다. 만약, 3개의 프로세스가 있고 90개로 커넥션 풀의 크기를 정하면 프로세스들은 각각 30개씩 커넥션 풀을 가지게된다.

 ```javascript
 const Sequelize = require('sequelize');
 const sequelize = new Sequelize('database', 'username', 'password', {
  host: 'localhost',
  dialect: 'mysql'|'sqlite'|'postgres'|'mssql',
  operatorsAliases: false,

  pool: {
    max: 5,
    min: 0,
    acquire: 30000,
    idle: 10000
  },

  // SQLite only
  storage: 'path/to/database.sqlite'
 });

 // Or you can simply use a connection uri
 const sequelize = new Sequelize('postgres://user:pass@example.com:5432/dbname');
 ```
 
 위와 같이 `sequelize`로 DB 설정을 할 수 있다.

 이후 위에서 정의한 `sequelize.sync()`로 DB와 연동할 수 있다.
 
 [Sequelize 공식 문서 getting-started 참고](http://docs.sequelizejs.com/manual/installation/getting-started.html)

 