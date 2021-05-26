# 2019 02.10 Sunday

## 1. Ubuntu

### .lck와 .bak

ubuntu를 보면 .lck 확장자와 .bak 확장자 파일을 볼 수 있다.

.lck는 패키지로 추가할 수 있는 sw가 하나 이상 실행되는 것을 막기위해 사용하는 확장자이다. \(lock file\)

[lock file 참고](https://askubuntu.com/questions/530598/what-is-the-purpose-of-lock-file)

.bak은 백업 파일을 뜻하는 확장자이다.

[backup file 참고](https://askubuntu.com/questions/672109/how-to-use-bak-file)

### - 패키지 설치

최근에 Ubuntu에서 패키지 설치에 가장 많이 사용되는 명령어는 `apt-get`이다. `apt-get` 이전에는 `dpkg(debian-package)`를 많이 사용하였으나 `apt-get`이 `dpkg`의 기능을 담고 있으므로 최신버전의 `ubuntu`에서는 `apt-get`이 많이 사용된다.

> ubuntu는 데비안 리눅스에서 파생되었다.

* dpkg

  초창기 리눅스에서는 새로운 프로그램 설치가 어려웠다. 때문에 초보자에게는 프로그램 설치 또한 쉬운일이 아니었다. 이런 문제점을 개선하기위해 데미안 리눅스에서는 `Windows`의 `setup.exe`와 같이 프로그램을 설치한 후 바로 실행할 수 있는 `deb` 확장자 설치파일을 제작하였다. 이를 `패키지`라고 부른다.

  `deb` 파일의 형식은 패키지이름_버전-개정번호_아키텍처.deb 로 이뤄진다.

  아래는 `dpkg` 명령 사용시 자주 사용되는 옵션이다.

  1. dpkg -i\(--install\) 패키지이름.deb

     해당 패키지를 설치

  2. dpkg -r\(--remove\) 패키지이름

     현재 ubuntu에 설치된 패키지를 삭제

  3. dpkg -P\(--purge\) 패키지이름

     기존에 설치된 패키지와 설정파일 삭제

  4. dpkg -l 패키지이름

     설치된 패키지에 대한 정보를 보여준다.

  5. dpkg -L 패키지이름

     패키지가 설치한 파일 목록을 보여준다.

  6. dpkg --info 패키지이름.deb

     현재 설치되지 않은 패키지 파일에 대한 정보를 보여준다.

     리눅스에서 프로그램 설치를 편리하게 해준 `dpkg`도 단점이 존재한다. 패키지에는 해당 패키지를 구성하는 다른 패키지가 있을 수 있다\(의존 패키지\). 이 때 `dpkg`는 의존 패키지가 현재 리눅스에 없다면 해당 패키지를 다운받지 못한다. 때문에 이런 불편함을 개선한 명령어인 `apt-get`이 나타나게 되었다.

* apt-get

  `apt-get`은 `dpkg`의 의존 패키지 문제를 완전히 해결해준다. 어떤 패키지를 설치할 때 해당 패키지에 의존 패키지를 자동으로 먼저 설치한 후 해당 패키지를 설치한다.

  추가로 `dpkg` 명령은 설치하려는 `deb` 파일이 DVD에 있거난 인터넥에서 미리 다운로드를 받은 후 설치해야한다. 하지만 `apt-get`은 ubuntu에서 제공하는 `deb` 파일 저장소에서 설치할 `deb` 파일은 물론이고 해당 파일과 의존성 있는 다른 `deb` 파일까지 다운로드한 후 설치해준다. 때문에 패키지 설치시 의존 패키지 문제를 고민하지 않아도 되는게 바로 `apt-get`이다.

  > `apt-get`의 deb 파일 저장소 주소는 `/etc/apt/sources.list`에서 관리한다.

  아래는 `apt-get` 명령 사용법이다.

  1. apt-get install 패키지이름

     패키지 설치 명령

  2. apt-get update

     `/etc/apt/sources.list`파일이 수정됐을때 반영하기 위해 사용하는 명령 \(패키지 목록 업데이트\)

  3. apt-get remove 패키지이름

     설치된 패키지를 삭제하는 명령

  4. apt-get purge 패키지이름

     설치된 패키지를 설정파일 포함하여 삭제하는 명령

  5. apt-get autoremove

     사용하지 않는 패키지 모두 삭제

  6. apt-get clean / apt-get autoclean

     설치할 떼 내려받기한 파일 및 과거의 파일 삭제

     > `apt-cache`는 패키지 설치 전에 패키지 정보나 의존성 문제를 미리 확인할 수 있다.

  7. apt-cache show 패키지이름

     패키지 정보 확인

  8. apt-cache depends 패키지이름

     해당 패키지의 의존성 정보

  9. apt-cache rdepends 패키지이름

     해당 패키지를 의존 패키지로 가지는 패키지들 목록을 확인할 수 있다.

* apt-get 작동 원리와 설정

  `apt-get`의 설정파일은 `/etc/apt`에서 관리한다. 이때 제일 중요한 파일이 바로 `sources.list`이다. 이 파일에는 `apt-get` 실행시 인터넷에서 해당 패키지 파일을 검색하는 네트워크 주소가 있다.

  `apt-get`으로 패키지 설치 시 다음과 같은 순서로 이뤄진다.

  1. 먼저 `/etc/apt/sources.list`에서 저장소 주소를 확인한다.
  2. Ubuntu 패키지 저장소에서 패키지 설치와 관련된 패키지 목록을 요청한다.
  3. 설치와 관련된 패키지 목록만 다운로드. \(실제 패키지 파일을 다운로드하는 것이 아니라 패키지의 이름만 다운로드\)
  4. 사용자에게 설치 진행을 묻는다.
  5. y 입력시 Ubuntu 패키지 저장소에 패키지 다운로드를 요청한다.
  6. 해당 패키지 파일을 다운도르하고 자동 설치한다.

     이때 Ubuntu 패키지 저장소에는 4가지 유형의 저장소가 있다. \(`main`, `universe`, `restricted`, `multiverse`\)

  7. main : Ubuntu에서 공식적으로 지원하는 무료 SW
  8. universe : Ubuntu에서 공식적으로 지원하지 않는 무료 SW
  9. restricted : Ubuntu에서 공식적으로 지원하는 유료 SW
  10. multiverse : Ubuntu에서 공식적으로 지원하지 않는 유료 SW

      만약 유료 SW 사용을 원치 않는다면 `/etc/apt/sources.list`에서 `restricted`와 `multiverse` 저장소를 주석처리하거나 삭제하면 된다.

### - 파일 압축 및 묶기

리눅스에서는 xz, bz2, gz, zip, Z 등을 압축파일의 확장명으로 사용한다. 최근에는 주로 xz, bz2를 사용한다.

* 압축
* xz

  확장명 `xz`로 압축 및 풀때 사용한다.

  **※ 사용법**

  1. xz 파일이름 : 해당 파일을 `파일이름.xz`로 압축한다. 이때 기존의 파일은 제거된다.
  2. xz -d 파일이름.xz : d는 decompress라는 의미로 압축을 푼다는 의미이다. 즉, `파일이름.xz`를 파일이름으로 만든다는 의미이다.
  3. xz -l 파일이름.xz : `파일이름.xz` 압축파일에 포함된 파일 목록과 압축률을 출력한다.
  4. xz -k 파일이름 : k는 keep이라는 의미로 압축시 기존 파일을 유지할때 사용한다.

* bzip2

  확장명 `bz2`로 압축 및 풀때 사용

  사용법 및 옵션은 -l을 제외하고 `xz`와 동일

* gzip

  확장명 `gz`로 압축 및 풀때 사용

* zip / unzip

  windows와 호환되는 `zip` 형식으로 압축\(`zip`\) 및 풀어줄때\(`unzip`\) 사용한다.

  **※ 사용법**

  1. zip 압축파일이름.zip 압축할파일이름 : 기존 파일은 유지한 채로 `zip`형식으로 압축
  2. unzip 압축파일이름.zip : 압축 파일은 유지한 채로 압축해제

* 묶기

  windows에서는 여러 파일을 압축시 묶기 + 압축이 동시에 진행된다. 다만, Unix 계열에서는 파일 압축과 묶기는 별개의 작업이다.

* tar

  파일을 묶는 명령으로 확장자명 tar 형태의 파일이 나타난다.

  **※ 사용**

  * c : 새로운 묶음을 만듦
  * C : 묶음 풀 때 지정된 디렉터리에 압축을 푼다. 지정하지 않으면 묶을때와 동일한 폴더에 압축이 풀린다.
  * x : 묶인 파일을 푼다.
  * t : 묶음을 풀기 전 묶인 경로를 보여준다.
  * f\(`필수!`\) : 묶음 파일 이름 지정 \(tar는 테이프 장치 백업이 기본이므로 생략시 테이프로 보내진다.\)
  * v : 파일이 묶이거나 풀리는 과정을 보여준다.
  * J : tar + xz
  * z : tar + gzip
  * j : tar + bzip2

### - 파일 위치 검색

1. find '경로' '옵션' '찾을 조건' action

   해당 경로의 조건에 해당하는 파일을 검색하는 명령어

   > 옵션 : -name\(파일이름\), -user\(소유자\), -newer\(전, 후\), -perm\(허가권\), -size\(크기\)
   >
   > action : -print\(default\), -exec\(외부 명령 실행\)

   **※ 사용**

   find /etc -name \*.conf : /etc 디렉터리 내부 및 하위 디렉터리에 확장자 명이 conf인 파일 검색 find /home -user park : /home 디렉터리 내부에 소유자가 park인 파일 검색 find .. -perm 777 : 상위 디렉터리 하위에 허가권이 777인 파일 검색 find /tmp -size +10k -size -100k : /tmp 디렉터리 하위에 10kb보다 크다 100kb보다 작은 파일 검색

   > 참고! -size 검색시 크기 단위를 `b(default, 512-byte block)`, `c(bytes)`, `w(two-byte words)`, `k(1024bytes, kilobytes)`, `M(1048576bytes, megabytes)`, `G(1073741824bytes, gigabytes)`로 줄 수 있다.
   >
   > 그리고 해당 크기에 +를 붙이면 그 크기보다 큰 파일을, -는 그 크기보다 작은 파일을 검색한다. 따라서 `find /tmp -size +10k -size -100k`가 tmp 디렉터리 하위에 10kb보다 크고 100kb보다 작은 파일을 찾는 명령이 된다.

   find ~ -size 0k -exec ls -l { }  : 현재 사용자의 홈 디렉터리 하위에 파일 크기가 0인 파일의 목록을 상세히 출력

   find /home -name \*.swp -exec rm {}  : /home 디렉터리 하위에 swp 확장자 파일을 지우는 명령

   > 참고! `-exec`는 외부 명령의 시작을 알려주고 `\`는 끝을 알려준다. 그리고 -exec 앞의 명령 결과가 `{ }`로 들어간다.

2. which 실행파일 이름

   `PATH`에 설정된 디렉터리만 검색, 절대 경로를 포함한 위치를 검색한다.

3. whereis 실행파일 이름

   실행파일, 소스, man 페이지 파일까지 검색

4. locate 파일이름

   파일목록 db에서 검색하므로 매우 빠름. 다만, updatedb 명령을 1회 실행해야하며 updatedb 명령 이후에 설치된 파일은 검색되지 않으므로 updatedb 명령을 하고 locate 명령으로 파일을 찾아야한다.

### - 예약설정

* cron

  주기적으로 반복되는 일을 자동으로 실행하도록 시스템 작업을 예약하는 것.

  `cron` 관련 데몬\(서비스\)는 `crond`이며 관련 설정 파일은 `/etc/crontab`이다.

  > `/etc/crontab`의 형식은 `분 시 일 월 요일 사용자 실행명령`이다.

* at

  cron이 주기적으로 반복되는 작업을 예약한다면 at은 일회성 작업 예약시 사용한다.

  **※ 사용**

  1. at 시간

      ex\) at 2:00pm tomorrow

  2. 프롬프트에 예약 명령어 입력 후 enter

      ex\) at&gt; reboot

     ```text
      at>
     ```

  3. 완료되면 ctrl + D

     > 예약확인 : at -l
     >
     > 취소 : atrm 작업번호

