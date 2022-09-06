# 프레임워크 구현
## Java Reflection
### 요구사항 1 - 클래스 정보 출력
- [ ] showClass() 메소드를 구현해 Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.

### 요구사항 2 - test로 시작하는 메소드 실행
- [ ] Junit3Test 클래스에서 test로 시작하는 메소드만 Java Reflection을 활용해 실행하도록 구현한다.

### 요구사항 3 - @Test 애노테이션 메소드 실행
- [ ] Junit4Test 클래스에서 @MyTest애노테이션으로 설정되어 있는 메소드만 Java Reflection을 활용해 실행하도록 구현한다

### 요구사항 4 - private field에 값 할당
- [ ] Student클래스의 name과 age 필드에 값을 할당한 후 getter메소드를 통해 값을 확인한다.

### 요구사항 5 - 인자를 가진 생성자의 인스턴스 생성
- [ ] Question클래스의 인스턴스를 자바 Reflection API를 활용해 Question인스턴스를 생성한다.

### 요구사항 6 - Component Scan
- [ ] @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력
- 
## @MVC 구현
### 요구사항 1 - 애노테이션 기반 MVC 프레임워크
- [ ] @Controller 애노테이션이 설정되어 있는 클래스를 찾는다.
- [ ] @RequestMapping 설정에 따라 요청 URL과 메소드를 연결하도록 구현한다.

### 요구사항 2 - 레거시 MVC와 애노테이션 기반 MVC 통합
- [ ] 이전의 컨트롤러를 애노테이션 기반으로 바꿔서 공존 할 수 있도록 구현한다.

## @MVC 구현 리펙토링
### 요구사항 1 - 힌트
- 힌트 1 - ControllerScanner 클래스 추가
  - [ ] @Controller 애노테이션이 설정되어 있는 모든 클래스를 찾는다.
  - [ ] 찾은 클래스에 대한 인스턴스를 생성해 Map<Class<?>, Object>에 추가한다.
- 힌트 2 - AnnotationHandlerMapping 클래스 추가
  - [ ] ControllerScanner를 통해 찾은 @Controller 클래스의 메소드 중 RequestMapping 애노테이션이 설정되어 있는 모든 메소드를 찾는다.
  - [ ] 찾은 메소드를 Map<HandlerKey, HandlerExecution>에 각 요청 URL과 URL과연결되는 메소드 정보를 추가한다.
    - HandlerKey는 RequestMapping 애노테이션이 가지고있는 URL과 HTTP 메소드 정보를 가진다.
    - HandlerExecution은 자바 Reflection을 실행하기 위한 정보를 가진다.
- 힌트 3 - 요청에 대한 Controller 반환
  - [ ] AnnotationHandlerMapping에 클라이언트 요청정보(HttpServletRequest)를 전달하면 요청에 해당하는 HandlerExecution을 반환하는 메소드를 구현한다.
    - HandlerExecution의 getHandler(HttpServletRequest req) 메소드를 구현한다. 

### 요구사항 2 - 레거시 MVC와 애노테이션 기반 MVC 통합
- 힌트 1 - HandlerMapping
- 힌트 2 - HandlerMapping 초기화
  - DispatcherServlet의 초기화(init() 메소드) 과정에서LegacyHandlerMapping, AnnotationHandlerMapping 모두 초기화한다. 
  - 초기화한 2개의 HandlerMapping을 List로 관리한다.
- 힌트 3 - Controller 실행
  - DispatcherServlet의 service() 메소드에서는 앞에서 초기화한 2개의 HandlerMapping에서 요청 URL에 해당하는 Controller를 찾아 메소드를 실행한다.


## Controller 메소드 인자 매핑
### 요구사항 1 - Controller 메소드의 인자 타입에 따라 HttpServletRequest에서 값을 꺼내와 자동으로 형 변환
- 힌트 1 - HttpServletRequest에 값 전달하기
  - Spring에서 제공하는 MockHttpServletRequest을 활용
- 힌트 2 - 메소드의 인자 이름 구하기
  - 인자 이름은 Spring에서 제공하는 ParameterNameDiscoverer을 활용
- 힌트 3 - primitive type을 비교
  - primitive type을 비교할 경우 int.class, long.class로 비교할 수 있다.

### 요구사항 2 - URL을 통해서도 동적으로 값을 전달
- 힌트 - PathVariable 구현을 위해 URL 매칭과 값 추출
  - Spring의 PathPattern을 활용해 쉽게 구현할 수 있다.

## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)
