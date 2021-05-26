# 2019 01.05 Saturday

## 1. Node.js

* Node.js 내장 모듈
  * os
  * path
  * url
  * querystring
  * crypto
  * fs
* Node.js 이벤트
* http 웹서버 생성
* 라우팅 적용

### Node.js 내장모듈

> 아래 제시된 모듈들은 Node.js가 기본적으로 제공하는 모듈로 `npm install` 없이도 require 할 수 있다.

1. os 모듈

browser javascript 환경에서는 os 정보를 가져올 수 없지만, Node.js는 os 정보를 확인 할 수 있다.

```javascript
const os = require("os")
```

위와 같이 os 모듈을 가져올 수 있다.

* 주요 os 모듈 메서드
  * os.arch\(\) : 프로세서 아키텍쳐 정보로 `process.arch`와 동일
  * os.platform\(\) : 운영체제 플랫폼 정보로 `process.platform`과 동일
  * os.type\(\) : 운영체제 종류
  * os.uptime\(\) : 운영체제 부팅 후 얼마나 시간이 지났는지 보여줌

    > process.uptime\(\)은 Node의 실행시간을 보여준다.

  * os.hostname\(\) : 컴퓨터의 이름
  * os.release\(\) : 운영체제 버전
  * os.homedir\(\) : 홈 디렉터리 경로를 보여줌
  * os.tmpdir\(\) : 임시 파일 저장 경로를 보여줌
  * os.cpus\(\) : 컴퓨터의 코어정보

    > os.cpus\(\).length로 현재 컴퓨터의 코어 갯수를 알 수 있다.

  * os.freemem\(\) : 사용가능한 메모리를 보여줌
  * os.totalmem\(\) : 전체 메모리 용량을 보여줌

위 os 모듈은 컴퓨터 자원에 관련된 정보를 가지고 있기 때문에 일반적인 웹 서비스에는 잘 사용되지 않는다. 운영체제 별로 다른 서비스를 제공하고자 할 때 유용하다.

1. path 모듈

폴더와 파일의 경로를 쉽게 조작하도록 도와주는 모듈

```javascript
const path = require("path");
```

위와 같이 path 모듈을 가져올 수 있다.

