# 2021.05.21 TIL - JDK 11 JAXB 관련 이슈

JDK의 기본으로 들어있던 JAXB 관련 라이브러리들이 JDK 9부터는 별도 Java 모듈화를 사용하여 분리되었고 JDK 11부터는 JDK에서 제거되었다.

JAXB는 Java EE의 기능이므로 Java SE에서 제외하고자 Java 팀에서 결정하였고 이에 따라 Java EE와 CORBA 모듈이 제거되었다. 

> [JEP 320](http://openjdk.java.net/jeps/320#Java-EE-modules) 참고

## Hibernate 5.4 미만 사용시 주의사항

Hibernate 5.3.3부터 JDK 11를 공식 지원하는데 이때 이슈가 하나 있다. Hibernate에서 JAXBException를 쓰곤 하는데 JDK 11을 사용하면 JAXBException이 클래스패스에 없으므로 NoClassDefFoundError가 발생한다.

```text
java.lang.NoClassDefFoundError: javax/xml/bind/JAXBException
        at org.hibernate.boot.cfgxml.internal.ConfigLoader$1.initialize(ConfigLoader.java:41)
        at org.hibernate.boot.cfgxml.internal.ConfigLoader$1.initialize(ConfigLoader.java:38)
        at org.hibernate.internal.util.ValueHolder.getValue(ValueHolder.java:55)
        at org.hibernate.boot.cfgxml.internal.ConfigLoader.loadConfigXmlResource(ConfigLoader.java:57)
        at org.hibernate.boot.registry.StandardServiceRegistryBuilder.configure(StandardServiceRegistryBuilder.java:163)
        at org.hibernate.boot.registry.StandardServiceRegistryBuilder.configure(StandardServiceRegistryBuilder.java:152)
        at net.sf.basedb.core.HibernateUtil.init1(HibernateUtil.java:206)
        at net.sf.basedb.core.Application.start(Application.java:514)
        at net.sf.basedb.core.Install.createTables(Install.java:131)
        at net.sf.basedb.install.InitDB.main(InitDB.java:75)
Caused by: java.lang.ClassNotFoundException: javax.xml.bind.JAXBException
        at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:583)
        at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)
        ... 10 more
```

이 이슈는 Hibernate 5.4부터 해결이 되었으며 Spring Boot를 사용하는 경우 Hibernate 의존성을 제공해주는 starter인 `spring-boot-starter-data-jpa`에서 `javax.xml.bind:jaxb-api`가 포함되어 있기 때문에 문제없이 사용할 수 있다.

### 참고

JDK 11에서 java.xml.bind 관련 에러가 나는 경우: [https://luvstudy.tistory.com/61](https://luvstudy.tistory.com/61)

JDK 11에서 JAXB 사용하기: [https://m.blog.naver.com/PostView.naver?blogId=kgw1988&logNo=221383692381&proxyReferer=https:%2F%2Fwww.google.com%2F](https://m.blog.naver.com/PostView.naver?blogId=kgw1988&logNo=221383692381&proxyReferer=https:%2F%2Fwww.google.com%2F)

JEP 320: Java EE 및 CORBA 모듈 제거: [http://openjdk.java.net/jeps/320#Java-EE-modules](http://openjdk.java.net/jeps/320#Java-EE-modules)

How to resolve java.lang.NoClassDefFoundError: javax/xml/bind/JAXBException: [https://stackoverflow.com/questions/43574426/how-to-resolve-java-lang-noclassdeffounderror-javax-xml-bind-jaxbexception](https://stackoverflow.com/questions/43574426/how-to-resolve-java-lang-noclassdeffounderror-javax-xml-bind-jaxbexception)

Hibernate JDK 11: [https://discourse.hibernate.org/t/jdk11-missing-jaxb-dependency/1646](https://discourse.hibernate.org/t/jdk11-missing-jaxb-dependency/1646)

Spring Boot with Java 9 and above: [https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-with-Java-9-and-above](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-with-Java-9-and-above)