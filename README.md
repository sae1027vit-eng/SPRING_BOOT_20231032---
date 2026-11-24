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
| [🟫 9주차](#week9) | 게시판 CRUD 만들고, 본인 글만 수정·삭제하게 막기 |
| [⬛ 10주차](#week10) | 게시판에 페이지 번호·검색·댓글 붙이고, 회원 삭제 시 외래 키 문제 해결 |
| [⬜ 11주차](#week11) | 모집글 + 포스터 업로드, 세션 신청 바구니, `@Transactional`로 신청 확정 |

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

---

<a id="week9"></a>

# 🟫 9주차 — 게시판 CRUD와 본인 확인

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 이번 주 주제: 게시판(글쓰기·목록·상세·수정·삭제)을 만들고, 서버가 "이 글의 주인이 맞나?"를 확인하게 하기

## 📖 오늘 배운 것

### 1. 게시판 = CRUD

- 게시판은 거의 모든 웹 서비스에 있는 필수 기능이다. (공지사항, 자유게시판, Q&A, 리뷰, 커뮤니티)
- 기본 기능은 **CRUD** : Create(쓰기), Read(읽기), Update(수정), Delete(삭제)
- 게시판에 꼭 필요한 것
  - **누가** 썼는지 (작성자)
  - **내 글만** 고칠 수 있는지 (권한)
  - 없는 글을 열면 **안내 화면**이 나오는지 (404)

| 서비스 | 게시판 모습 | 특징 |
|---|---|---|
| 에브리타임 | 학교별 자유게시판 | 익명·실명 선택, 신고 기능 |
| 네이버 카페 | 카페별 여러 게시판 | 등급별 쓰기 권한 |
| GitHub Issues | 저장소별 이슈 | 작성자·담당자, 상태(열림/닫힘) |
| 우리 포트폴리오 | 게시판 (9주차) | 작성자 연결, 본인 글만 수정·삭제 |

| 기능 | SQL | JPA (리포지토리) | 화면 주소 (9주차) | 7주차 REST로 만들면? |
|---|---|---|---|---|
| 쓰기 (Create) | INSERT | `save()` | `POST /board/write` | `POST /api/posts` |
| 목록 (Read) | SELECT | `findAll()` | `GET /board` | `GET /api/posts` |
| 상세 (Read) | SELECT … WHERE id | `findById()` | `GET /board/3` | `GET /api/posts/3` |
| 수정 (Update) | UPDATE | `save()` | `POST /board/3/edit` | `PATCH /api/posts/3` |
| 삭제 (Delete) | DELETE | `delete()` | `POST /board/3/delete` | `DELETE /api/posts/3` |

- 목록·상세는 **누구나**(GET), 쓰기·수정·삭제는 **로그인한 사람**(폼 → POST)
- 이번에는 API가 아니라 기존 `@Controller` 방식으로 만든다. **HTML 폼은 GET·POST만 보낼 수 있어서** PATCH·DELETE를 못 쓴다. → 주소 끝에 할 일(`/edit`, `/delete`)을 붙인다.

### 2. 게시판 보안 — 버튼만 숨기면 안 된다

- 게시판의 대표적인 위협 : **주소의 번호만 바꿔서** 남의 글을 수정·삭제하기 → 7주차에 본 OWASP **A01 접근 제어 실패**와 같은 유형
- 6주차 : 관리자 메뉴만 숨겼더니 주소로 들어갔다 → 9주차 : 수정 버튼만 숨기면 **주소로 들어간다**
- 그래서 서버가 반드시 **"이 글의 작성자가 맞나?"** 를 확인해야 한다.

| 주차 | 기준 | 질문 | 예 |
|---|---|---|---|
| 5주차 | 로그인 | 누구세요? | 비로그인 → 로그인 화면 |
| 6주차 | 역할(ROLE) | 관리자인가요? | USER → 관리자 페이지 403 |
| 9주차 | **주인(작성자)** | **이 글을 쓴 사람인가?** | 남의 글 수정 → 403 |

### 3. 제공 화면 복사 (단계 1)

- 제공 코드를 먼저 복사했다. `pom.xml`은 그대로 두고 **자바 코드만** 작성한다.
  - `templates/board/` : 목록(`list.html`) · 상세(`detail.html`) · 글쓰기/수정 공용(`form.html`)
  - `templates/error/404.html` : 없는 글 화면
  - `static/css/board.css` : 게시판 디자인
  - `templates/index.html` : 메뉴에 "게시판" 추가 (직접 꾸민 사람은 메뉴 부분만 반영)

**`index.html` (메뉴에 추가, "연락처" 다음 · "회원목록" 앞)**
```html
<!-- [9주차] 게시판 : 목록·상세는 누구나, 글쓰기는 로그인 -->
<li class="nav-item">
    <a class="nav-link" th:href="@{/board}">게시판</a>
</li>
```

- 제공 화면이 컨트롤러에게 기다리는 값 (화면 ↔ 컨트롤러 약속)

| 화면 | 주소 | 컨트롤러가 넘겨줄 값 |
|---|---|---|
| `board/list.html` | `GET /board` | `posts` (글 목록) → "전체 N개"도 이 목록의 크기 |
| `board/detail.html` | `GET /board/{id}` | `post` (글 1개) |
| `board/form.html` | `GET /board/write`, `/board/{id}/edit` | `postForm` (입력값), `postId` (수정일 때), `error` |

### 4. 글과 작성자 — `@ManyToOne`

- 회원 1명이 글을 **여러 개** 쓴다 → 회원 1 : 글 N
- 작성자를 아이디 글자(`String writer = "student1"`)로만 저장하면 문제가 많다. → **회원 객체와 연결**한다.
  - DB에서는 **외래 키(FK)** : `posts.member_id` = `member.id`
  - 자바에서는 객체로 : `post.getAuthor().getName()` → 작성자 이름까지 한 번에

| 문제 | `String writer = "student1"` | `Member author` + `@ManyToOne` |
|---|---|---|
| 작성자 이름 보기 | 회원을 다시 찾아야 한다 | `post.author.name` 한 번에 |
| 회원 정보가 바뀌면 | 글 쪽 값과 어긋날 수 있다 | 항상 최신 회원 정보 |
| 없는 회원 이름 입력 | 막을 수 없다 | DB의 FK가 막아 준다 |
| 회원 삭제 시 | 글만 남는다 (주인 없는 글) | FK 때문에 오류 → 처리 방법 필요 (10주차) |

| 시점 | 읽는 법 | 어노테이션 | 붙는 위치 |
|---|---|---|---|
| 회원에서 보면 | 회원 1명 → 글 여러 개 (1 : N) | `@OneToMany` | `Member` 클래스 |
| 글에서 보면 | 글 여러 개 → 회원 1명 (N : 1) | `@ManyToOne` | `Post` 클래스 |

→ 처음엔 "회원 1 : 글 N"인데 왜 `ManyToOne`이지? 했는데, **글(Post) 기준으로 읽기 때문**이었다.

### 5. 글 엔티티 - `Post` (단계 2)

**`Post.java` (새로 만듦, `model/domain`)**
```java
@Entity // 게시글 엔티티 : posts 테이블
@Table(name = "posts")
@Data // set/get/tostring 등 필수 메서드 자동 생성
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 (1, 2, 3 ...)
    private Long id;
    @Column(nullable = false, length = 100) // 제목 : 필수, 100자까지
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT") // 내용 : 긴 글도 저장
    private String content;
    @ManyToOne(fetch = FetchType.LAZY) // 작성자 : 회원 1명이 글 여러 개 (N : 1)
    @JoinColumn(name = "member_id", nullable = false) // 외래 키(FK) 컬럼 이름
    private Member author;
    @Column(nullable = false) // 작성 시간
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // 수정 시간 (수정한 적 없으면 null)
    @Column(nullable = false) // 조회수 (연습문제에서 사용)
    private int viewCount = 0;
}
```
- 4주차 TestDB, 5주차 Member와 같은 방식에 **작성자 연결만** 새로 등장했다.
- 서버를 재시작하면 터미널(`show-sql=true`)에 `create table posts (… member_id bigint not null …)`와 `foreign key (member_id) references member (id)`가 찍힌다.

| 자바 필드 | DB 컬럼 | 타입 |
|---|---|---|
| id | id | bigint (자동 증가, PK) |
| title | title | varchar(100) |
| content | content | TEXT |
| author | **member_id** | bigint (**FK → member.id**) |
| createdAt / updatedAt | created_at / updated_at | datetime |
| viewCount | view_count | int |

- **외래 키(FK) = DB가 지키는 약속** : `member_id`는 반드시 **있는 회원** 번호여야 한다. (그래서 글 쓴 회원을 지울 때 문제가 생긴다 → 10주차에 해결)
- `fetch = FetchType.LAZY` : 연결된 객체(작성자)를 바로 읽지 않고 **필요할 때** 가져온다. (불필요한 조회 감소)

### 6. 글쓰기 준비 - DTO와 리포지토리 (단계 2)

**`PostForm.java` (새로 만듦, `model/dto`)**
```java
@Data
public class PostForm { // 글쓰기·수정 화면 → 컨트롤러로 전달되는 데이터(DTO)
    private String title; // 제목 (input name="title")
    private String content; // 내용 (textarea name="content")
    // 작성자는 받지 않는다 → 로그인한 사람으로 서버가 정한다
}
```

**`PostRepository.java` (새로 만듦, `model/repository`)**
```java
@Repository // 게시글 리포지토리 : save, findAll, findById, delete 는 이미 있다
public interface PostRepository extends JpaRepository<Post, Long> {
}
```

| 구분 | MemberForm (5주차) | PostForm (9주차) |
|---|---|---|
| 받는 값 | 아이디, 비밀번호, 확인, 이름 | **제목, 내용** |
| 받지 않는 값 | role (권한) | **author (작성자)** |
| 막는 것 | 권한 조작 | **작성자 바꿔치기 (남의 이름으로 글쓰기)** |

### 7. 글쓰기 서비스 - `PostService` (단계 2)

**`PostService.java` (새로 만듦, `model/service`)**
```java
@Service // 게시판 서비스
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;

    public List<Post> findAll() { // 전체 목록 (최신 글이 위로)
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Post write(PostForm form, String username) { // 글쓰기 (작성자 = 로그인한 회원)
        validate(form);
        Member author = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Post post = new Post();
        post.setTitle(form.getTitle().trim());
        post.setContent(form.getContent());
        post.setAuthor(author); // 작성자는 화면이 아니라 서버가 정한다
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post); // INSERT
    }

    private void validate(PostForm form) { // 빈 값 검사 (5주차 회원가입과 같은 방식)
        if (form.getTitle() == null || form.getTitle().isBlank()
                || form.getContent() == null || form.getContent().isBlank()) {
            throw new IllegalArgumentException("제목과 내용을 모두 입력하세요.");
        }
        if (form.getTitle().trim().length() > 100) {
            throw new IllegalArgumentException("제목은 100자까지 입력할 수 있습니다.");
        }
    }
}
```
- 글쓰기 순서 : ① 검사 → ② 작성자 찾기 → ③ 저장

### 8. 게시판 컨트롤러 - 목록 · 글쓰기 (단계 2)

**`BoardController.java` (새로 만듦, `controller`)**
```java
@Controller // 게시판 화면 컨트롤러
@RequestMapping("/board") // 모든 URL 앞에 /board
public class BoardController {
    @Autowired
    PostService postService;

    @GetMapping // 목록 : GET /board (누구나)
    public String list(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "board/list";
    }

    @GetMapping("/write") // 글쓰기 화면 : GET /board/write (로그인 필요)
    public String writeForm() {
        return "board/form";
    }

    @PostMapping("/write") // 글쓰기 처리 : POST /board/write
    public String write(PostForm postForm, Principal principal, Model model) {
        try {
            Post post = postService.write(postForm, principal.getName()); // 작성자 = 로그인한 사람
            return "redirect:/board/" + post.getId(); // 저장 후 상세 화면으로
        } catch (IllegalArgumentException e) { // 빈 제목·내용
            model.addAttribute("error", e.getMessage());
            return "board/form"; // 입력값 유지한 채 다시 글쓰기 화면
        }
    }
}
```

**`SecurityConfig.java` (`authorizeHttpRequests` 블록 안, `anyRequest()`보다 위에 한 줄 추가)**
```java
.requestMatchers("/board", "/board/{id:\\d+}").permitAll() // 게시판 목록·상세 : 누구나
```
- `{id:\d+}` = **숫자만** → `/board/write`는 포함되지 않아서 로그인이 필요하다.
- 목록의 작성자 칸은 `post.author.name` → 글 → 작성자(Member) → 이름 (`@ManyToOne` 덕분)

| # | 계정 | 하는 일 | 기대 결과 |
|---|---|---|---|
| 1 | 로그인 안 함 | 메인 → 게시판 메뉴 | 목록, **글쓰기 버튼 없음** |
| 2 | 로그인 안 함 | 주소창 `/board/write` | 로그인 화면으로 이동 |
| 3 | student1 | 글쓰기 → 제목·내용 입력 → 등록 | 상세 화면으로 이동 (단계 3 전이면 404) |
| 4 | student1 | 제목 비우고 등록 | "제목과 내용을 모두 입력하세요." |
| 5 | admin | 글 하나 더 쓰기 | 목록 맨 위에 새 글, 작성자 = 관리자 |

### 9. 상세 보기와 404 (단계 3)

**`PostService.java` (추가, import : `HttpStatus`, `ResponseStatusException`)**
```java
public Post findById(Long id) { // 글 1개 (없으면 404 → templates/error/404.html)
    return postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "글이 존재하지 않습니다."));
}
```

**`BoardController.java` (추가)**
```java
@GetMapping("/{id}") // 상세 : GET /board/3 (누구나)
public String detail(@PathVariable Long id, Model model) {
    Post post = postService.findById(id);
    model.addAttribute("post", post);
    return "board/detail";
}
```
- 확인 : 글 제목 클릭 → 상세 화면, 주소창 `/board/99999` → 404 화면

| 화면 | 언제 | 만든 주차 |
|---|---|---|
| `error/403.html` | 권한 없음 | 6주차 |
| `error/404.html` | 없는 글 · 없는 주소 | 9주차 (7주차에 "같은 방식"이라고 했던 것) |

### 10. 수정 · 삭제 (단계 4)

- JPA에는 "수정" 전용 메서드가 없다. 조회한 엔티티의 값을 바꾸면 DB에 반영된다. → **변경 감지(Dirty Checking)**
- `save()`는 4주차부터 쓰던 같은 메서드 : **id가 없으면 INSERT, 있으면 UPDATE**

| 상황 | post의 id | `save()`가 하는 일 |
|---|---|---|
| 글쓰기 (write) | 없음 (null) | INSERT |
| 수정 (edit) | 있음 (2) | UPDATE |

**`PostService.java` (추가)**
```java
public Post findForEdit(Long id) { // 수정 화면에 보여 줄 글
    Post post = findById(id);
    return post;
}

public Post edit(Long id, PostForm form) { // 글 수정
    Post post = findForEdit(id);
    validate(form);
    post.setTitle(form.getTitle().trim());
    post.setContent(form.getContent());
    post.setUpdatedAt(LocalDateTime.now()); // 수정 시간 기록
    return postRepository.save(post); // id 가 있으니 UPDATE
}

public void delete(Long id) { // 글 삭제
    Post post = findById(id);
    postRepository.delete(post); // DELETE
}
```

**`BoardController.java` (추가)**
```java
@GetMapping("/{id}/edit") // 수정 화면 : GET /board/3/edit
public String editForm(@PathVariable Long id, Model model) {
    Post post = postService.findForEdit(id);
    PostForm postForm = new PostForm(); // 기존 내용을 폼에 채워서 보여 준다
    postForm.setTitle(post.getTitle());
    postForm.setContent(post.getContent());
    model.addAttribute("postForm", postForm);
    model.addAttribute("postId", id); // 화면이 "수정" 모드로 바뀐다
    return "board/form";
}

@PostMapping("/{id}/edit") // 수정 처리 : POST /board/3/edit
public String edit(@PathVariable Long id, PostForm postForm, Model model) {
    try {
        postService.edit(id, postForm);
        return "redirect:/board/" + id;
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("postId", id);
        return "board/form";
    }
}

@PostMapping("/{id}/delete") // 삭제 처리 : POST /board/3/delete
public String delete(@PathVariable Long id) {
    postService.delete(id);
    return "redirect:/board?deleted"; // 목록 + "삭제되었습니다"
}
```

- 글쓰기·수정은 화면 하나(`form.html`)를 **`postId` 유무**로 나눠 쓴다.

| 구분 | 글쓰기 | 수정 |
|---|---|---|
| postId | 없음 | 글 번호 |
| 폼 전송 주소 | `POST /board/write` | `POST /board/{id}/edit` |
| 입력칸 | 빈 칸 | 기존 제목·내용 채움 |

**여기서 문제 발견!** 😱

| # | 계정 | 하는 일 | 결과 |
|---|---|---|---|
| 1 | student1 | 내 글 → 수정 → 저장 | 상세에 "수정 시간" 표시 |
| 2 | student1 | 내 글 → 삭제 | 목록 + "글이 삭제되었습니다." |
| 3 | student3 | student1의 글 열기 | 수정·삭제 버튼 없음 (화면에서 숨김) |
| 4 | student3 | 주소창에 `/board/2/edit` 입력 | 어? **수정 화면이 열린다** |
| 5 | student3 | 제목을 바꾸고 저장 | **남의 글이 바뀌었다!** |

- 6주차 관리자 페이지와 **똑같은 문제** : 버튼은 숨겼지만, 서버는 확인하지 않았다.

### 11. 본인 확인 (단계 5)

- 서버가 확인할 것 : **"로그인한 사람 = 글쓴이?"** → 다르면 `AccessDeniedException` → 403 화면
- 관리자는? **수정은 본인만**(대신 고치면 안 됨), **삭제는 본인 또는 관리자**(규칙 위반 글 정리)
- 왜 URL 규칙으로는 못 막을까? → `/board/2/edit`의 주인은 **DB를 봐야** 알 수 있다.

| 구분 | 6주차 관리자 페이지 | 9주차 게시글 |
|---|---|---|
| 기준 | 역할 (ROLE_ADMIN) | 주인 (글쓴이) |
| 확인 위치 | URL 규칙 + `@PreAuthorize` | **서비스 코드** (글마다 주인이 다름) |
| 화면 | 관리자 메뉴 숨김 | 수정·삭제 버튼 숨김 |
| 실패 시 | 403 | 403 |

**`PostService.java` (수정, import : `AccessDeniedException`, `Authentication`, `SecurityContextHolder`)**
```java
public Post findForEdit(Long id) { // 수정 화면에 보여 줄 글 (본인 글만)
    Post post = findById(id);
    checkWriter(post); // ★ 추가
    return post;
}

public void delete(Long id) { // 글 삭제 (본인 또는 관리자)
    Post post = findById(id);
    if (!isAdmin()) checkWriter(post); // ★ 추가 : 관리자는 누구 글이든 삭제 가능
    postRepository.delete(post);
}

private void checkWriter(Post post) { // 로그인한 사람이 글쓴이인지 → 아니면 403
    String loginUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    if (!post.getAuthor().getUsername().equals(loginUsername)) {
        throw new AccessDeniedException("본인이 쓴 글만 수정·삭제할 수 있습니다.");
    }
}

private boolean isAdmin() { // 로그인한 사람이 관리자(ROLE_ADMIN)인지
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
}
```
- `edit()`는 `findForEdit()`를 부르니까 **자동으로 본인 확인**이 된다.

| # | 계정 | 하는 일 | 기대 결과 |
|---|---|---|---|
| 1 | student3 | 주소창 `/board/2/edit` (남의 글) | **403 접근 거부 화면** |
| 2 | student1 | 내 글 수정 | 수정됨, "수정 시간" 표시 |
| 3 | admin | student3의 글 열기 | **관리자 삭제** 버튼 |
| 4 | admin | 관리자 삭제 | 삭제됨 |
| 5 | admin | student1의 글 `/edit` 주소 입력 | 403 (수정은 본인만) |

→ **화면(버튼 숨김) + 서비스(본인 확인) = 이중 확인**

### 12. 자주 나는 오류

- 전체 흐름 다시 확인 : 비로그인 목록 → 글쓰기 → 수정 → 남의 글 403 → 관리자 삭제

| 증상 | 원인 → 해결 |
|---|---|
| 게시판 목록이 로그인 화면으로 | `/board` permitAll 누락 |
| 글쓰기 저장 시 403 | 폼에 `th:action` 대신 `action` 사용 → CSRF (5주차) |
| `/board/write`가 누구에게나 열림 | `/board/**`로 너무 넓게 허용 → `{id:\d+}` 사용 |
| 글 제목 클릭 시 404 화면 | 상세(단계 3) 미구현, 또는 없는 번호 |
| 남의 글이 수정됨 | `checkWriter()` 누락 (단계 5) |
| 글 쓴 회원을 관리자가 삭제하면 500 | 외래 키 때문 → 10주차에 해결. 지금은 그 회원의 글을 먼저 삭제 |

### 13. Git 연동

- `README.md`에 이번 주 진행 상황 기록 (예 : "9주차 게시판(CRUD) + 본인 확인 완료")
- 바뀐 파일 add → commit → GitHub로 push
- 팁 : 오늘 쓴 **글은 깃에 올라가지 않는다.** 글은 DB에 저장되고, 깃에는 코드만 올라간다.

### 🔧 하다가 막혔던 것

- 제공 `index.html`을 통째로 복사했더니 직접 꾸민 내용(소개 문구, 프로필 사진, 서비스 제목·아이콘·링크, 회원목록 `hasAnyRole` 조건 등)이 전부 덮어써졌다. → `git checkout`으로 커밋해 둔 버전으로 되돌리고, **게시판 메뉴 `<li>` 한 개만** "연락처"와 "회원목록" 사이에 추가했다. (직접 꾸민 사람은 메뉴 부분만 반영하라는 게 이 뜻이었다.)

### 상태

- `Post.java`, `PostForm.java`, `PostRepository.java`, `PostService.java`(글쓰기·목록·상세·수정·삭제 + `checkWriter`·`isAdmin`), `BoardController.java`(새로 만듦) : 코드 작성 완료
- `SecurityConfig.java`(`/board`, `/board/{id:\d+}` 허용), `index.html`(게시판 메뉴) 반영 완료
- `templates/board/`, `templates/error/404.html`, `static/css/board.css` 제공 화면 복사 완료
- 서버를 켜고 위 테스트 표 확인하기 ⬜

## ✏️ 실습 (9주차 연습문제 : 게시판 개선하기 — 조회수 & 내가 쓴 글 모아 보기)

### 문제는 이랬다

1. 조회수 : 상세 화면을 열 때마다 `viewCount` 1 증가 (힌트 : `PostService`에 `read(id)` → 컨트롤러 `detail()`에서 사용, 확인은 목록·상세의 **조회** 숫자)
2. 내가 쓴 글 모아 보기 : `GET /board/my` → 로그인한 사람의 글만, 최신 글이 위로 (힌트 : 리포지토리에 **메서드 이름만** 추가, 목록 화면 재사용하고 `listTitle`로 제목 변경)

### 이렇게 만들었다

**① 조회수 — `PostService.java` (추가)**
```java
public Post read(Long id) { // [연습문제 ①] 상세 화면 : 열 때마다 조회수 1 증가
    Post post = findById(id);
    post.setViewCount(post.getViewCount() + 1);
    return postRepository.save(post); // id 가 있으니 UPDATE
}
```

**`BoardController.java` (`detail()` 수정)**
```java
@GetMapping("/{id}") // 상세 : GET /board/3 (누구나)
public String detail(@PathVariable Long id, Model model) {
    Post post = postService.read(id); // [연습문제 ①] findById → read (조회수 1 증가)
    model.addAttribute("post", post);
    return "board/detail";
}
```
- `findById()`는 그대로 두고 상세 화면만 `read()`를 쓴다. → 수정 화면(`findForEdit`)이나 삭제에서 글을 찾을 때는 조회수가 안 올라간다.
- `viewCount`는 엔티티를 만들 때 이미 `int viewCount = 0`으로 넣어 둬서, 화면(`post.viewCount`)은 고칠 필요가 없었다.

**② 내가 쓴 글 — `PostRepository.java` (메서드 이름만 추가)**
```java
// [연습문제 ②] 작성자(author)의 username 으로 찾고, 번호 큰 순서(최신 글)로
List<Post> findByAuthor_UsernameOrderByIdDesc(String username);
```

| 이름 조각 | 뜻 | SQL 로 바꾸면 |
|---|---|---|
| `findBy` | 찾아라 | `SELECT … FROM posts` |
| `Author_Username` | 작성자(author)의 username으로 | `JOIN member … WHERE m.username = ?` |
| `OrderByIdDesc` | 번호 큰 순서(최신 글)로 | `ORDER BY p.id DESC` |

- `_`(언더스코어)는 "author **안의** username"이라고 경로를 끊어 주는 표시다. → `@ManyToOne`으로 연결해 둔 덕분에 메서드 이름만으로 JOIN까지 된다.

**`PostService.java` (추가)**
```java
public List<Post> findMine(String username) { // [연습문제 ②] 내가 쓴 글 (최신 글이 위로)
    return postRepository.findByAuthor_UsernameOrderByIdDesc(username);
}
```

**`BoardController.java` (추가, `/{id}` 위에)**
```java
@GetMapping("/my") // [연습문제 ②] 내가 쓴 글 : GET /board/my (로그인 필요)
public String myList(Principal principal, Model model) {
    List<Post> posts = postService.findMine(principal.getName());
    model.addAttribute("posts", posts);
    model.addAttribute("listTitle", "내가 쓴 글"); // 목록 화면 재사용, 제목만 변경
    return "board/list";
}
```
- 제공 `list.html`에 이미 `${listTitle ?: '게시판'}`(없으면 "게시판")과 "내가 쓴 글" 버튼(`/board/my`)이 들어 있어서 화면은 그대로 썼다.
- `SecurityConfig`는 고치지 않았다. `/board/my`는 `{id:\d+}`(숫자만)에 걸리지 않으니까 `anyRequest().authenticated()`로 가서 자동으로 **로그인 필요**가 된다.

### 확인하는 방법

| # | 하는 일 | 기대 결과 |
|---|---|---|
| ① | 같은 글 상세를 3번 열기 | 조회 3 |
| ② | 로그인 → 내가 쓴 글 | 내 글만, 제목 "내가 쓴 글" |
| ② | 로그인 안 하고 `/board/my` | 로그인 화면 |

### 결과

| # | 실제로 해 본 것 | 결과 |
|---|---|---|
| ① | 비로그인으로 1번 글(한새빛 작성, 조회 0) 상세를 3번 열기 | 조회 1 → 2 → **3** ✅, 목록의 조회 칸도 3 |
| ② | admin으로 `/board/my` | 제목 **"내가 쓴 글"**, 전체 2개 중 관리자 글(2번) 1개만 ✅ |
| ② | 로그인 안 하고 `/board/my` (브라우저) | 302 → `/login` ✅ |

### 🔧 하다가 막혔던 것

- 처음에 curl로 로그인 없이 `/board/my`를 열었더니 로그인 화면(302)이 아니라 **401**이 왔다. → 7주차에 넣은 `.httpBasic()` 때문에, 브라우저가 아닌 요청(`Accept: text/html` 헤더 없음)에는 스프링이 Basic 인증 방식으로 401을 돌려준다. 브라우저처럼 `Accept: text/html` 헤더를 붙이니 302 → `/login`이 나왔다. (보안 구멍이 아니라 요청 종류별로 응답만 다른 것)
- `/my`와 `/{id}`가 둘 다 `/board/○○` 모양이라 겹칠까 걱정했는데, 스프링은 **글자가 정해진 주소(`/my`)를 변수 주소(`/{id}`)보다 먼저** 고른다. 그래도 읽기 쉽게 `/my`를 `/{id}` 위에 두었다.

### 상태

- `PostRepository.java`(`findByAuthor_UsernameOrderByIdDesc`), `PostService.java`(`read`, `findMine`), `BoardController.java`(`detail()`에서 `read` 사용, `/my` 추가) : 실행해서 확인 완료 ✅
- 9주차 연습문제 2개 모두 서버를 켜고 위 표의 시나리오가 기대한 결과로 나왔다.

---

<a id="week10"></a>

# ⬛ 10주차 — 페이징 · 검색 · 댓글 (연관관계 활용)

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 이번 주 주제: 9주차 게시판에 페이지 번호와 검색을 붙이고, 댓글로 `@ManyToOne` · `@OneToMany` 연관관계를 써 보기

## 📖 오늘 배운 것

### 1. 9주차 게시판의 남은 문제

- 9주차 게시판은 동작은 하지만, 실제 서비스라고 하기엔 부족한 점이 있었다.

| 문제 | 왜 문제? | 10주차 해결 |
|---|---|---|
| 글이 1000개면 목록에 1000개 | 느리고 보기 힘들다 | **페이징** (10개씩) |
| 원하는 글을 못 찾는다 | 스크롤로 찾아야 한다 | **검색** (제목·내용) |
| 글에 반응할 방법이 없다 | 게시판인데 대화가 안 된다 | **댓글** |
| 글 쓴 회원을 지우면 500 | 외래 키(FK) 때문 | **삭제 전에 확인** |

### 2. 제공 화면 교체 (단계 0)

- 9주차 화면을 10주차 버전으로 **덮어쓴다.**
  - `templates/board/list.html` : 검색창 + 페이지 번호 추가
  - `templates/board/detail.html` : 댓글 영역 추가
  - `static/css/board.css` : 맨 아래에 페이지 번호 · 검색 · 댓글 디자인 추가
- 새 화면은 컨트롤러가 값을 넘겨줄 때만 새 부분이 보이게(`th:if`) 만들어져 있다. → 화면만 바꿔도 9주차처럼 그대로 동작하고, 단계를 하나씩 진행할 때마다 기능이 켜진다.

| 화면 | 컨트롤러가 넘겨줄 값 (새로 추가) | 없으면 |
|---|---|---|
| `board/list.html` | `page` (페이지 정보), `keyword` (검색어) | 페이지 번호 안 보임, "전체 N개"는 목록 크기 |
| `board/detail.html` | `comments` (댓글 목록) | 댓글 영역 안 보임 |

### 3. 페이징 — `Pageable` 과 `Page` (단계 1)

- 전체를 다 가져오지 않고 **한 페이지 분량만** DB에서 가져온다.
- JPA 리포지토리의 `findAll()`에 `Pageable`(몇 페이지, 몇 개씩, 정렬)을 넘기면 SQL에 `LIMIT`·`OFFSET`이 자동으로 붙는다.

| 이름 | 하는 일 | 예 |
|---|---|---|
| `PageRequest.of(page, size, sort)` | "몇 페이지를 몇 개씩, 어떤 순서로" 요청서 | `PageRequest.of(0, 10, Sort.by(DESC, "id"))` |
| `Page<Post>` | 결과 + 페이지 정보 | `getContent()` 글 목록, `getTotalPages()` 전체 페이지 수 |
| `page.number` | 지금 페이지 번호 | **0부터** 시작 → 화면에는 `+1` 해서 표시 |
| `page.totalElements` | 전체 글 수 | "전체 N개" |
| `page.first` / `page.last` | 첫·마지막 페이지인지 | 이전·다음 버튼 비활성화 |

**`PostService.java` (추가, import : `Page`, `PageRequest`)**
```java
public static final int PAGE_SIZE = 10; // 한 페이지에 보여 줄 글 수

// [10주차] 단계 1 : 페이지 목록 (page 는 0 부터)
public Page<Post> findPage(int page) {
    PageRequest pageable = PageRequest.of(Math.max(page, 0), PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
    return postRepository.findAll(pageable); // SELECT ... ORDER BY id DESC LIMIT 10 OFFSET page*10
}
```
- `Math.max(page, 0)` : 주소창에 `?page=-1`을 넣어도 0페이지로 → 음수면 오류가 나기 때문

**`BoardController.java` (`list()` 수정, 9주차 버전은 주석 처리)**
```java
@GetMapping // 목록 : GET /board?page=0 (누구나)
public String list(@RequestParam(defaultValue = "0") int page, Model model) {
    Page<Post> posts = postService.findPage(page); // 페이지 단위로 10개씩
    model.addAttribute("posts", posts.getContent()); // 이번 페이지의 글 목록
    model.addAttribute("page", posts); // 페이지 정보 → 화면에 번호가 생긴다
    return "board/list";
}
```
- `@RequestParam(defaultValue = "0")` : `/board`처럼 `page`가 없으면 0페이지
- 화면이 받는 이름은 그대로 `posts` → 목록 표(`th:each`)는 고칠 필요가 없다.

| 주소 | 가져오는 글 (최신 순) | SQL |
|---|---|---|
| `/board` 또는 `/board?page=0` | 1~10번째 | `LIMIT 10 OFFSET 0` |
| `/board?page=1` | 11~20번째 | `LIMIT 10 OFFSET 10` |
| `/board?page=2` | 21~30번째 | `LIMIT 10 OFFSET 20` |

### 4. 검색 — `@Query` 와 JPQL (단계 2)

- 9주차 연습문제처럼 **메서드 이름**으로도 만들 수 있지만, "제목 **또는** 내용"처럼 조건이 길어지면 이름이 너무 길어진다. → `@Query`로 직접 쓴다.
- **JPQL** : SQL과 비슷하지만 **테이블이 아니라 엔티티(클래스)·필드** 이름을 쓴다. (`posts` 테이블 ❌ → `Post` 엔티티 ⭕)

**`PostRepository.java` (추가, import : `Page`, `Pageable`, `Query`, `Param`)**
```java
// [10주차] 단계 2 : 제목 또는 내용에 검색어가 들어간 글 (JPQL)
@Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
Page<Post> search(@Param("keyword") String keyword, Pageable pageable);
```

| 부분 | 뜻 |
|---|---|
| `SELECT p FROM Post p` | Post 엔티티를 `p`라고 부르고 가져와라 |
| `LIKE %:keyword%` | 검색어가 **들어 있는** (앞뒤 아무 글자) |
| `:keyword` ↔ `@Param("keyword")` | 메서드 파라미터를 쿼리에 넣는 자리 |
| `Pageable pageable` | 검색 결과도 페이징 + 정렬 (쿼리에 `ORDER BY` 안 써도 됨) |

**`PostService.java` (단계 1의 `findPage(int page)`를 바꿈)**
```java
// [10주차] 단계 2 : 페이지 목록 + 검색 (검색어가 없으면 전체)
public Page<Post> findPage(String keyword, int page) {
    PageRequest pageable = PageRequest.of(Math.max(page, 0), PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
    if (keyword == null || keyword.isBlank()) { // 앞이 true면 뒤를 실행하지 않음
        return postRepository.findAll(pageable);
    }
    return postRepository.search(keyword.trim(), pageable); // 검색 결과도 10개씩
}
```

**`BoardController.java` (`list()` 수정)**
```java
@GetMapping // 목록 : GET /board?page=0&keyword=스프링 (누구나)
public String list(@RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String keyword, Model model) {
    Page<Post> posts = postService.findPage(keyword, page);
    model.addAttribute("posts", posts.getContent());
    model.addAttribute("page", posts);
    model.addAttribute("keyword", keyword); // 검색창에 검색어 유지, 페이지 링크에도 붙인다
    return "board/list";
}
```
- `required = false` : 검색어 없이 `/board`로 들어와도 오류가 나지 않는다. (`keyword`는 `null`)
- 화면의 페이지 링크는 `@{${url}(page=${i},keyword=${keyword})}` → **검색 결과의 2페이지도 검색 결과**로 이어진다.
- `SecurityConfig`는 고칠 필요 없다. `?page=`, `?keyword=`는 주소 뒤에 붙는 값이라 `/board` permitAll에 그대로 포함된다.

| # | 하는 일 | 기대 결과 |
|---|---|---|
| 1 | 글을 11개 이상 쓰고 `/board` | 10개 + 페이지 번호 `1 2` |
| 2 | 2페이지 클릭 | 주소 `?page=1`, 나머지 글 |
| 3 | 검색창에 "스프링" | 제목·내용에 "스프링"이 있는 글만, "'스프링' 검색 결과 N개" |
| 4 | 없는 단어 검색 | "검색 결과가 없습니다." |
| 5 | "전체 목록 보기" | 검색 해제, 전체 목록 |

### 5. 댓글 엔티티 - `Comment` (단계 3)

- 댓글은 **글 하나에 여러 개**, **회원 한 명이 여러 개** → `@ManyToOne`이 **두 개**
- 9주차 `Post.author`와 똑같은 방식을 한 번 더 쓴다.

| 연결 | 읽는 법 | 외래 키 |
|---|---|---|
| `Comment.post` | 댓글 N → 글 1 | `comments.post_id` → `posts.id` |
| `Comment.author` | 댓글 N → 회원 1 | `comments.member_id` → `member.id` |

**`Comment.java` (새로 만듦, `model/domain`)**
```java
@Entity // 댓글 엔티티 : comments 테이블
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 500) // 댓글 내용 : 필수, 500자까지
    private String content;
    @ManyToOne(fetch = FetchType.LAZY) // 어느 글의 댓글인지 (글 1 : 댓글 N)
    @JoinColumn(name = "post_id", nullable = false) // 외래 키 → posts.id
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY) // 댓글 작성자 (9주차 글 작성자와 같은 방법)
    @JoinColumn(name = "member_id", nullable = false) // 외래 키 → member.id
    private Member author;
    @Column(nullable = false) // 작성 시간
    private LocalDateTime createdAt;
}
```
- 서버를 **완전히 재시작**하면 터미널에 `create table comments (...)`와 외래 키 2개가 찍힌다. (`ddl-auto=update`는 시작할 때만 테이블을 만든다)

**`CommentRepository.java` (새로 만듦, `model/repository`)**
```java
@Repository // 댓글 리포지토리
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByIdAsc(Post post); // 이 글의 댓글 (오래된 순)
}
```
- 글 목록은 최신 순(`Desc`)인데, 댓글은 대화라서 **오래된 순(`Asc`)** 이 자연스럽다.

### 6. 댓글 쓰기 - `CommentService` · `CommentController` (단계 3)

**`CommentService.java` (새로 만듦, `model/service`)**
```java
@Service // 댓글 서비스
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostService postService; // 글 찾기는 9주차 PostService 를 그대로 쓴다

    public List<Comment> findByPost(Post post) { // 이 글의 댓글 목록
        return commentRepository.findByPostOrderByIdAsc(post);
    }

    public Comment write(Long postId, String content, String username) { // 댓글 쓰기
        if (content == null || content.isBlank() || content.length() > 500) {
            throw new IllegalArgumentException("댓글은 1~500자로 입력하세요.");
        }
        Post post = postService.findById(postId); // 없는 글이면 404
        Member author = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        Comment comment = new Comment();
        comment.setContent(content.trim());
        comment.setPost(post); // 어느 글의 댓글인지
        comment.setAuthor(author); // 작성자는 서버가 정한다
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment); // INSERT
    }
}
```
- 순서는 9주차 글쓰기와 같다 : ① 검사 → ② 글 찾기 → ③ 작성자 찾기 → ④ 저장
- 다른 서비스(`PostService`)를 주입해서 재사용할 수 있다. → 없는 글 404 처리를 다시 만들 필요가 없다.

**`CommentController.java` (새로 만듦, `controller`)**
```java
@Controller // 댓글 컨트롤러 : 처리 후 글 상세 화면으로 돌아간다
@RequestMapping("/board/{postId}/comments") // 댓글은 글 아래에 있다 → 주소도 글 아래
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping // 댓글 쓰기 : POST /board/3/comments (로그인 필요)
    public String write(@PathVariable Long postId,
            @RequestParam(defaultValue = "") String content,
            Principal principal) {
        try {
            commentService.write(postId, content, principal.getName());
            return "redirect:/board/" + postId + "#comments"; // 댓글 위치로
        } catch (IllegalArgumentException e) { // 빈 댓글, 500자 초과
            return "redirect:/board/" + postId + "?commentError#comments";
        }
    }
}
```
- 주소 설계 : 댓글은 **글에 속해 있으니까** 주소도 `/board/{글번호}/comments` (7주차 REST 주소 규칙과 같은 생각)
- `#comments` : 새로고침된 화면이 **댓글 영역(`id="comments"`)으로 바로 스크롤**된다.
- `?commentError` : 화면의 `th:if="${param.commentError}"`가 오류 메시지를 띄운다. (9주차 `?deleted`와 같은 방식)

**`BoardController.java` (추가)**
```java
@Autowired // 필드 추가 (postService 아래)
CommentService commentService;

@GetMapping("/{id}") // 상세 : GET /board/3 (누구나)
public String detail(@PathVariable Long id, Model model) {
    Post post = postService.read(id);
    model.addAttribute("post", post);
    model.addAttribute("comments", commentService.findByPost(post)); // ★ 댓글 목록 → 화면에 댓글 영역
    return "board/detail";
}
```
- `SecurityConfig`는 고칠 필요 없다. `POST /board/3/comments`는 `{id:\d+}`(숫자만)에 걸리지 않으니까 `anyRequest().authenticated()` → **로그인 필요**
- 댓글 읽기는 상세 화면(`/board/3`)에서 하니까 비로그인도 볼 수 있다.

### 7. 댓글 삭제와 본인 확인 (단계 4)

- 9주차 글 삭제와 **똑같은 규칙** : 본인 또는 관리자만, 버튼 숨김(화면) + 본인 확인(서비스)

**`CommentService.java` (추가, import : `HttpStatus`, `ResponseStatusException`, `AccessDeniedException`, `Authentication`, `SecurityContextHolder`)**
```java
public void delete(Long id) { // 댓글 삭제 (본인 또는 관리자)
    Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."));
    if (!isAdmin())
        checkWriter(comment);
    commentRepository.delete(comment); // DELETE
}

private void checkWriter(Comment comment) { // 로그인한 사람이 댓글 작성자인지 → 아니면 403
    String loginUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    if (!comment.getAuthor().getUsername().equals(loginUsername)) {
        throw new AccessDeniedException("본인이 쓴 댓글만 삭제할 수 있습니다.");
    }
}

private boolean isAdmin() { // 로그인한 사람이 관리자인지
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
}
```

**`CommentController.java` (추가)**
```java
@PostMapping("/{id}/delete") // 댓글 삭제 : POST /board/3/comments/7/delete
public String delete(@PathVariable Long postId, @PathVariable Long id) {
    commentService.delete(id);
    return "redirect:/board/" + postId + "?commentDeleted#comments";
}
```

| # | 계정 | 하는 일 | 기대 결과 |
|---|---|---|---|
| 1 | 로그인 안 함 | 글 상세 | 댓글 목록 보임, "로그인하면 댓글을 쓸 수 있습니다." |
| 2 | student1 | 댓글 쓰기 | 댓글 영역으로 이동, 내 댓글 + 삭제 버튼 |
| 3 | student1 | 빈 댓글 등록 | "댓글은 1~500자로 입력하세요." |
| 4 | student3 | student1의 댓글 | 삭제 버튼 없음 |
| 5 | admin | student1의 댓글 | **관리자 삭제** 버튼 → 삭제됨 |

### 8. 글 → 댓글 방향 - `@OneToMany` (단계 5)

- 지금까지는 **댓글 → 글** 방향(`comment.post`)만 있었다. 글에서 댓글을 보려면 리포지토리로 다시 찾아야 했다.
- `@OneToMany`를 붙이면 **글 → 댓글** 방향(`post.comments`)으로도 따라갈 수 있다. → **양방향 연관관계**

| 방향 | 필드 | 어노테이션 | 외래 키 관리 |
|---|---|---|---|
| 댓글 → 글 | `Comment.post` | `@ManyToOne` | **주인** (실제 `post_id` 컬럼) |
| 글 → 댓글 | `Post.comments` | `@OneToMany(mappedBy = "post")` | 읽기 전용 (컬럼 안 생김) |

**`Post.java` (추가, import : `ArrayList`, `List`, `EqualsAndHashCode`, `ToString`)**
```java
// 글 1개 → 댓글 여러 개 (1 : N). 글을 지우면 댓글도 함께 지운다
@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) // 외래 키는 Comment.post 가 관리
@ToString.Exclude
@EqualsAndHashCode.Exclude // 글 → 댓글 → 글 … 무한 반복 방지
private List<Comment> comments = new ArrayList<>();
```

| 부분 | 뜻 |
|---|---|
| `mappedBy = "post"` | "외래 키는 `Comment`의 `post` 필드가 관리한다" → `posts` 테이블에 컬럼이 **안 생긴다** |
| `cascade = CascadeType.REMOVE` | 글을 지우면 **댓글도 같이** 지운다 (안 하면 FK 때문에 글 삭제가 500) |
| `@ToString.Exclude` 등 | `@Data`가 만든 `toString()`이 글 → 댓글 → 글 … 을 끝없이 부르는 것(`StackOverflowError`) 방지 |

**`list.html` (댓글 수 표시 주석 해제)**
```html
<span th:if="${post.comments.size() > 0}" class="comment-count" th:text="|[${post.comments.size()}]|">[3]</span>
```
- 목록에서 제목 옆에 `[3]`처럼 댓글 수가 보인다. → `post.comments`로 바로 따라갈 수 있게 된 덕분

| # | 하는 일 | 기대 결과 |
|---|---|---|
| 1 | 댓글 2개 있는 글 → 목록 | 제목 옆 `[2]` |
| 2 | 댓글 있는 글 삭제 | 글과 댓글 모두 삭제 (500 안 남) |

### 9. 회원 삭제와 외래 키 (단계 6)

- 9주차에 남겨 둔 문제 : 글을 쓴 회원을 관리자가 삭제하면 **500 오류**
  - `posts.member_id`, `comments.member_id`가 그 회원을 가리키고 있어서 DB(FK)가 삭제를 막는다.
- 해결 방법은 여러 가지가 있다.

| 방법 | 장점 | 단점 |
|---|---|---|
| 회원 글·댓글도 같이 삭제 (cascade) | 간단 | **다른 사람의 대화까지 사라진다** |
| 작성자를 "탈퇴한 회원"으로 바꾸기 | 글이 남는다 | 처리가 복잡하다 |
| **글·댓글이 있으면 삭제 거부** | 간단하고 안전 | 관리자가 글을 먼저 정리해야 한다 |

→ 수업에서는 **삭제 전에 확인하고 거부**하는 방법을 썼다.

**`PostRepository.java`, `CommentRepository.java` (각각 추가, import : `Member`)**
```java
boolean existsByAuthor(Member author); // 이 회원이 쓴 글(댓글)이 있는지
```
- `existsBy…` : 메서드 이름만으로 **있는지 없는지(true/false)** 를 확인한다. 목록을 다 가져오지 않아서 빠르다.

**`MemberService.java` (`deleteMember()` 수정, 필드 추가)**
```java
@Autowired // 필드 추가
private PostRepository postRepository;
@Autowired
private CommentRepository commentRepository;

public void deleteMember(Long id) {
    Member member = memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    checkNotSelf(member); // [7주차 연습문제 ①] 본인 보호
    if (postRepository.existsByAuthor(member) || commentRepository.existsByAuthor(member)) { // ★ 추가
        throw new IllegalArgumentException("작성한 글이나 댓글이 있는 회원은 삭제할 수 없습니다.");
    }
    memberRepository.delete(member); // DELETE
}
```
- `IllegalArgumentException`으로 던졌기 때문에 6주차 `AdminController`의 `try-catch`가 그대로 받아서 **회원 관리 화면에 오류 메시지**로 보여 준다. (7주차 API는 `ApiExceptionHandler`가 400으로 응답)

| # | 하는 일 | 기대 결과 |
|---|---|---|
| 1 | 관리자 → 글·댓글 없는 회원 삭제 | 삭제됨 |
| 2 | 관리자 → 글 쓴 회원 삭제 | "작성한 글이나 댓글이 있는 회원은 삭제할 수 없습니다." (500 아님) |
| 3 | 그 회원의 글을 모두 지운 뒤 다시 삭제 | 삭제됨 |

### 10. 자주 나는 오류

| 증상 | 원인 → 해결 |
|---|---|
| 새 화면으로 바꿨는데 그대로 9주차 화면 | 파일을 `templates/`에 넣음 → 컨트롤러는 `board/list`를 찾으니 **`templates/board/`** 에 넣기 |
| 리포지토리에 빨간 줄 | `@Query` 메서드를 인터페이스 `}` **밖에** 붙여 넣음 → `}` 안으로 |
| `PAGE_SIZE cannot be resolved` | 단계 1 메서드를 지우면서 상수까지 지움 → 상수는 남겨 두기 |
| `comments` 테이블이 안 생김 | 서버 자동 재시작으로는 반영 안 될 때가 있음 → **완전히 끄고 다시 실행** |
| 상세에 댓글 영역이 안 보임 | 컨트롤러에서 `comments`를 안 넘김 (단계 3) |
| 목록에 `// [10주차 ...]` 글자가 보임 | HTML 안에 자바 주석(`//`)을 씀 → `<!-- -->` 로 |
| 상세 화면에서 `StackOverflowError` | `@OneToMany` 필드에 `@ToString.Exclude` 누락 |
| 댓글 있는 글 삭제 시 500 | `cascade = CascadeType.REMOVE` 누락 |
| 글 쓴 회원 삭제 시 500 | `existsByAuthor` 확인 누락 (단계 6) |

### 11. Git 연동

- `README.md`에 이번 주 진행 상황 기록 (예 : "10주차 페이징 · 검색 · 댓글 완료")
- 바뀐 파일 add → commit → GitHub로 push
- 새로 만든 파일(`Comment.java`, `CommentRepository.java`, `CommentService.java`, `CommentController.java`)은 아직 깃이 모르는 파일(`??`)이라 **꼭 add** 해야 한다.

### 🔧 하다가 막혔던 것

- 10주차 화면 파일을 `templates/` 바로 아래에 넣었더니 화면이 하나도 안 바뀌었다. → 컨트롤러가 `"board/list"`를 반환하니까 실제로 쓰이는 건 `templates/board/list.html`이었다. `board/` 폴더로 옮겨서 해결.
- `search()` 메서드를 붙여 넣다가 인터페이스를 닫는 `}` 아래에 붙여서 오류가 났다. → 메서드는 반드시 `{ }` 안에.
- 단계 2에서 `findPage(int page)`를 `findPage(String keyword, int page)`로 바꾸면서 위에 있던 `PAGE_SIZE` 상수까지 같이 지워서 컴파일 오류. → 상수를 다시 넣었다. (VS Code가 이전에 컴파일한 결과를 쓰고 있어서 `mvnw compile`은 처음에 성공으로 나왔다. `clean compile`로 해야 진짜 오류가 보인다.)
- 댓글 수 주석을 풀 때 HTML 주석 대신 `//`를 남겨 둬서 목록 화면에 글자로 찍혔다. → `<!-- -->`로 바꿨다.

### 상태

- `Comment.java`, `CommentRepository.java`, `CommentService.java`, `CommentController.java`(새로 만듦) : 코드 작성 완료
- `Post.java`(`@OneToMany comments`), `PostRepository.java`(`search`, `existsByAuthor`), `PostService.java`(`findPage`), `BoardController.java`(`list()` 페이징·검색, `detail()` 댓글), `MemberService.java`(삭제 전 확인) 수정 완료
- `templates/board/list.html`, `detail.html`, `static/css/board.css` 10주차 화면으로 교체 완료
- `mvnw clean compile` 성공 ✅
- 서버를 켜고 위 테스트 표 확인하기 ⬜

## ✏️ 실습 (10주차 연습문제 : 게시판 개선하기 — 내가 쓴 글 페이징 & 작성자로 검색)

### 문제는 이랬다

1. 내가 쓴 글 페이징 : 9주차 연습문제 "내가 쓴 글"에도 페이징 → `GET /board/my?page=0`이면 내 글 10개씩 + 페이지 번호 (힌트 : 리포지토리 `findByAuthor_Username(String username, Pageable pageable)`, 컨트롤러가 `page`와 `pageUrl`(`/board/my`)을 넘기면 페이지 링크가 바뀐다)
2. 작성자로 검색 : 검색 범위를 **제목+내용 / 작성자** 중에서 고르기 (힌트 : JPQL `p.author.name LIKE %:name%`, 컨트롤러가 `type`을 넘기면 화면에 선택 상자가 나타난다)

### 이렇게 만들었다

**① 내가 쓴 글 페이징 — `PostRepository.java` (메서드 이름만 추가)**
```java
// [10주차 연습문제 ①] 내가 쓴 글 페이징 : 마지막에 Pageable 만 더하면 Page 가 된다 (정렬은 Pageable 이 담당)
Page<Post> findByAuthor_Username(String username, Pageable pageable);
```
- 9주차 `findByAuthor_UsernameOrderByIdDesc`에서 **`OrderByIdDesc`를 빼고** 마지막에 `Pageable`을 더했다. 정렬은 `PageRequest`의 `Sort`가 맡으니까 이름에 또 쓸 필요가 없다.

| 구분 | 9주차 연습문제 | 10주차 연습문제 ① |
|---|---|---|
| 메서드 | `findByAuthor_UsernameOrderByIdDesc(username)` | `findByAuthor_Username(username, pageable)` |
| 반환 | `List<Post>` (전부) | `Page<Post>` (10개 + 페이지 정보) |
| 정렬 | 이름에 `OrderByIdDesc` | `Pageable` 안의 `Sort` |

**`PostService.java` (`findMine()` 수정 + 페이지 요청 만드는 부분 묶기)**
```java
// [10주차 연습문제 ①] 내가 쓴 글도 10개씩 (최신 글이 위로)
public Page<Post> findMine(String username, int page) {
    return postRepository.findByAuthor_Username(username, pageRequest(page));
}

private PageRequest pageRequest(int page) { // 목록·검색·내가 쓴 글이 같은 규칙 (10개씩, 최신 순)
    return PageRequest.of(Math.max(page, 0), PAGE_SIZE, Sort.by(Sort.Direction.DESC, "id"));
}
```
- `PageRequest.of(...)` 줄이 `findPage()`와 `findMine()`에 똑같이 들어가서 `pageRequest()` 하나로 묶었다. → 나중에 "20개씩"으로 바꿔도 한 곳만 고치면 된다.
- 9주차의 `List` 버전 `findMine(username)`은 더 이상 쓰는 곳이 없어서 지웠다.

**`BoardController.java` (`myList()` 수정)**
```java
@GetMapping("/my") // [9주차 연습문제 ②] 내가 쓴 글 : GET /board/my?page=0 (로그인 필요)
public String myList(@RequestParam(defaultValue = "0") int page, Principal principal, Model model) {
    Page<Post> posts = postService.findMine(principal.getName(), page); // [10주차 연습문제 ①] 10개씩
    model.addAttribute("posts", posts.getContent());
    model.addAttribute("page", posts); // 페이지 번호가 생긴다
    model.addAttribute("pageUrl", "/board/my"); // 페이지 링크를 /board 대신 /board/my 로
    model.addAttribute("listTitle", "내가 쓴 글"); // 목록 화면 재사용, 제목만 변경
    return "board/list";
}
```
- 제공 `list.html`의 페이지 링크는 `th:with="url=${pageUrl ?: '/board'}"` → `pageUrl`이 없으면 `/board`, 있으면 그 주소로 링크를 만든다. 그래서 `pageUrl`을 안 넘기면 내가 쓴 글 2페이지를 눌렀을 때 **전체 목록 2페이지**로 가 버린다.
- "전체 N개"도 `page`가 있으면 `page.totalElements`를 쓰니까 10개가 아니라 **내 글 전체 수**가 나온다.

**② 작성자로 검색 — `PostRepository.java` (추가)**
```java
// [10주차 연습문제 ②] 작성자 이름으로 검색 (글 → 작성자 → 이름, JOIN 은 JPA 가 자동으로)
@Query("SELECT p FROM Post p WHERE p.author.name LIKE %:name%")
Page<Post> searchByWriter(@Param("name") String name, Pageable pageable);
```

| 검색 범위 | JPQL 조건 | SQL 로 바꾸면 |
|---|---|---|
| 제목+내용 (단계 2) | `p.title LIKE … OR p.content LIKE …` | `posts` 테이블만 |
| 작성자 (연습 ②) | `p.author.name LIKE …` | `posts JOIN member` (자동) |

- `p.author.name` : 글 → 작성자(`@ManyToOne Member`) → 이름. JPQL에서 점(`.`)으로 따라가기만 하면 JPA가 `JOIN`을 알아서 만들어 준다.

**`PostService.java` (`findPage()`에 `type` 추가)**
```java
// [10주차] 단계 2 : 페이지 목록 + 검색 (검색어가 없으면 전체)
// [10주차 연습문제 ②] type 추가 : "writer" 면 작성자 이름, 그 밖에는 제목+내용
public Page<Post> findPage(String keyword, String type, int page) {
    PageRequest pageable = pageRequest(page);
    if (keyword == null || keyword.isBlank()) { // 앞이 true면 뒤를 실행하지 않음
        return postRepository.findAll(pageable);
    }
    if ("writer".equals(type)) { // "writer".equals(...) : type 이 null 이어도 오류 없음
        return postRepository.searchByWriter(keyword.trim(), pageable);
    }
    return postRepository.search(keyword.trim(), pageable); // 검색 결과도 10개씩
}
```
- `type.equals("writer")`라고 쓰면 `type`이 `null`일 때 `NullPointerException`이 난다. 글자를 앞에 두는 `"writer".equals(type)`이 안전하다.
- `writer`가 아니면 전부 제목+내용으로 검색한다. → 주소창에 `type=abc`처럼 이상한 값을 넣어도 오류 없이 기본 검색으로 처리된다.

**`BoardController.java` (`list()`에 `type` 추가)**
```java
@GetMapping // 목록 : GET /board?page=0&keyword=스프링&type=all (누구나)
public String list(@RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "all") String type, Model model) { // [10주차 연습문제 ②] 검색 범위
    Page<Post> posts = postService.findPage(keyword, type, page);
    model.addAttribute("posts", posts.getContent());
    model.addAttribute("page", posts);
    model.addAttribute("keyword", keyword); // 검색창에 검색어 유지, 페이지 링크에도 붙인다
    model.addAttribute("type", type); // [10주차 연습문제 ②] 넘기면 화면에 선택 상자가 나타나고, 페이지 링크에도 붙는다
    return "board/list";
}
```
- 제공 `list.html`에 이미 `<select th:if="${type != null}" name="type">`(제목+내용 / 작성자)와 페이지 링크의 `type=${type}`이 들어 있어서 화면은 고치지 않았다.
- `defaultValue = "all"` : 처음 `/board`로 들어와도 `type`이 `null`이 아니라서 선택 상자가 항상 보이고, "제목+내용"이 기본으로 선택된다.
- `SecurityConfig`는 고치지 않았다. `?type=`도 주소 뒤에 붙는 값이라 `/board` permitAll에 그대로 포함된다.

### 확인하는 방법

| # | 하는 일 | 기대 결과 |
|---|---|---|
| ① | admin 로그인 → 내가 쓴 글 | 10개씩, 링크가 `/board/my?page=1` |
| ① | 로그인 안 하고 `/board/my` | 로그인 화면 |
| ② | 작성자 선택 + "홍길" 검색 | 홍길동의 글만 |
| ② | 작성자 검색 결과 2페이지 | 주소에 `type=writer` 유지 |

### 결과

- 서버를 켜고 브라우저 대신 curl로 확인했다. (DB에 글 107개 : 관리자 106개, 한새빛 1개)

| # | 실제로 해 본 것 | 결과 |
|---|---|---|
| ② | `/board` (검색 없이) | 선택 상자가 나타나고 "제목+내용"이 선택됨, 다음 링크 `/board?page=1&keyword=&type=all` ✅ |
| ② | 작성자 + "한새" | 검색 결과 **1개**, 작성자 한새빛만, 선택 상자 "작성자" 유지 ✅ |
| ② | 작성자 + "관리" | 검색 결과 **106개**, 작성자 관리자만 ✅ |
| ② | 제목+내용 + "관리" (같은 검색어) | 검색 결과 **1개** → 작성자 검색과 결과가 다르다 = 범위가 제대로 나뉨 ✅ |
| ② | 작성자 + "홍길" | 홍길동 회원이 없어서 0개, "검색 결과가 없습니다." ✅ |
| ② | 작성자 + "관리" 2페이지 | 다음 링크 `/board?page=2&keyword=관리&type=writer` → **`type=writer` 유지** ✅ |
| ① | 로그인 안 하고 `/board/my?page=1` | 302 → `/login` ✅ |
| ① | admin 로그인 → 내가 쓴 글 | 브라우저에서 직접 확인하기 ⬜ (관리자 글이 106개라 10개씩 11페이지가 나와야 한다) |

### 🔧 하다가 막혔던 것

- 처음에는 `findPage()`와 `findMine()`에 `PageRequest.of(Math.max(page, 0), PAGE_SIZE, Sort.by(...))`를 똑같이 두 번 썼다. → `pageRequest(page)` 메서드 하나로 묶었다.
- `myList()`를 `Page`로 바꾸고 나니 `BoardController`의 `import java.util.List;`가 주석에서만 쓰여서 노란 줄(안 쓰는 import)이 생겼다. → 지웠다.
- 작성자 검색은 **아이디(`username`)가 아니라 이름(`name`)** 으로 찾는다. 목록의 작성자 칸에 보이는 게 이름(`post.author.name`)이라서, 화면에 보이는 대로 검색되는 게 자연스럽다.

### 상태

- `PostRepository.java`(`findByAuthor_Username`, `searchByWriter`), `PostService.java`(`findPage`에 `type`, `findMine` 페이징, `pageRequest`), `BoardController.java`(`list()`에 `type`, `myList()` 페이징 + `pageUrl`) : 코드 작성 완료, `mvnw clean compile` 성공 ✅
- 연습문제 ② 전부, 연습문제 ① 비로그인 확인 : 서버를 켜고 확인 완료 ✅
- 연습문제 ① admin 로그인 후 내가 쓴 글 페이지 번호 확인 ⬜

---

<a id="week11"></a>

# ⬜ 11주차 — 파일 업로드 · 신청 바구니 · 트랜잭션

- 강의: 자바웹프로그래밍(2) (강사: 최도현)
- 이번 주 주제: 게시판을 "특강·스터디 모집글"로 키우고, 포스터 업로드 → 세션 바구니 → 신청 확정을 `@Transactional`로 한 묶음 처리하기

## 📖 오늘 배운 것

### 1. 오늘의 트렌드 — 파일 업로드 · 장바구니 · 트랜잭션

- **파일 업로드** : 프로필 사진, 중고 거래 사진, 과제 제출, 포스터. 대신 파일은 글자보다 **크고 위험하다.**
  - 큰 파일 → 저장 공간·시간을 잡아먹는다
  - 악성 파일 → 해킹에 악용 → **크기 제한 · 형식 검사 · 이름 바꾸기**가 필요

| 저장 위치 | 예 | 장점 | 단점 |
|---|---|---|---|
| **서버 폴더 (오늘)** | 작은 사이트, 학교 과제 | 바로 구현 | 서버를 여러 대로 늘리면 파일이 흩어짐 |
| DB 에 통째로 (BLOB) | 작은 첨부 파일 | 글과 함께 백업 | DB 가 무거워진다 |
| 클라우드 저장소 (S3) | 인스타그램, 쇼핑몰 | 용량 걱정 없음, 빠른 전송(CDN) | 비용, 설정 |

- **장바구니** : 쇼핑몰 장바구니, 대학 수강신청 장바구니. 담을 때는 자리가 있었어도 **확정하는 순간 다시 확인**해야 한다.

| 보관 위치 | 언제 사라지나 | 다른 기기에서 | 쓰는 곳 |
|---|---|---|---|
| 쿠키 · 브라우저 저장소 | 사용자가 지울 때 | 안 보임 | 비로그인 장바구니 |
| **세션 (오늘)** | 로그아웃 · 30분 동안 요청 없을 때 | 안 보임 | 잠깐 담아 두는 바구니 |
| DB | 사용자가 뺄 때 | 보임 | 대형 쇼핑몰, 수강신청 |

- **트랜잭션** : 계좌 이체 = 출금 + 입금. 출금만 되고 입금이 실패하면 돈이 사라진다 → **둘 다 성공하거나, 둘 다 없던 일로**

| 서비스 | 한 묶음으로 처리할 작업 | 하나라도 실패하면 |
|---|---|---|
| 은행 이체 | 내 계좌 출금 + 상대 계좌 입금 | 출금도 취소 |
| 쇼핑몰 결제 | 주문 저장 + 재고 차감 + 포인트 사용 | 주문 전체 취소 |
| **우리 신청기 (오늘)** | 바구니의 모집글 3개 신청 | 3개 모두 취소 |

### 2. 오늘의 실습 순서

| 단계 | 할 일 | 나오는 문제 |
|---|---|---|
| 0 | 제공 코드 복사 | |
| 1 | 모집글 (정원, 남은 자리) | |
| 2 | 포스터 업로드 | 사진이 안 올라간다 (크기 제한) |
| 3 | 신청 바구니 (세션) | |
| 4 | 신청 확정 | **일부만 신청된다** |
| 5 | `@Transactional` 로 해결 | |

| 주차 | 신청기에 더해지는 것 |
|---|---|
| 9 · 10주차 | 게시판 CRUD, 페이징, 검색, 댓글 |
| **11주차 (오늘)** | 모집글 + 포스터 + 신청 바구니 + 신청 확정 |
| 12주차 | 남은 자리 실시간 알림 (WebSocket) |
| 13주차 | 마지막 1자리 동시 신청 (동시성, 선착순) |
| 14주차 | 확정 메일, 오래된 바구니 비우기 (스케줄링) |

### 3. 제공 코드 복사 (단계 0)

- `11주차_제공코드` 폴더에서 복사했다.
  - `model/service/FileStore.java` : 업로드 파일 저장
  - `templates/cart.html` : 신청 바구니 + 내 신청 내역
  - `templates/error/413.html` : 파일이 너무 클 때 화면
  - `static/css/board.css` : 모집 표시 · 포스터 · 바구니
  - `테스트이미지/`, `11주차_신청테스트.sql`
- `templates/board/` 화면 3개(list · detail · form)는 **단계 1 에서** 덮어쓴다. → `Post`에 정원 필드가 생긴 **다음에** 써야 하는 화면이라서, 먼저 덮어쓰면 목록이 500 오류

| 화면 | 주소 | 필요한 값 | 언제 |
|---|---|---|---|
| `board/list` · `detail` | `GET /board`, `/board/{id}` | `Post`의 `capacity`, `appliedCount`, `posterFile` | 단계 1 |
| `board/form` | `POST /board/write` | `PostForm`의 `capacity`, `poster` | 단계 1 · 2 |
| `cart.html` | `GET /cart` | `cartPosts` (담은 모집글) | 단계 3 |
| | | `enrollments` (내 신청 내역), `message` / `error` | 단계 4 |

### 4. 게시글 → 모집글 (단계 1)

- 새 테이블을 만들지 않고 **`Post`를 확장**한다. 정원이 **있으면** 모집글, **없으면(null)** 일반 글 → 목록 · 검색 · 댓글을 그대로 쓴다.
- 남은 자리는 DB에 저장하지 않고 **계산**해서 보여 준다. (남은 자리 = 정원 - 신청 인원)

| 칸 | 일반 글 | 모집글 | 타입 |
|---|---|---|---|
| title, content, author … | 있음 | 있음 | (9주차) |
| capacity 정원 | **null** | 5 | `Integer` (null 가능) |
| applied_count 신청 인원 | 0 | 0 → 신청마다 +1 | `int` (기본 0) |
| poster_file 포스터 | null | `3f84…c2.png` | `String` (파일 **이름만**) |

**`Post.java` (맨 아래, 댓글 목록 다음에 추가)**
```java
// [11주차] 단계 1 : 모집글 (정원이 있으면 모집글, null 이면 일반 글)
private Integer capacity; // 정원 (모집 인원)
@Column(nullable = false) // 신청한 인원 (신청 확정마다 1 증가)
private int appliedCount = 0;
private String posterFile; // 포스터 이미지 : uploads/ 에 저장된 파일 이름 (단계 2)

public boolean isRecruit() { return capacity != null; }                         // 모집글인가
public boolean isFull() { return isRecruit() && appliedCount >= capacity; }     // 마감인가
public int getRemaining() { return isRecruit() ? capacity - appliedCount : 0; } // 남은 자리
```
- `int`가 아니라 **`Integer`** 를 쓴 이유 : `int`는 null이 될 수 없어서 "일반 글(정원 없음)"을 표현할 수 없다.
- `isFull()`에서 `isRecruit()`를 **먼저** 확인한다. → `capacity`가 null인 일반 글에서 비교하면 `NullPointerException`

| 화면에 쓰는 이름 | 호출되는 메서드 | 필드 · DB 칸 | 값을 만드는 방법 |
|---|---|---|---|
| `${post.recruit}` | `isRecruit()` | 없음 | `capacity != null` |
| `${post.full}` | `isFull()` | 없음 | `appliedCount >= capacity` |
| `${post.remaining}` | `getRemaining()` | 없음 | `capacity - appliedCount` |

→ Thymeleaf(SpEL)는 `get`이나 `is`를 붙인 메서드를 찾아서 호출한다. 그래서 **필드가 없어도** 화면에서 `post.remaining`처럼 쓸 수 있다.

- 제공 `templates/board/` 화면 3개를 덮어쓰고 재시작하면 터미널에 칸 추가 로그가 찍힌다.
```
alter table posts add column applied_count integer not null
alter table posts add column capacity integer
alter table posts add column poster_file varchar(255)
```
- 기존 글은 `capacity = null`(일반 글), `applied_count = 0` → MySQL이 숫자 칸의 빈 값을 0으로 채운다.
- 참고 : `ddl-auto=update`는 개발용이다. 운영 서버에서는 켜지 않는다.

**`PostForm.java` (추가)**
```java
// [11주차] 단계 1 : 모집 인원 (input name="capacity", 비우면 null = 일반 글)
private Integer capacity;
```

**`PostService.java` (두 곳에 추가)**
```java
// ① write() 안 : 작성 시간 다음 줄
post.setCapacity(form.getCapacity()); // [11주차] 단계 1 : 정원 (비우면 null = 일반 글)

// ② validate() 맨 위
// [11주차] 단계 1 : 모집 인원은 1~100명
if (form.getCapacity() != null && (form.getCapacity() < 1 || form.getCapacity() > 100)) {
    throw new IllegalArgumentException("모집 인원은 1~100명으로 입력하세요.");
}
```

| # | 계정 | 하는 일 | 기대 결과 |
|---|---|---|---|
| 1 | admin | 모집 인원 5 로 글쓰기 | 정원 5 · 신청 0 · 남은 자리 5 |
| 2 | - | 게시판 목록 | 제목 앞 `모집 0/5` |
| 3 | admin | 모집 인원을 비우고 글쓰기 | 일반 글 |
| 4 | admin | 모집 인원 0 또는 101 | "모집 인원은 1~100명으로 입력하세요." |
| 5 | - | 검색 · 페이지 · 댓글 | 그대로 동작 |

### 5. 파일 업로드 — `MultipartFile` (단계 2)

- 지금까지 form은 **글자만** 보냈다. 파일을 보내려면 form에 `enctype="multipart/form-data"` → 글자와 파일을 **조각(part)** 으로 나눠 함께 보낸다. (제공 `form.html`에 이미 있다. 없으면 파일 **이름만** 간다)
- 파일 조각은 **`MultipartFile`** 로 받는다. `PostForm`에 `MultipartFile poster`를 두면 `name="poster"`와 **이름으로 자동 연결**된다.

| 메서드 | 뜻 | 예 |
|---|---|---|
| `isEmpty()` | 파일을 안 골랐나 | 포스터 없는 글 → true |
| `getOriginalFilename()` | 사용자 PC 의 파일 이름 | **믿으면 안 됨** |
| `getContentType()` | 브라우저가 붙인 종류 | image/png (**바꿀 수 있음**) |
| `getSize()` · `getInputStream()` | 크기 · 파일 내용 | 29862 바이트 |

**`PostForm.java` (추가, import : `MultipartFile`)**
```java
// [11주차] 단계 2 : 포스터 이미지 (input type="file" name="poster")
private MultipartFile poster;
```

**`PostService.java` (두 곳에 추가)**
```java
// ① 필드 (memberRepository 아래)
@Autowired // [11주차] 단계 2 : 업로드 파일 저장소 (제공 코드)
private FileStore fileStore;

// ② write() 안 : 정원 다음 줄
post.setPosterFile(fileStore.save(form.getPoster())); // [11주차] 단계 2 : 포스터 저장 → 파일 이름 (없으면 null)
```

### 6. 제공 코드 `FileStore` — 업로드 보안 4원칙

```java
String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
if (ext == null || !ALLOWED.contains(ext.toLowerCase()))         // ① 허용한 확장자만
    throw new IllegalArgumentException("이미지 파일(jpg, png, gif, webp)만 ...");
if (!isImage(in.readNBytes(12)))                                 // ② 파일 앞부분으로 진짜 이미지인지
    throw new IllegalArgumentException("이미지 파일이 아닙니다.");
String storedName = UUID.randomUUID() + "." + ext.toLowerCase(); // ③ 이름은 서버가 새로
Files.copy(in, root.resolve(storedName));                        // ④ uploads/ 에 저장
return storedName;                                               // → DB 에는 이름만
```

| 원칙 (OWASP 권고) | 지키지 않으면? | FileStore |
|---|---|---|
| ① 확장자 **허용 목록** | `.jsp` · `.html`을 올려 서버 · 브라우저에서 실행 | jpg · png · gif · webp 만 |
| ② **내용** 확인 | 이름만 `.png`인 스크립트 (확장자 · Content-Type은 바꿀 수 있다) | 첫 바이트 (PNG = `89 50 4E 47`) |
| ③ 이름은 **서버가** | 같은 이름 덮어쓰기, `../../`로 다른 폴더에 쓰기 | UUID (예 : `3f84…c2.png`) |
| ④ **별도 폴더** | 웹 폴더(static)에 두면 바로 노출 · 실행 위험 | `프로젝트/uploads/` |

### 7. 포스터 보여 주기 — `WebConfig` (단계 2)

**`WebConfig.java` (새로 만듦, `config`)**
```java
package com.example.demo.config;

@Configuration // [11주차] 단계 2 : 업로드한 파일을 주소로 보여 주는 설정
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")      // 주소 : /uploads/파일이름
                .addResourceLocations("file:uploads/"); // 실제 위치 상대 경로 : 프로젝트 폴더/uploads/
    }
}
```

**`SecurityConfig.java` (게시판 줄 아래에 추가)**
```java
.requestMatchers("/uploads/**").permitAll() // [11주차] 포스터 이미지 : 누구나 (올리기는 글쓰기라서 로그인)
```
- 이게 없으면 로그아웃한 사람에게는 포스터가 엑스박스로 보인다.

| 구분 | static 폴더 | uploads/ (오늘) |
|---|---|---|
| 실행 중에 저장하면 | 재시작 전엔 **안 보일 수 있음** | 바로 보인다 |
| 배포(jar)하면 | jar 안이라 **쓸 수 없다** | 서버 폴더에 그대로 |
| GitHub | 사용자 사진이 소스와 섞임 | `.gitignore`로 제외 |

| # | 하는 일 (제공 `테스트이미지/`) | 기대 결과 |
|---|---|---|
| 1 | 모집글 + 포스터_스프링스터디.png | 상세에 포스터, `uploads/`에 UUID 이름 파일 |
| 2 | 로그아웃 후 같은 상세 | 포스터가 보인다 |
| 3 | 가짜이미지.png | "이미지 파일이 아닙니다." |
| 4 | 😱 휴대폰 사진 (큰사진_3MB.jpg) | **413 파일이 너무 큽니다** |

### 8. 업로드 크기 설정 (단계 2)

- 스프링의 기본 업로드 크기는 **파일 1MB**라서 휴대폰 사진(3MB)이 413으로 막혔다.

**`application.properties` (맨 아래에 추가)**
```properties
# [11주차] 단계 2 : 파일 업로드 크기 (기본값 : 파일 1MB, 요청 전체 10MB)
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=10MB
```

| 설정 | 뜻 | 기본값 | 오늘 |
|---|---|---|---|
| `max-file-size` | 파일 **한 개**의 최대 크기 | 1MB | **5MB** |
| `max-request-size` | 요청 **전체** (글자 + 파일) | 10MB | 10MB |

- 재시작 → 3MB 사진 성공, 5MB 넘는 파일(큰사진_7MB.jpg)은 여전히 413
- ⚠️ 크기 제한을 `-1`(무제한)로 하면 안 된다. 수 GB 파일로 디스크 · 메모리를 채우는 **DoS 공격**에 노출된다.

| 크기 제한은 겹으로 | 무엇으로 | 넘으면 |
|---|---|---|
| ① 브라우저 | `form.html` 자바스크립트 (`file.size`) | 알림 — **우회 가능**, 안내용 |
| ② 스프링 : 파일 1개 | `max-file-size=5MB` | 413 (7MB 사진) |
| ③ 스프링 : 요청 전체 | `max-request-size=10MB` | 413 (합계 10MB 초과) |

- 업로드 요청을 실제로 보면 `boundary`(구분선) 사이에 조각이 하나씩 들어 있다. (`_csrf` → `title` → `content` → `capacity` → `poster` 파일 조각) `filename`과 `Content-Type`은 **보내는 쪽이 적는 글자**라서, `FileStore`가 파일 내용(`89 50 4E 47`)을 직접 확인하는 것이다.

### 9. 신청 바구니 — 세션 (단계 3)

- **세션** (5주차 로그인에서 이미 쓰고 있다)
  - 서버 메모리에 **사용자마다 보관함 1개**, 브라우저는 번호표(`JSESSIONID`)만 가진다.
  - `session.setAttribute("cart", …)` / `session.getAttribute("cart")`
- 바구니에는 **모집글 번호만** 담는다. 남은 자리는 볼 때마다 DB에서 새로 가져온다.

| 데이터 | 저장 위치 | 이유 |
|---|---|---|
| 로그인 정보 | 세션 (5주차) | 로그인한 동안만 |
| **신청 바구니** (오늘) | **세션** | 확정 전 임시 목록 |
| **신청 내역** (오늘) | **DB** (enrollments) | 오래 남고, 정원 계산에 쓴다 |
| 남은 자리 | DB 에서 계산 | 다른 사람 신청으로 계속 바뀐다 |

**`PostService.java` (`findById()` 위에 추가, import : `Collection`)**
```java
// [11주차] 단계 3 : 바구니에 담긴 글들 (번호 목록으로 한 번에)
public List<Post> findAllById(Collection<Long> ids) {
    return postRepository.findAllById(ids); // SELECT ... WHERE id IN (?, ?, ?)
}
```

| 코드 | 뜻 | 9 · 10주차와 비교 |
|---|---|---|
| `findAllById(ids)` | `JpaRepository`에 **이미 있다** → 감싸기만 | `findById(id)`는 1개, 이것은 여러 개 |
| `Collection<Long>` | Set · List 무엇이든 받는다 | 바구니는 Set |
| `HttpSession session` | 컨트롤러 매개변수에 쓰면 **지금 사용자의 세션** | `Principal`(로그인 사용자)과 같은 방식 |

**`CartController.java` (새로 만듦, `controller`)**
```java
@Controller // [11주차] 단계 3 : 신청 바구니 (세션에 모집글 번호를 담아 둔다)
@RequestMapping("/cart")
public class CartController {
    @Autowired
    PostService postService;

    @GetMapping // 바구니 화면 : GET /cart (로그인 필요)
    public String cart(HttpSession session, Model model) {
        model.addAttribute("cartPosts", postService.findAllById(getCart(session))); // 담은 모집글
        return "cart"; // templates/cart.html
    }

    @PostMapping("/{postId}") // 담기 : POST /cart/3
    public String add(@PathVariable Long postId, HttpSession session) {
        Post post = postService.findById(postId); // 없는 글이면 404
        if (!post.isRecruit()) {
            return "redirect:/board/" + postId; // 일반 글은 담지 않는다
        }
        getCart(session).add(postId); // Set : 같은 글을 두 번 담아도 1개
        return "redirect:/cart";
    }

    @PostMapping("/{postId}/remove") // 빼기 : POST /cart/3/remove
    public String remove(@PathVariable Long postId, HttpSession session) {
        getCart(session).remove(postId);
        return "redirect:/cart";
    }

    // 세션에서 바구니 꺼내기 (처음이면 빈 바구니를 만들어 넣는다)
    @SuppressWarnings("unchecked")
    private Set<Long> getCart(HttpSession session) {
        Set<Long> cart = (Set<Long>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashSet<>(); // 담은 순서를 지키는 Set
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
```
- 담기 · 빼기는 상태를 바꾸는 요청이라 GET이 아니라 **POST** (CSRF 보호 대상), 끝나면 `redirect:/cart` (PRG, 10주차)
- `SecurityConfig`는 고칠 필요 없다. `/cart/**`는 `anyRequest().authenticated()` → **로그인 필요**

| 코드 | 뜻 |
|---|---|
| `session.getAttribute("cart")` | 보관함에서 "cart" 꺼내기 → `Object`로 나온다 |
| `(Set<Long>)` + `@SuppressWarnings` | Set 으로 바꾼다 (경고 숨기기) |
| `session.setAttribute("cart", cart)` | 처음이면 빈 바구니를 넣어 둔다 |
| `LinkedHashSet` | 중복 없음 + **담은 순서 기억** (단계 4 에서 중요) |

- `getCart(session).add(...)`처럼 Set을 고치기만 해도 세션에 반영되는 이유 : 서버 메모리의 세션이 **같은 객체를 가리키기** 때문

| # | 계정 | 하는 일 | 기대 결과 |
|---|---|---|---|
| 1 | student1 | 모집글 상세 → 바구니에 담기 | 바구니 화면에 1개 |
| 2 | student1 | 2개 더 담기, 같은 글 한 번 더 | 3개 (중복 없음), 목록 배지 3 |
| 3 | student1 | 빼기 | 2개 |
| 4 | student3 (시크릿 창) | 바구니 열기 | **빈 바구니 (세션 다름)** |
| 5 | student1 | 로그아웃 → 로그인 → 바구니 | 빈 바구니 |
| 6 | 로그인 안 함 | `/cart` | 로그인 화면 |

- 이 단계에서 **모두 신청 확정** 버튼은 400 → `/cart/confirm`의 `confirm`을 `/{postId}`가 **번호로 읽으려다** 실패 (단계 4에서 해결)

| 세션 vs DB 바구니 | 세션 바구니 (오늘) | DB 바구니 |
|---|---|---|
| 보관 | 서버 **메모리** | 테이블 (cart_items) |
| 사라지는 때 | 로그아웃, **30분** 동안 요청 없음, 서버 재시작 | 사용자가 뺄 때 |
| 휴대폰 ↔ PC | 따로 | 같은 바구니 |
| 서버 여러 대 | 서버마다 세션이 달라 **공유 장치 필요** (Redis 세션 등) | 문제없음 |
| 어울리는 곳 | 짧게 쓰는 임시 목록 | 쇼핑몰 장바구니, 수강신청 |

### 10. 신청 확정의 흐름 (단계 4)

| 순서 | 모집글 하나마다 할 일 | 실패하면 |
|---|---|---|
| ① 검사 | 이미 신청? 마감? **SELECT** | "이미 신청한 …" · "정원이 마감 …" |
| ② 저장 | 신청 내역 **INSERT** | - |
| ③ 차감 | 신청 인원 + 1 **UPDATE** | - |

- 바구니에 3개(A, B, C)면 → 테이블 2개(enrollments, posts)에 **쓰기 6번**. 이 6번이 **한 묶음**이어야 한다.

| 테이블 | 무엇 | 외래 키 | 주차 |
|---|---|---|---|
| posts | 글 · 모집글 | member_id | 9주차 |
| comments | 댓글 | post_id, member_id | 10주차 |
| **enrollments** | **신청 내역** | **post_id, member_id** | 11주차 |

**`Enrollment.java` (새로 만듦, `model/domain`)**
```java
@Entity // [11주차] 단계 4 : 신청 내역 (회원 1명이 모집글 1개에 신청) : enrollments 테이블
@Table(name = "enrollments")
@Data
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 신청한 회원 (N : 1)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) // 신청한 모집글 (N : 1)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false) // 신청 시간
    private LocalDateTime createdAt;
}
```
- 10주차 `Comment`와 같은 모양 : `@ManyToOne` 두 개 (한 학생이 여러 모집글에 신청할 수 있다)

**`EnrollmentRepository.java` (새로 만듦, `model/repository`)**
```java
@Repository // [11주차] 단계 4 : 신청 내역 리포지토리
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // 이미 신청했는지 : SELECT ... WHERE member_id = ? AND post_id = ?
    boolean existsByMemberAndPost(Member member, Post post);

    // 내 신청 내역 (최근 신청이 위로) : member → username 으로 찾기
    List<Enrollment> findByMember_UsernameOrderByIdDesc(String username);
}
```

| 메서드 | 만들어지는 SQL | 비슷한 것 |
|---|---|---|
| `existsByMemberAndPost` | `... WHERE member_id=? AND post_id=?` (있으면 true) | 10주차 `existsByAuthor` |
| `findByMember_UsernameOrderByIdDesc` | `... JOIN member ... WHERE username=? ORDER BY id DESC` | 10주차 `findByAuthor_Username` |

**`EnrollmentService.java` (새로 만듦, `model/service`)**
```java
@Service // [11주차] 단계 4 : 신청 서비스
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;

    // 내 신청 내역
    public List<Enrollment> findMine(String username) {
        return enrollmentRepository.findByMember_UsernameOrderByIdDesc(username);
    }

    // 단계 4 : 바구니의 모집글을 한 번에 신청 확정
    public int confirm(String username, Collection<Long> postIds) {
        if (postIds.isEmpty()) {
            throw new IllegalStateException("바구니가 비어 있습니다.");
        }
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("회원이 존재하지 않습니다."));
        for (Long postId : postIds) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalStateException("삭제된 모집글이 바구니에 있습니다."));
            if (enrollmentRepository.existsByMemberAndPost(member, post)) {
                throw new IllegalStateException("이미 신청한 모집글입니다 : " + post.getTitle());
            }
            if (post.isFull()) { // 담을 때는 자리가 있었지만, 그사이 마감됐을 수 있다
                throw new IllegalStateException("정원이 마감되었습니다 : " + post.getTitle());
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setMember(member);
            enrollment.setPost(post);
            enrollment.setCreatedAt(LocalDateTime.now());
            enrollmentRepository.save(enrollment); // ① INSERT 신청 내역

            post.setAppliedCount(post.getAppliedCount() + 1);
            postRepository.save(post); // ② UPDATE 신청 인원 + 1 (남은 자리 - 1)
        }
        return postIds.size();
    }
}
```
- 리포지토리 **3개**를 함께 쓴다. → 여러 테이블을 한 번에 다루는 일은 **서비스**가 맡는다.

**`CartController.java` (세 곳 수정, import : `Principal`, `RedirectAttributes`, `EnrollmentService`)**
```java
// ① 필드 (postService 아래)
@Autowired
EnrollmentService enrollmentService; // 단계 4

// ② cart() : Principal 추가 + 내 신청 내역
@GetMapping // 바구니 화면 : GET /cart (로그인 필요)
public String cart(HttpSession session, Principal principal, Model model) {
    model.addAttribute("cartPosts", postService.findAllById(getCart(session))); // 담은 모집글
    model.addAttribute("enrollments", enrollmentService.findMine(principal.getName())); // 단계 4 : 내 신청 내역
    return "cart"; // templates/cart.html
}

// ③ 맨 아래에 추가
@PostMapping("/confirm") // 단계 4 : 신청 확정 : POST /cart/confirm
public String confirm(HttpSession session, Principal principal, RedirectAttributes redirect) {
    Set<Long> cart = getCart(session);
    try {
        int count = enrollmentService.confirm(principal.getName(), cart);
        cart.clear(); // 성공하면 바구니를 비운다
        redirect.addFlashAttribute("message", count + "개 모집글 신청이 확정되었습니다.");
    } catch (IllegalStateException e) {
        redirect.addFlashAttribute("error", e.getMessage()); // 실패 이유 (바구니는 그대로)
    }
    return "redirect:/cart";
}
```

| redirect 후 값 전달 | 쓴 곳 | 특징 |
|---|---|---|
| 주소 파라미터 `?commentError` | 10주차 댓글 | 주소에 보인다, 짧은 표시용 |
| **flash** `addFlashAttribute` | 오늘 | 다음 화면에 **딱 한 번**, 새로고침하면 사라짐 |

### 11. 😱 일부만 신청된다 (단계 4 테스트)

- 재시작 → `11주차_신청테스트.sql` 실행 (A · B 정원 5, **C 정원 1**)

| # | 계정 | 하는 일 | 기대 결과 |
|---|---|---|---|
| 1 | student1 | A 담기 → 모두 신청 확정 | "1개 … 확정", A 1/5 |
| 2 | student1 | A 다시 담기 → 확정 | "이미 신청 : A" |
| 3 | student1 | A 빼기 → **B, C 순서로** 담기 | 바구니 2개 |
| 4 | student3 (시크릿) | C 담기 → 확정 | C **마감** (1/1) |
| 5 | student1 | 모두 신청 확정 | "정원이 마감되었습니다 : C" |
| 6 | student1 | 내 신청 내역 · B 상세 | 😱 **B 는 신청됨** (1/5) |

- 실패라고 했는데 B는 신청됐다. → 다시 확정하면 "이미 신청 : B" → **영영 확정할 수 없다**
- 왜? 코드는 **A 검사 → A 저장 → B 검사 → B 저장 → C 검사** 순서라서, C 검사에서 멈추면 앞의 저장은 이미 끝나 있다.
- 리포지토리의 `save()`는 **혼자서** 트랜잭션을 시작하고 **바로 커밋**한다. (스프링 데이터 JPA의 CRUD 메서드는 각자 `@Transactional`)

```
Creating new transaction with name [...SimpleJpaRepository.save]   ← ① B 신청 저장
insert into enrollments (created_at,member_id,post_id) values (?,?,?)
Committing JPA transaction on EntityManager                        ← 확정! (되돌릴 수 없음)
Creating new transaction with name [...SimpleJpaRepository.save]   ← ② B 인원 + 1
Committing JPA transaction on EntityManager                        ← 확정!
update posts set applied_count=?, ... where id=?
Creating new transaction with name [...SimpleJpaRepository.findById] ← ③ C 조회 → 마감 → 예외
```
(로그 설정 : `logging.level.org.springframework.orm.jpa.JpaTransactionManager=DEBUG`)

### 12. 트랜잭션과 `@Transactional` (단계 5)

- **트랜잭션** = 여러 작업을 **하나처럼** 묶는다.
  - 끝까지 성공 → **커밋** (모두 확정)
  - 중간에 실패 → **롤백** (모두 없던 일)
- 스프링에서는 **`@Transactional` 한 줄** : 메서드 시작 = 트랜잭션 시작, 정상 끝 = 커밋, 예외 = 롤백

| ACID | 뜻 | 신청 확정에서 |
|---|---|---|
| **A**tomicity 원자성 | 모두 되거나, 하나도 안 되거나 | B · C 모두 or 모두 취소 (**오늘**) |
| **C**onsistency 일관성 | 규칙이 깨지지 않는다 | 신청 내역 수 = 신청 인원 |
| **I**solation 격리성 | 동시에 해도 끼어들지 않는다 | 마지막 자리 동시 신청 → **13주차** |
| **D**urability 지속성 | 커밋한 것은 사라지지 않는다 | 서버가 꺼져도 남는다 |

**`EnrollmentService.java` (`confirm()` 위에 한 줄 추가, import : `Transactional`)**
```java
// 단계 4 : 바구니의 모집글을 한 번에 신청 확정
@Transactional // 단계 5 : 하나라도 실패하면 모두 취소 (롤백)
public int confirm(String username, Collection<Long> postIds) {
```
- `import org.springframework.transaction.annotation.Transactional;` (`jakarta.transaction.Transactional`이 아니라 **스프링 것**)
- 신청테스트.sql ② 초기화 → 재시작 → 위 표 3~5번 다시

| 같은 시나리오 : B, C 확정 중 C 마감 | 단계 4 (없음) | 단계 5 (`@Transactional`) |
|---|---|---|
| 화면 안내 | "정원 마감 : C" | 같음 |
| B 신청 내역 | 😱 **저장됨** | **없음** |
| B 신청 인원 | 😱 1 / 5 | **0 / 5** |
| 바구니 | B, C | B, C (C 빼고 다시 확정 → 성공) |

```
Creating new transaction with name [...EnrollmentService.confirm]
Participating in existing transaction   ← findById(B) : 바깥 트랜잭션에 "참여"
Participating in existing transaction   ← save(B 신청)
insert into enrollments (...) values (?,?,?)   ← 나가지만 아직 확정 아님
Participating in existing transaction   ← save(B 인원) : UPDATE 는 아직 안 나감
Participating in existing transaction   ← findById(C)
Initiating transaction rollback         ← C 마감 예외 → 롤백
Rolling back JPA transaction on EntityManager  ← B 의 INSERT 도 취소
```

| 로그 | 단계 4 | 단계 5 |
|---|---|---|
| 트랜잭션 이름 | `SimpleJpaRepository.save` (여러 번) | `EnrollmentService.confirm` (**1번**) |
| `save()` 할 때 | Creating new … → **Committing** | **Participating** in existing |
| 예외가 나면 | 이미 커밋됨 | **Rolling back** |
| UPDATE posts | `save()` 즉시 | 커밋 직전에 (롤백이면 **안 나감**) |

### 13. 프록시와 `@Transactional` 함정

- 스프링은 `EnrollmentService` 앞에 **대리 객체(프록시)** 를 세운다. (10주차 LAZY의 프록시와 같은 생각)
  - `CartController` → **프록시** (① 트랜잭션 시작) → 진짜 `EnrollmentService.confirm()` 실행 → **프록시** (② 정상이면 커밋, 예외면 롤백)

| 함정 | 예 | 결과 | 해결 |
|---|---|---|---|
| ① **체크 예외** | `throw new IOException()` | **커밋된다** (기본은 `RuntimeException` · `Error`만 롤백) | `RuntimeException` 또는 `rollbackFor` |
| ② 예외를 **삼킨다** | `confirm()` 안에서 try-catch 후 무시 | 예외가 프록시까지 안 가서 **커밋** | 서비스 밖(컨트롤러)에서 잡는다 |
| ③ **같은 클래스 안** 호출 | `this.confirm()` | 프록시를 안 거쳐 **트랜잭션 없음** | 다른 빈에서 호출 |
| ④ **파일은 롤백 안 됨** | DB 실패 후 이미 저장한 포스터 | `uploads`에 **쓰레기 파일** | DB 성공 후 저장 · 실패 시 삭제 |

- 우리 코드는 `IllegalStateException`(`RuntimeException`)을 던지고, try-catch는 **컨트롤러**에 있어서 ①② 둘 다 해당하지 않는다.

### 14. 변경 감지 (Dirty Checking)

- 트랜잭션 **안에서** 꺼낸 엔티티는 JPA가 지켜본다. → 값을 바꾸기만 해도 커밋할 때 **UPDATE가 자동으로** 나간다.

```java
post.setAppliedCount(post.getAppliedCount() + 1);
postRepository.save(post); // ← @Transactional 안에서는 없어도 UPDATE 가 나간다
```

| 구분 | 트랜잭션 밖 (단계 4) | 트랜잭션 안 (단계 5) |
|---|---|---|
| `findById()`로 꺼낸 Post | 꺼내자마자 관리 끝 | 커밋까지 **관리** |
| `setAppliedCount()`만 | DB 그대로 | 커밋 때 **UPDATE** |
| `save()`를 쓰면 | 그때 바로 UPDATE (커밋) | 이미 관리 중 → 커밋 때 한 번 |

- 오늘은 읽기 쉽게 `save()`를 남겨 두었다. (연습문제 ① 신청 취소에서 빼 본다, 9주차 조회수 `read()`도 같은 원리로 줄일 수 있다)

### 15. 자주 나는 오류

- 전체 흐름 다시 확인 : 모집글 + 포스터 → 바구니 담기 · 빼기 → 확정 성공 → 정원 마감 롤백

| 증상 | 원인 → 해결 |
|---|---|
| 목록 500 : `Property or field 'recruit' cannot…` | `Post`를 고치기 전에 board 화면을 덮어썼다 (단계 1 순서) |
| 모집 인원을 넣어도 일반 글 | `PostForm`에 `capacity` 없음 또는 `setCapacity` 누락 |
| `form.getPoster()` 빨간 줄 (`cannot find symbol`) | `PostForm`에 `MultipartFile poster` 필드 누락 |
| 포스터를 골랐는데 저장 안 됨 | form에 `enctype="multipart/form-data"` 누락 (제공 화면 확인) |
| 포스터가 엑스박스 | `WebConfig` 누락(또는 `package` 줄 누락), 또는 시큐리티 `/uploads/**` 누락 |
| 3MB 사진이 413 | `max-file-size` 설정 누락 (기본 1MB) |
| 바구니 담기 404 | `@RequestMapping("/cart")` · `@PostMapping("/{postId}")` 확인 |
| 모두 신청 확정이 400 | `@PostMapping("/confirm")` 누락 → `/{postId}`가 "confirm"을 번호로 읽다 실패 |
| 실패했는데 일부만 신청됨 | `@Transactional` 누락 (단계 5) → 신청테스트.sql ② 초기화 후 다시 |

### 16. Git 연동

- `README.md`에 이번 주 진행 상황 기록 (예 : "11주차 파일 업로드 + 신청 바구니 + 트랜잭션 완료")
- 추가된 파일 add → commit → GitHub로 push
- 팁 : `.gitignore`에 `uploads/` 추가 → **올린 사진은 깃에 올리지 않는다.**

```gitignore
### [11주차] 업로드한 사진은 깃에 올리지 않는다 ###
uploads/
```

### 🔧 하다가 막혔던 것

- PDF의 `WebConfig.java` 코드를 복사하면서 맨 위의 `package com.example.demo.config;`가 빠져서 빨간 줄이 생겼다. → `package` 줄을 추가. (이 줄이 없으면 스프링이 설정 파일을 찾지 못해서 포스터 주소도 동작하지 않는다)
- `PostService`에 `form.getPoster()`를 먼저 넣고 `PostForm`에 `poster` 필드를 안 넣어서 `cannot find symbol` 컴파일 오류. → `PostForm`에 `private MultipartFile poster;`와 import 추가.
- 서버를 새로 켜려고 했더니 `Port 8080 was already in use` → VS Code에서 켜 둔 서버가 이미 돌고 있었다. 코드를 바꾼 뒤에는 **켜 둔 서버를 끄고 다시 실행**해야 새 코드(`enrollments` 테이블 등)가 반영된다.
- 수업 자료는 단계 4에서 `@Transactional` 없이 "일부만 신청되는" 문제를 먼저 보고 단계 5에서 추가하는데, 코드는 처음부터 `@Transactional`을 넣어서 작성했다. → 문제를 직접 보려면 그 줄을 잠깐 주석 처리하고 위 11번 표대로 해 보면 된다.

### 상태

- `Post.java`(정원 · 신청 인원 · 포스터 + `isRecruit`/`isFull`/`getRemaining`), `PostForm.java`(`capacity`, `poster`), `PostService.java`(`write`에 정원·포스터, `validate`에 1~100명, `findAllById`) 수정 완료
- `WebConfig.java`, `CartController.java`(담기 · 빼기 · 바구니 · 확정), `Enrollment.java`, `EnrollmentRepository.java`, `EnrollmentService.java`(`confirm` + `@Transactional`) 새로 만듦
- `SecurityConfig.java`(`/uploads/**` 허용), `application.properties`(업로드 5MB / 10MB), `.gitignore`(`uploads/`) 반영 완료
- 제공 코드(`FileStore.java`, `templates/board/` 3개, `cart.html`, `error/413.html`, `board.css`) 복사 완료
- `mvnw clean compile` 성공 ✅
- 서버를 다시 켜고 `11주차_신청테스트.sql` 실행 후 위 테스트 표 확인하기 ⬜

## ✏️ 실습 (11주차 연습문제 : 신청기 개선하기 — 신청 취소 & 신청자가 있는 모집글 삭제 정리)

### 문제는 이랬다

1. 신청 취소 : 내 신청 내역의 **취소** 버튼 → 신청 내역 삭제 + 신청 인원 - 1 (`POST /cart/enrollments/{id}/cancel`, 제공 `cart.html`의 연습 ① 주석을 풀면 버튼이 보인다)
   - 조건 : 본인 신청만 취소 (남의 것은 403), 두 작업을 **한 트랜잭션**으로, `save()` 없이 **변경 감지**로 인원 줄이기 (힌트 : `enrollment.getPost()` → `setAppliedCount(... - 1)`)
2. 삭제 정리 : 신청자가 있는 모집글을 삭제하면 **500** (10주차 "댓글 달린 글 삭제"와 같은 외래 키 문제)
   - 신청자가 있는 모집글 삭제 → 막기 (`?deleteError`), 신청 내역이 있는 회원 삭제 → 막기, 글을 지우면 포스터 파일도 삭제 (`fileStore.delete`)

### 이렇게 만들었다

**① 신청 취소 — `EnrollmentService.java` (`cancel()` 추가, import : `HttpStatus`, `AccessDeniedException`, `ResponseStatusException`)**
```java
// [11주차 연습문제 ①] 신청 취소 : 신청 내역 삭제 + 신청 인원 - 1 (한 트랜잭션)
@Transactional // 둘 중 하나라도 실패하면 모두 취소
public String cancel(Long enrollmentId, String username) {
    Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "신청 내역이 존재하지 않습니다."));
    if (!enrollment.getMember().getUsername().equals(username)) { // 본인 신청만 → 아니면 403
        throw new AccessDeniedException("본인의 신청만 취소할 수 있습니다.");
    }
    Post post = enrollment.getPost();
    post.setAppliedCount(post.getAppliedCount() - 1); // save() 없음 → 변경 감지로 커밋 때 UPDATE
    enrollmentRepository.delete(enrollment); // DELETE 신청 내역
    return post.getTitle();
}
```
- `@Transactional` 안이라서 `findById()`로 꺼낸 신청 → 모집글(`getPost()`, LAZY)을 JPA가 커밋까지 지켜본다. → `setAppliedCount()`만 해도 커밋할 때 `UPDATE posts`가 나간다. (오늘 14번 "변경 감지")
- 본인 확인은 9주차 `checkWriter()`와 같은 방식 : `AccessDeniedException`을 던지면 스프링 시큐리티가 **403**으로 바꿔 준다. 없는 번호는 `ResponseStatusException(NOT_FOUND)` → **404**.
- 두 예외 모두 `RuntimeException`이라서 던지는 순간 롤백된다. (함정 ① 체크 예외에 해당하지 않음)

| 작업 | 신청 확정 `confirm()` | 신청 취소 `cancel()` |
|---|---|---|
| 신청 내역 | `save()` → INSERT | `delete()` → DELETE |
| 신청 인원 | `+ 1` 후 `postRepository.save(post)` | `- 1` 만 (**`save()` 없음**, 변경 감지) |
| 묶음 | `@Transactional` | `@Transactional` |

**`CartController.java` (맨 아래에 추가)**
```java
@PostMapping("/enrollments/{id}/cancel") // [11주차 연습문제 ①] 신청 취소 : POST /cart/enrollments/7/cancel
public String cancel(@PathVariable Long id, Principal principal, RedirectAttributes redirect) {
    enrollmentService.cancel(id, principal.getName()); // 남의 신청이면 403, 없는 번호면 404
    redirect.addFlashAttribute("message", "신청이 취소되었습니다.");
    return "redirect:/cart";
}
```
- `confirm()`과 달리 try-catch를 두지 않았다. 403 · 404를 **그대로** 응답해야 해서, 잡아 버리면 "남의 것은 403" 조건을 못 지킨다.
- 주소가 `/cart/enrollments/7/cancel`로 세 칸이라 `/{postId}`(한 칸) · `/{postId}/remove`(두 칸)와 헷갈리지 않는다. (단계 3의 `/confirm` 400 문제와 비교)

**`cart.html`** : `[11주차 연습문제 1]` 주석(`<!--/* ... */-->`)을 풀어서 내 신청 내역 줄마다 **취소** 버튼이 보이게 했다.

**② 삭제 정리 — `EnrollmentRepository.java` (추가)**
```java
// [11주차 연습문제 ②] 이 모집글에 신청한 사람이 있는지 / 이 회원의 신청 내역이 있는지
boolean existsByPost(Post post);

boolean existsByMember(Member member);
```

**`PostService.java` (`delete()` 수정 + `EnrollmentRepository` 필드 추가)**
```java
public void delete(Long id) { // 글 삭제 (본인 또는 관리자)
    Post post = findById(id);
    if (!isAdmin())
        checkWriter(post);
    if (enrollmentRepository.existsByPost(post)) { // [11주차 연습문제 ②] 신청자가 있으면 막는다 (외래 키 500 방지)
        throw new IllegalStateException("신청자가 있는 모집글은 삭제할 수 없습니다.");
    }
    postRepository.delete(post); // DELETE (댓글은 cascade 로 함께)
    fileStore.delete(post.getPosterFile()); // [11주차 연습문제 ②] DB 삭제가 끝난 뒤 포스터 파일도 삭제
}
```
- 파일 삭제를 **DB 삭제 다음**에 둔 이유 : 오늘 13번 함정 ④ "파일은 롤백 안 됨". 파일을 먼저 지웠는데 DB 삭제가 실패하면 글은 남고 포스터만 엑스박스가 된다.
- `fileStore.delete()`는 제공 코드에 이미 있다. `null`(포스터 없는 글)이면 아무것도 안 하고, `uploads/` 밖의 경로는 지우지 않는다.

**`BoardController.java` (`delete()` 수정)**
```java
@PostMapping("/{id}/delete") // 삭제 처리 : POST /board/3/delete
public String delete(@PathVariable Long id) {
    try {
        postService.delete(id);
    } catch (IllegalStateException e) { // [11주차 연습문제 ②] 신청자가 있는 모집글
        return "redirect:/board/" + id + "?deleteError"; // 상세 화면 + 안내 메시지
    }
    return "redirect:/board?deleted"; // 목록 + "삭제되었습니다"
}
```
- 제공 `detail.html`에 이미 `th:if="${param.deleteError}"` 안내("신청자가 있는 모집글은 삭제할 수 없습니다.")가 있어서 화면은 고치지 않았다.

**`MemberService.java` (`deleteMember()`에 추가 + `EnrollmentRepository` 필드 추가)**
```java
if (enrollmentRepository.existsByMember(member)) { // [11주차 연습문제 ②] 신청 인원 계산이 어긋나지 않게 막는다
    throw new IllegalArgumentException("신청 내역이 있는 회원은 삭제할 수 없습니다.");
}
```
- `IllegalArgumentException`으로 던져서 관리자 화면(`AdminController`)은 빨간 안내, 관리자 API는 7주차 `ApiExceptionHandler`가 그대로 **400** `{"error": "..."}`로 바꿔 준다.

| 연관 데이터 | 선택 | 이유 |
|---|---|---|
| 글 → 댓글 (10주차) | 함께 삭제 (cascade) | 글이 없으면 댓글도 의미 없음 |
| 모집글 → 신청 내역 | **막기** | 신청한 사람이 있는데 몰래 사라지면 안 된다 |
| 회원 → 신청 내역 | **막기** | 신청 인원(`appliedCount`) 계산이 어긋난다 |

### 확인하는 방법

| # | 하는 일 | 기대 결과 |
|---|---|---|
| ① | student1 : B 신청 취소 | "신청이 취소되었습니다.", B 0/5 |
| ① | 남의 신청 번호로 취소 요청 | 403 |
| ① | 마감된 C 를 student3 이 취소 | C 가 다시 모집 중 |
| ② | 신청자 있는 모집글 삭제 | 안내 메시지 |
| ② | 신청자 없는 포스터 글 삭제 | 파일도 사라짐 |
| ② | 신청 내역 있는 회원 삭제 | 안내 메시지 (API 400) |

### 결과

- 서버를 켜고 Python 스크립트(로그인 · CSRF 토큰 · 파일 업로드까지)로 확인했다. 관리자 API로 테스트 회원 `t11a`(student1 역할), `t11b`(student3 역할)를 만들고, admin이 모집글 B(정원 5), C(정원 1, 포스터), D(정원 3, 포스터)를 썼다. → t11a는 B, t11b는 C 신청 확정 (C 마감)

| # | 실제로 해 본 것 | 결과 |
|---|---|---|
| ① | 신청 확정 후 `/cart` | 내 신청 내역에 **취소 버튼**이 보인다 ✅ |
| ① | t11a : B 취소 | 302 → `/cart`, "신청이 취소되었습니다.", 내 신청 내역 비어 있음, B **신청 1명 → 0명 (남은 자리 5)** ✅ |
| ① | t11a 가 t11b 의 신청 번호로 취소 | **403**, t11b 의 신청은 그대로 ✅ |
| ① | 없는 신청 번호 99999 취소 | 404 ✅ |
| ① | t11b : 마감된 C 취소 | C **신청 1명 · 남은 자리 0 → 신청 0명 · 남은 자리 1** (다시 모집 중) ✅ |
| ② | admin : 신청자 있는 C 삭제 | 302 → `/board/C번호?deleteError`, "신청자가 있는 모집글은 삭제할 수 없습니다." ✅ (500 아님) |
| ② | API `DELETE /api/admin/members/{t11b}` (신청 내역 있음) | **400** `{"error":"신청 내역이 있는 회원은 삭제할 수 없습니다."}` ✅ |
| ② | admin : 신청자 없는 포스터 글 D 삭제 | 302 → `/board?deleted`, `uploads/`의 포스터 파일 **삭제 전 있음 → 삭제 후 없음** ✅ |
| ② | 취소 후 C 다시 삭제 | 이번에는 삭제됨, 포스터 파일도 사라짐 ✅ |

- 끝나고 테스트 글(B · C · D)과 테스트 회원(t11a · t11b)은 모두 지웠다. (신청 내역이 없어지니 회원 삭제도 204로 성공)

### 🔧 하다가 막혔던 것

- 처음에 Git Bash의 `curl`로 테스트했는데, Windows에서 명령줄의 한글(`"name":"테스트A"`)이 UTF-8로 안 넘어가서 API가 `Invalid UTF-8 middle byte` 400을 냈다. → 한글을 그대로 보낼 수 있게 Python(`urllib`) 스크립트로 바꿨다.
- 바구니가 비어 있을 때 `/cart` 화면에는 form이 없어서 CSRF 토큰(`_csrf`)을 찾을 수 없었다. → 항상 form이 있는 글쓰기 화면(`/board/write`)에서 토큰을 가져왔다. (토큰은 세션마다 같아서 다른 화면에 써도 된다)
- 취소 컨트롤러에 `confirm()`처럼 try-catch를 넣을까 했지만, 그러면 남의 신청 취소가 403이 아니라 "안내 메시지 + 302"가 된다. → 조건대로 403이 나오게 잡지 않았다.

### 상태

- `EnrollmentService.java`(`cancel()` + `@Transactional`, 변경 감지), `CartController.java`(`POST /cart/enrollments/{id}/cancel`), `cart.html`(취소 버튼 주석 해제) : 연습문제 ① 완료 ✅
- `EnrollmentRepository.java`(`existsByPost`, `existsByMember`), `PostService.java`(신청자 있으면 막기 + 포스터 파일 삭제), `BoardController.java`(`?deleteError`), `MemberService.java`(신청 내역 있는 회원 막기) : 연습문제 ② 완료 ✅
- `mvnw clean compile` 성공, 서버를 켜고 위 표의 시나리오 전부 기대한 결과 확인 ✅
