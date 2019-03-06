# 2019 02.15 Friday

## 1. Ubuntu

### - 사용자 / 그룹

리눅스는 기본적으로 `다중 사용자 시스템`! 따라서, 하나의 리눅스에 다수의 사용자가 존재할 수 있다.

그리고 모든 사용자는 하나 이상의 그룹에 속해야한다.

리눅스에서 사용자를 관리하는 파일은 `/etc/passwd`이다.

위 `/etc/passwd`의 각 행은 다음과 같은 의미를 같는다.

> 사용자이름:암호:사용자ID:사용자가 속한 그룹ID:추가정보:홈디렉터리:기본셸
>
> 추가정보는 전체이름, 사무실 호수, 직장 전화번호, 집 전화번호, 기타가 있다.

이때 `/etc/passwd`에 저장된 사용자의 비밀번호가 `x`로 표시된 것이 있는데 이는 `/etc/shadow`에 지정되어있다는 의미이다.

그룹 정보는 `/etc/group`에 저장되어있다.

리눅스에서 모든 사용자는 주 그룹 1개에 속해야하며 보조 그룹은 여러 개 속할 수 있다. 주 그룹은 `/etc/passwd`, 보조 그룹은 `/etc/group`에서 확인할 수 있다.

`/etc/group`에 저장된 정보는 다음과 같은 의미를 같는다.

> 그룹 이름:비밀번호:그룹ID:보조 그룹 사용자

### - 사용자 / 그룹 관련 명령어

1. adduser : 사용자를 추가하는 명령어

**※ 사용 예**

* adduser test-user # test-user 사용자를 추가 (단, 추가 정보 입력을 묻는다)
* adduser --uid 1001 test-user2 # test-user2 사용자를 1001 id로 추가
* adduser --gid 1000 test-user3 # test-user3 사용자를 group id가 1000인 그룹에 포함하여 추가
* adduser --home /newtest test-user4 # test-user4 사용자의 홈 디렉터리를 /newtest로 지정하여 추가
* adduser --shell /bin/csh test-user5 # test-user5 사용자의 기본 셸을 /bin/csh로 지정
* adduser Username Groupname # 기존에 존재하는 사용자를 기존에 있는 그룹에 추가

2. passwd : 사용자의 비밀번호 변경

**※ 사용 예**

* passwd test-user # test-user의 패스워드 변경

3. usermod : 사용자의 속성 변경

**※ 사용 예**

* usermod --shell /bin/csh test-user # test-user의 기본 셸을 /bin/csh로 변경
* usermod --groups ubuntu test-user # test-user의 보조그룹에 ubuntu 추가

4. userdel : 사용자를 삭제

**※ 사용 예**

* userdel test-user # test-user라는 이름의 사용자를 삭제. 단, 홈 디렉터리는 삭제하지 못함
* userdel -r test-user # test-user라는 이름의 사용자를 삭제하는 것은 같다. 단, `-r`옵션으로 홈 디렉터리까지 삭제한다.

5. chage : 사용자의 암호를 주기적으로 변경하도록 설정하는 명령어

**※ 사용 예**

* chage -l test-user # 암호 관련 test-user에게 설정된 사항을 확인하는 명령
* chage -m 2 test-user # user에게 설정한 암호를 최소한 사용해야하는 날의 수를 정할 때 `-m`옵션을 사용한다. 즉, 이 명령은 test-user에게 설정한 암호를 최소한 2일은 사용해야한다는 의미이다.
* chage -M 30 test-user # `-m`과는 반대로 최대한 사용할 수 있는 날짜를 `-M` 옵션으로 설정한다. 즉, 위 명령은 test-user에 설정한 암호를 30일까지 사용할 수 있다는 의미이다.
* chage -E 2020/12/31 test-user # `-E` 옵션은 설정한 암호가 만료되는 날짜를 의미한다. 이 명령은 test-user에게 설정하는 암호가 20년 12월 31일에 만료된다는 의미이다.
* chage -W 10 test-user # `-W` 옵션은 암호가 만료되기 전에 경고하는 기간을 의미한다. (default 7일) 즉, 위 명령은 test-user의 암호가 만료되기 10일 전부터 경고메세지를 내보내는 명령이다.

6. groups : 사용자가 속한 그룹을 보여준다.

**※ 사용 예**

* groups : 현재 사용자가 속한 그룹을 알려준다.
* groups test-user : test-user가 속한 그룹을 알려준다.

7. groupadd : 그룹을 추가하는 명령어

**※ 사용 예**

* groupadd new-group # new-group이라는 그룹을 생성하는 명령
* groupadd --gid 1234 new-group # 그룹 id가 1234인 new-group이라는 이름의 그룹을 생성

8. groupmod : 그룹의 속성을 변경하는 명령어

**※ 사용 예**

