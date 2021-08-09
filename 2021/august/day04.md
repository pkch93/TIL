# 2021.08.04 TIL - Spring Boot 2.4 static resource 변경 사항

Spring Boot 2.4부터  DefaultServlet은 임베디드 서블릿 컨테이너에 기본적으로 등록되지 않는다.

> [default servlet?](https://zetcode.com/spring/defaultservlet/)

```yaml
server:
    servlet:
        register-default-servlet: true
```

위와 같이 설정하면 기본 등록된다.

## 참고

Spring Boot 2.4 release notes 참고: [https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.4-Release-Notes#default-servlet-registration](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.4-Release-Notes#default-servlet-registration)