# 2020.08.10 Monday - Redis CLI Basic

redis cli docs: [https://redis.io/topics/rediscli](https://redis.io/topics/rediscli)

redis cli는 말그대로 command 명령어로 redis를 다룰수 있는 툴이다. 따라서 터미널에서 직접 redis로 명령을 내릴 수 있는 툴이다.

redis cli는 크게 2가지 모드를 지원한다. 하나는 REPL을 지원하는 `interactive` 모드, 다른 하나는 redis cli의 인자로 보내 실행하고 결과를 표준 출력으로 프린트하는 명령모드이다.

## Command line 사용하기

redis cli는 기본적으로 `127.0.0.1` host와 `6379` port를 사용한다.

```text
redis-cli
```

즉, 위 명령을 입력하면 기본적으로 `127.0.0.1:6379`에 접속하는 것이다.

만약 특정 host를 명시하고 싶다면 `-h` 옵션을 사용한다.

```text
redis-cli -h redis15.localnet.org
```

위 명령은 `redis15.localnet.org`로 접근하는 명령이다. 이때도 port는 기본적으로 `6379`이다. port도 명시하고 싶다면 `-p` 옵션을 사용한다.

만약 접근하려는 redis 인스턴스가 비밀번호로 막혀있다면 `-a <password>` 옵션을 통해 인증을 수행할 수 있다. 단, 이렇게 접근할 때마다 `-a` 옵션으로 접근하는 것이 번거롭다면 `REDISCLI_AUTH` 환경변수를 등록하여 인증을 쉽게 수행할 수 있다.

이렇게 각각 `-h -p -a` 등의 옵션으로 따로따로 제공하여 사용할 수 있지만 `-u` 옵션으로 uri를 통해 한번에 위 값들을 제공할 수 있다.

## SSL/TLS

기본적으로 redis cli는 TCP 프로토콜로 네트워크 통신을 한다. 또한 SSL/TLS를 사용하여 연결을 수행할 수 있다. 이를 지원하는 옵션은 `--tls`이다. 또한 `--cacert`나 `--cacertdir`와 함께 신뢰할 수 있는 자격 번들이나 폴더를 설정할 수 있다.

만약 연결하려는 redis 서버가 클라이언트 사이드의 자격증명을 사용한 인증을 요구한다면 `--cert`나 `--key` 옵션을 통해 redis 서버가 요구하는 private key를 제공해야한다.

## 다른 프로그램에서 input 가져오기

다른 command에서 input 값을 가져오기 위한 방법으로 redis cli는 2가지 방법을 제공한다.

하나는 표준입력으로 읽어오는 값을 redis cli의 마지막 인자로 사용하는 방법이다.

```text
$ redis-cli -x set foo < /etc/services
OK
$ redis-cli getrange foo 0 50
"#\n# Network services, Internet style\n#\n# Note that "
```

위와 같이 `set` 명령에 첫번째 인자는 key, 두번째 인자는 value이다. 단, 위 set 명령에서는 두번째 인자는 표준입력을 사용하였다. 이렇게 표준입력을 input으로 가져오기 위해서는 `-x` 옵션을 사용해야한다.

다른 방법은 text file을 읽은 후 redis 명령의 연속으로 사용하는 방법이다.

```text
$ cat /tmp/commands.txt
set foo 100
incr foo
append foo xxx
get foo
$ cat /tmp/commands.txt | redis-cli
OK
(integer) 101
(integer) 6
"101xxx"
```

위와 같이 `/tmp/commands.txt`는 redis 명령들이 나열되어 있다. 이 파일의 내용을 redis-cli로 차례대로 읽어 수행하도록 만들 수 있다.

## 연속으로 같은 command 수행하기

redis cli는 같은 명령을 여러번 수행하거나 수행하는 사이에 일시정지를 할 수 있도록 옵션을 제공한다.

`-r <count>` 옵션을 사용하면 count 만큼 명령을 반복한다. 반면 `-i <delay>`를 하면 delay만큼 실행을 지연한다. 이때 delay의 단위는 초이다.

```text
$ redis-cli -r 5 incr foo
(integer) 1
(integer) 2
(integer) 3
(integer) 4
(integer) 5
```

위와 같이 `redis-cli -r 5 incr foo`를 하면 `incr foo` 명령이 5번 반복한다.

## redis cli로 대량의 데이터 넣기

참고: [https://redis.io/topics/mass-insert](https://redis.io/topics/mass-insert)

## CSV로 추출하기

redis cli는 redis에서 관리하는 데이터를 csv 형태의 파일로 추출하도록 지원한다.

```text
$ redis-cli lpush mylist a b c d
(integer) 4
$ redis-cli --csv lrange mylist 0 -1
"d","c","b","a"
```

단, 현재는 전체 DB를 csv로 추출하는 것은 불가하며 단일 명령을 수행하여 csv로 추출하는 것만 가능하다.

## Lua script 실행 지원

redis 3.2 이후부터 Lua 스크립트를 통해 debbugging 기능을 사용할 수 있도록 지원한다. 그리고 redis cli는 debbugger를 사용하지 않아도 Lua 스크립트 실행이 가능하도록 지원한다. 참고: [https://redis.io/topics/ldb](https://redis.io/topics/ldb)

```text
$ cat /tmp/script.lua
return redis.call('set',KEYS[1],ARGV[1])
$ redis-cli --eval /tmp/script.lua foo , bar
OK
```

