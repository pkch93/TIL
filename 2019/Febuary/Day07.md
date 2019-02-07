# 2019 02.07 Thursday

1. python scraping

urllib는 python의 표준 라이브러리로 url을 다루는 모듈이다.

그 중에서도 `urllib.request` 모듈은 웹 사이트에 있는 데이터에 접근하는 기능을 제공한다. 또한 인증, 리다이렉트, 쿠키와 같은 다양한 요청/처리를 도와준다.

- urllib.request

파일 다운로드시 `urllib.request`의 `urlretrieve()` 함수를 사용한다.

아래는 urllib를 사용하여 피자 이미지를 가져오는 예시이다.

```python
import urllib.request as req

url = "https://cdn.dominos.co.kr/admin/upload/goods/20180827_eMt877Eq.jpg"
savename = "pizza.png"

req.urlretrieve(url, savename)
```
urllib.request의 `urlretrieve`의 첫번째 인자는 가져올 데이터의 url, 두번째 인자는 저장시 사용될 이름이 들어간다. 이를 통해 위 예시에 있는 피자 이미지를 `pizza.png`로 현재 폴더에 저장하게 된다.

- request.urlopen()

이전에 `request.urlretrieve()`로 파일을 곧바로 저장했다면 `urlopen`은 파이썬 메모리 위에 데이터를 올릴 수 있도록 도와준다. (즉, 변수에 저장 가능하도록 만들어 준다.)

```python
import urllib.request as req

url = "https://cdn.dominos.co.kr/admin/upload/goods/20180827_eMt877Eq.jpg"
savename = "pizza2.png"

img = req.urlopen(url).read()

with open(savename, mode="wb") as f:
    f.write(img)
```
위와 같이 url 데이터를 읽어 img 변수에 저장한 후 파일로 저장할 수 있다.

- urllib.parse.urlencode()

주로 요청 전용 매개변수를 생성할 때 사용한다.

- BeautifulSoup으로 정규식 사용하기

정규표현식을 사용하기 위해 re 모듈을 사용한다.

re.complie()을 활용하면 정규식으로 웹 스크래핑이 가능하다. 

```python
from bs4 import BeautifulSoup
import re

soup = BeautifulSoup(url, "html.parser")

soup.find_all(href=re.complie(r"^http://"))
```

위와 같이 코드를 짜면 url의 문서에서 href가 http://로 시작하는 모든 요소를 추출할 수 있다.

- 링크에 있는 것 모두 내려받기

`a` 태그가 상대경로일 경우, `a` 태그의 경로가 HTML인 경우 추가적인 처리를 해주어야한다.

상대경로인 경우는 절대경로로 변환해야 해당 경로에 데이터를 가져올 수 있다. 이 경우는 `urllib.parse.urljoin()`을 활용하여 변환할 수 있다.

urljoin에는 2가지 인자가 들어간다. 첫번째는 base url, 두번째는 상대경로가 들어간다. 이때 두번째 인자에 절대경로가 들어가는 경우 첫번째 base url은 무시하고 두 번째 인자의 절대경로로 url이 만들어진다.

두번째 html의 경우는 재귀적으로 파악하여 파일을 다운받을 수 있다.

다음과 같은 순서로 연결된 html 파일을 다운받을 수 있다.

1. html 분석
2. 링크 추출
3. 각 링크 대상에 urljoin을 통해 경로 추출
4. 파일 다운로드
5. 경로가 html이라면 재귀적으로 돌아가 처음부터 다시 실행
