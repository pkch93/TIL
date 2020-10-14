# 20.10.14 - OAuth2

네이버 D2 OAuth2: [https://d2.naver.com/helloworld/24942](https://d2.naver.com/helloworld/24942)

OAuth 2.0은 인증에 대한 산업-표준 프로토콜이다. OAuth 2.0은 클라이언트 개발자들에게 인증에 대한 단순함에 집중한다.

OAuth 명세는 IETF OAuth Working Group에 의해 확장되고 발전하고 있다.

OAuth 명세 참고: [https://www.ietf.org/mailman/listinfo/oauth?**cf_chl_jschl_tk**=c1a199f4074cb91b59ba4dfd1fb56aad3c608580-1594293968-0-AW48UoE_13Efw01hAv2KdsbKpf3EQJIKfkLLClpbjhfRR8uhMIOV9mBe9SXnQeJkSjO5U2vBp0_8kqXHuhF8MhIVaSuSWhxKd7q4fZIiXbwkgEu784VdBtjZAu5Hz1cxHKWTMfjsb-KyjHlpm5kHWA2szk6N0nUsEuO2qBfkJJkEC5U_ZqUtAHmfdcm9ZYq\_\_XWhZx57q7hrgDiJ_GXsXJvWu-SYJnoCd3ctpNaepvmSHohF50FE-hgVLy8rU4o3cKJ-WEM_QVIuXfslBNFH0rWch9IybYfdiR77jNnNxydRGHBtwWZA64TcrPh6zlD8pw](https://www.ietf.org/mailman/listinfo/oauth?__cf_chl_jschl_tk__=c1a199f4074cb91b59ba4dfd1fb56aad3c608580-1594293968-0-AW48UoE_13Efw01hAv2KdsbKpf3EQJIKfkLLClpbjhfRR8uhMIOV9mBe9SXnQeJkSjO5U2vBp0_8kqXHuhF8MhIVaSuSWhxKd7q4fZIiXbwkgEu784VdBtjZAu5Hz1cxHKWTMfjsb-KyjHlpm5kHWA2szk6N0nUsEuO2qBfkJJkEC5U_ZqUtAHmfdcm9ZYq__XWhZx57q7hrgDiJ_GXsXJvWu-SYJnoCd3ctpNaepvmSHohF50FE-hgVLy8rU4o3cKJ-WEM_QVIuXfslBNFH0rWch9IybYfdiR77jNnNxydRGHBtwWZA64TcrPh6zlD8pw)
OAuth2 이해하기: [http://www.bubblecode.net/en/2016/01/22/understanding-oauth2/](http://www.bubblecode.net/en/2016/01/22/understanding-oauth2/)

OAuth 2.0은 open authorization 프로토콜로 페이스북, Github과 같은 HTTP 서비스 Provider에게서 client의 자원에 접근할 수 있도록 허용한다.

즉, 서드파티 애플리케이션이 HTTP를 통해 한정된 자원 `resource`에 접근하도록 허용하거나 자원을 소유하도록 허용할 수 있도록 규정한 약속이다. 이때 웹 애플리케이션이나 모바일 애플리케이션과 같은 클라이언트에서 접근 요청이 이뤄진다.

## Roles

OAuth2에서는 4가지 역할을 명시하고 있다.

1. Resource Owner

   보통 애플리케이션을 사용하는 User를 지칭

2. Resource Server

   Client가 접근하고자 하는 데이터를 호스팅하는 서버

3. Client

   서드파티 애플리케이션. 즉, Resource Server에 접근을 요청하는 클라이언트 애플리케이션이다.

4. Authorization Server

   access token을 Client에 발급하는 서버. access token은 Client가 Resource Server에 접근할 때 사용한다.

## Tokens

token이란 Authorization Server에서 발급하는 무작위 문자열 값이다. 이 토큰은 Client가 Authorization Server에 요청할 때 발급된다.

token에는 2가지 타입이 있다.

- access token

  User `Resource Owner`의 데이터에 접근할 수 있도록 만들어주는 토큰. 즉, Resource Server가 보호하고 있는 자원에 접근하기 위해서 필요한 토큰이다. access token이 유출되는 경우, 제3자가 해당 User의 자원에 접근할 수 있는 권한이 생기므로 특히 보안에 유의해야한다.

- refresh token

  access token과 함께 발급되는 토큰. Client가 Resource Server에 자원을 요청할 때마다 사용하는 access token과는 달리 Authorization Server에 만료된 access token을 새롭개 발급하는 용도로 사용하는 토큰이다.

이런 토큰들의 특징으로 access token의 생명주기를 짧게 가져가고 `보통 30분~1시간?` refresh token의 생명주기를 길게 가져가는거 같다.

### access token scope

scope이란 access token의 권한을 한정하기 위해 사용한다. 이 scope은 Authorization Server에서 정한다. Client는 반드시 Authorizaton Server에 애플리케이션에서 사용하고자 하는 scope를 전달해야만 한다.

참고: [http://tools.ietf.org/html/rfc6749#section-3.3](http://tools.ietf.org/html/rfc6749#section-3.3)

### HTTPS

참고로 OAuth2는 Client와 Authorization Server 간의 통신을 할 때 HTTPS를 사용하도록 권장한다. 이는 인증 정보이다 보니 민감 데이터가 HTTP를 통해서 전송되기 때문이다.

HTTPS가 무조건 사용되어야 OAuth2를 사용할 수 있는 것은 아니지만 권장하는 부분이다.

> 실제로 OAuth2를 제공하는 Google은 HTTPS를 사용하는 경우만 사용하도록 지원.
> 국내 서비스인 네이버는 HTTP를 사용해도 지원을 하는걸로 보임

## Client 등록

Client 애플리케이션에서 User의 데이터를 관리하고 있는 Resource Server의 데이터에 접근하기 위해서는 Client 애플리케이션을 Authorization Server에 등록해야한다.

이때 OAuth2 서비스 제공자들은 등록하는 방식, 과정을 자유롭게 설정한다. 이는 OAuth2 프로토콜에서 한정하지는 않는다. OAuth2는 단지 Client가 명시해야하는 파라미터는 무엇인지, Authorization Server가 제공해주어야하는 것은 무엇인지를 정의할 뿐이다.

OAuth2에서 명시한 Client가 제공해야하는 파라미터는 다음과 같다.

1. Application Name

   말 그대로 애플리케이션의 이름

2. Redirect URLs

   access token과 authorization code를 받을 Client의 URL을 의미

3. Grant Type

   OAuth2 인증 방식에 사용할 Grant Type

4. Javascript Origin `optional`

   XMLHttpRequest를 통해 resource server에 요청을 허용할 hostname

반대로 OAuth2에서 명시한 Authorization Server가 제공해야하는 응답은 다음과 같다.

1. Client Id
2. Client Secret

### 네이버의 예시

Naver Developers 애플리케이션 등록: [https://developers.naver.com/apps/#/register](https://developers.naver.com/apps/#/register)

![https://user-images.githubusercontent.com/30178507/96003294-0d2dcd00-0e75-11eb-9fb0-33ca885a9ddf.png](https://user-images.githubusercontent.com/30178507/96003294-0d2dcd00-0e75-11eb-9fb0-33ca885a9ddf.png)

등록시 애플리케이션 이름, Callback URL을 통해서 Redirect URLs를 받는 것으로 보임.

> Grant Type은 따로 지정하지 않는듯..?

값을 입력한 후 등록을 하면 다음과 같은 응답 화면이 나타난다.

![https://user-images.githubusercontent.com/30178507/96003303-0e5efa00-0e75-11eb-8313-de09102c4a4e.png](https://user-images.githubusercontent.com/30178507/96003303-0e5efa00-0e75-11eb-8313-de09102c4a4e.png)

OAuth2에서 명시한 대로 Client ID와 Client Secret을 전달해준다.

## Authorization Grant Types

Grant Type은 Authorization Server에서 access token을 발급받기 위한 인증 타입을 의미한다.
OAuth2에서는 4가지 Grant Type을 제공한다.

### Authorization Code Grant

참고: [https://tools.ietf.org/html/rfc6749#section-4.1](https://tools.ietf.org/html/rfc6749#section-4.1)

이 Grant Type은 Client 애플리케이션이 Web으로 구성된 경우에 사용한다. Authoriztaion Code Grant는 refresh token으로 갱신을 허용하므로 긴 생명주기를 가진 access token을 허용한다.

![https://user-images.githubusercontent.com/30178507/96003305-0f902700-0e75-11eb-8202-199ee51599db.png](https://user-images.githubusercontent.com/30178507/96003305-0f902700-0e75-11eb-8202-199ee51599db.png)

User `Resource Owner`가 Authorization Server에 인증을 하여 받은 Authorization Code를 기반으로 Client와 통신하는 타입

### Implicit Grant

참고: [https://tools.ietf.org/html/rfc6749#section-4.2](https://tools.ietf.org/html/rfc6749#section-4.2)

보통 Javascript와 같은 스크립트 언어로 Client `Angular, React, Vue 등`를 운영중인 애플리케이션에서 사용한다. 이 Grant Type은 refresh token을 허용하지 않는다.

![https://user-images.githubusercontent.com/30178507/96003308-10c15400-0e75-11eb-817d-dc9cc908bd81.png](https://user-images.githubusercontent.com/30178507/96003308-10c15400-0e75-11eb-817d-dc9cc908bd81.png)

Implicit는 Authorization Code와 마찬가지로 Authorization Server에 리다이렉트 되는 것은 동일하다.

다만, Implicit은 URI fragment를 통해 access token을 가진채로 리다이렉트 시킨다. 즉, URI에 access token이 노출되는 구조이므로 나머지 4개의 Grant Type 중 가장 보안에 취약한 구조라고 할 수 있다. 다른 Grant Type을 사용할 수 없을때만 사용해야한다.

### Resource Owner Password Credentials Grant

참고: [https://tools.ietf.org/html/rfc6749#section-4.3](https://tools.ietf.org/html/rfc6749#section-4.3)

이 Grant Type은 Client 애플리케이션과 Authorization Server 간의 절대적인 신뢰가 있어야 가능하다. 주로 Client와 Authorization Server가 동일 권한으로 개발이 된 경우에 사용한다.

![https://user-images.githubusercontent.com/30178507/96003314-11f28100-0e75-11eb-9917-803e3f519f16.png](https://user-images.githubusercontent.com/30178507/96003314-11f28100-0e75-11eb-9917-803e3f519f16.png)

위 Authorization Code와 Implicit와 다른점은 User의 인증 정보가 Client를 통해서 Authorization Server로 흘러간다는 점이다.

### Client Credentials Grant

참고: [https://tools.ietf.org/html/rfc6749#section-4.4](https://tools.ietf.org/html/rfc6749#section-4.4)

주로 Client가 Resource Owner인 경우 사용하는 타입이다.

![https://user-images.githubusercontent.com/30178507/96003322-13bc4480-0e75-11eb-8e0b-b8ce9d50fdeb.png](https://user-images.githubusercontent.com/30178507/96003322-13bc4480-0e75-11eb-8e0b-b8ce9d50fdeb.png)
