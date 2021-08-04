# 2021.07.28 TIL - Amazon FSx

Amazon FSx는 Microsoft Window 스토리지나 Lustre와 같은 서드파티 고성능 파일 시스템 서비스이다. 완전 관리형 서비스로 Windows File Server나 Lustre 파일 시스템을 지원한다.

## Amazon FSx for Windows File Server

![](https://user-images.githubusercontent.com/30178507/128176659-e516dd41-2dac-482c-9a9c-b5f6bb1ef143.png)

Amazon FSx for Windows File Server는 업계 표준 SMB `서버 메세지 블록` 프로토콜을 통해 엑세스 가능한 고도로 안정적이고 확장가능한 완전관리형 파일 스토리지 서비스를 제공한다.

Window Server에 구축되며 사용자 할당량, 최종 사용자 파일 복원, Microsoft Active Directory `AD` 통합과 같은 광범위한 관리 기능을 제공한다.

SMB 프로토콜을 기본적으로 지원하기 때문에 Window 기반의 애플리케이션과 완벽히 호환된다. Window 뿐만 아니라 Linux나 MacOS에서도 엑세스 가능하다.

## Amazon FSx for Lustre

Amazon FSx for Lustre는 Lustre 파일 시스템을 활용하여 구현된 고성능 스토리지이다. Lustre가 슈퍼컴퓨터에 사용되는 파일 시스템인만큼 높은 퍼포먼스를 보여준다.

따라서 머신러닝, HPC `고성능 컴퓨팅`, 동영상 렌더링, 금융 시물레이션과 같은 워크로드에서 많이 사용된다.

> Lustre 참고: [https://ko.wikipedia.org/wiki/러스터_(파일_시스템)](https://ko.wikipedia.org/wiki/%EB%9F%AC%EC%8A%A4%ED%84%B0_(%ED%8C%8C%EC%9D%BC_%EC%8B%9C%EC%8A%A4%ED%85%9C))

Lustre는 POSIX와 호환이 되기 때문에 리눅스 애플리케이션에 완벽하게 호환한다. 읽기 후 쓰기 일관성 확인 기능을 제공하며 파일 잠금 기능도 제공해준다.

### 병렬 파일 시스템

Lustre가 병렬 분산 파일 시스템이기 때문에 Lustre를 활용하고 있는 Amazon FSx for Lustre도 마찬가지로 병렬 처리를 지원한다.

### S3 데이터 사용 편의성 제공

Amazon FSx for Lustrsms S3 버킷에 연결이 가능하다.

## 참고

Amazon FSx: [https://docs.aws.amazon.com/ko_kr/fsx/index.html](https://docs.aws.amazon.com/ko_kr/fsx/index.html)