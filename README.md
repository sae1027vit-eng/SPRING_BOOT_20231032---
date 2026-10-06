# 자바웹프로그래밍(2) 수업 정리

> 강사: 최도현 · 주차별 수업 내용과 실습 진행 기록

## 목차

| 주차 | 주제 |
|---|---|
| [🟦 2주차](#week2) | 스프링 부트 개발환경 설정 및 테스트 |
| [🟩 3주차](#week3) | 정적 리소스(Static Resources) 처리와 외부 웹 템플릿 적용 |
| [🟧 4주차](#week4) | 데이터베이스(MySQL) 연동 및 JPA를 통한 CRUD 테스트 |
| [🟨 5주차](#week5) | 로그인/로그아웃 구현과 비밀번호 암호화 (Spring Security) |

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