* 주요 path 모듈 변수 및 메서드
  * path.sep : 경로의 구분자를 보여준다.

    > Windows는 \, POSIX\(Unix 계열 - MacOS, Linux\)는 /

  * path.delimiter : 환경변수 구분자

    > Windows는 세미콜론`;`, POSIX\(Unix 계열 - MacOS, Linux\)는 콜론`:`

  * path.dirname\(path: string\) : 파일이 위치한 폴더의 경로를 보여준다.
  * path.extname\(path: string\) : path에 위치한 파일의 확장자를 보여준다.
  * path.basename\(path: string\[, ext: string\]\)

    파일의 이름을 보여주는 메서드, 파일의 이름만 표시하고 싶다면 두번째 인자인 ext를 넣어주면 된다.

    ```javascript
      path.basename('/foo/bar/baz/asdf/quux.html');
      // Returns: 'quux.html'

      path.basename('/foo/bar/baz/asdf/quux.html', '.html');
      // Returns: 'quux'
    ```

  * path.parse\(path: string\) : path를 root, dir, base, ext, name으로 분리
  * path.format\(pathObject: object\) : path string을 반환하는 메서드, pathObject의 정보를 이용하여 path string으 만든다. pathObject에는 `dir`, `root`, `base`, `name`, `ext`이 있다.

    > pathObject에 `dir`이 있다면 `root`는 무시된다.
    >
    > pathObject에 `base`이 있다면 `ext`와 `name`은 무시된다.

    ```javascript
    path.format({
      root: '\\ignored',
      dir: '\\home\\user\\dir',
      base: 'file.txt'
    }); // Returns: '/home/user/dir/file.txt'

    path.format({
      root: '\\',
      base: 'file.txt',
      ext: 'ignored'
    }); // Returns: '/file.txt'

    path.format({
      root: '\\',
      name: 'file',
      ext: '.txt'
    }); // Returns: '/file.txt'
    ```

  * path.normalize\(path: string\) : `\`나 `/`를 실수로 여러번 사용하거나 혼용했을 때 정상적인 경로로 바꿔주는 메서드
  * path.isAbsolute\(path: string\) : 파일의 경로가 절대경로인지 상대경로인지 알려주는 메서드 \(절대경로라면 true\)
  * path.relative\(from: string, to: string\) : from에서 to로 가는 경로를 보여주는 메서드
  * path.join\(\[...paths: string\]\) : 여러 path 인자를 넣으면 하나의 경로로 합쳐준다. 상대경로까지 알아서 처리해준다.
  * path.resolve\(\[...paths: string\]\) : join 메서드와 비슷하지만 `/`를 만나는 순간 절대경로로 인식하고 앞의 경로는 무시한다.

    ```javascript
    path.join('/foo', 'bar', 'baz/asdf', 'quux', '..');
    // Returns: '/foo/bar/baz/asdf'

    path.join('foo', {}, 'bar');
    // throws 'TypeError: Path must be a string. Received {}'

    path.resolve('/foo/bar', './baz');
    // Returns: '/foo/bar/baz'

    path.resolve('/foo/bar', '/tmp/file/');
    // Returns: '/tmp/file'

    path.resolve('wwwroot', 'static_files/png/', '../gif/image.gif');
    // this returns '/home/myself/node/wwwroot/static_files/gif/image.gif'
    ```
* url 모듈

이름 그대로 URL을 조작하는 모듈이다.

url처리에는 크게 2가지 방식이 있다. Node v7에서 추가된 WHATWG 방식과 기본의 URL 방식이 있다.

[Day3.md에 URL 관련 학습내용](day3.md)

```javascript
const url = require('url');
```

위와 같은 방식으로 url 모듈을 가져올 수 있다.

`WHATWG` 방식의 URL은 url 모듈의 생성자로 만들 수 있다. 반면 기존의 URL은 url모듈의 URL 속성을 참조한다.

```javascript
const URL = url.URL // 기존에 Node에서 사용하던 url
const WHATWG = new URL("https://www.google.com"); // Node v7의 WHATWG 방식
```

WHATWG 방식은 기존에 URL에는 없는 `username`, `password`, `origin`, `searchParams` 속성이 있다.

기존의 URL에는 `username`과 `password` 정보는 `auth` 객체 내부에 존재한다. 또한, `searchParams` 대신 `query`가 querystring 정보를 담고 있다.

기존의 URL은 주로 `parse(url: string)`와 `format(urlObject: object)` 메서드를 사용한다.

parse는 string의 url 주소를 url 객체로 분해하는 메서드이다. 반면 format은 url 객체로 흩어진 정보를 하나의 주소로 만드는 메서드이다. WHATWG와 기존 URL 방식 둘다 사용가능하다.

> ※ 기존의 URL 방식을 사용해야하는 경우!
>
> 주소가 host부분 없이 pathname만 오는 경우는 WHATWG 방식으로는 처리할 수 없다. 이 경우는 기존의 URL 방식으로만 처리해야한다.

WHATWG 방식에서는 `querystring`을 `searchParams`라는 객체로 제공해주므로 `querystring` 제어가 편하다는 장점이 있다.

* **※ searchParams**

```javascript
const myURL = new URL('https://example.org/?abc=123');
console.log(myURL.searchParams.get('abc'));
// Prints 123

myURL.searchParams.append('abc', 'xyz');
console.log(myURL.href);
// Prints https://example.org/?abc=123&abc=xyz

myURL.searchParams.delete('abc');
myURL.searchParams.set('a', 'b');
console.log(myURL.href);
// Prints https://example.org/?a=b

