# 19.11.27 Wendesday

# 1. 메모리 사용량 확인

리눅스에서 메모리 사용량을 가장 빠르게 확인할 수 있는 명령이 바로 **free**이다. free로 전체 메모리, 사용중인 메모리, buffered와 cached로 명명되는 캐싱 영역의 용량을 확인하는 데 사용된다.

![](https://user-images.githubusercontent.com/30178507/69725813-6825db00-1162-11ea-8550-d2b7c90e6ba9.png)

free 명령을 터미널에서 실행시 결과화면

기본적으로 KB 단위로 메모리 용량을 보여준다. 만약 옵션을 다르게 주면 이에 맞춰서 다르게 표시해준다.

### free 용량 옵션

- -k: KB 단위로 메모리 용량을 보여주는 옵션 (default)
- -m: MB 단위로 메모리 용량을 보여주는 옵션
- -g: GB 단위로 메모리 용량을 보여주는 옵션

## free 컬럼의 의미

- total: 총 메모리의 용량
- used: 현재 사용중인 메모리의 용량
- free: 시스템에서 아직 사용하고 있지 않은 메모리의 용량.
- shared: 프로세스 사이에서 공유하고 있는 메모리의 용량.
- buff/cache: 버퍼와 캐시에 사용되고 있는 메모리의 용량.

    buff/cache 참고: [https://blog.asamaru.net/2016/01/19/centos-emptying-the-buffers-cache/](https://blog.asamaru.net/2016/01/19/centos-emptying-the-buffers-cache/)

- available: 새로운 프로세스나 현재 사용중인 프로세스에 할당할 수 있는 메모리의 용량

    free vs available 참고: [https://askubuntu.com/questions/867068/what-is-available-memory-while-using-free-command](https://askubuntu.com/questions/867068/what-is-available-memory-while-using-free-command)

- 스왑(Swap): 스왑 영역에 대한 정보를 보여주는 줄이다.
첫번째 값은 스왑 영역 전체 용량을 보여준다. 현재 시스템에서는 4GB 가량을 스왑 영역 용량으로 사용한다.
두번째 값은 스왑 영역 중 실제 사용 용량을 보여준다. 현재는 사용하지 않고 있다.
마지막 값은 사용하지 않는 스왑영역이다. 사용중인 영역이 없으므로 전체 스왑영역 그대로 값을 가진다.

## buff/cache 영역

커널은 블록 디바이스라고 부르는 **디스크**에서 데이터를 읽거나 저장한다. 단, 디스크는 매우 느린 저장매체이므로 디스크에 대한 요청을 기다리는 시간이 상당히 많이 소요되고 이로 인해 시스템 부하가 일어나기도 한다. 이에 대한 해결 방안 중 하나로 **빠르게 접근할 수 있는 메모리 일부 영역을 캐싱 영역으로 할당**하여 사용한다.

이때 사용되는 캐싱 영역을 buffers와 cached라고 부른다. 그럼 각각은 어떻게 다른걸까?

## buffers와 cached 영역의 차이점

buffers와 cached의 차이를 알려면 커널이 블록 디바이스에서 데이터를 읽는 과정을 알아야한다.

커널은 블록 디바이스에 접근할 때 데이터가 위치한 특정 블록의 주소를 넘겨주고 블록 디바이스에 커널로부터 넘겨받은 주소의 데이터를 커널에 전달한다. 이때 커널이 읽어야 할 데이터가 파일의 내용이라면 커널은 **bio 구조체를 만들고 해당 구조체에 Page Cache 용도로 할당한 메모리 영역은 연결**한다. 그리고 bio 구조체는 **디바이스 드라이버와 통신하며 디스크에서 읽은 데이터를 Page Cache에 저장**한다.

만약 super block, inode block과 같은 파일 시스템을 관리하는 메타 데이터를 읽는다면 bio 구조체를 사용하는 것이 아니라 `_get_blk` 커널함수를 사용하여 블록 디바이스와 직접 통신한다. 이때 가져온 메타 데이터는 **Buffer Cache**에 저장한다.

즉, Page Cache는 파일의 내용을 담는 캐시, Buffer Cache는 파일 시스템의 메타 데이터를 저장한 캐시로 정리할 수 있다.

### buffers와 cached의 의의

결국 buffers, cached 캐시 영역을 사용하는 이유는 **I/O 성능 향상을 위해 커널이 사용하는 영역**이다. 메모리가 부족하면 커널은 해당 영역을 자동으로 반환하므로 free 영역에서도 해당 영역을 제외한 영역을 실제 가용영역으로 계산한다.
