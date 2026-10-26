# 📒 자바웹프로그래밍(2) 공부 노트

> 최도현 교수님 수업을 들으면서 주차별로 배운 것, 직접 실습해 본 것, 막혔던 것들을 정리한 노트예요.

## 목차

| 주차 | 이번 주에 한 것 |
|---|---|
| [🟦 2주차](#week2) | 스프링 부트 개발환경 세팅하고 첫 페이지 띄워 보기 |
| [🟩 3주차](#week3) | 정적 리소스 이해하고 외부 웹 템플릿으로 포트폴리오 만들기 |
| [🟧 4주차](#week4) | MySQL 연동하고 JPA로 DB 읽어 오기 |
| [🟨 5주차](#week5) | 로그인/로그아웃, 비밀번호 암호화 (Spring Security) |
| [🟥 6주차](#week6) | 권한(ROLE)으로 접근 제어하고 관리자 페이지 분리하기 |
| [🟪 7주차](#week7) | 회원 관리를 REST API(JSON)로 바꿔 보고 Postman으로 테스트 |

<br>

<a id="week2"></a>

# 🟦 2주차 — 스프링 부트 개발환경 설정 및 테스트

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 이번 주 주제: 스프링 부트 개발환경 설정하고 테스트해 보기

## 📖 오늘 배운 것

### 1. 스프링 부트는 어떻게 돌아갈까?

**내장 서버 & 컨테이너**
- 스프링 부트 안에는 **톰캣(Tomcat)**이 들어 있어서(WAS), 서버를 따로 설치하지 않아도 자바로 만든 동적 웹이 바로 돌아간다.
- **스프링 컨테이너**가 자바 객체(빈)를 만들고, 초기화하고, 호출하고, 없애는 것까지 다 관리해 준다. → 이게 의존성 주입(DI)의 바탕.

**DispatcherServlet(프론트 컨트롤러) 흐름**
1. 브라우저가 `/URI` 형태로 요청을 보낸다.
2. `DispatcherServlet`이 `HandlerMapping`으로 알맞은 컨트롤러를 찾아서 넘겨 준다.
3. `Controller`가 할 일을 처리하고 `Model`(데이터) + `View 이름`을 돌려준다.
4. `ViewResolver`가 View 이름으로 실제 화면(템플릿)을 정한다.
5. `Thymeleaf`가 서버에서 HTML을 완성해서 응답한다.

**MVC 패턴 한 줄 정리**
- **Model** : 화면에 넘길 데이터
- **View** : 화면 출력 담당 (Thymeleaf, JSP 같은 템플릿 엔진)
- **Controller** : 요청을 받아서 흐름을 조절하는 역할

### 2. Thymeleaf

- HTML 기반 템플릿 엔진이다. JSP를 대신하는 요즘 방식이라고 한다.
- MVC에서 View를 맡고, `DispatcherServlet`을 거쳐서 동작한다.
- 기본 문법 (자주 쓰니까 외워 두기)
  - 선언 : `<html xmlns:th="http://www.thymeleaf.org">`
  - 주석 : `<!--/* 주석 내용 */-->`
  - 텍스트 출력 : `<span th:text="${data}"></span>` (`${...}` 는 변수 꺼내는 표현식)
- `pom.xml`의 `spring-boot-starter-thymeleaf` 의존성에 이미 포함되어 있다.

### 3. URL 매핑과 컨트롤러

- 화면의 링크는 전부 컨트롤러를 거쳐야 한다. 정적 파일을 바로 연결하면 404가 난다. (처음에 이걸로 한 번 헤맸다)
- 컨트롤러는 이렇게 쓴다.
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
- 컨트롤러가 돌려준 문자열(view name)은 `templates/viewName.html` 파일과 이어진다.

### 4. Git 연동

- `README.md`에 그 주 진행 상황을 적는다. (예 : "2주차 스프링 부트 개발환경, 테스트 완료")
- 바뀐 파일 add → commit → GitHub로 push
- 실습할 때마다 계속 커밋해 두는 습관 들이기!

## ✏️ 실습 (2주차 연습문제 : URL 맵핑과 컨트롤러 추가하기)

### 문제는 이랬다
1. 기존 `hello.html`의 링크를 "두번째 헬로 페이지"로 바꾸고, `/hello2`로 이동하게 하기
2. `DemoController`에 `/hello2` 매핑을 추가하고, 속성(Model attribute) 5개를 담아서 넘기기
3. `hello2.html`을 만들어서 속성 5개를 전부 출력하기

### 이렇게 만들었다

**`DemoController.java`**
- `/index` : `index.html` 로 연결
- `/hello` : `hello.html` 로 연결, `data` 속성 전달 (원래 있던 기능)
- `/hello2` : `hello2.html` 로 연결, `data1` ~ `data5` 속성 5개 전달 (이번 연습문제)

**`hello2.html` (새로 만듦)**
- `th:text` 로 `data1` ~ `data5` 값을 순서대로 출력
- 맨 아래에 `/index` 로 돌아가는 링크도 넣었다.

**페이지 사이 링크 고치기**
- `index.html` → `/hello` 링크 문구를 "첫번째 헬로 페이지"로 수정
- `hello.html` → `/hello2` 로 가는 "두번째 헬로 페이지" 링크로 변경
- 그래서 `index → hello → hello2 → index` 로 한 바퀴 도는 구조가 됐다.

**`pom.xml`**
- `spring-boot-starter-actuator` 의존성 추가 (앱 상태 확인용, `/actuator/health` 같은 것)

### 확인하는 방법
1. `DemoApplication.java` 실행 (Ctrl+F5)
2. 브라우저에서 `http://localhost:8080/index` 접속
3. "첫번째 헬로 페이지" 링크 → `/hello` 로 이동, `data` 값 확인
4. "두번째 헬로 페이지" 링크 → `/hello2` 로 이동, `data1`~`data5` 확인
5. "홈페이지 메인" 링크 → `/index` 로 돌아오는지 확인

### 상태
- index.html, hello.html, hello2.html : 실행해서 확인까지 끝! ✅

---

<a id="week3"></a>

# 🟩 3주차 — 정적 리소스(Static Resources) 처리와 외부 웹 템플릿 적용

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 이번 주 주제: 정적 리소스 처리하는 법, 외부 웹 템플릿 가져다 쓰기

## 📖 오늘 배운 것

### 1. 스프링 부트는 정적 리소스를 어떻게 처리할까?

- 설정을 따로 안 해도 아래 폴더들은 자동으로 정적 리소스 폴더로 인식한다.
  - `src/main/resources/static`
  - `src/main/resources/public`
  - `src/main/resources/resources`
  - `src/main/resources/META-INF/resources`
- 이 폴더 안의 파일은 컨트롤러 없이도 파일 경로 그대로 URL이 된다. (예 : `static/css/style.css` → `/css/style.css`)

### 2. templates 폴더 vs static/public 폴더

| 구분 | templates | static / public |
|---|---|---|
| 처리 방식 | Thymeleaf 뷰 엔진이 서버에서 렌더링 | 별도 처리 없이 파일 그대로 응답 |
| 접근 방법 | 컨트롤러가 view name을 반환해야 접근 가능 | URL 경로로 직접 접근 가능 (컨트롤러 불필요) |
| 동적 데이터 | `${...}` 등으로 Model 데이터 출력 가능 | 순수 정적 파일이라 동적 데이터 바인딩 불가 |
| 주요 용도 | 화면(View)의 HTML 템플릿 | CSS, JS, 이미지, 폰트 등 프론트엔드 자원 및 별도 처리가 필요 없는 HTML |

→ 헷갈리면 이렇게 기억하자. **데이터가 들어가는 화면은 templates, 그냥 보여 주기만 하면 되는 파일은 static/public.**

### 3. 정적 자원 경로 쓰기 - `th:href`, `th:src`

- Thymeleaf 템플릿에서 정적 자원을 가져올 때는 그냥 `href`/`src` 말고 `th:href="@{...}"`, `th:src="@{...}"` 를 쓴다.
- `@{...}` 는 스프링 부트의 컨텍스트 경로를 알아서 붙여 주는 URL 표현식이다.
  ```html
  <link th:href="@{/css/bootstrap.min.css}" rel="stylesheet">
  <img th:src="@{/images/profile.png}" class="avatar-image" alt="">
  ```

### 4. 외부 웹 템플릿을 프로젝트에 붙이기

- Bootstrap 기반으로 만들어진 HTML 템플릿(TemplateMo 등)을 스프링 부트 프로젝트에 맞게 적용해 봤다.
- 템플릿에 들어 있던 `css`, `js`, `fonts`, `images` 는 `static` 폴더 아래로 옮겨서 정적 자원으로 등록
- 템플릿 HTML의 `href`, `src` 는 `th:href="@{...}"`, `th:src="@{...}"` 로 바꿔서 스프링 부트 경로랑 연결
- 컨트롤러(`/index`)와 연결되는 `index.html`을 이 방식으로 다시 만들어서 진짜 포트폴리오 같은 메인 화면을 완성!
- 컨트롤러를 안 거치고 주소로 바로 들어가야 하는 상세 페이지(`detailed_web.html`)는 `public` 폴더에 두었다.
- `<head>`에서 불러오는 외부 리소스도 하나씩 확인해 봤다.
  - Google Fonts: DM Sans (폰트)
  - `bootstrap.min.css` + `bootstrap-icons.css` (레이아웃/아이콘, 5.1.3 버전)
  - `magnific-popup.css` (이미지 팝업, jQuery 기반 플러그인)
  - `templatemo-first-portfolio-style.css` (이 사이트만의 디자인)

### 5. 네비게이션 바 & 상세 페이지 링크

- 위쪽에 고정된 nav는 `#section_1`~`#section_5` 앵커로 각 섹션으로 이동하게 만들어져 있다.
- `click-scroll.js`가 앵커(`#`)를 눌렀을 때 페이지를 새로 안 불러오고 부드럽게 스크롤해 준다.
- 상세 페이지로 나가는 링크에는 `target="_blank" rel="noopener noreferrer"` 를 붙인다.

| 값 | 역할 |
|---|---|
| `noopener` | 새로 열린 페이지가 `window.opener`로 원래 페이지를 조작하지 못하게 막는다 (피싱 사이트로 바꿔치기하는 공격 방지) |
| `noreferrer` | 이동한 페이지에 어디서 왔는지(Referer 헤더)를 알려 주지 않는다. `noopener` 효과도 같이 들어 있다. |

### 6. 기존 파일 백업

- 새 내용으로 덮어쓰기 전에 기존 `index.html`, `hello.html`, `hello2.html`을 "-복사본"이라는 이름으로 복사해 둬서 예전 버전을 남겨 두었다.

### 7. 내 프로필로 바꾸기

- Hero 섹션의 이름/소개 문구, 프로필 이미지(`profile.png`)를 내 정보로 교체
- 기술(Services) 영역 4가지(웹/AI/보안/게임)를 한글로 번역하고, 영역마다 제목과 설명을 직접 썼다.
- [Bootstrap Icons](https://icons.getbootstrap.com/)에서 어울리는 아이콘을 찾아 바꿨다. (예: AI → `bi-anthropic`)
- 프로젝트에 들어 있는 CSS(5.1.3)에는 최신 아이콘이 없어서, 최신 Bootstrap Icons CDN 링크를 `<head>`에 하나 더 붙였다.

### 8. 반응형/성능 확인

- F12 개발자 도구 → 기기 툴바(Ctrl+Shift+M)로 모바일 화면에서 어떻게 보이는지 확인
- Lighthouse 리포트를 PC/모바일 각각 뽑아서 Performance 점수가 왜 다른지 비교해 봤다.

## ✏️ 실습 (3주차 연습문제 : 포트폴리오 수정하기)

### 문제는 이랬다

1. 기술(Services) 4가지의 아이콘을 (웹만 빼고) 내용에 맞는 아이콘으로 교체하기
2. 각 기술(웹/AI/보안/게임)의 상세 페이지를 `detailed_web.html`을 재활용해서 추가로 만들기

### 이렇게 만들었다

**아이콘 교체**
- 웹(Web) : 원래 쓰던 `bi-globe` 그대로
- 인공지능(AI) : `bi-anthropic`
- 보안(Security) : `bi-shield-fill-check`
- 게임(Game) : `bi-controller`

**상세 페이지 추가 (새로 만듦)**
- `detailed_web.html`의 구조(고정 네비 + 페이지 헤더 + 요약 숫자 + 기술 스택 카드 + 숙련도/진행방식 + 푸터)를 그대로 재사용해서 3개 페이지를 `public` 폴더에 만들었다.
  - `detailed_ai.html` : 머신러닝/딥러닝, LLM & 프롬프트 엔지니어링, 데이터 처리, AI 서비스 연동
  - `detailed_security.html` : 웹 보안, 인증/인가, 취약점 분석, 보안 운영
  - `detailed_game.html` : 게임 클라이언트(Unity), 게임 서버/네트워크, 게임 기획/디자인, 데이터/배포
- `index.html`의 서비스 카드 링크(`th:href="@{/detailed_xxx.html}"`)와 실제 파일 이름이 정확히 같아야 해서 하나씩 맞춰 봤다.
- 컨트롤러 등록 없이 `public` 폴더에 넣기만 해도 주소로 바로 열리는 정적 페이지로 동작한다.

### 확인하는 방법

1. 서버 실행 후 `http://localhost:8080/` 접속
2. 기술(Services) 섹션에서 웹/AI/보안/게임 카드의 "Discover More" 클릭
3. `/detailed_web.html`, `/detailed_ai.html`, `/detailed_security.html`, `/detailed_game.html` 4개 페이지가 모두 열리고(200) 디자인도 비슷한지 확인

### 상태
- 아이콘 교체, 상세 페이지 4종 : 실행해서 확인까지 끝! ✅

---

<a id="week4"></a>

# 🟧 4주차 — 데이터베이스(MySQL) 연동 및 JPA를 통한 CRUD 테스트

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 이번 주 주제: MySQL 연동하고 JPA로 데이터 가져와 보기

## 📖 오늘 배운 것

### 1. JPA(Java Persistence API)와 영속성

- **JPA** : 자바 객체(엔티티)와 DB 테이블을 연결(ORM)해 주는 표준 API. SQL을 대신 만들어 줘서, 메서드만 호출해도 DB를 다룰 수 있다.
- **영속성(Persistence)** : 프로그램이 꺼져도 객체(데이터)가 DB에 남아 있는 성질
- **엔티티 매니저** : Repository 안에서 영속성 컨텍스트를 알아서 관리해 줘서, 트랜잭션 처리가 덜 복잡해진다.
- 대표적인 구현체(ORM)는 **하이버네이트(Hibernate)**

### 2. MySQL 연동 설정

- `pom.xml`에 주석 처리되어 있던 JPA 의존성을 다시 켰다.
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  ```
- MySQL 연결 드라이버(`mysql-connector-j`)는 JPA에 기본으로 안 들어 있어서 따로 추가해야 한다.
- MySQL 서버를 설치(Server only)한 뒤 Command Line Client로 접속해서 데이터베이스를 만들었다.
  ```sql
  create database spring;
  flush privileges;
  ```
- `application.properties`에 접속 정보와 JPA 옵션을 넣었다.
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/spring?serverTimezone=Asia/Seoul
  spring.datasource.username=root
  spring.datasource.password=비밀번호
  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
  ```
- 연결이 잘 되면 터미널에 `HikariPool-1 - Start completed.` 로그가 뜬다.
- 실행이 안 될 때 확인할 것 (트러블슈팅)
  - 일단 터미널에 나온 에러 메시지부터 읽기
  - `application.properties`의 비밀번호, DB 이름 오타 여부
  - `pom.xml`에서 JPA 의존성이 아직 주석 처리된 채로 남아 있진 않은지

### 3. VS Code MySQL 확장 쓰기

- 확장에서 `mysql`을 검색해 설치하고, 왼쪽 Database 패널에 연결 정보(호스트, 포트 3306, 계정, DB명)를 넣어서 연결한다.
- 트리에서 Tables / Views / Functions / Procedures를 볼 수 있다.
- 테이블을 우클릭하면 SQL Template(SELECT/INSERT/UPDATE/DELETE), Dump Struct(And Data), Import SQL 같은 메뉴가 있어서 백업/복원도 가능하다.
- 위쪽의 "Open in terminal"을 누르면 그 DB에 바로 접속된 `mysql>` 터미널이 열린다.

### 4. 계층형 패키지로 나누기

- 하나뿐이던 패키지(`demo`)를 MVC + Service/Repository 계층으로 쪼갰다.
  - `controller` : 요청/응답 흐름 제어 (원래 있던 `DemoController.java`를 여기로 이동)
  - `model/domain` : 엔티티 클래스 (DB 테이블 모양)
  - `model/repository` : DB에 접근(쿼리)하는 인터페이스
  - `model/service` : 비즈니스 로직 (데이터를 한 번 가공하는 곳)
  - `templates` : View, 위치는 그대로
- 요청이 흘러가는 순서 : `Controller → Service → Repository → JPA(Hibernate) → MySQL`

### 5. 테스트용 엔티티 · 리포지토리 · 서비스 · 컨트롤러 · 뷰 만들기

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
- `@Autowired` 덕분에 `TestRepository`를 직접 `new` 하지 않고 스프링 컨테이너가 넣어 준 걸 쓴다. (의존성 주입, DI)
  - 강한 결합 : 필요한 객체를 클래스가 직접 만든다 → 구현이 바뀌면 쓰는 쪽 코드도 같이 고쳐야 한다.
  - 약한 결합(DI) : 스프링이 만들어서 넣어 준다 → 구현체를 바꿔도 쓰는 쪽 코드는 그대로다.

**`JpaRepository`가 기본으로 주는 메서드들**

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
- 처음엔 `findByName("홍길동")`으로 한 명만 조회했다가, 나중에 `findAll()`로 전체 목록을 가져오도록 고쳤다.
```java
@GetMapping("/testdb")
public String getAllTestDBs(Model model) {
    List<TestDB> users = testService.findAll();
    model.addAttribute("users", users);
    return "testdb";
}
```

**`testdb.html` (새로 만듦)**
- `th:each`로 `users` 리스트를 돌면서 표로 출력한다.
```html
<table border="1">
    <tr th:each="user : ${users}">
        <td th:text="${user.id}"></td>
        <td th:text="${user.name}"></td>
    </tr>
</table>
```
- 서버를 다시 시작하면 엔티티를 보고 `testdb` 테이블이 자동으로 만들어진다. (`ddl-auto=update` 덕분)

### 6. 데이터 확인하기

- VS Code MySQL 확장의 SQL Template(INSERT)으로 `testdb` 테이블에 데이터를 직접 넣었다.
- `localhost:8080/testdb`에 들어가서 넣은 데이터가 화면에 나오는지 확인했다.

### 7. Git 연동

- `README.md`에 이번 주 진행 상황 기록 (예 : "4주차 데이터베이스 연동 완료")
- 바뀐 파일 add → commit → GitHub로 push
- 실습할 때마다 소스 코드를 계속 커밋해서 올려 두기

## ✏️ 실습 (4주차 연습문제 : 사용자 정보 수정 및 출력하기)

### 문제는 이랬다

1. `TestDB` 엔티티에 나이, 성별 컬럼 추가하기
2. VS Code MySQL 확장으로 각 사용자의 나이/성별을 DB에 직접 넣기
3. `testdb.html`을 고쳐서 아이디/이름/나이/성별을 모두 출력하기

### 이렇게 만들었다

**`TestDB.java` (컬럼 추가)**
```java
@Column(nullable = true)
private Integer age;

@Column(nullable = true)
private String gender;
```

**DB에 데이터 넣기**
- VS Code MySQL 확장에서 `testdb` 테이블에 `INSERT`로 사용자별 이름/나이/성별을 직접 입력했다.

**`testdb.html` (헤더 행 + 컬럼 추가)**
- `th:each`로 반복되는 행 위에 고정 헤더 행(`<th>`)을 넣어서 각 열이 뭔지 보이게 했다.
- `age`, `gender` 컬럼도 `<td>`로 추가했다.
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

### 확인하는 방법

1. 서버 실행 후 `http://localhost:8080/testdb` 접속
2. 아이디 / 이름 / 나이 / 성별 4개 컬럼이 헤더와 함께 사용자 수만큼 나오는지 확인

### 상태
- `TestDB.java`, `testdb.html`, DB 데이터 추가 : 실행해서 확인까지 끝! ✅

---

<a id="week5"></a>

# 🟨 5주차 — 로그인/로그아웃 구현과 비밀번호 암호화 (Spring Security)

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 이번 주 주제: 로그인/로그아웃 만들기, 비밀번호 암호화(BCrypt)

## 📖 오늘 배운 것

### 1. 웹 보안과 인증

- 해킹은 대부분 "로그인"에서 시작한다. OWASP Top 10(2025) 중에서도 **A01 접근 제어 실패**, **A07 인증 실패**가 이번 주제랑 바로 연결된다.
- **인증(Authentication)** : "너 누구야?" (로그인 = 아이디 + 비밀번호 확인)
- **인가(Authorization)** : "너 이거 해도 돼?" (권한 → 6주차에 이어서 한다)

### 2. 비밀번호는 어떻게 저장할까?

- 비밀번호를 평문으로 저장하면 안 된다. (개인정보 안전성 확보조치 기준) → 반드시 **일방향 해시**로 저장!
- **암호화 vs 해시**, 처음엔 같은 건 줄 알았는데 다르다.
  - 암호화 : 다시 풀 수(복호화) 있다. 키가 털리면 끝.
  - 해시 : 비밀번호 + 솔트(SALT)를 합쳐서 계산한다. 되돌릴 수 없다.
- **BCrypt** : 매번 무작위 솔트(22자)를 자동으로 만든다. 그래서 같은 비밀번호도 저장할 때마다 해시값이 달라진다. 일부러 느리게 만들어서 무차별 대입(brute-force) 공격도 막는다.
- MD5, SHA-1은 대입 공격에 약하다. 비밀번호 저장에는 BCrypt, Argon2 같은 걸 쓴다.

### 3. 세션 기반 로그인

- 이번 실습은 **세션(Session)** 방식이다. 서버가 로그인 상태를 기억하고, 브라우저는 세션 ID가 든 `JSESSIONID` 쿠키만 들고 있는다. (Thymeleaf/JSP 같은 전통적인 웹사이트 방식)
- 토큰(JWT), 소셜 로그인(OAuth2), 패스키는 뒤에서(7주차~) 다룬다고 한다.

### 4. Spring Security 의존성 추가

- `pom.xml`에 의존성 2개를 추가했다.
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
- 추가만 했는데 **메인, CSS까지 모든 페이지가 로그인을 요구**했다. 스프링 시큐리티는 기본이 "전부 잠금"이라서!
- 계정을 만들기 전에는, 서버를 켤 때 콘솔에 나오는 `Using generated security password: ...` 로 임시 로그인할 수 있다. (아이디는 `user`, 비밀번호는 재시작할 때마다 랜덤. 개발용이라 실제 서비스에서는 못 쓴다.)

### 5. 필터 체인과 SecurityConfig

- **필터(Filter) 기반 보안** : 요청이 `DispatcherServlet`(컨트롤러)에 닿기 전에 여러 보안 필터가 사슬(체인)처럼 이어져서 검사한다. (`CsrfFilter`, `UsernamePasswordAuthenticationFilter`, `LogoutFilter`, `AuthorizationFilter` 등) 통과 못 하면 로그인 화면(302)으로 보내거나 거부(403)한다.
- `config` 패키지를 새로 만들고 `SecurityConfig.java`를 썼다.
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
| `/css/**` 등 permitAll | 정적 리소스 허용 (빠지면 디자인이 깨진다) |
| `anyRequest().authenticated()` | 나머지는 전부 로그인 필요 (예: `/testdb`) |
| `loginPage("/login")` | 기본 로그인 화면 대신 직접 만든 로그인 화면 사용 |
| `defaultSuccessUrl("/")` | 로그인 성공하면 메인으로 (원래 가려던 페이지가 있으면 그쪽이 우선) |
| `logoutUrl("/logout")` | 로그아웃 처리 URL (CSRF 보호 때문에 POST만 된다. 주소창에 직접 치는 GET은 안 됨) |

- `authorizeHttpRequests`는 **위에서부터 순서대로** 검사한다. `anyRequest()`는 항상 맨 마지막!
- 스프링 시큐리티 7.x부터는 `.and()`로 잇는 옛날 방식이 없어져서 람다 DSL(`auth -> auth...`)로만 쓴다.

### 6. 회원 관련 계층 만들기

- `config` : 보안 설정 (`SecurityConfig`)
- `model/dto` : 화면 ↔ 컨트롤러가 주고받는 데이터 (`MemberForm`)
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
- `MemberRepository` : `findByUsername`, `existsByUsername` → 메서드 이름 규칙만 지키면 JPA가 SQL을 알아서 만들어 준다. (신기!)
- `MemberForm` (DTO) : `username`, `password`, `passwordConfirm`, `name`만 갖고 있다. 엔티티를 화면에 바로 안 쓰는 이유는 `role` 같은 민감한 값을 사용자가 몰래 조작해서 보내는 걸 막으려고.
- `MemberService`
  - `signup(MemberForm form)` : 아이디 중복 확인(`existsByUsername`) → `passwordEncoder.encode()`로 BCrypt 암호화 → `role = "USER"` 기본값으로 저장(`save`)
  - `implements UserDetailsService`, `loadUserByUsername(String username)` : 로그인할 때 스프링 시큐리티가 알아서 부르는 메서드. DB에서 회원을 찾아서 시큐리티용 `UserDetails`로 바꿔 돌려준다. 비밀번호 비교는 내가 짜는 게 아니라 시큐리티가 `PasswordEncoder.matches()`로 처리한다. (고정 시간 비교로 타이밍 공격을 막고, 계정이 있는지 없는지도 응답 시간으로 드러나지 않게 해 준다고 한다.)
- `MemberController` : `GET /login`(로그인 화면), `GET /signup`(가입 화면), `POST /signup`(가입 처리 — 아이디가 중복이면 에러 메시지와 함께 입력값을 유지한 채 가입 화면을 다시 보여 주고, 성공하면 로그인 화면으로 리다이렉트)

### 7. Thymeleaf + Spring Security 문법

- `<html>` 태그에 `xmlns:sec="http://www.thymeleaf.org/extras/spring-security"` 네임스페이스를 선언해야 한다.
- `sec:authorize="isAnonymous()"` / `"isAuthenticated()"` : 로그인 상태에 따라 그 태그를 보여 주거나 아예 지운다.
- `sec:authentication="name"` : 로그인한 사용자의 아이디(username)를 출력
  ```html
  <a sec:authorize="isAnonymous()" th:href="@{/login}">로그인</a>
  <div sec:authorize="isAuthenticated()">
      <span sec:authentication="name"></span>님
  </div>
  ```
- `index.html` 네비게이션(모바일/PC 둘 다)에 로그인 전(로그인/회원가입 버튼)과 로그인 후(아이디님 + 로그아웃)를 나눠 넣고, 회원 전용 페이지(`/testdb`)로 가는 "회원목록" 메뉴도 추가했다.

### 8. 로그인/로그아웃 직접 해 보기

- 로그인하면 세션에 인증 정보가 저장되고, 브라우저에는 `JSESSIONID` 쿠키만 생긴다. (F12 → Application → Cookies에서 확인)
- 로그인 안 한 채로 `/testdb`에 가면 로그인 화면으로 가고, 로그인에 성공하면 원래 가려던 페이지로 자동으로 돌아온다.
- 로그아웃은 CSRF 보호 때문에 주소창에 `/logout`을 직접 치면(GET) 안 되고, POST 폼으로만 된다.

### 9. Git 연동과 비밀번호 지키기

- `application.properties`에 DB 아이디/비밀번호를 그대로 두고 커밋하면 GitHub에 비밀번호가 그대로 올라간다. (!!)
- 그래서 계정 정보를 `application-secret.properties`라는 별도 파일로 뺐다.
  ```properties
  spring.datasource.username=root
  spring.datasource.password=비밀번호
  ```
- `application.properties`에는 아래 한 줄만 추가해서 분리한 설정을 불러온다. (`optional:` 이 붙어 있어서 그 파일이 없어도 앱은 일단 실행된다.)
  ```properties
  spring.config.import=optional:application-secret.properties
  ```
- `.gitignore`에 `application-secret.properties`를 넣어서 커밋/업로드 대상에서 완전히 뺐다.

> **(수업 밖에서 한 일)** 확인해 보니 4주차 커밋에 DB 비밀번호가 이미 평문으로 push되어 있었다. 히스토리를 다시 쓰는 대신 **DB 비밀번호 자체를 바꿔서** 무력화했다. (과거 커밋에 남은 값은 이제 쓸 수 없는 비밀번호)

### 상태

- `pom.xml`, `SecurityConfig.java`, `Member.java`, `MemberRepository.java`, `MemberForm.java`, `MemberService.java`, `MemberController.java`, `login.html`, `signup.html`, `index.html` 네비게이션 : 실행해서 확인 완료 ✅
- 실제로 회원가입해서 BCrypt 해시로 DB에 저장되는 것 확인, `application-secret.properties` 분리와 DB 비밀번호 변경도 끝!

## ✏️ 실습 (5주차 연습문제 : 로그인 기능 개선하기)

### 문제는 이랬다

1. 로그인 상태 유지(`remember-me`) : `SecurityConfig`의 로그아웃 설정 뒤에 `.rememberMe()`를 추가하기 (`key("비밀키")`, `tokenValiditySeconds(7일)`). 브라우저를 껐다 켜도 로그인이 유지되어야 한다.
2. 비밀번호 확인 검증 : `signup()`에서 `password`와 `passwordConfirm`을 비교해서, 다르면 "비밀번호가 일치하지 않습니다."를 보여 주고 DB에는 저장하지 않기

### 이렇게 만들었다

**`SecurityConfig.java` (remember-me 추가)**
```java
.logout(logout -> logout
    ...
    .deleteCookies("JSESSIONID", "remember-me"))
.rememberMe(remember -> remember // 4. 로그인 상태 유지
    .key("portfolio-remember-me-key") // 쿠키 위변조 방지용 비밀키
    .tokenValiditySeconds(60 * 60 * 24 * 7)); // 유지 기간 : 7일
```
- `login.html`의 체크박스(`name="remember-me"`)를 체크하고 로그인하면 `remember-me` 쿠키가 7일짜리로 발급된다.
- 세션이 사라져도 이 쿠키로 `MemberService`(`UserDetailsService`)가 사용자를 다시 찾아서 로그인 상태를 되살려 준다.
- 로그아웃 설정에 `deleteCookies("JSESSIONID", "remember-me")`가 이미 있어서, 로그아웃하면 `remember-me` 쿠키도 같이 지워진다.

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
- 비교를 `save()` **앞에서** 하니까 달라도 DB에는 저장되지 않는다.
- `MemberController.signup()`에 이미 있던 `catch (IllegalArgumentException e)`가 메시지를 `${error}`로 넘겨 주기 때문에, `signup.html`에 빨간 경고창이 자동으로 뜬다. (컨트롤러·화면은 안 고쳐도 됐다!)

### 확인하는 방법

1. 서버 실행 후 `http://localhost:8080/signup` 접속
2. 비밀번호와 비밀번호 확인을 다르게 입력해서 가입 → "비밀번호가 일치하지 않습니다." 표시, 그 아이디로 로그인 안 됨(DB에 안 들어감)
3. 같게 입력해서 가입 → 로그인 화면으로 이동
4. "로그인 상태 유지" 체크 후 로그인 → F12 → Application → Cookies에서 `remember-me` 쿠키 확인 (Expires가 약 7일 뒤)
5. `JSESSIONID`를 지우거나 브라우저를 껐다 켜도 로그인이 유지됨(`/testdb` 접근 가능). 체크 안 하면 `remember-me` 쿠키가 안 생긴다.

### 결과

| 시나리오 | 결과 |
|---|---|
| 비밀번호 불일치 가입 | 가입 화면이 다시 뜨고 "비밀번호가 일치하지 않습니다." 출력 |
| 불일치로 가입한 아이디로 로그인 | `/login?error` (DB에 저장되지 않았다는 뜻) |
| 비밀번호 일치 가입 | `/login?signup` 으로 이동 (정상 가입) |
| 상태 유지 체크하고 로그인 | `remember-me` 쿠키 발급, 유효기간 604,800초(7일) |
| `remember-me` 쿠키만으로 재접속 (세션 쿠키 없음) | 로그아웃 버튼 표시, `/testdb` 200 (로그인 유지) |
| 상태 유지 체크 안 하고 로그인 | `remember-me` 쿠키 없음 |

### 🔧 하다가 막혔던 것

- 다른 PC에서 DB 비밀번호를 바꿔 놔서, 이 PC의 `application-secret.properties`와 안 맞아 `Access denied for user 'root'` 오류가 났다. → 이 PC의 DB 비밀번호에 맞게 파일을 고쳤다. (`.gitignore` 대상이라 커밋되지 않는다.)
- 이 PC의 DB에는 `member` 테이블이 없었는데, 서버를 켜자 `ddl-auto=update` 덕분에 자동으로 만들어졌다.
- `mvnw spring-boot:run`으로 실행했더니 DevTools 재시작 클래스로더 때문에 `Found 0 JPA repository interfaces`가 뜨면서 `TestRepository` 빈을 못 찾았다. `-Dspring.devtools.restart.enabled=false`를 붙여 실행하면 정상이다. (VS Code에서 실행할 때는 상관없었다.)

### 상태

- `SecurityConfig.java`(rememberMe), `MemberService.java`(비밀번호 확인 검증) : 실행해서 확인 완료 ✅
- 5주차 연습문제 2개 모두 서버를 켜고 직접 확인했다.

---

<a id="week6"></a>

# 🟥 6주차 — 권한(ROLE) 접근 제어와 관리자 페이지 분리

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 이번 주 주제: 권한(ROLE)으로 접근 제어하기, 관리자 페이지 분리 (인가 · Authorization)

## 📖 오늘 배운 것

### 1. 접근 제어 실패 (OWASP A01)

- OWASP Top 10(2025)의 **A01 접근 제어 실패(Broken Access Control)** 대표 사례는 이런 것들이다.
  - 관리자 페이지 주소가 노출됨 : `/admin` 을 직접 치면 그냥 열린다.
  - 번호만 바꿔서 남의 정보를 본다 (**IDOR**) : `/order/1001` → `/order/1002`
  - 요청을 조작해서 관리자 권한을 얻는다 (**권한 상승**) : USER → ADMIN
- 공통 원인은 로그인(인증)은 했는데 **권한 검사(인가)** 를 빠뜨린 것. "메뉴에 안 보이니까 괜찮겠지" 하는 생각이 제일 위험하다.
- **화면에서 숨기는 건 보안이 아니다.** 로그인했더라도 **요청마다 권한을 검사**해야 한다.

### 2. 권한 관리 모델

| 모델 | 방식 | 예 |
|---|---|---|
| **RBAC** (Role-Based, 이번 실습) | 사람 → **역할(Role)** → 권한 | USER / MANAGER / ADMIN |
| ABAC (Attribute-Based) | 부서·시간·위치 같은 **속성 조건** | 근무 시간에만 접근 허용 |

- 사람마다 권한을 하나씩 주는 대신 역할을 부여하니까 관리가 쉽고 실수도 적다.
- **최소 권한 원칙(Least Privilege)** : 꼭 필요한 권한만 준다. 계정이 해킹당해도 피해가 작다. (높은 역할일수록 사람 수는 적게)
- 관리자 화면은 **URL부터 분리**하고(`/admin/**`), 위험한 기능(권한 변경·삭제)은 **관리자만** 할 수 있게 한다.

### 3. ROLE 과 권한(Authority)

- 5주차 `loadUserByUsername()`의 `.roles(member.getRole())` 에서, DB 값 `"USER"` 가 세션 권한 `ROLE_USER` 로 바뀐다.
- 스프링 시큐리티는 비교할 때 **`ROLE_` 을 자동으로 붙인다.** (그래서 헷갈린다…)

| 표현 | 결과 |
|---|---|
| `hasRole("ADMIN")` | ✅ `ROLE_ADMIN` 을 가졌는지 검사 |
| `hasAuthority("ROLE_ADMIN")` | ✅ 권한 문자열을 그대로 비교 (위와 같은 결과) |
| `hasRole("ROLE_ADMIN")` | ❌ 서버 시작 오류 (`should not start with ROLE_`) |

### 4. 로그인한 사용자 정보 꺼내기

| 위치 | 방법 |
|---|---|
| 컨트롤러 | 파라미터에 `Principal principal` → `principal.getName()` (아이디) |
| 어디서나 | `SecurityContextHolder.getContext().getAuthentication()` |
| 화면 | `sec:authentication="name"` / 권한 목록은 `sec:authentication="principal.authorities"` |
| 표현식 안 | `${#authentication.name}` |

- **DB 권한 ≠ 세션 권한** 일 수 있다. 권한은 **로그인하는 순간** 세션에 복사되기 때문!
  - DB의 role을 바꿔도 **다시 로그인해야** 반영된다. (내 정보 화면에서 DB는 `ADMIN`인데 세션은 `[ROLE_USER]`로 나왔다.)

### 5. 이번 주에 바뀐 구조

- 작성한 자바 파일 (4개)
  - `AdminController` : 관리자 전용 컨트롤러 (새로 만듦)
  - `SecurityConfig` : URL 권한 규칙 추가, 메서드 보안 켜기
  - `MemberController` : `/mypage` 추가
  - `MemberService` : 조회 · 권한 변경 · 삭제 추가
- 엔티티·리포지토리·DTO는 그대로 두었다. (5주차에 만든 `Member.role` 컬럼을 이번에 쓴다. `JpaRepository` 기본 메서드 `findAll · findById · save · delete`를 활용)
- 제공받은 화면 : `templates/admin/members.html`, `templates/error/403.html`, `templates/mypage.html` (새로 추가), `index.html` (수정)

### 6. 프론트 코드 적용 (index.html 교체부분 ①~③)

- 내가 꾸민 `index.html`은 통째로 덮어쓰지 않고 **교체부분 ①~③만 반영**했다. (인사말·프로필·카드 링크 같은 내 커스터마이징을 지키려고)
  - ① `<style>` : 권한 배지(`.role-badge`, `.role-ADMIN/MANAGER/USER`), 빨간 관리자 메뉴, 메뉴가 한 줄로 유지되는 스타일
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
- `Principal` : 파라미터에 적어 두기만 하면 스프링이 로그인한 사용자를 알아서 넣어 준다.
- `/mypage?id=3` 처럼 id를 URL로 받지 않고 **로그인 아이디로 조회**한다. → 번호를 바꿔서 남의 정보를 보는 IDOR이 막힌다.

### 8. 관리자 계정 만들기

1. 회원가입 화면에서 `admin / 1234 / 관리자` 로 가입 (비밀번호는 BCrypt로 저장됨)
2. 제공받은 SQL(`관리자_권한_부여.sql`)로 role만 바꾼다.
   ```sql
   SELECT id, username, name, role FROM member;
   UPDATE member SET role = 'ADMIN' WHERE username = 'admin';
   ```
3. admin으로 로그인 → 빨간 관리자 메뉴 + ADMIN 배지 확인 (로그인해 있었다면 로그아웃했다가 다시 로그인!)

- SQL `INSERT`로 계정을 만들면 안 되는 이유 : 평문 `1234`는 BCrypt 형식이 아니라서 **로그인이 안 된다.** → 정상적으로 가입한 뒤 role만 `UPDATE`한다.

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
- `ROLES` 화이트리스트 덕분에, 개발자도구로 select 값을 `SUPER` 같은 걸로 바꿔 보내도 거부된다.
- `save()` : id가 없으면 INSERT, 있으면 UPDATE

**`AdminController.java` (새로 만듦)**
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
| `addFlashAttribute` | redirect한 뒤 화면에 **딱 한 번만** 표시 |

- **PRG 패턴(Post-Redirect-Get)** : POST를 처리한 뒤 redirect하면 새로고침해도 다시 전송되지 않는다.

### 10. 보안 구멍 발견! → 접근 제어 3단계

- 이 시점에는 USER로 로그인해도 주소창에 `/admin/members`를 직접 치면 **회원 관리 화면이 열렸고**, 자기 권한을 ADMIN으로 바꿀 수도 있었다. → A01 **권한 상승**
- 원인은 화면(메뉴 숨김)만 처리했기 때문.

| 단계 | 위치 | 코드 | 역할 |
|---|---|---|---|
| ① 화면 | Thymeleaf | `sec:authorize="hasRole('ADMIN')"` | 보이기/숨기기만 (보안 아님) |
| ② URL | SecurityConfig | `.requestMatchers("/admin/**").hasRole("ADMIN")` | **1차 잠금** : 컨트롤러에 도착하기 전에 차단 |
| ③ 메서드 | Service | `@PreAuthorize("hasRole('ADMIN')")` | **2차 잠금** : 메서드가 실행되기 직전에 차단 |

- ②가 있는데 ③도 필요한 이유는, URL 규칙을 실수로 빠뜨리거나 틀렸을 때를 대비하려고. → **이중 잠금**

| 구분 | `hasRole()` | `@PreAuthorize` |
|---|---|---|
| 정체 | 권한 검사 표현식(SpEL) | 메서드 보안 어노테이션 |
| 쓰는 위치 | 설정 클래스, 어노테이션, 뷰 | 컨트롤러·서비스 메서드 위 |
| 보호 대상 | URL 또는 표현식이 쓰인 대상 | 메서드(비즈니스 로직) |
| 필요 설정 | 기본 제공 | `@EnableMethodSecurity` |

### 11. URL 권한 규칙 추가 (1차 잠금)

- 기존 `SecurityConfig`의 `authorizeHttpRequests` 블록 **안**에, 정적 리소스 줄 아래로 한 줄만 추가했다.
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/", "/hello", "/detailed_web.html",
            "/login", "/signup", "/error").permitAll()
    .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
    .requestMatchers("/admin/**").hasRole("ADMIN") // [6주차] 관리자만
    .anyRequest().authenticated()) // 나머지 : 로그인 필요
```
- 규칙은 **위에서부터 순서대로** 검사한다. → 구체적인 규칙을 위에, `anyRequest()`는 항상 맨 마지막에.
- `/admin/**` 는 하위 경로 전부(GET·POST 모두)를 뜻한다.

**403 접근 거부 테스트** (`/admin/members` 접속)

| 상태 | 결과 | 의미 |
|---|---|---|
| 비로그인 | 302 → 로그인 화면 | 인증이 필요하다 |
| USER | 403 접근 거부 화면 | 인가 실패 |
| ADMIN | 200 회원 관리 | 통과 |

- 권한 검사는 `AuthorizationFilter`가 **컨트롤러보다 먼저** 한다.
- 403 화면은 컨트롤러 없이 `templates/error/403.html`에 두기만 하면 스프링 부트가 알아서 연결해 준다. (404, 500도 같은 방식)

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
- `@EnableMethodSecurity`가 없으면 `@PreAuthorize`는 **그냥 무시된다.** (에러도 안 난다!)
- 조회(`findAll`)는 그대로 두고 **데이터를 바꾸는 메서드만** 잠갔다. 검사에 실패하면 같은 403 화면이 나온다.

**이중 잠금 실험** (URL 규칙을 잠깐 주석 처리하고 USER로 공격해 봤다)

| 상태 | 목록 GET | 변경·삭제 | 데이터 |
|---|---|---|---|
| 잠금 없음 | 200 | 성공 | 변경됨 |
| URL 규칙만 | 403 | 403 | 보호 |
| `@PreAuthorize` 만 | 200 | 403 | 보호 |
| 둘 다 (최종) | 403 | 403 | 보호 |

- 메서드 수준에서도 막아야 하는 경우 : URL 패턴 오타(`/admin/*`), 규칙 순서 실수, 새 URL(`/api/...`)에서 같은 기능을 호출, 스케줄러·배치처럼 URL을 안 거치는 호출

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

- 화면·URL·메서드 세 곳이 **같은 표현식**을 쓴다. 다만 SecurityConfig는 메서드 형태(`.hasRole("ADMIN")`), 화면과 `@PreAuthorize`는 문자열 형태로 쓴다.

### 14. 자주 나는 오류

| 증상 | 원인 → 해결 |
|---|---|
| `ROLE_ADMIN should not start with ROLE_` | `hasRole("ROLE_ADMIN")` → `hasRole("ADMIN")` |
| `Can't configure requestMatchers after anyRequest` | 규칙 순서 문제 → `/admin/**` 를 위로 |
| USER도 `@PreAuthorize` 메서드가 실행됨 | `@EnableMethodSecurity` 빠뜨림 |
| SQL로 ADMIN을 줬는데 메뉴가 안 보임 | 다시 로그인 (세션 권한) |
| 403일 때 하얀 Whitelabel 화면 | `templates/error/403.html` 위치 확인 |
| `/admin/members` 500 오류 | `return "admin/members"` (폴더 포함) |
| 권한 변경할 때 400 | 폼 `name="role"` ↔ `@RequestParam String role` 이름 일치 |

### 15. Git 연동

- `README.md`에 이번 주 진행 상황 기록 (예 : "6주차 권한(ROLE) 접근 제어, 관리자 페이지 분리 완료")
- 바뀐 파일 add → commit → GitHub로 push
- 관리자 계정(`admin/1234`)은 실습용이다. 실제 서비스라면 강한 비밀번호를 쓰고 따로 관리해야 한다.

### 🔧 하다가 막혔던 것

- 슬라이드 코드를 **통째로 붙여 넣었다가** 오류가 났다. "기존 ○○에 추가"라고 되어 있으면 새 줄만 기존 코드 안에 끼워 넣어야 한다.
  - `SecurityConfig` : `authorizeHttpRequests` 블록 전체를 `;` 뒤에 또 붙여서 문법 오류 → 기존 블록 안에 `/admin/**` 한 줄만 추가
  - `MemberService` : `{ ... }`(생략 표시)까지 붙여 넣어서 메서드가 중복 정의됨 → 기존 메서드 위에 `@PreAuthorize`만 추가
- import 누락, 잘못된 import : `MemberController`에 `java.lang.reflect.Member`가 선택되어 있었다 → `com.example.demo.model.domain.Member`로 수정. `MemberService`에는 `List`·`Sort` import가 빠져 있었다.
- 파일 이름 대소문자 : `adminController.java` 안에 `public class AdminController`가 있었다 → 파일명을 `AdminController.java`로 변경 (public 클래스 이름 = 파일 이름)
- 편집하고 저장을 안 해서 디스크의 파일이 비어 있던 일, 제공받은 템플릿 중 `mypage.html`을 복사 안 한 일
- 이 프로젝트의 일반 USER 계정은 슬라이드의 student1 대신 `saevit`, `saevit2`를 썼다.

### 상태

- `SecurityConfig.java`(URL 권한 규칙, `@EnableMethodSecurity`), `AdminController.java`(새로 만듦), `MemberController.java`(`/mypage`), `MemberService.java`(조회·권한 변경·삭제, `@PreAuthorize`) : 실행해서 확인 완료 ✅
- `admin/members.html`, `error/403.html`, `mypage.html` 추가, `index.html` 교체부분 ①~③ 반영 완료
- `admin` 계정을 만들고 role을 `ADMIN`으로 바꿨다.
- 서버를 켜고 `/mypage`, `/admin/members` 접근(302/403/200), 권한 변경·삭제까지 확인했다.

## ✏️ 실습 (6주차 연습문제 : 관리자 보호 & 매니저(MANAGER) 권한 추가하기)

### 문제는 이랬다

1. 관리자 본인 보호 : 관리자가 자기 자신을 USER로 바꾸거나 삭제하면 관리자가 0명이 될 수 있다. → `changeRole`, `deleteMember`에서 대상이 본인이면 거부하기 (힌트 : `SecurityContextHolder ... getName()`)
2. 매니저(MANAGER) 권한 추가 : 회원 관리에서 MANAGER를 줄 수 있게 하고, `/testdb`는 ADMIN, MANAGER만 접근하게 하기 (USER는 403, 힌트 : `hasAnyRole(...)`)
3. (추가 도전) 회원목록 메뉴도 USER에게는 숨기기

### 이렇게 만들었다

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
- `SecurityContextHolder` : 컨트롤러가 아닌 서비스에서도 현재 로그인한 사용자를 꺼낼 수 있다. (`Principal` 파라미터 대신 쓴다.)
- 권한 변경·삭제 두 메서드가 같은 검사를 하니까 `checkNotSelf()`로 따로 뺐다.
- 예외 메시지는 기존 `AdminController`의 `catch (IllegalArgumentException e)`가 flash 메시지(`error`)로 넘겨서 회원 관리 화면에 빨간 경고로 나온다. (컨트롤러·화면은 안 고쳤다.)

**`MemberService.java` (② 허용 권한에 MANAGER 추가)**
```java
private static final List<String> ROLES = List.of("USER", "MANAGER", "ADMIN"); // [연습문제 ②] MANAGER 추가
```
- 회원 관리 화면(`admin/members.html`)의 select에는 MANAGER가 이미 들어 있어서, 화이트리스트만 고치면 됐다.

**`SecurityConfig.java` (② `/testdb` 접근 제한)**
```java
.requestMatchers("/admin/**").hasRole("ADMIN") // [6주차] 관리자만
.requestMatchers("/testdb").hasAnyRole("ADMIN", "MANAGER") // [연습문제 ②] 회원목록 : 관리자, 매니저만
.anyRequest().authenticated()) // 나머지 : 로그인 필요
```
- `hasAnyRole(...)` : 나열한 역할 중 하나라도 있으면 통과한다. (`ROLE_`은 자동으로 붙는다.)
- `anyRequest()`보다 반드시 **위에** 써야 한다.

**`index.html` (추가 도전 : 회원목록 메뉴 숨기기)**
```html
<!-- [5주차] 회원 전용 페이지 / [연습문제 추가 도전] 관리자, 매니저에게만 메뉴 표시 -->
<li class="nav-item" sec:authorize="hasAnyRole('ADMIN', 'MANAGER')">
    <a class="nav-link" th:href="@{/testdb}">회원목록</a>
</li>
```
- 메뉴를 숨기는 건 편의일 뿐이고, 진짜 차단은 `SecurityConfig`의 URL 규칙이 한다.

### 확인하는 방법

1. 서버 실행 후 `admin`으로 로그인 → 관리자 → 회원 관리
2. 본인(admin) 행에서 권한을 USER로 바꾸거나 삭제해 본다 → "자기 자신의 권한 변경·삭제는 할 수 없습니다." 가 뜨고, DB의 role은 ADMIN 그대로
3. 다른 회원의 권한을 MANAGER로 변경 → "권한을 MANAGER (으)로 변경했습니다." 표시
4. 그 회원으로 **다시 로그인** → MANAGER 배지, 회원목록 메뉴 표시, `/testdb` 접근 가능, `/admin/members`는 403
5. USER 계정으로 로그인 → 회원목록 메뉴가 숨겨지고, `/testdb`를 주소창에 직접 치면 403

### 결과

| 시나리오 | 결과 |
|---|---|
| admin이 자기 권한을 USER로 변경 | "자기 자신의 권한 변경·삭제는 할 수 없습니다." 출력, DB는 ADMIN 유지 |
| admin이 자기 자신을 삭제 | 같은 메시지로 거부 |
| admin이 다른 회원에게 MANAGER 부여 | "권한을 MANAGER (으)로 변경했습니다." 출력, DB 반영 |
| DB는 MANAGER인데 재로그인 전 세션 | `/testdb` 403 (세션 권한은 아직 `ROLE_USER`) |
| MANAGER가 권한 변경 요청(POST)을 직접 전송 | 403 |

| 주소 | USER | MANAGER | ADMIN | 비로그인 |
|---|---|---|---|---|
| `/mypage` | 200 | 200 | 200 | 302 → 로그인 |
| `/testdb` (회원목록) | 403 | 200 | 200 | 302 → 로그인 |
| `/admin/members` (회원 관리) | 403 | 403 | 200 | 302 → 로그인 |
| 회원목록 메뉴 | 숨김 | 표시 | 표시 | 숨김 |

### 🔧 하다가 막혔던 것

- 테스트용 임시 계정을 만들어서 USER/MANAGER 시나리오를 확인하고, 끝난 뒤 관리자 회원 삭제 기능으로 다시 지웠다. (기존 계정의 권한은 건드리지 않았다.)

### 상태

- `MemberService.java`(본인 보호 `checkNotSelf`, 허용 권한 MANAGER 추가), `SecurityConfig.java`(`/testdb` `hasAnyRole`), `index.html`(회원목록 메뉴 숨김) : 실행해서 확인 완료 ✅
- 6주차 연습문제 2개 + 추가 도전까지 모두 서버를 켜고 확인했다.

---

<a id="week7"></a>

# 🟪 7주차 — 회원 관리 REST API(JSON)와 Postman 테스트

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 이번 주 주제: 6주차에 만든 회원 관리를 REST API(JSON)로 바꿔 보고, Postman으로 테스트하기

## 📖 오늘 배운 것

### 1. API가 뭐지?

- **API** : 프로그램끼리 데이터(JSON)를 주고받기로 한 약속. 요청(Request)을 보내면 응답(Response)이 돌아온다.
- 서버 1 : N 구조 — 웹, 모바일 앱, 스마트워치, AI 챗봇이 **같은 서버**를 쓴다. 그래서 서버는 화면이 아니라 **데이터만** 응답한다.
  - 참고로 6주차까지 만든 페이지는 완성된 화면(HTML)을 응답하는 방식이었다.
- 오픈 API 예 : 지도·길찾기, 간편결제, 소셜 로그인, 날씨, 공공데이터포털(버스 도착, 미세먼지)

### 2. SSR 과 CSR

| 구분 | SSR (6주차까지 우리 방식) | CSR (요즘 많이 쓰는 방식) |
|---|---|---|
| 서버가 보내는 것 | Thymeleaf로 **완성한 HTML** | **JSON 데이터만** (REST API) |
| 화면을 그리는 쪽 | 서버 | 브라우저·앱 (React, Vue 등) |

- 실무에서는 둘을 섞어 쓴다고 한다. 관리자 화면은 Thymeleaf, 앱·외부 연동은 API. → 오늘은 회원 관리를 API로 만들어 본다.
- React는 Next.js, React Native(앱까지)가 있고 생태계가 제일 크다. Vue는 Nuxt가 있고 빠른 개발·중소 규모·기존 페이지에 일부만 도입할 때 좋다.

### 3. REST 와 JSON

- **REST** : API 설계 방식. 핵심은 **주소(URL)는 "무엇을", HTTP 메서드는 "어떻게"**
  - GET 조회, POST 생성, PATCH 수정(일부), DELETE 삭제 (PUT은 전체 교체)
- **JSON** : 요청/응답에 쓰는 표준 데이터 형식. 사람이 읽기도 쉽고 거의 모든 언어가 지원한다.
  - `{ }` 는 객체("이름": 값 묶음), `[ ]` 는 배열(여러 개 나열), 값은 `"USER"`, `3`, `true`, `null`

| 구분 | 기존 페이지 (6주차까지) | API 통신 (7주차) |
|---|---|---|
| 주소 예 | `/admin/members` | `/api/admin/members` |
| 요청 방식 | 링크 클릭, 폼 제출 (GET·POST) | fetch, 앱 요청 (GET·POST·PATCH·DELETE) |
| 결과 확인 | 바뀐 화면을 눈으로 확인 | 상태 코드(200, 201, 404 …)로 판단 |
| 페이지 이동 | 매번 새로고침 | 새로고침 없음, 필요한 부분만 바뀜 |

### 4. `@Controller` vs `@RestController`

- `@Controller` : **화면 이름**을 반환 → HTML 화면 (받는 쪽은 사람/브라우저)
- `@RestController` : **객체**를 반환 → 스프링이 **JSON으로 자동 변환** (받는 쪽은 앱·JS·Postman)
- `@RestController` = `@Controller` + `@ResponseBody`

→ 처음엔 둘이 뭐가 다른지 헷갈렸는데, **반환값이 "화면 이름"이냐 "데이터"냐**의 차이였다.

### 5. 첫 번째 API - `/api/hello` (단계 1)

**`MemberApiController.java` (새로 만듦)**
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
- `SecurityConfig`의 `authorizeHttpRequests` 블록 안에 `.requestMatchers("/api/hello").permitAll()` 한 줄을 추가했다.
- 결과 : `{"message":"안녕하세요, REST API!","time":"..."}` (크롬에서 Pretty-print를 체크하면 예쁘게 정렬된다.)

### 6. 내 정보 API - `/api/me` 와 응답 DTO (단계 2·3)

- 엔티티(`Member`)를 그대로 반환했더니 → **비밀번호 해시(`$2a$10$...`)까지 JSON에 나왔다.** 😱
  - 반환하는 객체의 **getter가 있는 값은 전부** JSON이 되기 때문이다.
  - 해시도 유출되면 공격에 쓰일 수 있다. (`1234` 같은 약한 비밀번호는 사전 목록·대입으로 금방 찾아낸다.) 만약 목록 API였다면 전원 유출!
- 해결 : 응답용 객체를 따로 만든다 → **응답 DTO** (필요한 값만 : id, 아이디, 이름, 권한)

**`MemberResponse.java` (새로 만듦, `model/dto`)**
```java
// API 응답용 DTO : 화면(JSON)에 내보낼 값만 담는다 → password 는 빠진다
public record MemberResponse(Long id, String username, String name, String role) {
    public static MemberResponse from(Member member) { // 엔티티 → 응답 DTO 변환
        return new MemberResponse(member.getId(), member.getUsername(),
                member.getName(), member.getRole());
    }
}
```
- `record` (자바 16+) : 생성자·getter·equals 등을 자동으로 만들어 주는 간단한 데이터 클래스

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
| 막는 공격 | — | 권한 조작 (role을 ADMIN으로 몰래 보내기) | **정보 노출 (비밀번호 해시 유출)** |

- 6주차에는 Thymeleaf가 서버 안에서 화면에 쓴 값만 출력해서 나가는 DTO가 필요 없었다. 그런데 JSON은 값이 전부 보이니까 필요해진다.

### 7. Postman 과 Basic 인증 (단계 4)

- **Postman** : API를 직접 호출해 보는 프로그램. 외부 앱처럼 요청을 보낼 수 있고, 무료에 가입 없이 쓸 수 있다.
  - 메서드 + 주소를 넣고 Send, Authorization · Body 설정, 응답 상태 코드·헤더·본문 확인
  - Postman이 없다면 브라우저 F12 → 네트워크(Network) 탭이나 curl로도 가능
- 문제 발생 : Postman으로 `/api/me`를 호출했더니 JSON 대신 **로그인 화면 HTML**이 왔다. (실제로는 302 → `/login`)
  - 브라우저는 로그인한 뒤 **세션 쿠키(`JSESSIONID`)** 를 항상 같이 보내지만, Postman은 쿠키가 없어서 "로그인 안 한 사람"이 된다.
  - 서버는 브라우저인지 앱인지 구분하지 않는다는 점이 포인트.

| 구분 | 브라우저 (화면) | 앱 · Postman (API) |
|---|---|---|
| 인증 방법 | 로그인 한 번 → 세션 쿠키로 계속 인증 | **요청할 때마다** 인증 정보 전송 |
| 로그인 안 했으면 | 302 → 로그인 화면 | **401 Unauthorized** |

- **Basic 인증** : `아이디:비밀번호`를 **Base64**로 바꿔서 `Authorization: Basic ...` 헤더로 보낸다. (뒤에서는 JWT 토큰 방식)
  - Base64는 데이터를 64가지 ASCII 문자로 표현하는 **인코딩**일 뿐(전송 오류 방지) **암호화가 아니다!** → 실서비스는 HTTPS가 필수
- 401은 "누구세요?"(인증 실패), 403은 "권한이 없어요"(인가 실패). 302와 401을 나누는 기준은 주소(`/api/`)다.

**`SecurityConfig.java` (`.rememberMe(...)` 뒤에 이어서 추가, 앞 줄 끝의 `;` 를 지우고 작성)**
```java
// 5. [7주차] REST API 설정
.httpBasic(Customizer.withDefaults()) // Postman : 요청마다 아이디·비밀번호 (Basic 인증)
.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // JSON API 는 CSRF 토큰 검사 제외
.exceptionHandling(ex -> ex.defaultAuthenticationEntryPointFor( // 로그인 안 한 API 요청 :
        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), // 로그인 화면(302) 대신 401
        request -> request.getRequestURI().startsWith("/api/")));
```
- 결과 : No Auth → 401(본문 없음), Basic `admin / 1234` → 200 + 내 정보, 비밀번호가 틀리면 401
- 브라우저(로그인 상태)는 그대로 200이고, 일반 화면 페이지는 여전히 로그인 화면으로 이동한다.

### 8. REST 주소 규칙과 상태 코드

- 6주차 화면은 GET·POST만 썼다. 7주차는 **주소에는 대상만, 할 일은 HTTP 메서드로** 구분한다. (같은 주소라도 메서드가 다르면 다른 일!)

| 하고 싶은 일 | 6주차 화면 방식 | 7주차 REST 방식 |
|---|---|---|
| 회원 목록 보기 | `GET /admin/members` | `GET /api/admin/members` |
| 회원 1명 보기 | (없음, 목록에서 봄) | `GET /api/admin/members/3` |
| 회원 등록 | `POST /signup` | `POST /api/admin/members` |
| 권한 변경 | `POST /admin/members/3/role` | `PATCH /api/admin/members/3/role` |
| 회원 삭제 | `POST /admin/members/3/delete` | `DELETE /api/admin/members/3` |

- API 응답은 **상태 코드 + 헤더 + 본문**이다. 화면을 눈으로 보는 게 아니라 숫자로 결과를 판단한다. (200번대는 성공, 400번대는 요청 쪽 문제)

| 코드 | 의미 | 언제 |
|---|---|---|
| 200 OK | 성공 | 조회, 수정 성공 |
| 201 Created | 새로 만들었음 | 등록 성공 |
| 204 No Content | 성공, 돌려줄 내용 없음 | 삭제 성공 |
| 400 Bad Request | 요청 내용이 잘못됨 | 빈 값, 중복 아이디, 허용 안 되는 권한 |
| 401 Unauthorized | 누구인지 모름 | 로그인 정보 없음 |
| 403 Forbidden | 권한 없음 | USER가 관리자 API 호출 |
| 404 Not Found | 대상이 없음 | 없는 회원 번호 조회 |

- **`ResponseEntity`** : 상태 코드를 내가 직접 정해서 응답할 때 쓴다.
```java
ResponseEntity.ok(body)                  // 200
ResponseEntity.created(uri).body(body)   // 201
ResponseEntity.noContent().build()       // 204
ResponseEntity.badRequest().body(error)  // 400
ResponseEntity.notFound().build()        // 404
```

### 9. 관리자 API - 조회 (단계 5)

**`AdminApiController.java` (새로 만듦)**
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
- `stream().map(MemberResponse::from).toList()` : 엔티티 목록을 하나씩 DTO로 바꿔서 새 목록을 만든다.
- `MemberService`에 `Optional<Member> findById(Long id)`를 추가했다. (없을 수도 있는 값이라 `Optional`)
- `SecurityConfig`의 관리자 규칙도 고쳤다. → **새 주소를 만들면 접근 규칙도 바로 같이 추가!** (6주차 교훈)
  ```java
  .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
  ```
- 결과 : admin은 목록 200(배열, 비밀번호 없음), `/9999`는 404, USER는 403. (이 규칙이 없으면 USER도 전체 회원 목록이 200으로 보인다…)

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
- `@RequestBody` : 요청 본문의 JSON을 자바 객체로 바꿔 준다. 5주차 폼 데이터는 스프링이 알아서 채워 줬지만, JSON은 `@RequestBody`가 꼭 필요하다.
- `ResponseEntity<?>` : 성공하면 `MemberResponse`, 실패하면 `Map`이 본문이라서 제네릭 와일드카드(모든 타입 가능)를 썼다.

**`MemberService.java` (`signup()` 첫 줄에 빈 값 검사)** — JSON 요청에는 HTML의 `required` 같은 검사가 없으니까
```java
public Member signup(MemberForm form) {
    if (isBlank(form.getUsername()) || isBlank(form.getPassword()) || isBlank(form.getName())) {
        throw new IllegalArgumentException("아이디, 비밀번호, 이름은 필수입니다.");
    }
    ... // 기존 중복 아이디 · 비밀번호 확인 검사
}

private boolean isBlank(String value) { return value == null || value.isBlank(); }
```
- 결과 : 첫 등록은 201 + `Location: /api/admin/members/번호`, 같은 아이디는 400, `{"username":"x"}`도 400(필수 값). `"role":"ADMIN"`을 몰래 넣어 봐도 **USER**로 저장된다. (`MemberForm`에 role이 없어서!) DB에는 `$2a$10$...`로 저장된다.

### 11. 관리자 API - 권한 변경 · 삭제 (단계 7)

**`RoleRequest.java` (새로 만듦, `model/dto`)**
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
- 보안 검사는 6주차에 서비스에 이미 넣어 뒀다. → **화면이든 API든 같은 서비스를 부르니까 같은 규칙이 그대로 적용된다.** (이게 제일 신기했다)
  - `@PreAuthorize` (관리자만) → USER가 호출하면 403
  - 허용 권한 검사 → `"SUPER"`는 400
  - 본인 보호 → admin이 자기 자신을 변경·삭제하면 400

| 주차 | DTO 핵심 |
|---|---|
| 5주차 (`MemberForm`) | 들어오는 값을 DTO로 분리 → 권한 조작 방지 |
| 6주차 (없음) | Thymeleaf가 서버 안에서 화면에 쓴 값만 출력 → 나가는 DTO 불필요 |
| 7주차 (2개 추가) | JSON은 값이 전부 보임 → 응답용 `MemberResponse`, 받는 값도 JSON이라 `RoleRequest` 추가 |

### 12. 화면에서 API 써 보기 (단계 8)

- 제공받은 화면을 복사했다 : `templates/admin/members-api.html` (새로 추가), `templates/admin/members.html` ("API 버전으로 보기" 링크가 추가된 버전으로 교체)

**`AdminController.java` (추가)**
```java
@GetMapping("/members-api") // 우리가 만든 JSON API 를 fetch 로 사용하는 회원 관리 화면
public String membersApi() {
    return "admin/members-api"; // 데이터는 화면이 API 로 가져온다
}
```
- 화면은 HTML 껍데기만 있고, 데이터는 JavaScript `fetch`가 `/api/admin/members`에서 JSON으로 가져와 다시 그린다. → **페이지가 새로고침되지 않는다!**
- 오른쪽 기록창에 요청과 응답 코드가 나온다. (F12 네트워크 탭에서도 볼 수 있다.)
- 등록 POST 201, 변경 PATCH 200, 삭제 DELETE 204, 빈 칸 등록 400, 9999번 조회 404

| 손님 | 신분 증명 방법 | 결과 |
|---|---|---|
| 브라우저 (제공 화면의 fetch) | admin으로 로그인할 때 받은 **세션 쿠키**를 자동으로 함께 보냄 | 같은 API, 같은 결과 |
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

- Body는 **raw → JSON**으로 보내고, Authorization은 **Basic Auth**로 설정한다.

### 14. 자주 나는 오류

| 증상 | 원인 → 해결 |
|---|---|
| 415 Unsupported Media Type | Body를 raw → JSON으로 안 보냄 |
| 400 JSON parse error | 따옴표, 쉼표, 괄호 확인 |
| 405 Method Not Allowed | 메서드가 다름 (PUT ↔ PATCH) |
| Postman에 로그인 HTML | 단계 4 보안 설정 누락 |
| 401 / 403 | Authorization 미설정·비밀번호 오류 / USER로 관리자 API 호출 |
| 제공 화면에서 변경·삭제가 403 | `csrf.ignoringRequestMatchers("/api/**")` 누락 |
| JSON에 password | 엔티티를 그대로 반환 → `MemberResponse.from()` |

- 개발 중에는 오류 JSON에 trace(서버 내부 정보)가 같이 나온다. → 실서비스에서는 숨겨야 한다.

### 15. Git 연동

- `README.md`에 이번 주 진행 상황 기록 (예 : "7주차 회원 관리 REST API(JSON) 완료")
- 바뀐 파일 add → commit → GitHub로 push
- Postman 결과도 요약해 두면 받은 사람이 바로 API를 테스트해 볼 수 있다. (위 13번 표)

### 🔧 하다가 막혔던 것

- `SecurityConfig`에 `.requestMatchers("/api/hello").permitAll()`을 `.rememberMe(...);` 뒤에 붙여서 문법 오류가 났다. → `requestMatchers`는 `authorizeHttpRequests` 블록 **안**, `anyRequest()`보다 **위**에 넣어야 한다. (반대로 `.httpBasic()` 같은 건 `.rememberMe()` 뒤에 `;`를 지우고 이어 쓴다.)
- 이전에 켠 서버가 8080 포트를 잡고 있어서 새 서버가 안 켜졌다. → 기존 서버를 중지한 뒤 다시 실행
- `MemberApiController`에 `Member` import가 빠져 있었다. (`com.example.demo.model.domain.Member`)
- `/api/me` 수정 코드를 `MemberResponse.java`의 record 아래에 붙여서 문법 오류 → `MemberApiController`에 이미 있는 `me()` 메서드를 고쳐야 했다.
- 빈 값 검사를 `signup()`의 세 번째 검사로 넣었더니 `{"username":"x"}` 요청에 "비밀번호가 일치하지 않습니다."가 나왔다. → 첫 줄로 옮겨서 "필수입니다." 메시지가 나오게 고쳤다.
- Postman Authorization에 일반(USER) 계정을 넣어서 관리자 API가 403 → admin 계정으로 바꿨다. (403은 보안 규칙이 제대로 동작한다는 뜻이라 오히려 좋은 신호!)
- `changeRole()`을 고치다가 기존 `save()` 줄을 남긴 채 `return ... save()`를 또 추가해서 저장이 두 번 실행됐다. → 기존 줄을 지웠다.
- 이 프로젝트의 일반 USER 계정은 슬라이드의 student1 대신 `saevit`을 썼고, 허용 권한은 6주차 연습문제로 `MANAGER`까지 포함되어 있다.

### 상태

- `MemberApiController.java`(새로 만듦, `/api/hello`·`/api/me`), `AdminApiController.java`(새로 만듦, GET·POST·PATCH·DELETE), `MemberResponse.java`·`RoleRequest.java`(새 DTO) : 실행해서 확인 완료 ✅
- `SecurityConfig.java`(`/api/hello` 허용, `/api/admin/**` 관리자 규칙, Basic 인증·CSRF 제외·401 응답), `MemberService.java`(`findById`, 빈 값 검사, `changeRole` 반환형), `AdminController.java`(`/members-api`) : 실행해서 확인 완료 ✅
- `admin/members-api.html` 추가, `admin/members.html` 링크 반영 완료
- 서버를 켜고 13번 표의 테스트(200·201·204·400·401·403·404)가 모두 기대한 결과로 나오는 걸 확인했다.

## ✏️ 실습 (7주차 연습문제 : REST API 개선하기 — 회원 검색 API & 오류 처리 한 곳에 모으기)

### 문제는 이랬다

1. 회원 검색 API : `GET /api/admin/members?keyword=student` 로 요청하면 아이디나 이름에 검색어가 들어간 회원만 돌려주기 (힌트 : `@RequestParam(required = false)`, `findBy…Containing…Or…Containing`, 확인은 제공 화면의 검색창)
2. 오류 처리 한 곳에 모으기 : 등록·수정·삭제마다 똑같은 `try-catch`가 반복되니까, `@RestControllerAdvice`로 한 곳에서 처리하기 (리팩토링하기 전후로 응답 결과(상태 코드·본문)가 똑같으면 성공)

### 이렇게 만들었다

**`MemberRepository.java` (① 검색 쿼리 메서드)**
```java
// [7주차 연습문제 ①] 아이디 또는 이름에 검색어가 포함된 회원 (SQL : LIKE '%검색어%')
List<Member> findByUsernameContainingOrNameContaining(String username, String name, Sort sort);
```
- 메서드 이름 규칙만 지키면 JPA가 `WHERE username LIKE %?% OR name LIKE %?%`를 알아서 만들어 준다. (5주차 `findByUsername`과 같은 원리)
- 마지막 인자로 `Sort`를 받으면 정렬(id 순서)까지 같이 적용된다.

**`MemberService.java` (① 검색 서비스)**
```java
// [7주차 연습문제 ①] 검색어가 없으면 전체, 있으면 아이디·이름에 포함된 회원만 (id 순서)
public List<Member> search(String keyword) {
    if (isBlank(keyword)) {
        return findAll();
    }
    String word = keyword.trim();
    return memberRepository.findByUsernameContainingOrNameContaining(word, word, Sort.by("id"));
}
```
- 같은 검색어를 아이디 조건과 이름 조건에 둘 다 넘겨서 `OR`로 검색한다.
- 검색어가 없거나 공백뿐이면 전체 목록을 돌려준다. → 원래 있던 `GET /api/admin/members` 동작은 그대로 유지!

**`AdminApiController.java` (① 검색어 받기 + ② `try-catch` 지우기)**
```java
@GetMapping // 회원 목록·검색 : GET /api/admin/members 또는 GET /api/admin/members?keyword=student
public List<MemberResponse> list(@RequestParam(required = false) String keyword) { // 검색어는 선택
    List<Member> members = memberService.search(keyword); // 검색어가 없으면 전체
    return members.stream().map(MemberResponse::from).toList();
}

@PostMapping
public ResponseEntity<MemberResponse> create(@RequestBody MemberForm form) {
    Member saved = memberService.signup(form);
    return ResponseEntity.created(URI.create("/api/admin/members/" + saved.getId()))
            .body(MemberResponse.from(saved)); // 오류(400)는 ApiExceptionHandler 가 처리
}

@PatchMapping("/{id}/role")
public ResponseEntity<MemberResponse> changeRole(@PathVariable Long id, @RequestBody RoleRequest request) {
    Member member = memberService.changeRole(id, request.role());
    return ResponseEntity.ok(MemberResponse.from(member));
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    memberService.deleteMember(id);
    return ResponseEntity.noContent().build();
}
```
- `@RequestParam(required = false)` : 주소에 `?keyword=`가 없어도 오류 없이 `null`로 들어온다.
- 세 메서드에서 `try-catch`가 싹 사라져서 성공하는 흐름만 남았다. 반환형도 `ResponseEntity<?>` 대신 `ResponseEntity<MemberResponse>`, `ResponseEntity<Void>`로 구체적이 됐다.

**`ApiExceptionHandler.java` (② 새로 만듦 — 오류 처리 한 곳)**
```java
@RestControllerAdvice(assignableTypes = AdminApiController.class)
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // 400 {"error": "..."}
    }
}
```
- `@RestControllerAdvice` : 컨트롤러에서 던진 예외를 한 곳에서 받아 JSON으로 응답해 주는 도우미 클래스
- `@ExceptionHandler(IllegalArgumentException.class)` : 서비스가 던지는 중복 아이디·필수값 누락·허용되지 않는 권한·없는 회원·본인 보호 오류를 전부 `400 {"error": "메시지"}`로 바꿔 준다.
- `assignableTypes = AdminApiController.class`로 **관리자 API에만 적용**했다. → `MemberController`의 화면용 `catch (IllegalArgumentException e)`(회원가입 오류 메시지)나 `/api/me`에는 영향이 없다.
- `@PreAuthorize`가 던지는 권한 오류(`AccessDeniedException`)는 `IllegalArgumentException`이 아니라서 가로채지 않고, 원래대로 403으로 응답한다.

### 확인하는 방법

1. 서버 실행 후 Postman(Basic 인증 : admin 계정)으로 `GET /api/admin/members?keyword=student` → 아이디·이름에 `student`가 들어간 회원만 나온다.
2. `keyword`를 빼거나 빈 값으로 보내면 전체 목록, 없는 검색어를 보내면 빈 배열 `[]`
3. 제공 화면 `/admin/members-api`의 검색창에 검색어를 넣으면, 같은 요청(`GET /api/admin/members?keyword=…`)이 오른쪽 기록창에 나온다.
4. 등록·수정·삭제를 오류 상황(중복 아이디, 필수값 누락, `"SUPER"` 권한, 본인 변경·삭제, 없는 회원)으로 호출해 본다 → 리팩토링 전과 같은 400 + `{"error": "..."}` 가 나오는지 확인

### 결과

**① 회원 검색**

| 요청 (admin) | 상태 | 결과 |
|---|---|---|
| `GET /api/admin/members` | 200 | 전체 회원 (id 순서) |
| `GET /api/admin/members?keyword=student` | 200 | 아이디에 `student`가 들어간 회원만 |
| `GET /api/admin/members?keyword=Kim` | 200 | 이름에 `Kim`이 들어간 회원만 (아이디가 아니라 이름도 검색된다) |
| `GET /api/admin/members?keyword=zzzz` | 200 | `[]` (결과 없음) |
| `GET /api/admin/members?keyword=` | 200 | 전체 회원 (빈 값은 검색 안 함) |
| `GET /api/admin/members?keyword=STUDENT` | 200 | 대소문자 구분 없이 검색됨 (MySQL 기본 설정) |
| student1(USER) 계정으로 검색 | 403 | `/api/admin/**` 규칙으로 차단 |
| 인증 없이 검색 | 401 | 로그인 화면이 아니라 401 |

**② 오류 처리 (`ApiExceptionHandler` 적용 후)**

| 요청 | 상태 | 응답 본문 |
|---|---|---|
| `POST` 정상 등록 (`"role":"ADMIN"` 몰래 포함) | 201 | `{"id":6,...,"role":"USER"}` (role은 무시됨) |
| `POST` 같은 아이디 | 400 | `{"error":"이미 사용 중인 아이디입니다."}` |
| `POST` `{"username":"x"}` | 400 | `{"error":"아이디, 비밀번호, 이름은 필수입니다."}` |
| `POST` 비밀번호 불일치 | 400 | `{"error":"비밀번호가 일치하지 않습니다."}` |
| `PATCH` `{"role":"ADMIN"}` | 200 | 바뀐 회원 JSON |
| `PATCH` `{"role":"SUPER"}` | 400 | `{"error":"허용되지 않는 권한입니다 : SUPER"}` |
| `PATCH` admin 본인 변경 | 400 | `{"error":"자기 자신의 권한 변경·삭제는 할 수 없습니다."}` |
| `PATCH` 없는 회원 | 400 | `{"error":"회원이 존재하지 않습니다."}` |
| `DELETE` USER 계정으로 호출 | 403 | 보안 규칙 (`ApiExceptionHandler`와 무관) |
| `DELETE` admin 본인 | 400 | `{"error":"자기 자신의 권한 변경·삭제는 할 수 없습니다."}` |
| `DELETE` 없는 회원 | 400 | `{"error":"회원이 존재하지 않습니다."}` |
| `DELETE` 정상 | 204 | (본문 없음) |
| `GET /api/admin/members/9999` | 404 | (본문 없음) |
| 화면 회원가입(`/signup`)에서 중복 아이디 | 200 | "이미 사용 중인 아이디입니다." (화면 오류 처리는 그대로 동작) |

- 본 실습 13번 표와 **상태 코드·본문이 전부 똑같다** → 리팩토링 성공! 🎉
- `try-catch` 3개(등록·수정·삭제)가 `ApiExceptionHandler` 1곳으로 정리됐다. 이제 새 API를 만들어도 오류 처리를 따로 안 써도 된다.

### 🔧 하다가 막혔던 것

- 이 PC에는 6·7주차 코드가 없고 5주차 연습문제까지만 있었다. `git fetch`로 원격(다른 PC에서 push한 6·7주차)을 확인하고 `git pull --ff-only`로 받은 뒤 작업했다. (로컬 변경이 없어서 충돌 없이 fast-forward)
- 이 PC의 DB에는 관리자 계정이 없어서, 테스트용 임시 계정을 가입시킨 다음 DB에서 role만 바꿔서 썼다. 테스트가 끝난 뒤에는 임시 계정을 전부 지웠다.
- 검색 결과는 `Sort.by("id")`로 정렬해서, 검색하기 전후로 목록 순서가 같게 맞췄다.
- 슬라이드의 "지운 뒤에도 결과가 똑같으면 성공"은 `try-catch`를 지운(리팩토링한) 뒤에도 응답이 이전과 같아야 한다는 뜻으로 이해했다. (삭제 API에서 없는 회원 → 400은 본 실습 동작 그대로 유지)

### 상태

- `MemberRepository.java`(`findByUsernameContainingOrNameContaining`), `MemberService.java`(`search`), `AdminApiController.java`(`keyword` 파라미터, `try-catch` 제거), `ApiExceptionHandler.java`(새로 만듦) : 실행해서 확인 완료 ✅
- 7주차 연습문제 2개 모두 서버를 켜고 위 표의 시나리오 전부 기대한 결과로 나왔다.
