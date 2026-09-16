# 자바웹프로그래밍(2) - 2주차

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

# 자바웹프로그래밍(2) - 3주차

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

### 5. 기존 파일 백업

- 새 내용으로 덮어쓰기 전, 기존 `index.html`, `hello.html`, `hello2.html`을 "-복사본" 이름으로 복사해두어 이전 버전을 보존하는 방식 실습