const newSearchParams = new URLSearchParams(myURL.searchParams);
// The above is equivalent to
// const newSearchParams = new URLSearchParams(myURL.search);

newSearchParams.append('a', 'c');
console.log(myURL.href);
// Prints https://example.org/?a=b
console.log(newSearchParams.toString());
// Prints a=b&a=c

// newSearchParams.toString() is implicitly called
myURL.search = newSearchParams;
console.log(myURL.href);
// Prints https://example.org/?a=b&a=c
newSearchParams.delete('a');
console.log(myURL.href);
// Prints https://example.org/?a=b&a=c
```

searchParams는 URL의 search 부분을 조작하는 다양한 메서드를 제공한다.

* getAll\(key: string\) : 키에 해당하는 모든 값을 가져온다.
* get\(key: string\) : 키에 해당하는 첫 번째 값을 가져온다.
* has\(key: string\) : 해당 키가 있는지 없는지 검사한다.
* keys\(\) : searchParams의 모든 키를 iterator 객체로 반환
* values\(\) : searchParams의 모든 값를 iterator 객체로 반환
* append\(key: string, value: string\) : 해당 키를 추가, 만약 해당 키가 searchParams에 있다면 유지하고 하나 더 추가
* set\(key: string, value: string\) : append와 비슷하지만 해당 킥가 searchParams에 있다면 searchParams에서 해당 키를 모두 지우고 새롭게 추가한다.
* delete\(key: string\) : 해당 키를 제거
* toString\(\): 조작한 searchParams 객체를 문자열로 만드는 메서드

이와 같이 WHATWG 방식은 `search` 정보\(querystring\)를 searchParams 객체로 쉽게 다룰 수 있다. 다만, 기존의 URL 방식에서는 새로이 모듈을 사용해야한다.

1. querystring

querystring 모듈은 기존의 노드 url 사용시 search 부분을 사용하기 쉽게 객체로 만드는 모듈이다.

```javascript
const qs = require("querystring");
```

위와 같은 방식으로 querystring 모듈을 가져온다.

querystring 모듈에서는 parse\(str: string\[, sep: string \[, eq: string \[, options: object\]\]\]\) 메서드와 stringify\(obj: object\[, sep: string \[, eq: string \[, options: object\]\]\]\)를 주로 사용한다.

* parse\(str: string\[, sep: string \[, eq: string \[, options: object\]\]\]\)

  parse 메서드는 str에 있는 query 부분을 객체로 분해해주는 메서드이다.

  * str : 문자열로 된 사이트의 주소
  * sep : query 부분의 구분자 \(default : &\)
  * eq : query 부분에서 key와 value를 구분하는데 사용하는 구분자 \(default : =\)
  * options

    decodeURIComponent : query string 안에서 퍼센트 인코딩을 해석하기 위해 사용하는 함수를 설정할 수 있다. \(default: querystring.unescape\(\)\)

    [퍼센트 인코딩이란?](https://ko.wikipedia.org/wiki/퍼센트_인코딩)

    maxKeys : parse 할 수 있는 key의 최대 숫자 설정 \(default: 1000\)

* stringify\(obj: object\[, sep: string \[, eq: string \[, options: object\]\]\]\)

  stringify는 분해된 query 객체를 문자열로 다시 조합해주는 메서드이다.

  > ※ 이하 옵션은 parse와 동일하다.

1. crypto 모듈

다양한 방식의 암호화를 도와주는 모듈, 특히 회원가입 과정에서 사용자의 비밀번호를 암호화하는데 많이 쓰인다.

```javascript
const crypto = require("crypto");
```

위와 같은 방식으로 `crypto` 모듈을 가져올 수 있다.

* 단방향 암호화

  비밀번호의 경우 주로 단방향 암호화를 사용한다. 단방향 암호화는 복호화 할 수 없는 암호화 방식을 뜻한다. 즉, 암호화하면 원래의 문자열을 찾을 수 없다는 것을 의미한다.

  단방향 암호화 알고리즘은 주로 해쉬 기법을 이용한다.

  > 해쉬 기법이란 어떠한 문자열을 고정된 길이의 다른 문자열로 바꾸는 방식이다.

  ```javascript
  const crypto = require("crypto");

  const password = "1234";
  encodedPw = crypto.createHash("sha512").update(password).digest("base64");
  // return 1ARVn2Auq2/WAqx2gNrL+q3RNjAzXpUfCXrzkA6d4Xa22yhRLy4AC50E+6UTPoscbo31nbOoq51gvkuXzJ6B2w==
  ```

  위와 같이 단방향 암호화를 할 수 있다.

  * createHash\(algorithm: string\) : 사용할 해쉬 알고리즘을 지정하는 메서드. `md5`, `sha1`, `sha256`, `sha512` 등의 알고리즘을 사용할 수 있다. 단, `md5`와 `sha1`은 취약점이 발견되어 사용을 자제하는 것이 좋다.
  * update\(str: string\) : 암호화할 문자열을 인자로 넣어준다. 위 예제에서는 `password`가 인자로 들어가 암호화 되었다.
  * digest\(encoding: string\) : 인코딩할 알고리즘을 넣어준다. `base64`, `hex`, `latin1`이 주로 사용되는데 `base64`가 결과 문자열이 짧아 애용된다.

  현재는 주로 `pbkdf2`, `bcrypt`, `scrypt` 알고리즘으로 암호화한다.

  > ※ pbkdf2 해쉬 알고리즘
  >
  > Node의 crypto에서는 `pbkdf2` 알고리즘을 지원한다.
  >
  > `pbkdf2`는 기존의 문자열에 `salt`라는 문자열을 붙인 후 해시 알고리즘을 반복하여 적용하는 기법이다.

  ```javascript
  const crypto = require("crypto");

  const password = "1234";
  crypto.randomBytes(64, (err, buf) => {
     const salt = buf.toString('base64');
     console.log(salt); // DTkDelJT5+Uc6NZRqujxZBP2otoagbzsU+gnPZvUwTZPlhZmxVpOgvJuowzBdxKUo76FyoKv/badeDQmJmU0fQ==
     crypto.pbkdf2(password, salt, 100000, 64, 'sha512', (err, key) => {
         console.log(key.toString('base64'));
         // OXjdxEkRQN0rd1TXeVgyYAvUzaWxswiGwooIM4UM1t10mq37omhabBbQ0f6SOpi2WUDT+aJgQVosqm3fdv1lvA==
     });
  });
  ```

  > crypto.pdkdf2\(password: string, salt: string, repeat: integer, bytes: integer, algorithm: string, callback: function\)

  위 예시에서는 randomBytes 메서드를 통해 64bytes의 문자열을 만들었다. 이 문자열이 salt가 되었고 이를 활용하여 crypto의 pbkdf2 메서드로 암호화하였다.

* 양방향 암호화

  양방향 암호화는 단방향과는 달리 암호호한 값을 원래의 값으로 복구시킬 수 있다. 이때 `키`가 중요한 역할을 한다.

  ```javascript
  const crypto = require('crypto');
  const key = '키';
  const cipher = crypto.createCipher('aes-256-cbc', key);
  let result = cipher.update('문장', 'utf8', 'base64');
  result += cipher.final('base64');

  const decipher = crypto.createDecipher('aes-256-cbc', key);
  let result2 = decipher.update(result, 'base64', 'utf8');
  result2 += decipher.final('utf8');
  ```

  암호화는 `createCipher`로 복호화는 `createDecipher` 메서드로 각각의 객체를 만든다.

  암호화, 복호화 모두 `update()` 메서드를 통해 실행할 수 있으며 `final()` 메서드로 결과물을 얻을 수 있다.

이외에도 다양한 암호화를 crypto가 지원해준다.

[crypto 공식문서 참고](https://nodejs.org/dist/latest-v10.x/docs/api/crypto.html)

* fs 모듈

  `fs` 모듈은 파일시스템에 접근하기 위한 모듈이다. 즉, 파일을 생성 / 삭제 / 읽기 / 쓰기를 지원해주는 모듈이 바로 `fs` 모듈이다. `fs` 모듈을 통해 차후 `http` 모듈과 함께 라우팅을 적용하여 요청에 맞는 페이지를 사용자에게 보여줄 수 있다.

  ```javascript
    const fs = require('fs');
  ```

  위와 같은 방법으로 `fs` 모듈을 받을 수 있다.

  * readFile\(filename: string, callback: function\) : 파일을 비동기적으로 불러오는 메서드
  * readFileSync\(filename: string\) : 파일을 동기적으로 불러오는 메서드
  * writeFile\(filename: string, data, callback: function\) : 파일을 비동기적으로 쓰는 메서드
  * writeFileSync\(filename: string, data\) : 파일을 동기적으로 쓰는 메서드

    단, `Sync`가 붙은 메서드는 잘 사용하지 않는다. 이는 Node.js가 싱글스레드로 동작하기 때문에 만약에 수백, 수천개의 파일 요청이 들어온 경우 앞서 들어온 요청을 처리할 때까지 아무런 작업을 할 수 없기 때문에 성능상의 문제가 발생할 수 있기 때문이다.

### Node.js의 이벤트

Node.js는 이벤트 기반 방식으로 동작하는 실행환경이다. 따라서, Node.js에서 이벤트를 다루는 것은 중요하다.

이벤트는 `events` 모듈의 `EventEmitter` 생성자를 통해 이벤트 생성 객체를 받아와서 등록할 수 있다.

```javascript
const EventEmitter = require('events');
```

`events`도 내장 모듈이기 때문에 위와 같이 받아올 수 있다.

이벤트를 등록하기 위해서는 먼저 EventEmitter 객체를 만들어야한다.

```javascript
const EventEmitter = require('events');

