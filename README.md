# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 기능 요구사항 - 1단계 (자바 Reflection)
- [x] `Question` 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
- [x] `Junit3Test` 클래스에서 'test'로 시작하는 메소드만 Java Reflection을 활용해 실행하도록 구현한다.
- [x] `Junit4Test` 클래스에서 `@MyTest` 애노테이션으로 설정되어 있는 메소드만 Java Reflection을 활용해 실행하도록 구현한다.
- [x] `Student` 클래스의 `name`과 `age` 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.
- [x] `Question` 클래스의 인스턴스를 자바 Reflection API를 활용하여 생성한다.
- [x] `core.di.factory.example` 패키지에서 `@Controller`, `@Service`, `@Repository` 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.

## 기능 요구사항 - 2, 3단계 (@MVC 구현)
- [x] 애노테이션 기반의 MVC 프레임워크를 구현한다. 
  - [x] 클라이언트 요청과 해당 요청을 처리할 핸들러의 매핑 정보를 갖는 `AnnotationHandlerMapping` 클래스를 구현한다.
  - [x] 요청을 처리할 핸들러인 `HandlerExecution` 클래스를 구현한다.
- [x] 레거시 MVC 프레임워크와 애노테이션 기반 MVC 프레임워크를 통합한다.

## 기능 요구사항 - 4단계 (Controller 메소드 인자 매핑)
- [x] Controller 메소드의 인자들을 각 인자의 타입에 따라 자동으로 매핑되도록 기능을 추가한다.
