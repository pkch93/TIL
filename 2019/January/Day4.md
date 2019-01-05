# 2019 01.05 Saturday

## 1. Node.js

- Node.js 내장 모듈
    - os
    - path
    - url
    - querystring
    - crypto
    - fs
- Node.js 이벤트
- http 웹서버 생성
- 라우팅 적용

### Node.js 내장모듈

> 아래 제시된 모듈들은 Node.js가 기본적으로 제공하는 모듈로 `npm install` 없이도 require 할 수 있다.

1. os 모듈

browser javascript 환경에서는 os 정보를 가져올 수 없지만, Node.js는 os 정보를 확인 할 수 있다.

```javascript
const os = require("os") 
```

위와 같이 os 모듈을 가져올 수 있다.

- 주요 os 모듈 메서드

    - os.arch() : 프로세서 아키텍쳐 정보로 `process.arch`와 동일
    - os.platform() : 운영체제 플랫폼 정보로 `process.platform`과 동일
    - os.type() : 운영체제 종류
    - os.uptime() : 운영체제 부팅 후 얼마나 시간이 지났는지 보여줌

    > process.uptime()은 Node의 실행시간을 보여준다.

    - os.hostname() : 컴퓨터의 이름
    - os.release() : 운영체제 버전
    - os.homedir() : 홈 디렉터리 경로를 보여줌
    - os.tmpdir() : 임시 파일 저장 경로를 보여줌
    - os.cpus() : 컴퓨터의 코어정보

    > os.cpus().length로 현재 컴퓨터의 코어 갯수를 알 수 있다.

    - os.freemem() : 사용가능한 메모리를 보여줌
    - os.totalmem() : 전체 메모리 용량을 보여줌

위 os 모듈은 컴퓨터 자원에 관련된 정보를 가지고 있기 때문에 일반적인 웹 서비스에는 잘 사용되지 않는다. 운영체제 별로 다른 서비스를 제공하고자 할 때 유용하다.

2. path 모듈

폴더와 파일의 경로를 쉽게 조작하도록 도와주는 모듈

```javascript
const path = require("path");
```

위와 같이 path 모듈을 가져올 수 있다.

- 주요 path 모듈 변수 및 메서드

    - path.sep : 경로의 구분자를 보여준다.

    > Windows는 \, POSIX(Unix 계열 - MacOS, Linux)는 /

    - path.delimiter : 환경변수 구분자

    > Windows는 세미콜론`;`, POSIX(Unix 계열 - MacOS, Linux)는 콜론`:`

    - path.dirname(path: string) : 파일이 위치한 폴더의 경로를 보여준다.
    - path.extname(path: string) : path에 위치한 파일의 확장자를 보여준다.
    - path.basename(path: string[, ext: string])

        파일의 이름을 보여주는 메서드, 파일의 이름만 표시하고 싶다면 두번째 인자인 ext를 넣어주면 된다.

        ```javascript
        path.basename('/foo/bar/baz/asdf/quux.html');
        // Returns: 'quux.html'

        path.basename('/foo/bar/baz/asdf/quux.html', '.html');
        // Returns: 'quux'
        ```

    - path.parse(path: string) : path를 root, dir, base, ext, name으로 분리
    - path.format(pathObject: object) : path string을 반환하는 메서드, pathObject의 정보를 이용하여 path string으 만든다. pathObject에는 `dir`, `root`, `base`, `name`, `ext`이 있다.

    > pathObject에 `dir`이 있다면 `root`는 무시된다.
    >
    > pathObject에 `name`이 있다면 `ext`와 `name`은 무시된다.

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

    - path.normalize(path: string) : `\`나 `/`를 실수로 여러번 사용하거나 혼용했을 때 정상적인 경로로 바꿔주는 메서드
    - path.isAbsolute(path: string) : 파일의 경로가 절대경로인지 상대경로인지 알려주는 메서드 (절대경로라면 true)
    - path.relative(from: string, to: string) : from에서 to로 가는 경로를 보여주는 메서드
    - path.join([...paths: string]) : 여러 path 인자를 넣으면 하나의 경로로 합쳐준다. 상대경로까지 알아서 처리해준다.
    - path.resolve([...paths: string]) : join 메서드와 비슷하지만 `/`를 만나는 순간 절대경로로 인식하고 앞의 경로는 무시한다.

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

3. url 모듈

이름 그대로 URL을 조작하는 모듈이다.

url처리에는 크게 2가지 방식이 있다. Node v7에서 추가된 WHATWG 방식과 기본의 URL 방식이 있다.

[Day3.md에 URL 관련 학습내용](Day3.md)겨

```javascript
const url = require('url');
```

위와 같은 방식으로 url 모듈을 가져올 수 있다.

`WHATWG` 방식의 URL은 url 모듈의 생성자로 만들 수 있다.
반면 기존의 URL은 url모듈의 URL 속성을 참조한다.

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

- **※ searchParams**

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

 - getAll(key: string) : 키에 해당하는 모든 값을 가져온다.
 - get(key: string) : 키에 해당하는 첫 번째 값을 가져온다.
 - has(key: string) : 해당 키가 있는지 없는지 검사한다.
 - keys() : searchParams의 모든 키를 iterator 객체로 반환
 - values() : searchParams의 모든 값를 iterator 객체로 반환
 - append(key: string, value: string) : 해당 키를 추가, 만약 해당 키가 searchParams에 있다면 유지하고 하나 더 추가
 - set(key: string, value: string) : append와 비슷하지만 해당 킥가 searchParams에 있다면 searchParams에서 해당 키를 모두 지우고 새롭게 추가한다.
 - delete(key: string) : 해당 키를 제거
 - toString(): 조작한 searchParams 객체를 문자열로 만드는 메서드

이와 같이 WHATWG 방식은 `search` 정보(querystring)를 searchParams 객체로 쉽게 다룰 수 있다. 다만, 기존의 URL 방식에서는 새로이 모듈을 사용해야한다.

4. querystring

querystring 모듈은 기존의 노드 url 사용시 search 부분을 사용하기 쉽게 객체로 만드는 모듈이다.

```javascript 
const qs = require("querystring"); 
```

위와 같은 방식으로 querystring 모듈을 가져온다.

querystring 모듈에서는 parse(str: string[, sep: string [, eq: string [, options: object]]]) 메서드와 stringify(obj: object[, sep: string [, eq: string [, options: object]]])를 주로 사용한다.

 - parse(str: string[, sep: string [, eq: string [, options: object]]])
  
    parse 메서드는 str에 있는 query 부분을 객체로 분해해주는 메서드이다.

    - str : 문자열로 된 사이트의 주소
    - sep : query 부분의 구분자 (default : &)
    - eq : query 부분에서 key와 value를 구분하는데 사용하는 구분자 (default : =)
    - options

    decodeURIComponent : query string 안에서 퍼센트 인코딩을 해석하기 위해 사용하는 함수를 설정할 수 있다. (default: querystring.unescape())

    [퍼센트 인코딩이란?](https://ko.wikipedia.org/wiki/퍼센트_인코딩)

    maxKeys : parse 할 수 있는 key의 최대 숫자 설정 (default: 1000)

 - stringify(obj: object[, sep: string [, eq: string [, options: object]]])

  stringify는 분해된 query 객체를 문자열로 다시 조합해주는 메서드이다.

  > ※ 이하 옵션은 parse와 동일하다.

5. crypto 모듈