const event = new EventEmitter();
```

위와 같이 event 변수에 EventEmitter 객체를 만들어야 비로소 이벤트를 등록할 수 있다.

* 이벤트 관리를 위한 주요 메서드
  * on\(eventName: string, callback: function\) : 이벤트 이름과 이벤트 발생 시 콜백을 연결해준다.
  * addListener\(eventName: string, callback: function\) : on과 똑같이 이벤트 리스닝 메서드
  * emit\(eventName: string\): eventName에 해당하는 이벤트를 호출하는 메서드, 등록한 event의 콜백이 실행된다.
  * once\(eventName: string, callback: function\) : 한 번만 실행되는 이벤트를 등록하는 메서드

    > once는 실행하는 메서드가 아닌 등록하는 메서드이다! 따라서, emit을 통해 실행이 한번만 된다는 의미이다.

  * removeAllListeners\(eventName: string\) : 이벤트에 연결된 모든 이벤트 리스너를 제거하는 메서드
  * removeListener\(eventName: string, listner: function\) : 이벤트에 연결된 리스너를 하나씩 제거하는 메서드, listener와 동일한 콜백을 가진 이벤트를 `listener array`에서 지운다.
  * off\(eventName: string, callback: function\): Node v10부터 새로 추가된 메서드, removeListener와 같은 기능을 한다.
  * listenerCount\(eventName: string\): 해당 이벤트에 대해 리스너가 몇 개 추가됐는지 알려주는 메서드

```javascript
const EventEmitter = require('events');

