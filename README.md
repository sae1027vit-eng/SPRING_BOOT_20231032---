# 자바웹프로그래밍(2) 수업 정리

> 강사: 최도현 · 주차별 수업 내용과 실습 진행 기록

## 목차

| 주차 | 주제 |
|---|---|
| [🟦 2주차](#week2) | 스프링 부트 개발환경 설정 및 테스트 |
| [🟩 3주차](#week3) | 정적 리소스(Static Resources) 처리와 외부 웹 템플릿 적용 |
| [🟧 4주차](#week4) | 데이터베이스(MySQL) 연동 및 JPA를 통한 CRUD 테스트 |
| [🟨 5주차](#week5) | 로그인/로그아웃 구현과 비밀번호 암호화 (Spring Security) |
| [🟥 6주차](#week6) | 권한(ROLE) 접근 제어와 관리자 페이지 분리 |
| [🟪 7주차](#week7) | 회원 관리 REST API(JSON)와 Postman 테스트 |

<br>

<a id="week2"></a>

# 🟦 2주차 — 스프링 부트 개발환경 설정 및 테스트

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 주제: 스프링 부트 개발환경 설정 및 테스트

## 수업 내용 정리

### 1. 스프링 부트 동작 과정

**내장 서버 & 컨테이너**
- 스프링 부트는 **톰캣(Tomcat)**을 WAS로 내장 → 별도 서버 설치 없이 자바 기반 동적 웹 실행
- **스프링 컨테이너**가 자바 객체(빈)의 생성/초기화/호출/종료를 관리 → 의존성 주입(DI)

**DispatcherServlet (프론트 컨트롤러) 흐름**
1. 클라이언트가 `/URI` 형태로 요청
2. `DispatcherServlet`이 `HandlerMapping`으로 알맞은 컨트롤러를 찾아 위임
3. `Controller`가 비즈니스 로직을 수행하고 `Model` + `View 이름`을 반환
4. `ViewResolver`가 뷰 이름으로 실제 뷰(템플릿)를 결정
5. `Thymeleaf`가 서버 측에서 HTML을 렌더링해 응답

**MVC 디자인 패턴**
- **Model** : 페이지에 전달할 데이터 저장
- **View** : 화면 출력 담당 (템플릿 엔진 - Thymeleaf, JSP 등)
- **Controller** : 요청을 받아 흐름을 제어

### 2. Thymeleaf

- HTML 기반 템플릿 엔진 (JSP를 대체하는 최신 방식)
- MVC에서 View 역할을 담당, `DispatcherServlet`을 통해 동작
- 기본 문법
  - 선언 : `<html xmlns:th="http://www.thymeleaf.org">`
  - 주석 : `<!--/* 주석 내용 */-->`
  - 텍스트 출력 : `<span th:text="${data}"></span>` (`${...}` 는 변수 표현식)
- `pom.xml`에 `spring-boot-starter-thymeleaf` 의존성으로 포함됨

### 3. URL 매핑과 컨트롤러

- 화면의 모든 링크는 컨트롤러를 거쳐 동작 (정적 파일 직접 연결 시 404 에러 발생)
- 컨트롤러 작성 예시
  ```java
  @Controller
  public class DemoController {
      @GetMapping("/hello")
      public String hello(Model model) {
          model.addAttribute("data", "반갑습니다.");
          return "hello"; // templates/hello.html 로 연결
      }
  }
  ```
- 컨트롤러가 반환한 문자열(view name)은 `templates/viewName.html` 파일과 매핑됨

### 4. Git 연동

- `README.md`에 진행 상황을 기록 (예 : "2주차 스프링 부트 개발환경, 테스트 완료")
- 변경 파일 add → commit → GitHub 원격 저장소로 push
- 매 실습마다 소스 코드를 지속적으로 커밋/업데이트

## 실습 (2주차 연습문제 : URL 맵핑과 컨트롤러 추가하기)

### 연습문제 요구사항
1. 기존 `hello.html`의 링크를 "두번째 헬로 페이지" 문구로 수정하고, `/hello2`로 이동하도록 변경
2. `DemoController`에 `/hello2` 매핑 메서드를 추가하고, 5개의 속성(Model attribute)을 담아 전달
3. `hello2.html`을 작성해 5개 속성 값을 모두 출력

### 구현 내용

**`DemoController.java`**
- `/index` : `index.html` 로 연결
- `/hello` : `hello.html` 로 연결, `data` 속성 전달 (기존 기능)
- `/hello2` : `hello2.html` 로 연결, `data1` ~ `data5` 5개 속성 전달 (연습문제 구현)

**`hello2.html` (신규)**
- Thymeleaf `th:text` 로 `data1` ~ `data5` 값을 순서대로 출력
- 하단에 `/index` 로 돌아가는 링크 포함

**페이지 간 링크 수정**
- `index.html` → `/hello` 링크 문구를 "첫번째 헬로 페이지"로 수정
- `hello.html` → `/hello2` 로 이동하는 "두번째 헬로 페이지" 링크로 변경
- `index → hello → hello2 → index` 순환 구조로 페이지 연결

**`pom.xml`**
- `spring-boot-starter-actuator` 의존성 추가 (애플리케이션 상태 확인용, `/actuator/health` 등)

### 실행 및 확인 방법
1. `DemoApplication.java` 실행 (Ctrl+F5)
2. 브라우저에서 `http://localhost:8080/index` 접속
3. "첫번째 헬로 페이지" 링크 → `/hello` 이동, `data` 값 확인
4. "두번째 헬로 페이지" 링크 → `/hello2` 이동, `data1`~`data5` 값 확인
5. "홈페이지 메인" 링크 → `/index` 로 복귀

### 실행/수정 상태
- index.html, hello.html, hello2.html : 실행/수정 완료

---

<a id="week3"></a>

# 🟩 3주차 — 정적 리소스(Static Resources) 처리와 외부 웹 템플릿 적용

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 주제: 정적 리소스(Static Resources) 처리와 외부 웹 템플릿 적용

## 수업 내용 정리

### 1. 스프링 부트의 정적 리소스 처리

- 스프링 부트는 별도 설정 없이 아래 경로들을 정적 리소스 디렉터리로 자동 인식
  - `src/main/resources/static`
  - `src/main/resources/public`
  - `src/main/resources/resources`
  - `src/main/resources/META-INF/resources`
- 위 경로에 있는 파일은 컨트롤러 없이 파일 경로 그대로 URL에 매핑되어 응답됨 (예 : `static/css/style.css` → `/css/style.css`)

### 2. templates 폴더 vs static/public 폴더

| 구분 | templates | static / public |
|---|---|---|
| 처리 방식 | Thymeleaf 뷰 엔진이 서버에서 렌더링 | 별도 처리 없이 파일 그대로 응답 |
| 접근 방법 | 컨트롤러가 view name을 반환해야 접근 가능 | URL 경로로 직접 접근 가능 (컨트롤러 불필요) |
| 동적 데이터 | `${...}` 등으로 Model 데이터 출력 가능 | 순수 정적 파일이라 동적 데이터 바인딩 불가 |
| 주요 용도 | 화면(View)의 HTML 템플릿 | CSS, JS, 이미지, 폰트 등 프론트엔드 자원 및 별도 처리가 필요 없는 HTML |

### 3. 정적 자원 경로 참조 - `th:href`, `th:src`

- Thymeleaf 템플릿에서 정적 자원을 참조할 때는 일반 `href`/`src` 대신 `th:href="@{...}"`, `th:src="@{...}"` 문법 사용
- `@{...}` 표현식은 스프링 부트 컨텍스트 경로를 자동으로 적용해주는 URL 링크 표현식
  ```html
  <link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
  <img th:src="@{/images/profile.png}" class="avatar-image" alt="">
  ```

### 4. 외부 웹 템플릿을 프로젝트에 통합하기

- 외부에서 제공되는 Bootstrap 기반 HTML 템플릿(TemplateMo 등)을 스프링 부트 프로젝트 구조에 맞게 적용하는 방법 실습
- 템플릿에 포함된 `css`, `js`, `fonts`, `images` 리소스를 `static` 폴더 하위로 이동하여 정적 자원으로 등록
- 템플릿 HTML의 `href`, `src` 속성을 `th:href="@{...}"`, `th:src="@{...}"` 표현식으로 교체해 스프링 부트 정적 자원 경로와 연결
- 컨트롤러(`/index`)와 연결되는 `index.html`을 위 방식으로 재구성하여 실제 포트폴리오 페이지 형태의 메인 화면 완성
- 컨트롤러를 거치지 않고 URL로 바로 접근해야 하는 상세 페이지(`detailed_web.html`)는 `public` 폴더에 배치
- `<head>`에서 연동하는 외부 리소스 확인
  - Google Fonts: DM Sans (폰트)
  - `bootstrap.min.css` + `bootstrap-icons.css` (레이아웃/아이콘, 5.1.3 버전)
  - `magnific-popup.css` (이미지 팝업, jQuery 기반 플러그인)
  - `templatemo-first-portfolio-style.css` (사이트 고유 디자인)

### 5. 네비게이션 바 & 상세 페이지 링크

- 상단 고정 nav에 `#section_1`~`#section_5` 앵커로 각 섹션 이동 링크 구성
- `click-scroll.js`가 앵커(`#`) 클릭 시 페이지 전환 없이 해당 위치로 부드럽게 스크롤 이동시켜줌
- 상세 페이지로 나가는 링크는 `target="_blank" rel="noopener noreferrer"` 속성을 사용

| 값 | 역할 |
|---|---|
| `noopener` | 새로 열린 페이지가 `window.opener`로 원래 페이지를 조작하지 못하게 차단 (피싱 사이트 전환 등 악용 방지) |
| `noreferrer` | 이동한 페이지에 어디서 왔는지(Referer 헤더)를 넘기지 않음, `noopener` 효과도 포함 |

### 6. 기존 파일 백업

- 새 내용으로 덮어쓰기 전, 기존 `index.html`, `hello.html`, `hello2.html`을 "-복사본" 이름으로 복사해두어 이전 버전을 보존하는 방식 실습

### 7. 프로필 커스터마이징

- Hero 섹션의 이름/소개 문구, 프로필 이미지(`profile.png`)를 본인 정보로 교체
- 기술(Services) 영역 4가지(웹/AI/보안/게임)를 한글로 번역하고 각 영역의 제목·설명 작성
- [Bootstrap Icons](https://icons.getbootstrap.com/)에서 항목에 맞는 아이콘을 검색해 기존 아이콘과 교체 (예: AI → `bi-anthropic`)
- 로컬 번들 CSS(5.1.3)에는 없는 최신 아이콘을 쓰기 위해 최신 Bootstrap Icons CDN 링크를 `<head>`에 추가로 연결

### 8. 반응형/성능 확인

- F12 개발자 모드 → 기기 툴바(Ctrl+Shift+M)로 모바일 화면 비율 확인
- Lighthouse 리포트를 PC/모바일 각각 생성해 Performance 점수 차이와 원인 비교

## 실습 (3주차 연습문제 : 포트폴리오 수정하기)

### 연습문제 요구사항

1. 기술(Services) 4가지 영역의 아이콘을 웹을 제외하고 모두 목적에 맞는 아이콘으로 교체
2. 각 기술(웹/AI/보안/게임)의 상세 페이지를 `detailed_web.html`을 재활용해 추가로 제작

### 구현 내용

**아이콘 교체**
- 웹(Web) : 기본 `bi-globe` 유지
- 인공지능(AI) : `bi-anthropic`
- 보안(Security) : `bi-shield-fill-check`
- 게임(Game) : `bi-controller`

**상세 페이지 추가 (신규)**
- `detailed_web.html`의 구조(고정 네비 + 페이지 헤더 + 요약 숫자 + 기술 스택 카드 + 숙련도/진행방식 + 푸터)를 그대로 재사용해 3개 페이지를 `public` 폴더에 추가 제작
  - `detailed_ai.html` : 머신러닝/딥러닝, LLM & 프롬프트 엔지니어링, 데이터 처리, AI 서비스 연동
  - `detailed_security.html` : 웹 보안, 인증/인가, 취약점 분석, 보안 운영
  - `detailed_game.html` : 게임 클라이언트(Unity), 게임 서버/네트워크, 게임 기획/디자인, 데이터/배포
- `index.html`의 각 서비스 카드 `th:href="@{/detailed_xxx.html}"` 링크와 실제 생성한 파일명을 일치시킴
- 컨트롤러 등록 없이 `public` 폴더 배치만으로 URL 직접 접근되는 정적 페이지로 동작

### 실행 및 확인 방법

1. 서버 실행 후 `http://localhost:8080/` 접속
2. 기술(Services) 섹션에서 웹/AI/보안/게임 각 카드의 "Discover More" 클릭
3. `/detailed_web.html`, `/detailed_ai.html`, `/detailed_security.html`, `/detailed_game.html` 4개 페이지 모두 정상 응답(200) 및 디자인 일관성 확인

### 실행/수정 상태
- 아이콘 교체, 상세 페이지 4종 : 실행/수정 완료

---

<a id="week4"></a>

# 🟧 4주차 — 데이터베이스(MySQL) 연동 및 JPA를 통한 CRUD 테스트

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 주제: 데이터베이스(MySQL) 연동 및 JPA를 통한 CRUD 테스트

## 수업 내용 정리

### 1. JPA(Java Persistence API)와 영속성

- **JPA** : 자바 객체(엔티티)와 DB 테이블을 매핑(ORM)해주는 표준 API, SQL을 대신 생성해 메서드 호출만으로 DB를 제어
- **영속성(Persistence)** : 객체가 프로그램 종료 이후에도 DB에 저장되어 유지되는 성질
- **엔티티 매니저** : Repository 내부에서 영속성 컨텍스트를 자동 관리, 트랜잭션 처리 복잡성 감소
- 대표 구현체(ORM) : **하이버네이트(Hibernate)**

### 2. MySQL 연동 설정

- `pom.xml`에 주석 처리되어 있던 JPA 의존성 활성화
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  ```
- MySQL 연결 드라이버(`mysql-connector-j`)는 JPA에 기본 포함되어 있지 않아 별도 의존성으로 추가
- MySQL 서버 설치(Server only) 후 Command Line Client로 접속해 데이터베이스 생성
  ```sql
  create database spring;
  flush privileges;
  ```
- `application.properties`에 접속 정보 및 JPA 옵션 추가
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/spring?serverTimezone=Asia/Seoul
  spring.datasource.username=root
  spring.datasource.password=비밀번호
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
  ```
- 정상 연결 시 터미널에 `HikariPool-1 - Start completed.` 로그 출력 확인
- 정상 실행이 안 될 때 확인할 것 (트러블슈팅)
  - 터미널에 출력되는 에러 메시지 우선 확인
  - `application.properties`의 비밀번호, DB명 오타 여부
  - `pom.xml`의 JPA 관련 의존성이 주석 처리된 채로 남아있는지 여부

### 3. VS Code MySQL 확장 모듈 활용

- 확장 검색(`mysql`) 후 설치, 좌측 Database 패널에서 서버 연결 정보(호스트, 포트 3306, 계정, DB명) 입력 후 연결
- 트리에서 Tables / Views / Functions / Procedures 확인 가능
- 테이블 우클릭 → SQL Template(SELECT/INSERT/UPDATE/DELETE), Dump Struct(And Data), Import SQL 등으로 백업/복원 가능
- 상단 "Open in terminal"로 해당 DB에 바로 접속된 `mysql>` 터미널 실행 가능

### 4. 계층형 패키지 구조로 변경

- 단일 패키지(`demo`)를 MVC + Service/Repository 계층으로 세분화
  - `controller` : 요청/응답 흐름 제어 (기존 `DemoController.java` 이동)
  - `model/domain` : 엔티티 클래스 (DB 테이블 구조)
  - `model/repository` : DB 접근(쿼리) 인터페이스
  - `model/service` : 비즈니스 로직(중간 데이터 가공)
  - `templates` : View, 기존 위치 그대로 유지
- 요청 처리 흐름 : `Controller → Service → Repository → JPA(Hibernate) → MySQL`

### 5. 테스트 엔티티 · 리포지토리 · 서비스 · 컨트롤러 · 뷰 구현

**`TestDB.java` (엔티티)**
```java
@Entity
@Table(name = "testdb")
@Data
public class TestDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;
}
```

**`TestRepository.java`**
```java
@Repository
public interface TestRepository extends JpaRepository<TestDB, Long> {
    TestDB findByName(String name);
}
```

**`TestService.java`**
```java
@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public TestDB findByName(String name) {
        return testRepository.findByName(name);
    }

    public List<TestDB> findAll() {
        return testRepository.findAll();
    }
}
```
- `@Autowired`로 `TestRepository`를 직접 생성하지 않고 스프링 컨테이너로부터 주입받음 (의존성 주입, DI)
  - 강한 결합 : 클래스가 필요한 객체를 직접 생성 → 구현이 바뀌면 사용하는 코드도 같이 수정해야 함
  - 약한 결합(DI) : 스프링이 객체를 생성해서 주입 → 구현체를 교체해도 사용하는 코드는 그대로 유지 가능

**`JpaRepository` 주요 제공 메서드**

| 메서드 | 설명 |
|---|---|
| `findAll()` | 전체 조회 (`SELECT * FROM testdb`) |
| `findById(ID id)` | PK로 1건 조회, 없으면 `Optional.empty()` |
| `existsById(ID id)` | PK 존재 여부 확인 |
| `count()` | 전체 행 개수 |
| `save(S entity)` | 저장 (PK 없으면 INSERT, 있으면 UPDATE) |
| `saveAll(Iterable<S>)` | 여러 건 저장 |
| `deleteById(ID id)` | PK로 삭제 |
| `delete(T entity)` | 엔티티로 삭제 |
| `deleteAll()` | 전체 삭제 |

**`DemoController.java`** (`/testdb` 매핑 추가)
- 초기에는 `findByName("홍길동")`으로 단건 조회 → 이후 `findAll()`로 전체 목록 조회하도록 수정
```java
@GetMapping("/testdb")
public String getAllTestDBs(Model model) {
    List<TestDB> users = testService.findAll();
    model.addAttribute("users", users);
    return "testdb";
}
```

**`testdb.html` (신규)**
- `th:each`로 `users` 리스트를 순회하며 테이블로 출력
```html
<table border="1">
    <tr th:each="user : ${users}">
        <td th:text="${user.id}"></td>
        <td th:text="${user.name}"></td>
    </tr>
</table>
```
- 서버 재시작 시 엔티티 기준으로 `testdb` 테이블이 자동 생성됨(`ddl-auto=update`)

### 6. 데이터 확인

- VS Code MySQL 확장의 SQL Template(INSERT)로 `testdb` 테이블에 데이터 직접 추가
- `localhost:8080/testdb` 접속해 등록한 데이터가 화면에 출력되는지 확인

### 7. Git 연동

- `README.md`에 이번 주 진행 상황 기록 (예 : "4주차 데이터베이스 연동 완료")
- 변경 파일 add → commit → GitHub 원격 저장소로 push
- 매 실습마다 소스 코드를 지속적으로 커밋/업데이트

## 실습 (4주차 연습문제 : 사용자 정보 수정 및 출력하기)

### 연습문제 요구사항

1. `TestDB` 엔티티에 나이, 성별 컬럼 추가
2. VS Code MySQL 확장을 이용해 각 사용자의 나이/성별 정보를 DB에 직접 추가
3. `testdb.html`을 수정해 아이디/이름/나이/성별을 모두 출력

### 구현 내용

**`TestDB.java` (컬럼 추가)**
```java
@Column(nullable = true)
private Integer age;

@Column(nullable = true)
private String gender;
```

**DB 데이터 추가**
- VS Code MySQL 확장에서 `testdb` 테이블에 `INSERT`로 사용자별 이름/나이/성별 직접 입력

**`testdb.html` (헤더 행 + 컬럼 추가)**
- `th:each` 반복 행 위에 고정된 헤더 행(`<th>`)을 추가해 각 열이 무엇인지 표시
- `age`, `gender` 컬럼을 `<td>`로 추가 출력
```html
<table border="1">
    <tr>
        <th>아이디</th>
        <th>이름</th>
        <th>나이</th>
        <th>성별</th>
    </tr>
    <tr th:each="user : ${users}">
        <td th:text="${user.id}"></td>
        <td th:text="${user.name}"></td>
        <td th:text="${user.age}"></td>
        <td th:text="${user.gender}"></td>
    </tr>
</table>
```

### 실행 및 확인 방법

1. 서버 실행 후 `http://localhost:8080/testdb` 접속
2. 아이디 / 이름 / 나이 / 성별 4개 컬럼이 헤더와 함께 전체 사용자 수만큼 출력되는지 확인

### 실행/수정 상태
- `TestDB.java`, `testdb.html`, DB 데이터 추가 : 실행/수정 완료

---

<a id="week5"></a>

# 🟨 5주차 — 로그인/로그아웃 구현과 비밀번호 암호화 (Spring Security)

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 주제: 로그인/로그아웃 구현과 비밀번호 암호화(BCrypt)

## 수업 내용 정리

### 1. 웹 보안과 인증

- 해킹의 시작은 대부분 "로그인" → OWASP Top 10(2025) 중 **A01 접근 제어 실패**, **A07 인증 실패**가 오늘 주제와 직결
- **인증(Authentication)** : "너 누구야?" (로그인 = 아이디 + 비밀번호 확인)
- **인가(Authorization)** : "너 이거 해도 돼?" (권한, 6주차 예정)

### 2. 비밀번호 저장 방식

- 비밀번호 평문 저장 금지 (개인정보 안전성 확보조치 기준) → 반드시 **일방향 해시**로 저장
- **암호화 vs 해시**
  - 암호화 : 복호화 가능 (키가 유출되면 끝)
  - 해시 : 비밀번호 + 솔트(SALT)를 결합해 계산, 복호화 불가능
- **BCrypt** : 매번 무작위 솔트(22자) 자동 생성 → 같은 비밀번호도 저장할 때마다 다른 해시값 생성, 일부러 느리게 설계되어 무차별 대입(brute-force) 공격 방어
- MD5, SHA-1은 대입 공격에 취약 → 비밀번호 저장용으로는 BCrypt, Argon2 등을 사용

### 3. 세션 기반 로그인

- 오늘 실습은 **세션(Session)** 방식 : 서버가 로그인 상태를 저장하고, 브라우저는 세션 ID가 담긴 `JSESSIONID` 쿠키만 보관 (전통적인 Thymeleaf/JSP 웹사이트 방식)
- 토큰(JWT), 소셜 로그인(OAuth2), 패스키 방식은 이후 주차(7주차~)에서 다룸

### 4. Spring Security 의존성 추가

- `pom.xml`에 의존성 2개 추가
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  <dependency>
      <groupId>org.thymeleaf.extras</groupId>
      <artifactId>thymeleaf-extras-springsecurity6</artifactId>
  </dependency>
  ```
- 의존성 추가만으로 **모든 페이지(메인, CSS 포함)가 로그인 필수로 잠김** (스프링 시큐리티는 기본이 전부 잠금)
- 별도 계정을 만들기 전까지는, 서버 시작 시 콘솔에 찍히는 `Using generated security password: ...` 문구로 임시 로그인 가능 (아이디 `user`, 비밀번호는 재시작마다 랜덤 생성 — 개발용 전용, 프로덕션에서는 사용 불가)

### 5. 필터 체인과 SecurityConfig

- **필터(Filter) 기반 보안** : 요청이 `DispatcherServlet`(컨트롤러)에 도달하기 전에 여러 보안 필터가 체인(사슬)처럼 연결되어 검사 (`CsrfFilter`, `UsernamePasswordAuthenticationFilter`, `LogoutFilter`, `AuthorizationFilter` 등) → 조건을 통과 못 하면 로그인 화면(302) 또는 거부(403)
- `config` 패키지를 새로 만들고 `SecurityConfig.java` 작성
  ```java
  @Configuration
  @EnableWebSecurity
  public class SecurityConfig {

      @Bean
      public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
      }

      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          http
              .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/", "/hello", "/detailed_web.html",
                          "/login", "/signup", "/error").permitAll()
                  .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                  .anyRequest().authenticated())
              .formLogin(form -> form
                  .loginPage("/login")
                  .defaultSuccessUrl("/")
                  .failureUrl("/login?error")
                  .permitAll())
              .logout(logout -> logout
                  .logoutUrl("/logout")
                  .logoutSuccessUrl("/login?logout")
                  .invalidateHttpSession(true)
                  .deleteCookies("JSESSIONID", "remember-me"));
          return http.build();
      }
  }
  ```

| 설정 | 의미 |
|---|---|
| `permitAll()` | 누구나 접근 (메인, 상세 페이지, 로그인, 가입) |
| `/css/**` 등 permitAll | 정적 리소스 허용 (빠지면 디자인 깨짐) |
| `anyRequest().authenticated()` | 나머지 전부 로그인 필요 (예: `/testdb`) |
| `loginPage("/login")` | 기본 로그인 화면 대신 직접 만든 로그인 화면 사용 |
| `defaultSuccessUrl("/")` | 로그인 성공 시 메인으로 (원래 가려던 페이지가 있으면 그쪽 우선) |
| `logoutUrl("/logout")` | 로그아웃 처리 URL (CSRF 보호로 POST만 가능, GET 직접 입력은 동작 안 함) |

- `authorizeHttpRequests`는 **위에서부터 순서대로** 검사, `anyRequest()`는 항상 마지막에 위치
- 스프링 시큐리티 7.x부터 `.and()`로 이어 쓰는 방식이 제거되어 람다 DSL(`auth -> auth...`)로만 작성

### 6. 회원 관련 계층 구조 추가

- `config` : 보안 설정 (`SecurityConfig`)
- `model/dto` : 화면 ↔ 컨트롤러 데이터 (`MemberForm`)
- `model/domain/Member.java` (엔티티)
  ```java
  @Entity
  @Table(name = "member")
  @Data
  public class Member {
      @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      @Column(nullable = false, unique = true, length = 50)
      private String username; // 로그인 아이디, 중복 불가
      @Column(nullable = false)
      private String password; // BCrypt 암호화 값(60자)
      @Column(nullable = false, length = 50)
      private String name;
      @Column(nullable = false, length = 20)
      private String role; // USER (6주차 ADMIN)
  }
  ```
- `MemberRepository` : `findByUsername`, `existsByUsername` — 메서드 이름 규칙만 지키면 JPA가 SQL을 자동 생성
- `MemberForm` (DTO) : `username`, `password`, `passwordConfirm`, `name`만 보유 — 엔티티를 화면에 직접 쓰지 않는 이유는 `role` 같은 민감한 필드를 사용자가 임의로 조작해 전송하는 것을 막기 위함
- `MemberService`
  - `signup(MemberForm form)` : 아이디 중복 확인(`existsByUsername`) → `passwordEncoder.encode()`로 BCrypt 암호화 → `role = "USER"` 기본값으로 저장(`save`)
  - `implements UserDetailsService`, `loadUserByUsername(String username)` : 로그인 시 스프링 시큐리티가 자동 호출하는 메서드, DB에서 회원을 조회해 시큐리티용 `UserDetails` 객체로 변환해 반환. 비밀번호 일치 여부 비교는 직접 구현하지 않고 시큐리티가 `PasswordEncoder.matches()`로 내부에서 처리 (고정 시간 비교로 타이밍 공격 방지, 계정 존재 여부도 응답 시간 차이로 노출되지 않도록 처리)
- `MemberController` : `GET /login`(로그인 화면), `GET /signup`(가입 화면), `POST /signup`(가입 처리 — 중복 아이디면 에러 메시지와 함께 입력값 유지한 채 가입 화면 재표시, 성공하면 로그인 화면으로 리다이렉트)

### 7. Thymeleaf + Spring Security 연동 문법

- `<html>` 태그에 `xmlns:sec="http://www.thymeleaf.org/extras/spring-security"` 네임스페이스 선언 필요
- `sec:authorize="isAnonymous()"` / `"isAuthenticated()"` : 로그인 상태에 따라 해당 태그를 출력하거나 완전히 제거
- `sec:authentication="name"` : 로그인한 사용자의 아이디(username)를 출력
  ```html
  <a sec:authorize="isAnonymous()" th:href="@{/login}">로그인</a>
  <div sec:authorize="isAuthenticated()">
      <span sec:authentication="name"></span>님
  </div>
  ```
- `index.html` 네비게이션(모바일/PC 모두)에 로그인 전(로그인/회원가입 버튼) · 로그인 후(아이디님 + 로그아웃) 분기 적용, 회원 전용 페이지(`/testdb`)로 가는 "회원목록" 메뉴 추가

### 8. 로그인/로그아웃 동작 확인

- 로그인 성공 시 세션에 인증 정보 저장, 브라우저에는 `JSESSIONID` 쿠키만 발급 (F12 → Application → Cookies에서 확인)
- 비로그인 상태로 인증이 필요한 페이지(`/testdb`)에 접근하면 로그인 화면으로 이동 → 로그인 성공 시 원래 가려던 페이지로 자동 복귀
- 로그아웃은 CSRF 보호 때문에 주소창에 `/logout`을 직접 입력(GET)해서는 동작하지 않고, 반드시 POST 폼으로만 처리됨

### 9. Git 연동과 비밀번호 보호

- `application.properties`에 DB 계정 정보(아이디/비밀번호)를 그대로 두고 커밋하면 원격 저장소에 비밀번호가 그대로 노출됨
- 계정 정보를 `application-secret.properties` 파일로 분리
  ```properties
  spring.datasource.username=root
  spring.datasource.password=비밀번호
  ```
- `application.properties`에는 아래 한 줄만 추가해 분리된 설정을 불러옴 (`optional:` 접두사 → 해당 파일이 없어도 앱은 정상 실행)
  ```properties
  spring.config.import=optional:application-secret.properties
  ```
- `.gitignore`에 `application-secret.properties`를 추가해 커밋/업로드 대상에서 완전히 제외

> **(강의 외 추가 조치)** 확인해보니 4주차 커밋에 DB 비밀번호가 이미 평문으로 push되어 있었음 → 히스토리를 다시 쓰는 대신 **DB 비밀번호 자체를 변경**하는 방법으로 무력화 (과거 커밋에 남은 값은 더 이상 유효하지 않은 비밀번호가 됨)

### 실행/수정 상태

- `pom.xml`, `SecurityConfig.java`, `Member.java`, `MemberRepository.java`, `MemberForm.java`, `MemberService.java`, `MemberController.java`, `login.html`, `signup.html`, `index.html` 네비게이션 : 실행/수정 완료
- 실제 회원가입 → BCrypt 해시로 DB 저장 확인, `application-secret.properties` 분리 및 DB 비밀번호 변경 완료
## 실습 (5주차 연습문제 : 로그인 기능 개선하기)

### 연습문제 요구사항

1. 로그인 상태 유지(`remember-me`) : `SecurityConfig`의 로그아웃 설정 뒤에 `.rememberMe()` 추가 (`key("비밀키")`, `tokenValiditySeconds(7일)`), 브라우저 종료 후 재접속해도 로그인 유지
2. 비밀번호 확인 검증 : `signup()`에서 `password`와 `passwordConfirm`을 비교, 다르면 "비밀번호가 일치하지 않습니다." 출력하고 DB에 저장하지 않을 것

### 구현 내용

**`SecurityConfig.java` (remember-me 추가)**
```java
.logout(logout -> logout
    ...
    .deleteCookies("JSESSIONID", "remember-me"))
.rememberMe(remember -> remember // 4. 로그인 상태 유지
    .key("portfolio-remember-me-key") // 쿠키 위변조 방지용 비밀키
    .tokenValiditySeconds(60 * 60 * 24 * 7)); // 유지 기간 : 7일
```
- `login.html`의 체크박스(`name="remember-me"`)를 체크하고 로그인하면 `remember-me` 쿠키가 7일 유효기간으로 발급됨
- 이 쿠키는 `MemberService`(`UserDetailsService`)로 사용자를 다시 조회해 로그인 상태를 복원 (세션이 사라져도 유지)
- `.logout()`의 `deleteCookies("JSESSIONID", "remember-me")`가 이미 있어 로그아웃 시 `remember-me` 쿠키도 삭제됨

**`MemberService.java` (비밀번호 확인 검증 추가)**
```java
public Member signup(MemberForm form) {
    if (memberRepository.existsByUsername(form.getUsername())) {
        throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
    }
    if (form.getPassword() == null || !form.getPassword().equals(form.getPasswordConfirm())) {
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다."); // DB 저장 전에 차단
    }
    ...
    return memberRepository.save(member);
}
```
- 비교를 `save()` 이전에 하므로 불일치 시 DB에 저장되지 않음
- `MemberController.signup()`의 기존 `catch (IllegalArgumentException e)`가 메시지를 `${error}`로 전달 → `signup.html`에 빨간 경고창으로 출력 (컨트롤러·화면 수정 없음)

### 실행 및 확인 방법

1. 서버 실행 후 `http://localhost:8080/signup` 접속
2. 비밀번호와 비밀번호 확인을 다르게 입력해 가입 → "비밀번호가 일치하지 않습니다." 표시, 해당 아이디로 로그인 불가(DB 미저장)
3. 같게 입력해 가입 → 로그인 화면으로 이동
4. "로그인 상태 유지" 체크 후 로그인 → F12 → Application → Cookies에서 `remember-me` 쿠키 확인 (Expires 약 7일 뒤)
5. `JSESSIONID`를 지우거나 브라우저를 종료한 뒤 재접속해도 로그인 유지(`/testdb` 접근 가능), 체크하지 않으면 `remember-me` 쿠키가 발급되지 않음

### 확인 결과

| 시나리오 | 결과 |
|---|---|
| 비밀번호 불일치 가입 | 가입 화면 재표시 + "비밀번호가 일치하지 않습니다." 출력 |
| 불일치 가입한 아이디로 로그인 | `/login?error` (DB에 저장되지 않음) |
| 비밀번호 일치 가입 | `/login?signup` 으로 이동 (정상 가입) |
| 상태 유지 체크 로그인 | `remember-me` 쿠키 발급, 유효기간 604,800초(7일) |
| `remember-me` 쿠키만으로 재접속 (세션 쿠키 없음) | 로그아웃 버튼 표시, `/testdb` 200 (로그인 유지) |
| 상태 유지 미체크 로그인 | `remember-me` 쿠키 없음 |

### 참고 : 실행 중 겪은 문제

- 다른 PC에서 DB 비밀번호를 변경해, 이 PC의 `application-secret.properties`와 맞지 않아 `Access denied for user 'root'` 오류 발생 → 이 PC의 DB 비밀번호에 맞게 파일 수정 (`.gitignore` 대상이라 커밋되지 않음)
- 이 PC의 DB에는 `member` 테이블이 없어 서버 시작 시 `ddl-auto=update`로 자동 생성됨
- `mvnw spring-boot:run` 으로 실행했을 때 DevTools 재시작 클래스로더 때문에 `Found 0 JPA repository interfaces` → `TestRepository` 빈을 찾지 못하는 오류 발생. `-Dspring.devtools.restart.enabled=false` 로 실행하면 정상 (VS Code에서 실행 시에는 해당 없음)

### 실행/수정 상태

- `SecurityConfig.java`(rememberMe), `MemberService.java`(비밀번호 확인 검증) : 실행/수정 완료
- 5주차 연습문제 2개 모두 서버 실행 후 동작 확인 완료

---

<a id="week6"></a>

# 🟥 6주차 — 권한(ROLE) 접근 제어와 관리자 페이지 분리

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 주제: 권한(ROLE) 접근 제어, 관리자 페이지 분리 (인가 · Authorization)

## 수업 내용 정리

### 1. 접근 제어 실패 (OWASP A01)

- OWASP Top 10(2025) **A01 접근 제어 실패(Broken Access Control)** 의 대표 사례
  - 관리자 페이지 주소 노출 : `/admin` 을 직접 입력하면 그냥 열림
  - 번호만 바꿔 남의 정보 조회 (**IDOR**) : `/order/1001` → `/order/1002`
  - 요청을 조작해 관리자 권한 획득 (**권한 상승**) : USER → ADMIN
- 공통 원인 : 로그인(인증)은 했는데 **권한 검사(인가)** 를 빠뜨림 → "메뉴에 안 보이니 괜찮겠지"
- **화면 숨김은 보안이 아니다** → 로그인했어도 **요청마다 권한 검사** 필요

### 2. 권한 관리 모델

| 모델 | 방식 | 예 |
|---|---|---|
| **RBAC** (Role-Based, 오늘 실습) | 사람 → **역할(Role)** → 권한 | USER / MANAGER / ADMIN |
| ABAC (Attribute-Based) | 부서·시간·위치 등 **속성 조건** | 근무 시간에만 접근 허용 |

- 사람마다 권한을 하나하나 주지 않고 역할을 부여 → 관리가 쉽고 실수가 적음
- **최소 권한 원칙(Least Privilege)** : 꼭 필요한 권한만 부여 → 계정이 해킹당해도 피해 최소화 (위 역할로 갈수록 사람 수는 적게)
- 관리자 화면은 **URL부터 분리** (`/admin/**`), 위험한 기능(권한 변경·삭제)은 **관리자만**

### 3. ROLE 과 권한(Authority)

- 5주차 `loadUserByUsername()` 의 `.roles(member.getRole())` → DB 값 `"USER"` 가 세션 권한 `ROLE_USER` 로 변환
- 스프링 시큐리티는 비교할 때 **`ROLE_` 을 자동으로 붙임**

| 표현 | 결과 |
|---|---|
| `hasRole("ADMIN")` | ✅ `ROLE_ADMIN` 보유 여부 검사 |
| `hasAuthority("ROLE_ADMIN")` | ✅ 권한 문자열 그대로 비교 (위와 동일) |
| `hasRole("ROLE_ADMIN")` | ❌ 서버 시작 오류 (`should not start with ROLE_`) |

### 4. 로그인 사용자 정보 꺼내기

| 위치 | 방법 |
|---|---|
| 컨트롤러 | 파라미터에 `Principal principal` → `principal.getName()` (아이디) |
| 어디서나 | `SecurityContextHolder.getContext().getAuthentication()` |
| 화면 | `sec:authentication="name"` / 권한 목록 `sec:authentication="principal.authorities"` |
| 표현식 안 | `${#authentication.name}` |

- **DB 권한 ≠ 세션 권한** 일 수 있음 : 권한은 **로그인하는 순간** 세션에 복사됨
  - DB의 role 을 바꿔도 → **다시 로그인해야** 반영 (내 정보 화면 : DB `ADMIN` / 세션 `[ROLE_USER]`)

### 5. 변경 구조

- 작성한 자바 파일 (4개)
  - `AdminController` : 관리자 전용 컨트롤러 (신규)
  - `SecurityConfig` : URL 권한 규칙 추가, 메서드 보안 ON
  - `MemberController` : `/mypage` 추가
  - `MemberService` : 조회 · 권한 변경 · 삭제 추가
- 엔티티·리포지토리·DTO 는 그대로 유지 (5주차 `Member.role` 컬럼을 오늘 사용, `JpaRepository` 기본 메서드 `findAll · findById · save · delete` 활용)
- 제공 화면 : `templates/admin/members.html`, `templates/error/403.html`, `templates/mypage.html` (신규), `index.html` (수정)

### 6. 프론트 코드 적용 (index.html 교체부분 ①~③)

- 직접 꾸민 `index.html` 은 전체 덮어쓰기 대신 **교체부분 ①~③만 반영** (인사말·프로필·카드 링크 등 커스터마이징 유지)
  - ① `<style>` : 권한 배지(`.role-badge`, `.role-ADMIN/MANAGER/USER`), 빨간 관리자 메뉴, 메뉴 한 줄 유지 스타일
  - ② 메뉴 : 회원목록 아래에 "내 정보", "관리자" 추가
  - ③ 사용자 이름 옆에 권한 배지 표시
  ```html
  <!-- 로그인한 사람만 -->
  <li class="nav-item" sec:authorize="isAuthenticated()">
      <a class="nav-link" th:href="@{/mypage}">내 정보</a>
  </li>
  <!-- 관리자(ROLE_ADMIN)만 -->
  <li class="nav-item" sec:authorize="hasRole('ADMIN')">
      <a class="nav-link nav-link-admin" th:href="@{/admin/members}"><i class="bi-shield-lock me-1"></i>관리자</a>
  </li>

  <span sec:authorize="hasRole('ADMIN')" class="role-badge role-ADMIN">ADMIN</span>
  <span sec:authorize="hasRole('MANAGER')" class="role-badge role-MANAGER">MANAGER</span>
  <span sec:authorize="hasRole('USER')" class="role-badge role-USER">USER</span>
  ```

### 7. 내 정보 페이지 - `/mypage`

**`MemberService.java`**
```java
public Member findByUsername(String username) { // 로그인한 아이디로 회원 조회
    return memberRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException(
                    "회원이 존재하지 않습니다 : " + username));
}
```

**`MemberController.java`** (import `java.security.Principal`, `com.example.demo.model.domain.Member`)
```java
@GetMapping("/mypage") // 내 정보 : 로그인한 사람만
public String mypage(Principal principal, Model model) { // 현재 로그인 사용자
    Member member = memberService.findByUsername(principal.getName());
    model.addAttribute("member", member);
    return "mypage";
}
```
- `Principal` : 파라미터에 적기만 하면 스프링이 로그인 사용자를 넣어줌
- `/mypage?id=3` 처럼 id 를 URL로 받지 않고 **로그인 아이디로 조회** → 번호 조작(IDOR) 차단

### 8. 관리자 계정 만들기

1. 회원가입 화면에서 `admin / 1234 / 관리자` 로 가입 (비밀번호는 BCrypt 로 저장)
2. 제공 SQL(`관리자_권한_부여.sql`)로 role 만 변경
   ```sql
   SELECT id, username, name, role FROM member;
   UPDATE member SET role = 'ADMIN' WHERE username = 'admin';
   ```
3. admin 로그인 → 빨간 관리자 메뉴 + ADMIN 배지 (로그인 중이었다면 로그아웃 후 다시 로그인)

- SQL `INSERT` 로 계정을 만들지 않는 이유 : 평문 `1234` 는 BCrypt 형식이 아니라 **로그인 불가** → 정상 가입 후 role 만 `UPDATE`

### 9. 관리자 회원 목록 · 권한 변경 · 삭제

**`MemberService.java`**
```java
public List<Member> findAll() { // 전체 회원 (id 순서)
    return memberRepository.findAll(Sort.by("id"));
}

// 관리자가 부여할 수 있는 권한 (화면에서 넘어온 값을 그대로 믿지 않는다)
private static final List<String> ROLES = List.of("USER", "ADMIN");

public void changeRole(Long id, String role) { // 권한 변경
    if (!ROLES.contains(role)) { // 허용된 권한만 (화이트리스트)
        throw new IllegalArgumentException("허용되지 않는 권한입니다 : " + role);
    }
    Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    member.setRole(role);
    memberRepository.save(member); // id 가 있으면 UPDATE
}

public void deleteMember(Long id) { // 회원 삭제
    Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    memberRepository.delete(member); // DELETE
}
```
- `ROLES` 화이트리스트 : 개발자도구로 select 값을 `SUPER` 등으로 바꿔 보내도 거부
- `save()` : id 가 없으면 INSERT, 있으면 UPDATE

**`AdminController.java` (신규)**
```java
@Controller
@RequestMapping("/admin") // 모든 URL 앞에 /admin (관리자 페이지 분리)
public class AdminController {
    @Autowired
    MemberService memberService;

    @GetMapping("/members") // GET /admin/members
    public String members(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "admin/members"; // templates/admin/members.html (폴더 포함)
    }

    @PostMapping("/members/{id}/role") // POST /admin/members/3/role
    public String changeRole(@PathVariable Long id, @RequestParam String role,
                             RedirectAttributes redirect) {
        try {
            memberService.changeRole(id, role);
            redirect.addFlashAttribute("message", "권한을 " + role + " (으)로 변경했습니다.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/members"; // 새로고침 중복 전송 방지 (PRG 패턴)
    }

    @PostMapping("/members/{id}/delete") // POST /admin/members/3/delete
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            memberService.deleteMember(id);
            redirect.addFlashAttribute("message", "회원을 삭제했습니다.");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/members";
    }
}
```

| 어노테이션 | 값을 꺼내는 곳 |
|---|---|
| `@PathVariable` | URL 경로 `{id}` → 3 |
| `@RequestParam` | 폼의 `<select name="role">` |
| `addFlashAttribute` | redirect 후 화면에 **1회만** 표시 |

- **PRG 패턴(Post-Redirect-Get)** : POST 처리 후 redirect → 새로고침해도 다시 전송되지 않음

### 10. 보안 구멍 발견 → 접근 제어 3단계

- 이 시점에는 USER 로 로그인해도 주소창에 `/admin/members` 를 직접 입력하면 **회원 관리 화면이 열리고**, 자기 권한을 ADMIN 으로 바꿀 수 있었음 → A01 **권한 상승**
- 원인 : 화면(메뉴 숨김)만 처리했기 때문

| 단계 | 위치 | 코드 | 역할 |
|---|---|---|---|
| ① 화면 | Thymeleaf | `sec:authorize="hasRole('ADMIN')"` | 보이기/숨기기만 (보안 아님) |
| ② URL | SecurityConfig | `.requestMatchers("/admin/**").hasRole("ADMIN")` | **1차 잠금** : 컨트롤러 도착 전에 차단 |
| ③ 메서드 | Service | `@PreAuthorize("hasRole('ADMIN')")` | **2차 잠금** : 메서드 실행 직전에 차단 |

- ②가 있는데 ③도 필요한 이유 : URL 규칙 실수·누락 대비 → **이중 잠금**

| 구분 | `hasRole()` | `@PreAuthorize` |
|---|---|---|
| 정체 | 권한 검사 표현식(SpEL) | 메서드 보안 어노테이션 |
| 쓰는 위치 | 설정 클래스, 어노테이션, 뷰 | 컨트롤러·서비스 메서드 위 |
| 보호 대상 | URL 또는 표현식이 쓰인 대상 | 메서드(비즈니스 로직) |
| 필요 설정 | 기본 제공 | `@EnableMethodSecurity` |

### 11. URL 권한 규칙 추가 (1차 잠금)

- 기존 `SecurityConfig` 의 `authorizeHttpRequests` 블록 **안에**, 정적 리소스 줄 아래 한 줄 추가
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/", "/hello", "/detailed_web.html",
            "/login", "/signup", "/error").permitAll()
    .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
    .requestMatchers("/admin/**").hasRole("ADMIN") // [6주차] 관리자만
    .anyRequest().authenticated()) // 나머지 : 로그인 필요
```
- 규칙은 **위에서부터 순서대로** 검사 → 구체적인 규칙이 위, `anyRequest()` 는 항상 마지막
- `/admin/**` : 하위 경로 전부 (GET·POST 모두)

**403 접근 거부 테스트** (`/admin/members` 접속)

| 상태 | 결과 | 의미 |
|---|---|---|
| 비로그인 | 302 → 로그인 화면 | 인증 필요 |
| USER | 403 접근 거부 화면 | 인가 실패 |
| ADMIN | 200 회원 관리 | 통과 |

- 권한 검사는 `AuthorizationFilter` 가 **컨트롤러 전에** 수행
- 403 화면은 컨트롤러 없이 `templates/error/403.html` 에 두면 스프링 부트가 자동 연결 (404 · 500 도 같은 방식)

### 12. 메서드 보안 - `@PreAuthorize` (2차 잠금)

**`SecurityConfig.java`**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // [6주차] 메서드 보안 활성화 : @PreAuthorize 사용
public class SecurityConfig {
```

**`MemberService.java`** (기존 메서드 바로 위에 어노테이션만 추가)
```java
@PreAuthorize("hasRole('ADMIN')") // 관리자만 실행 가능 (2차 잠금)
public void changeRole(Long id, String role) { ... }

@PreAuthorize("hasRole('ADMIN')") // 관리자만 실행 가능 (2차 잠금)
public void deleteMember(Long id) { ... }
```
- `@EnableMethodSecurity` 가 없으면 `@PreAuthorize` 는 **그냥 무시됨**
- 조회(`findAll`)는 그대로 두고 **데이터를 바꾸는 메서드만** 잠금, 검사 실패 시 같은 403 화면

**이중 잠금 실험** (URL 규칙을 잠시 주석 처리 후 USER 로 공격)

| 상태 | 목록 GET | 변경·삭제 | 데이터 |
|---|---|---|---|
| 잠금 없음 | 200 | 성공 | 변경됨 |
| URL 규칙만 | 403 | 403 | 보호 |
| `@PreAuthorize` 만 | 200 | 403 | 보호 |
| 둘 다 (최종) | 403 | 403 | 보호 |

- 메서드 수준 차단이 필요한 경우 : URL 패턴 오타(`/admin/*`), 규칙 순서 실수, 새 URL(`/api/...`)에서 같은 기능 호출, 스케줄러·배치처럼 URL을 거치지 않는 호출

### 13. 권한 표현식 정리

| 표현식 | 의미 | 사용 예 |
|---|---|---|
| `permitAll` | 누구나 | 메인, 로그인 |
| `authenticated` / `isAuthenticated()` | 로그인한 사람 | `/mypage` |
| `isAnonymous()` | 로그인 안 한 사람 | 로그인 버튼 |
| `hasRole('ADMIN')` | `ROLE_ADMIN` 보유 | `/admin/**` |
| `hasAnyRole('ADMIN','MANAGER')` | 둘 중 하나 | - |
| `hasAuthority('ROLE_ADMIN')` | 권한 문자열 그대로 | `hasRole` 과 동일 |
| `authentication.name` | 로그인 아이디 | "나" 표시 |

- 세 곳(화면·URL·메서드)이 **같은 표현식**을 사용 → SecurityConfig 는 메서드 형태(`.hasRole("ADMIN")`), 화면·`@PreAuthorize` 는 문자열 형태

### 14. 자주 나는 오류

| 증상 | 원인 → 해결 |
|---|---|
| `ROLE_ADMIN should not start with ROLE_` | `hasRole("ROLE_ADMIN")` → `hasRole("ADMIN")` |
| `Can't configure requestMatchers after anyRequest` | 규칙 순서 → `/admin/**` 를 위로 |
| USER 도 `@PreAuthorize` 메서드 실행됨 | `@EnableMethodSecurity` 누락 |
| SQL 로 ADMIN 줬는데 메뉴 안 보임 | 다시 로그인 (세션 권한) |
| 403 때 하얀 Whitelabel 화면 | `templates/error/403.html` 위치 확인 |
| `/admin/members` 500 오류 | `return "admin/members"` (폴더 포함) |
| 권한 변경 시 400 | 폼 `name="role"` ↔ `@RequestParam String role` 이름 일치 |

### 15. Git 연동

- `README.md` 에 이번 주 진행 상황 기록 (예 : "6주차 권한(ROLE) 접근 제어, 관리자 페이지 분리 완료")
- 변경 파일 add → commit → GitHub 원격 저장소로 push
- 관리자 계정(`admin/1234`)은 실습용 → 실제 서비스는 강한 비밀번호 + 별도 관리

### 참고 : 실습 중 겪은 문제

- 슬라이드 코드를 **통째로 붙여 넣어** 오류 발생 → "기존 ○○에 추가" 는 새 줄만 기존 코드 안에 끼워 넣어야 함
  - `SecurityConfig` : `authorizeHttpRequests` 블록 전체를 `;` 뒤에 다시 붙여 문법 오류 → 기존 블록 안에 `/admin/**` 한 줄만 추가
  - `MemberService` : `{ ... }`(생략 표시)까지 붙여 메서드 중복 정의 → 기존 메서드 위에 `@PreAuthorize` 만 추가
- import 누락 · 잘못된 import : `MemberController` 에 `java.lang.reflect.Member` 가 선택됨 → `com.example.demo.model.domain.Member`, `MemberService` 에 `List`·`Sort` import 누락
- 파일 이름 대소문자 : `adminController.java` 안에 `public class AdminController` → 파일명을 `AdminController.java` 로 변경 (public 클래스명 = 파일명)
- 편집 후 저장하지 않아 디스크 파일이 비어 있던 문제, 제공 템플릿 중 `mypage.html` 복사 누락
- 이 프로젝트의 일반 USER 계정은 슬라이드의 student1 대신 `saevit`, `saevit2` 사용

### 실행/수정 상태

- `SecurityConfig.java`(URL 권한 규칙, `@EnableMethodSecurity`), `AdminController.java`(신규), `MemberController.java`(`/mypage`), `MemberService.java`(조회·권한 변경·삭제, `@PreAuthorize`) : 실행/수정 완료
- `admin/members.html`, `error/403.html`, `mypage.html` 추가, `index.html` 교체부분 ①~③ 반영 완료
- `admin` 계정 생성 및 role `ADMIN` 변경 완료
- 서버 실행 후 `/mypage`, `/admin/members` 접근(302/403/200), 권한 변경·삭제 동작 확인 완료

## 실습 (6주차 연습문제 : 관리자 보호 & 매니저(MANAGER) 권한 추가하기)

### 연습문제 요구사항

1. 관리자 본인 보호 : 관리자가 자기 자신을 USER 로 바꾸거나 삭제하면 관리자가 0명이 될 수 있음 → `changeRole`, `deleteMember` 에서 대상이 본인이면 거부 (힌트 : `SecurityContextHolder ... getName()`)
2. 매니저(MANAGER) 권한 추가 : 회원 관리에서 MANAGER 를 부여할 수 있게 하고, `/testdb` 는 ADMIN, MANAGER 만 접근 (USER 403, 힌트 : `hasAnyRole(...)`)
3. (추가 도전) 회원목록 메뉴도 USER 에게 숨기기

### 구현 내용

**`MemberService.java` (① 본인 보호)**
```java
@PreAuthorize("hasRole('ADMIN')")
public void changeRole(Long id, String role) {
    ...
    Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    checkNotSelf(member); // [연습문제 ①] 본인 보호
    member.setRole(role);
    memberRepository.save(member);
}

@PreAuthorize("hasRole('ADMIN')")
public void deleteMember(Long id) {
    Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    checkNotSelf(member); // [연습문제 ①] 본인 보호
    memberRepository.delete(member);
}

// [연습문제 ①] 대상이 로그인한 본인이면 거부 (관리자 0명 방지)
private void checkNotSelf(Member member) {
    String loginUsername = SecurityContextHolder.getContext().getAuthentication().getName(); // 현재 로그인 아이디
    if (member.getUsername().equals(loginUsername)) {
        throw new IllegalArgumentException("자기 자신의 권한 변경·삭제는 할 수 없습니다.");
    }
}
```
- `SecurityContextHolder` : 컨트롤러가 아닌 서비스에서도 현재 로그인 사용자 정보를 꺼낼 수 있음 (`Principal` 파라미터 대신 사용)
- 권한 변경·삭제 두 메서드가 같은 검사를 쓰므로 `checkNotSelf()` 로 분리
- 예외 메시지는 기존 `AdminController` 의 `catch (IllegalArgumentException e)` 가 flash 메시지(`error`)로 전달 → 회원 관리 화면에 빨간 경고로 출력 (컨트롤러·화면 수정 없음)

**`MemberService.java` (② 허용 권한에 MANAGER 추가)**
```java
private static final List<String> ROLES = List.of("USER", "MANAGER", "ADMIN"); // [연습문제 ②] MANAGER 추가
```
- 회원 관리 화면(`admin/members.html`)의 select 에는 MANAGER 가 이미 있으므로 화이트리스트만 수정

**`SecurityConfig.java` (② `/testdb` 접근 제한)**
```java
.requestMatchers("/admin/**").hasRole("ADMIN") // [6주차] 관리자만
.requestMatchers("/testdb").hasAnyRole("ADMIN", "MANAGER") // [연습문제 ②] 회원목록 : 관리자, 매니저만
.anyRequest().authenticated()) // 나머지 : 로그인 필요
```
- `hasAnyRole(...)` : 나열한 역할 중 하나라도 있으면 통과 (`ROLE_` 은 자동으로 붙음)
- `anyRequest()` 보다 반드시 위에 작성

**`index.html` (추가 도전 : 회원목록 메뉴 숨기기)**
```html
<!-- [5주차] 회원 전용 페이지 / [연습문제 추가 도전] 관리자, 매니저에게만 메뉴 표시 -->
<li class="nav-item" sec:authorize="hasAnyRole('ADMIN', 'MANAGER')">
    <a class="nav-link" th:href="@{/testdb}">회원목록</a>
</li>
```
- 메뉴 숨김(화면)은 편의일 뿐이고, 실제 차단은 `SecurityConfig` 의 URL 규칙이 담당

### 실행 및 확인 방법

1. 서버 실행 후 `admin` 으로 로그인 → 관리자 → 회원 관리
2. 본인(admin) 행에서 권한을 USER 로 변경하거나 삭제 → "자기 자신의 권한 변경·삭제는 할 수 없습니다." 표시, DB 의 role 은 ADMIN 그대로
3. 다른 회원의 권한을 MANAGER 로 변경 → "권한을 MANAGER (으)로 변경했습니다." 표시
4. 해당 회원으로 **다시 로그인** → MANAGER 배지, 회원목록 메뉴 표시, `/testdb` 접근 가능, `/admin/members` 는 403
5. USER 계정으로 로그인 → 회원목록 메뉴 숨김, `/testdb` 주소 직접 입력 시 403

### 확인 결과

| 시나리오 | 결과 |
|---|---|
| admin 이 자기 권한을 USER 로 변경 | "자기 자신의 권한 변경·삭제는 할 수 없습니다." 출력, DB 는 ADMIN 유지 |
| admin 이 자기 자신을 삭제 | 같은 메시지로 거부 |
| admin 이 다른 회원에게 MANAGER 부여 | "권한을 MANAGER (으)로 변경했습니다." 출력, DB 반영 |
| DB 는 MANAGER, 재로그인 전 세션 | `/testdb` 403 (세션 권한은 여전히 `ROLE_USER`) |
| MANAGER 가 권한 변경 요청(POST) 직접 전송 | 403 |

| 주소 | USER | MANAGER | ADMIN | 비로그인 |
|---|---|---|---|---|
| `/mypage` | 200 | 200 | 200 | 302 → 로그인 |
| `/testdb` (회원목록) | 403 | 200 | 200 | 302 → 로그인 |
| `/admin/members` (회원 관리) | 403 | 403 | 200 | 302 → 로그인 |
| 회원목록 메뉴 | 숨김 | 표시 | 표시 | 숨김 |

### 참고 : 실행 중 겪은 문제

- 테스트용 임시 계정을 만들어 USER/MANAGER 시나리오를 확인한 뒤, 관리자 회원 삭제 기능으로 다시 삭제 (기존 계정의 권한은 변경하지 않음)

### 실행/수정 상태

- `MemberService.java`(본인 보호 `checkNotSelf`, 허용 권한 MANAGER 추가), `SecurityConfig.java`(`/testdb` `hasAnyRole`), `index.html`(회원목록 메뉴 숨김) : 실행/수정 완료
- 6주차 연습문제 2개 + 추가 도전 모두 서버 실행 후 동작 확인 완료

---

<a id="week7"></a>

# 🟪 7주차 — 회원 관리 REST API(JSON)와 Postman 테스트

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 주제: 6주차 회원 관리 기능을 REST API(JSON)로 바꾸고 Postman 으로 테스트

## 수업 내용 정리

### 1. API 란?

- **API** : 프로그램끼리 데이터(JSON)를 주고받는 약속 (요청 Request → 응답 Response)
- 서버 1 : N 연동 — 웹, 모바일 앱, 스마트워치, AI 챗봇이 **같은 서버**를 사용 → 서버는 화면이 아니라 **데이터만** 응답
  - 참고 : 6주차까지의 페이지는 완성된 화면(HTML)을 응답
- 오픈 API 예 : 지도·길찾기, 간편결제, 소셜 로그인, 날씨, 공공데이터포털(버스 도착, 미세먼지)

### 2. SSR 과 CSR

| 구분 | SSR (6주차까지 우리 방식) | CSR (요즘 많이 쓰는 방식) |
|---|---|---|
| 서버가 보내는 것 | Thymeleaf 로 **완성한 HTML** | **JSON 데이터만** (REST API) |
| 화면을 그리는 쪽 | 서버 | 브라우저·앱 (React, Vue 등) |

- 실무는 두 방식을 혼용 : 관리자 화면은 Thymeleaf, 앱·외부 연동은 API → 오늘은 회원 관리를 API 로 구현
- React(Next.js, React Native 로 앱까지, 생태계 가장 큼) vs Vue(Nuxt, 빠른 개발·중소 규모·일부 도입에 적합)

### 3. REST 와 JSON

- **REST** : API 설계 방식 — **주소(URL)는 무엇을, HTTP 메서드는 어떻게**
  - GET 조회, POST 생성, PATCH 수정(일부), DELETE 삭제 (PUT 은 전체 교체)
- **JSON** : 요청/응답 데이터 표준 형식, 사람도 읽기 쉽고 거의 모든 언어가 지원
  - `{ }` 객체("이름": 값 의 묶음), `[ ]` 배열(여러 개 나열), 값(`"USER"`, `3`, `true`, `null`)

| 구분 | 기존 페이지 (6주차까지) | API 통신 (7주차) |
|---|---|---|
| 주소 예 | `/admin/members` | `/api/admin/members` |
| 요청 방식 | 링크 클릭, 폼 제출 (GET·POST) | fetch, 앱 요청 (GET·POST·PATCH·DELETE) |
| 결과 확인 | 바뀐 화면을 눈으로 확인 | 상태 코드(200, 201, 404 …)로 판단 |
| 페이지 이동 | 매번 새로고침 | 새로고침 없음, 필요한 부분만 바뀜 |

### 4. `@Controller` vs `@RestController`

- `@Controller` : **화면 이름**을 반환 → HTML 화면 (받는 쪽 : 사람/브라우저)
- `@RestController` : **객체**를 반환 → 스프링이 **JSON 으로 자동 변환** (받는 쪽 : 앱·JS·Postman)
- `@RestController` = `@Controller` + `@ResponseBody`

### 5. 첫 번째 API - `/api/hello` (단계 1)

**`MemberApiController.java` (신규)**
```java
@RestController // 화면(HTML) 대신 데이터(JSON)를 응답하는 컨트롤러
@RequestMapping("/api") // 이 컨트롤러의 모든 URL 앞에 /api
public class MemberApiController {
    @Autowired
    MemberService memberService;

    @GetMapping("/hello") // 첫 번째 API : GET /api/hello (누구나)
    public Map<String, Object> hello() {
        // Map 을 반환하면 스프링이 JSON 으로 바꿔 준다
        return Map.of("message", "안녕하세요, REST API!",
                "time", LocalDateTime.now().withNano(0));
    }
}
```
- `SecurityConfig` 의 `authorizeHttpRequests` 블록 안에 `.requestMatchers("/api/hello").permitAll()` 추가
- 결과 : `{"message":"안녕하세요, REST API!","time":"..."}` (크롬 Pretty-print 체크 → 정렬)

### 6. 내 정보 API - `/api/me` 와 응답 DTO (단계 2·3)

- 엔티티(`Member`)를 그대로 반환하면 → **비밀번호 해시(`$2a$10$...`)까지 JSON 으로 노출**
  - 반환 객체의 **getter 가 있는 값은 전부** JSON 이 되기 때문
  - 해시도 유출되면 공격에 쓰일 수 있음 (`1234` 같은 약한 비밀번호는 사전 목록·대입으로 금방 찾음), 목록 API 였다면 전원 유출
- 해결 : 응답용 객체를 따로 만든다 → **응답 DTO** (필요한 값만 : id, 아이디, 이름, 권한)

**`MemberResponse.java` (신규, `model/dto`)**
```java
// API 응답용 DTO : 화면(JSON)에 내보낼 값만 담는다 → password 는 빠진다
public record MemberResponse(Long id, String username, String name, String role) {
    public static MemberResponse from(Member member) { // 엔티티 → 응답 DTO 변환
        return new MemberResponse(member.getId(), member.getUsername(),
                member.getName(), member.getRole());
    }
}
```
- `record` (자바 16+) : 생성자·getter·equals 등을 자동으로 만들어 주는 간결한 데이터 클래스

**`MemberApiController.java` (`/api/me` 추가)**
```java
@GetMapping("/me") // 내 정보 API : GET /api/me (로그인 필요)
public MemberResponse me(Principal principal) { // 반환형 Member → MemberResponse
    Member member = memberService.findByUsername(principal.getName());
    return MemberResponse.from(member); // 엔티티 대신 DTO 반환
}
```

| 구분 | Member (엔티티) | MemberForm (5주차 DTO) | MemberResponse (7주차 DTO) |
|---|---|---|---|
| 역할 | DB 저장용 | 들어오는 데이터 (요청) | **나가는 데이터 (응답)** |
| 방향 | DB ↔ 서버 | 사용자 → 서버 | 서버 → 사용자 |
| 담는 값 | id, username, password(해시), name, role 전부 | username, password, passwordConfirm, name | id, username, name, role |
| 빠진 값 | 없음 | role | **password** |
| 막는 공격 | — | 권한 조작 (role 을 ADMIN 으로 몰래 보내기) | **정보 노출 (비밀번호 해시 유출)** |

- 6주차에는 Thymeleaf 가 서버 안에서 화면에 쓴 값만 출력해서 나가는 DTO 가 필요 없었지만, JSON 은 값이 전부 보이므로 필요

### 7. Postman 과 Basic 인증 (단계 4)

- **Postman** : API 를 호출해 보는 프로그램 (외부 앱처럼 요청 전송, 무료·가입 없이 사용)
  - 메서드 + 주소 입력 → Send, Authorization · Body 설정, 응답 상태 코드·헤더·본문 확인
  - Postman 이 없으면 : 브라우저 F12 → 네트워크(Network) 탭, curl
- 문제 : Postman 으로 `/api/me` 를 호출하면 JSON 대신 **로그인 화면 HTML** (실제로는 302 → `/login`)
  - 브라우저는 로그인 후 **세션 쿠키(`JSESSIONID`)** 를 항상 함께 보내지만, Postman 은 쿠키가 없음 → 로그인 안 된 상태
  - 서버는 브라우저와 앱을 구분하지 않음

| 구분 | 브라우저 (화면) | 앱 · Postman (API) |
|---|---|---|
| 인증 방법 | 로그인 한 번 → 세션 쿠키로 계속 인증 | **요청할 때마다** 인증 정보 전송 |
| 로그인 안 했으면 | 302 → 로그인 화면 | **401 Unauthorized** |

- **Basic 인증** : `아이디:비밀번호` 를 **Base64** 로 변환해 `Authorization: Basic ...` 헤더로 전송 (이후 주차에서는 JWT 토큰)
  - Base64 는 데이터를 64가지 ASCII 문자로 표현하는 **인코딩**(전송 오류 방지)일 뿐 **암호화가 아님** → 실서비스는 HTTPS 필수
- 401 "누구세요?" (인증 실패) / 403 "권한이 없어요" (인가 실패), 302 와 401 을 나누는 기준은 주소(`/api/`)

**`SecurityConfig.java` (`.rememberMe(...)` 뒤에 이어서 추가, 앞 줄 끝의 `;` 를 지우고 작성)**
```java
// 5. [7주차] REST API 설정
.httpBasic(Customizer.withDefaults()) // Postman : 요청마다 아이디·비밀번호 (Basic 인증)
.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // JSON API 는 CSRF 토큰 검사 제외
.exceptionHandling(ex -> ex.defaultAuthenticationEntryPointFor( // 로그인 안 한 API 요청 :
        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), // 로그인 화면(302) 대신 401
        request -> request.getRequestURI().startsWith("/api/")));
```
- 결과 : No Auth → 401(본문 없음), Basic `admin / 1234` → 200 + 내 정보, 비밀번호 틀림 → 401
- 브라우저(로그인 상태)는 그대로 200, 일반 화면 페이지는 여전히 로그인 화면으로 이동

### 8. REST 주소 규칙과 상태 코드

- 6주차 화면은 GET·POST 만 사용 → 7주차는 **주소에 대상만, 할 일은 HTTP 메서드**로 (같은 주소라도 메서드가 다르면 다른 일)

| 하고 싶은 일 | 6주차 화면 방식 | 7주차 REST 방식 |
|---|---|---|
| 회원 목록 보기 | `GET /admin/members` | `GET /api/admin/members` |
| 회원 1명 보기 | (없음, 목록에서 봄) | `GET /api/admin/members/3` |
| 회원 등록 | `POST /signup` | `POST /api/admin/members` |
| 권한 변경 | `POST /admin/members/3/role` | `PATCH /api/admin/members/3/role` |
| 회원 삭제 | `POST /admin/members/3/delete` | `DELETE /api/admin/members/3` |

- API 응답 = **상태 코드 + 헤더 + 본문** → 숫자로 결과를 판단 (200번대 성공, 400번대 요청 쪽 문제)

| 코드 | 의미 | 언제 |
|---|---|---|
| 200 OK | 성공 | 조회, 수정 성공 |
| 201 Created | 새로 만들었음 | 등록 성공 |
| 204 No Content | 성공, 돌려줄 내용 없음 | 삭제 성공 |
| 400 Bad Request | 요청 내용이 잘못됨 | 빈 값, 중복 아이디, 허용 안 되는 권한 |
| 401 Unauthorized | 누구인지 모름 | 로그인 정보 없음 |
| 403 Forbidden | 권한 없음 | USER 가 관리자 API 호출 |
| 404 Not Found | 대상이 없음 | 없는 회원 번호 조회 |

- **`ResponseEntity`** : 상태 코드를 직접 정해서 응답
```java
ResponseEntity.ok(body)                  // 200
ResponseEntity.created(uri).body(body)   // 201
ResponseEntity.noContent().build()       // 204
ResponseEntity.badRequest().body(error)  // 400
ResponseEntity.notFound().build()        // 404
```

### 9. 관리자 API - 조회 (단계 5)

**`AdminApiController.java` (신규)**
```java
@RestController // 관리자 회원 관리 API (JSON)
@RequestMapping("/api/admin/members") // 주소는 하나, 할 일은 메서드로 구분
public class AdminApiController {
    @Autowired
    MemberService memberService;

    @GetMapping // 회원 목록 : GET /api/admin/members
    public List<MemberResponse> list() {
        List<Member> members = memberService.findAll();
        return members.stream().map(MemberResponse::from).toList(); // 엔티티 목록 → DTO 목록
    }

    @GetMapping("/{id}") // 회원 1명 : GET /api/admin/members/3
    public ResponseEntity<MemberResponse> get(@PathVariable Long id) {
        return memberService.findById(id)
                .map(member -> ResponseEntity.ok(MemberResponse.from(member))) // 있으면 200 OK
                .orElse(ResponseEntity.notFound().build()); // 없으면 404
    }
}
```
- `stream().map(MemberResponse::from).toList()` : 엔티티 목록을 하나씩 DTO 로 바꿔 새 목록 생성
- `MemberService` 에 `Optional<Member> findById(Long id)` 추가 (없을 수도 있는 값 → `Optional`)
- `SecurityConfig` 관리자 규칙 수정 → **새 주소를 만들면 접근 규칙도 바로 추가** (6주차 교훈)
  ```java
  .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
  ```
- 결과 : admin → 목록 200(배열, 비밀번호 없음), `/9999` → 404, USER → 403 (규칙이 없다면 USER 도 전체 회원 목록 200)

### 10. 관리자 API - 등록 (단계 6)

**`AdminApiController.java` (POST 추가)**
```java
@PostMapping // 회원 등록 : POST /api/admin/members (본문 JSON → MemberForm)
public ResponseEntity<?> create(@RequestBody MemberForm form) {
    try {
        Member saved = memberService.signup(form); // 5주차 회원가입 로직 재사용 (BCrypt)
        return ResponseEntity
                .created(URI.create("/api/admin/members/" + saved.getId())) // 201 + Location
                .body(MemberResponse.from(saved));
    } catch (IllegalArgumentException e) { // 중복 아이디, 빈 값 …
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // 400
    }
}
```
- `@RequestBody` : 요청 본문의 JSON 을 자바 객체로 변환 (5주차 폼 데이터는 스프링이 자동으로 채웠지만, JSON 은 `@RequestBody` 필수)
- `ResponseEntity<?>` : 성공 본문은 `MemberResponse`, 실패 본문은 `Map` → 제네릭 와일드카드(모든 타입 가능)

**`MemberService.java` (`signup()` 첫 줄에 빈 값 검사)** — JSON 요청에는 HTML 의 `required` 검사가 없기 때문
```java
public Member signup(MemberForm form) {
    if (isBlank(form.getUsername()) || isBlank(form.getPassword()) || isBlank(form.getName())) {
        throw new IllegalArgumentException("아이디, 비밀번호, 이름은 필수입니다.");
    }
    ... // 기존 중복 아이디 · 비밀번호 확인 검사
}

private boolean isBlank(String value) { return value == null || value.isBlank(); }
```
- 결과 : 첫 등록 201 + `Location: /api/admin/members/번호`, 같은 아이디 400, `{"username":"x"}` 400(필수 값), `"role":"ADMIN"` 을 몰래 넣어도 **USER** (`MemberForm` 에 role 이 없음), DB 에는 `$2a$10$...` 로 저장

### 11. 관리자 API - 권한 변경 · 삭제 (단계 7)

**`RoleRequest.java` (신규, `model/dto`)**
```java
// 권한 변경 요청 DTO : 본문 {"role": "ADMIN"} 을 받는다
public record RoleRequest(String role) {
}
```

**`MemberService.java` (`changeRole()` 이 바뀐 회원을 돌려주도록 수정)**
```java
public Member changeRole(Long id, String newRole) { // ① void → Member
    ... // 기존 코드 그대로 (허용 권한 검사, 본인 보호)
    return memberRepository.save(member); // ② 마지막 줄 : 바뀐 회원을 돌려준다
}
```

**`AdminApiController.java` (PATCH · DELETE 추가)**
```java
@PatchMapping("/{id}/role") // 권한 변경 : PATCH /api/admin/members/3/role {"role":"ADMIN"}
public ResponseEntity<?> changeRole(@PathVariable Long id, @RequestBody RoleRequest request) {
    try {
        Member member = memberService.changeRole(id, request.role());
        return ResponseEntity.ok(MemberResponse.from(member)); // 200 OK + 바뀐 회원
    } catch (IllegalArgumentException e) { // 허용되지 않는 권한, 없는 회원 …
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}

@DeleteMapping("/{id}") // 회원 삭제 : DELETE /api/admin/members/3
public ResponseEntity<?> delete(@PathVariable Long id) {
    try {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build(); // 204 No Content (돌려줄 본문 없음)
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
```
- 보안 검사는 6주차 서비스에 이미 있음 → **화면이든 API 든 같은 서비스를 호출하므로 같은 규칙이 적용**
  - `@PreAuthorize` (관리자만) → USER 가 호출하면 403
  - 허용 권한 검사 → `"SUPER"` 는 400
  - 본인 보호 → admin 이 자기 자신을 변경·삭제하면 400

| 주차 | DTO 핵심 |
|---|---|
| 5주차 (`MemberForm`) | 들어오는 값을 DTO 로 분리 → 권한 조작 방지 |
| 6주차 (없음) | Thymeleaf 가 서버 안에서 화면에 쓴 값만 출력 → 나가는 DTO 불필요 |
| 7주차 (2개 추가) | JSON 은 값이 전부 보임 → 응답용 `MemberResponse`, 받는 값도 JSON 이라 `RoleRequest` 추가 |

### 12. 화면에서 API 사용하기 (단계 8)

- 제공 화면 복사 : `templates/admin/members-api.html` (신규), `templates/admin/members.html` ("API 버전으로 보기" 링크 추가된 버전으로 교체)

**`AdminController.java` (추가)**
```java
@GetMapping("/members-api") // 우리가 만든 JSON API 를 fetch 로 사용하는 회원 관리 화면
public String membersApi() {
    return "admin/members-api"; // 데이터는 화면이 API 로 가져온다
}
```
- 화면은 HTML 껍데기만, 데이터는 JavaScript `fetch` 가 `/api/admin/members` 에서 JSON 으로 가져와 다시 그림 → **페이지가 새로고침되지 않음**
- 오른쪽 기록창에 요청과 응답 코드 표시 (F12 네트워크 탭에서도 확인 가능)
- 등록 POST 201, 변경 PATCH 200, 삭제 DELETE 204, 빈 칸 등록 400, 9999번 조회 404

| 손님 | 신분 증명 방법 | 결과 |
|---|---|---|
| 브라우저 (제공 화면의 fetch) | admin 으로 로그인할 때 받은 **세션 쿠키**를 자동으로 함께 보냄 | 같은 API, 같은 결과 |
| Postman | 요청마다 **아이디·비밀번호(Basic 인증)** 를 직접 실어 보냄 | 같은 API, 같은 결과 |

### 13. API 테스트 정리 (Postman)

| # | 메서드 · 주소 | 계정 | Body | 기대 결과 |
|---|---|---|---|---|
| 1 | `GET /api/hello` | 없음 | 없음 | 200 |
| 2 | `GET /api/me` | 없음 / admin | 없음 | 401 / 200 (password 없음) |
| 3 | `GET /api/admin/members` | admin / USER | 없음 | 200 / 403 |
| 4 | `GET /api/admin/members/9999` | admin | 없음 | 404 |
| 5 | `POST /api/admin/members` | admin | `{"username":"postman1","password":"1234","passwordConfirm":"1234","name":"포스트맨"}` | 201 (같은 아이디 다시 → 400) |
| 6 | `PATCH /api/admin/members/{id}/role` | admin | `{"role":"ADMIN"}` / `{"role":"SUPER"}` | 200 / 400 |
| 7 | `PATCH /api/admin/members/{admin id}/role` | admin | `{"role":"USER"}` | 400 (본인 변경) |
| 8 | `DELETE /api/admin/members/{id}` | USER / admin | 없음 | 403 / 204 |

- Body 는 **raw → JSON** 으로 전송, Authorization 은 **Basic Auth**

### 14. 자주 나는 오류

| 증상 | 원인 → 해결 |
|---|---|
| 415 Unsupported Media Type | Body 를 raw → JSON 으로 안 보냄 |
| 400 JSON parse error | 따옴표, 쉼표, 괄호 확인 |
| 405 Method Not Allowed | 메서드가 다름 (PUT ↔ PATCH) |
| Postman 에 로그인 HTML | 단계 4 보안 설정 누락 |
| 401 / 403 | Authorization 미설정·비밀번호 오류 / USER 로 관리자 API |
| 제공 화면에서 변경·삭제가 403 | `csrf.ignoringRequestMatchers("/api/**")` 누락 |
| JSON 에 password | 엔티티 그대로 반환 → `MemberResponse.from()` |

- 개발 중에는 오류 JSON 에 trace(서버 내부 정보)가 같이 나옴 → 실서비스에서는 숨겨야 함

### 15. Git 연동

- `README.md` 에 이번 주 진행 상황 기록 (예 : "7주차 회원 관리 REST API(JSON) 완료")
- 변경 파일 add → commit → GitHub 원격 저장소로 push
- Postman 결과도 요약해 두면 받은 사람이 바로 API 테스트 가능 (13번 표)

### 참고 : 실습 중 겪은 문제

- `SecurityConfig` 에 `.requestMatchers("/api/hello").permitAll()` 을 `.rememberMe(...);` 뒤에 붙여 문법 오류 → `requestMatchers` 는 `authorizeHttpRequests` 블록 **안**, `anyRequest()` 보다 위에 넣어야 함 (반대로 `.httpBasic()` 등은 `.rememberMe()` 뒤에 `;` 를 지우고 이어 씀)
- 이전에 실행한 서버가 8080 포트를 잡고 있어 새 서버 실행 실패 → 기존 서버를 중지한 뒤 다시 실행
- `MemberApiController` 에 `Member` import 누락 (`com.example.demo.model.domain.Member`)
- `/api/me` 수정 코드를 `MemberResponse.java` 의 record 아래에 붙여 문법 오류 → `MemberApiController` 의 기존 `me()` 메서드를 고쳐야 함
- 빈 값 검사를 `signup()` 의 세 번째 검사로 넣어 `{"username":"x"}` 요청에 "비밀번호가 일치하지 않습니다." 가 출력됨 → 첫 줄로 옮겨 "필수입니다." 메시지가 나오도록 수정
- Postman Authorization 에 일반(USER) 계정을 넣어 관리자 API 가 403 → admin 계정으로 변경 (403 은 보안 규칙이 제대로 동작한다는 뜻)
- `changeRole()` 수정 시 기존 `save()` 줄을 남긴 채 `return ... save()` 를 추가해 저장이 두 번 실행 → 기존 줄 삭제
- 이 프로젝트의 일반 USER 계정은 슬라이드의 student1 대신 `saevit` 사용, 허용 권한은 6주차 연습문제로 `MANAGER` 까지 포함

### 실행/수정 상태

- `MemberApiController.java`(신규, `/api/hello`·`/api/me`), `AdminApiController.java`(신규, GET·POST·PATCH·DELETE), `MemberResponse.java`·`RoleRequest.java`(신규 DTO) : 실행/수정 완료
- `SecurityConfig.java`(`/api/hello` 허용, `/api/admin/**` 관리자 규칙, Basic 인증·CSRF 제외·401 응답), `MemberService.java`(`findById`, 빈 값 검사, `changeRole` 반환형), `AdminController.java`(`/members-api`) : 실행/수정 완료
- `admin/members-api.html` 추가, `admin/members.html` 링크 반영 완료
- 서버 실행 후 13번 표의 테스트(200·201·204·400·401·403·404) 모두 기대 결과 확인 완료
