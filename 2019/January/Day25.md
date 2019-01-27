# 2019 01.26 Saturday

1. express.js API 서버

    - robots.txt NotFound error

        express 서버를 키고 api를 요청하면 처음에 항상 `robots.txt`를 찾을 수 없다는 NotFound error (404 에러)가 나타난다. 이에 `robots.txt`가 무엇인지? 해결책은 무엇인지 찾아보았다.

        * robots.txt?

            사이트의 `root directory`에 존재하는 txt 파일. 검색엔진 크롤러와 스파이더(crawler의 또다른 말)에 대한 명세를 기록한 txt 파일이다. (내가 방문하거나 가져가길 원하는 파일 / 다른사람이 방문하거나 가져가길 원하지 않는 파일에 대한 명세)

        `robots.txt`를 이해하기 위해서는 crawling이 무엇인지 알아야한다.  

        * crawling과 crawler

            crawler는 보통 시스템적으로 WEB 탐색을 목적으로 사이트를 살펴보는 인터넷 봇을 의미한다. web crawler는 보통 방문한 사이트의 모든 페이지 복사본을 생성하는데 사용되며 검색엔진은 생성된 페이지를 빠른 검색을 위해 인덱싱한다. 또한 웹페이지의 특정 형태의 데이터를 수집하는 데에도 사용한다.

            crawler는 방문한 시스템의 자원을 사용한다. 또한 승인 없이 사이트 방문이 이뤄지기 때문에 crawling 대상 사이트에 많은 자원을 소모할 수 있고 이에 따른 loading 문제, schedule 문제 등이 나타날 수 있다. 때문에 robots.txt를 파일로 internet bots(crawler들)이 검색할 수 있는 부분을 설정할 수 있다.

        ![Architecture of a web crawler](images/WebCrawlerArchitecture.svg)

        위 crawler 내용을 보면 `robots.txt`는 해당 사이트가 감추고 싶어하는 데이터를 접근하지 못하도록 막고 크롤러로 인한 대역폭을 아낄 수 있도록 만들어준다.

        > `robots.txt`는 반드시 웹사이트 당 하나만 있어야한다. document root와 상응하는 곳에 `robots.txt`가 위치해야한다.

        * robots.txt 작성법 (syntax)

            ```
            User-agent: * 
            Disallow: /cgi-bin/ 
            Disallow: /tmp/ 
            Disallow: /~different/
            ```

            위와 같이 `User-agent`와 `Disallow`로 이뤄진다.

            [User-agent 참고](https://www.popit.kr/%EB%82%B4-%EC%84%9C%EB%B2%84%EC%97%90%EB%8A%94-%EB%88%84%EA%B0%80-%EB%93%A4%EC%96%B4%EC%98%A4%EB%8A%94%EA%B1%B8%EA%B9%8C-%EC%8B%A4%EC%8B%9C%EA%B0%84-user-agent-%EB%B6%84%EC%84%9D%EA%B8%B0/)

            `Disallow`에는 crawler가 접근하길 원치않는 폴더나 파일의 경로가 들어간다. `Disallow`는 하나에 한개의 폴더 / 파일을 설정하므로 여러개가 필요하다면 `Disallow`를 여러개 지정해야한다.

    [robots.txt 참고](https://www.namecheap.com/support/knowledgebase/article.aspx/9463/2225/what-is-a-robotstxt-file-and-how-to-use-it)

    [web crawler 위키백과](https://en.wikipedia.org/wiki/Web_crawler)

    - javascript (ES6) for in vs for of

    [for in vs for of 참고](https://jsdev.kr/t/for-in-vs-for-of/2938)