class MyEmitter extends EventEmitter {}

const myEmitter = new MyEmitter();
myEmitter.on('event', () => {
  console.log('an event occurred!');
});
myEmitter.emit('event'); // an event occurred!
```

### Node.js http 모듈

`http` 모듈은 Node에서 웹서버를 다루는 모듈이다. http 모듈을 통해 웹서버를 생성하고 실행할 수 있다.

```javascript
const http = require('http');
// http 모듈 가져오기
const server = http.createServer((req, res) => {
    res.end("hello world!");
});
// 서버 생성, 응답으로 hello world!를 준다.
server.listen(3000, (err) => {
    if (err) console.error("server cause error at starting");
    console.log('server on');
});
// 서버실행시 오류가 없다면 server on이 콘솔에 찍힌다.
```

위와 같이 http 서버를 실행할 수 있다.

위와 같은 서버에도 이벤트 리스너를 등록할 수 있다. 미리 등록된 http 이벤트도 있으며 아래 공식문서에서 확인할 수 있다.

[참고 Node.js http 공식문서](https://nodejs.org/dist/latest-v10.x/docs/api/http.html)

위에서는 그냥 text를 응답에 실어 보냈지만 파일자체를 응답으로 띄울 수 있다.

```javascript
const http = require('http');
const fs = require('fs');