* groupmod -n(--new-name) test-group test # `-n` 옵션은 그룹의 이름을 바꾸는 옵션이다. 즉, 위 명령은 test-group이라는 이름의 그룹을 test라는 이름으로 바꾸는 옵션이다.
* groupmod -g(--gid) 1234 test-group # `-g` 옵션은 그룹의 id를 바꾸는 옵션이다. 즉, 위 명령은 test-group이라는 이름의 그룹 id를 1234로 바꾸는 명령이다.
* groupmod -p(--password) 1234 test-group # `-p` 옵션은 그룹의 password를 바꾸는옵션이다. 위 명령은 test-group의 password를 1234로 바꾸는 명령이다.

9. groupdel : 그룹을 삭제하는 명령

**※ 사용 예**

* groupdel test-group # test-group 그룹 삭제

10. gpasswd : 그룹의 암호 설정, 그룹 관리 명령

**※ 사용 예**

* gpasswd test-group # test-group 그룹의 암호 지정
* gpasswd -A test test-group # `-A` 옵션은 그룹의 관리자를 지정하는 옵션이다. 즉, test 사용자를 test-group 그룹의 관리자로 지정하는 옵션
* gpasswd -a test test-group # `-a` 옵션은 그룹에 사용자를 추가하는 옵션이다. 즉, test-group 그룹에 test 사용자를 추가하는 옵션이다.
* gpasswd -d test test-group # `-d` 옵션은 그룹에서 사용자를 삭제하는 옵션이다. 즉, test 사용자를 test-group 그룹에서 제거하는 옵션이다.

### - 리눅스 네트워크

랜카드가 리눅스에 장착되면 Ubuntu는 `ens32` 또는 `ens33`으로 인식한다.

> ※ 네트워크 관련 명령
>
> ifconfig `랜카드이름` # 네트워크 설정 정보 출력
>
> ifdown `랜카드이름` # 네트워크 장치 정지
>
> ifup `랜카드이름` # 네트쿼크 장치 가동

다음은 꼭 알아야할 네트워크 관련 명령어이다.

1. nm-connection-editor

네트워크 관련 작업의 대부분을 위 명령으로 처리할 수 있다. `nm-connection-editor`로 자동 IP 주소 또는 고정 IP 주소 설정, IP 주소 / 서브넷마스크 / 게이트웨이 정보 입력, DNS 정보 입력, 네트워크 카드 드라이버 설정, 네트워크 장치 설정을 할 수 있다.

> nm은 network manager의 약자
>
> nmtui를 사용하면 TUI 화면으로도 가능하다.

2. systemctl start|stop|restart|status networking

네트워크 설정 변경 후 변경된 내용을 시스템에 적용하는 명령이다.

즉, `nm-connection-editor`로 네트워크 정보를 바꾼 후에는 꼭 `systemctl restart networking`으로 설정 사항을 시스템에 반영해야한다.

> restart는 stop과 start가 합쳐진 명령

`systemctl status networking`은 현재 작동상태인지 정지상태인지 알려주는 명령이다.

3. ping URL|IP주소

해당 컴퓨터가 네트워크상에서의 응답을 테스트하는 명령어.

다음은 네트워크 설정과 관련된 파일들이다.

1. /etc/NetworkManager/system-connections/유선 연결 1

네트워크 장치에 설정된 네트워크 정보가 모두 들어 있는 파일이다. 에디터(`vi`, `gedit`)로도 직접 편집이 가능하지만 되도록이면 `nm-connection-editor`나 `nmtui`를 사용하여 편집하는 것이 좋다.

> 텍스트 모드에서는 /etc/network/interfaces 파일을 수정

2. /etc/resolv.conf

DNS 서버의 정보와 호스트 이름이 들어있는 파일. 임시로 사용되는 파일로 네트워크 재시작시 내용이 초기화되는 파일이다. 따라서, 영구적으로 DNS 서버 정보를 반영하기 위해서는 `nm-connection-editor` / `nmtui`나 `/etc/network/interfaces` 파일을 편집하여 반영해야한다.

> 참고로 DNS 서버는 URL의 이름을 IP 주소로 바꾸는 역할을 한다. URL을 입력하여 해당 주소로 접근할 때 바로 접근하는 것이 아니라 `/etc/resolv.conf`에 설정된 DNS 서버에게 URL의 IP 주소를 물어본다. 해당하는 DNS 서버에서 IP 주소를 알려주면 그제서야 알아낸 IP주소로 접근할 수 있다.

3. /etc/hosts

현 컴퓨터에 호스트 이름과 FQDN이 들어있는 파일

### - 서비스와 소켓

서비스는 평상시에도 늘 가동하는 서버 프로세스, 소켓은 필요할 때만 작동시키는 서버프로세스를 의미한다.

서비스와 소켓은 `systemd`라는 서비스 매니저 프로그램으로 작동시키거나 관리한다.

서비스는 시스템과 독자적으로 구동되어 제공하는 프로세스를 의미한다. (웹서버, FTP 서버 등)

> 보통 systemctl 명령으로 사용한다.

서비스의 실행 스크립트 파일은 `/lib/systemd/system/` 디렉터리에서 `서비스이름.service`라는 이름으로 확인 가능하다.

소켓의 경우도 `systemd`에서 관리한다. 소켓과 관련된 실행 스크립트 파일은 `/lib/systemd/system/` 디렉터리에서 `소켓이름.socket`으로 확인 가능하다.