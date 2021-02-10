## Spring Boot 2.4, Kotlin, Hibernate

### Spring Boot 2.2
* ConfigurationProperties에 Constructor Binding 지원, Immutable 하게 사용 가능
* Response Header의 Content-Type에 기본으로 들어가던 Charset=UTF-8이 빠졌다
  * RestTemplate에 별다른 설정 없이 해당 프로젝트의 API를 호출하는 경우 MessageConverter의 기본 Charset(ISO_8859_1)이 적용됨
    * 버전 업 후 응답에 포함된 다국어 문자열이 깨지기 시작...
* RestTemplate에 설정된 MessageConverter에 Charset을 명시적으로 넣어주는 것을 추천
    * 공통 시스템인 경우 Filter 등에서 Response Header에 대해서 명시적으로 헤더 값을 넣어주길...
    
### Spring Boot 2.4
이전 버전의 Multi-Document Files에서 존재했던 문제  
아래와 같은 설정에서 prod profile을 활성화 했다면 security.user.password 값은?, runlocal 프로퍼티는 존재 할까? 
```yaml
security:
  user:
    password: usera
---
spring:
  profiles: local
security:
  user:
    password: userb
runlocal: true
---
spring:
  profiles: !dev
    include: local
security:
  user:
    password: userc
```

Spring Boot 2.3 까지는 spring.profiles를 통해 특정 프로파일에 해당하는 document임을 명시했으나  
Spring Boot 2.4 부터는 spring.config.activate.on-profile을 사용 함
```yaml
spring:
  config:
    activate:
      on-profile: dev
```

Spring Boot 2.4 부터는 정의된 순서대로 설정이 로드될 것이며 Profile은 더이상 다른 Profile을 활성화 시킬 수 없게 됨  
```yaml
test: value
---
spring:
  config:
    activate:
      on-profile: dev
  profiles:
    active: local # will fail
test: overridden value
```

그리하여 아래와 같이 여러 프로파일을 묶어서 관리할 수 있도록 Profile group 지원 
```yaml
spring:
  profiles:
    group:
      local: local, local-db
```  

만약 이전 설정을 유지하고 싶다면
```yaml
spring:
  config:
    use-legacy-processing: true

```
 

### Kotlin과 Hibernate
Hibernate Entity 규칙 중 아래 항목이 틀린의 기본 클래스 개념과 충돌됩니다
* public or protected 가시성을 가진 no-argument constructor 필요
* Entity는 final 클래스가 될 수 없다 (Lazy loading을 위한 Proxy가 CGLIB 같이 상속을 기반으로 하는 듯)
 
(1) public or protected 가시성을 가진 no-argument constructor 필요  
Java + Lombok을 사용하는 경우는 @NoArgsConstructor(access = AccessLevel.PROTECTED)를 주로 사용했지만  
코틀린에선 kotlin-maven-noarg 플러그인을 사용하면 유용합니다.    

(2) Entity는 final 클래스가 될 수 없다 (Proxy가 CGLIB 같이 상속을 기반으로 하는 듯)  
프록시를 위해서 상속이 가능해야 하는데 코틀린의 필드들은 기본적으로 final입니다. 따라서 모든 필드에 open을 붙여주어야 하는 불편함이 있는데  
kotlin-maven-allopen 플러그인을 사용하면 유용합니다.  

```xml
<plugin>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-maven-plugin</artifactId>
    <configuration>
        <args>
            <arg>-Xjsr305=strict</arg>
        </args>
        <compilerPlugins>
            <plugin>spring</plugin>
            <plugin>jpa</plugin>
        </compilerPlugins>
        <pluginOptions>
          <option>all-open:annotation=javax.persistence.Entity</option>
          <option>all-open:annotation=javax.persistence.Embeddable</option>
          <option>all-open:annotation=javax.persistence.MappedSuperclass</option>
        </pluginOptions>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-allopen</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-noarg</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

개인적으로 Data 클래스가 유용하기는 하나 Hibernate Entity로는 궁합이 맞지 않는 것 같습니다.  
Hibernate에서 Entity의 equals, hashcode 등은 Entity의 식별자 혹은 NATURAL-ID, BUSINESS-KEY 등으로 구분이 되도록 하는 것이 좋다고 하지만  
data class는 equals, hashcode 메서드를 모든 필드를 가지고 자동으로 처리하기에 kassava 라이브러리 등을 통해 추가 구현이 필요합니다.  

destructuring은 유용할 수 있으나, 아래와 같은 copy를 이용한 immutable은 조금 더 귀찮아 지지 않을까 싶다
```kotlin
 var shorten = repository.save()
 // copy로 새로 객체를 생성한 경우 다시 영속화 해줘야 함
 // copy를 통해 생성된 객체는 아직 영속화 된 엔티티가 아니므로...
 shorten = repository.save(shorten.copy(
                hash = getHash(key, shorten.createdAt)
        ))


 // copy를 하지 않은 경우는 트랜잭션이 끝나면 업데이트 쿼리가나간다
 shorten.hash = getHash(key, shorten.createdAt)
```

결과적으로 JPA Entity는 불변 대상이 아니므로 copy는 딱히 Entity에 유용할 것 같지는 않다