const server = http.createServer((req, res) => {
    fs.readFile("./templates/index.html", (err, data) => {
        if (err) throw err;
        else res.end(data);
    });
});

server.listen(3000, (err) => {
    if (err) console.error("server cause error at starting");
    console.log('server on');
});
```

위와 같이 fs 모듈과 함께 응답을 파일 자체로 띄울 수 있다.

* 쿠키와 세션

쿠키는 요청과 응답의 헤더`header`에 저장된다. 쿠키는 다음과 같은 방식으로 저장할 수 있다.

```javascript
const server = http.createServer((req, res) => {
    res.writeHead(200, {
        'Set-Cookie': 'username=pkch'
    });
    fs.readFile("templates/index.html", (err, data) => {
        if (err) throw err;
        else res.end(data);
    });
});
```

위 예시처럼 응답 헤더를 작성하는 writeHead 메서드를 사용하여 쿠키를 생성할 수 있다.

> res.writeHead\(statusCode: integer, header: object\)
>
> 첫 번째인자로는 상태코드, 두 번째인자로는 헤더에 설정할 정보를 객체형태로 담는다.

그렇게 만들어진 쿠키는 응답으로 사용자에게 전달된다. 다음 요청부터는 쿠키가 헤더에 들어있는채로 서버에 요청을 보내며 서버에서는 `req.headers.cookie` 속성으로 쿠키를 참조할 수 있다.

위 쿠키는 단순히 username 정보만 담은 쿠키이다. 쿠키 설정시에는 내용 뿐 아니라 다양한 옵션을 줄 수 있다.

* cookie 옵션
  * Expires=datetime: 만료기한, 이 기한이 지나면 쿠키는 제거, 기본값은 클라이언트가 종료될 때까지
  * Max-age=seconds: Expires와 비슷하지만 Expires는 날짜를 입력하는 대신 Max-age는 초단위로 입력할 수 있다. Expires에 우선한다.
  * Domains=domainName: 쿠키가 전송될 도메인을 지정할 수 있다. 기본값은 현재 도메인이다.
  * Path=URL: 쿠키가 전송될 URL을 특정할 수 있다. 기본값은 `/`이고 이 경우는 모든 URL에서 쿠키전송이 가능하다.
  * Secure: `https`일 경우에만 쿠키 전송
  * HttpOnly: 설정시 자바스크립트에서 쿠키에 접근할 수 없다. \(쿠키조작방지를 위함\)

단, cookie는 client가 직접 저장하기 때문에 보안에 취약할 수 있다. 따라서 중요한 정보는 가급적 cookie에 넣지 않는 것이 좋다.

cookie와 더불어 로그인 처리를 위한 도구가 있다. `session`이 바로 그 대상이다.

session은 cookie와 다르게 서버에서 정보를 가지고 있으며 client의 브라우저가 꺼지기 전까지 유효하다.

server에서 `session`이라는 객체를 만들어 `session`을 구현할 수 있다.

* http 웹 서버 라우팅

