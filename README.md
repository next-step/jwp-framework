# 프레임워크 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

# STEP1
- [X] 요구사항 1 - 클래스 정보 출력. src/test/java > next.reflection > ReflectionTest의 showClass() 메소드를 구현해 Question 클래스의 모든 필드,
  생성자, 메소드에 대한 정보를 출력한다.
- [X] 요구사항 2 - test로 시작하는 메소드 실행 Junit3에서는 test로 시작하는 메소드를 자동으로 실행한다. 이와 같이 Junit3Test 클래스에서 test로 시작하는 메소드만 Java
  Reflection을 활용해 실행하도록 구현한다. 구현은 src/test/java > next.reflection > Junit3Runner 클래스의 runner() 메소드에 한다.
- [X] 요구사항 3 - @Test 애노테이션 메소드 실행 Junit4에서는 @Test 애노테이션일 설정되어 있는 메소드를 자동으로 실행한다. 이와 같이 Junit4Test 클래스에서 @MyTest 애노테이션으로
  설정되어 있는 메소드만 Java Reflection을 활용해 실행하도록 구현한다. 구현은 src/test/java > next.reflection > Junit4TestRunner 클래스의 run() 메소드에
  한다.
- [X] 요구사항 4 - private field에 값 할당 자바 Reflection API를 활용해 다음 Student 클래스의 name과 age 필드에 값을 할당한 후 getter 메소드를 통해 값을 확인한다.
  구현은 src/test/java > next.reflection > ReflectionTest 클래스의 privateFieldAccess() 메소드에 한다.
- [X] 요구사항 5 - 인자를 가진 생성자의 인스턴스 생성 Question 클래스의 인스턴스를 자바 Reflection API를 활용해 Question 인스턴스를 생성한다.
- [X] 요구사항 6 - component scan src/test/java 폴더의 core.di.factory.example 패키지를 보면 DI 테스트를 위한 샘플 코드가 있다.
  core.di.factory.example 패키지에서 @Controller, @Service, @Repository 애노테이션이 설정되어 있는 모든 클래스를 찾아 출력한다.

## STEP2

### 요구사항 1 - 애노테이션 기반 MVC 프레임워크

> 지금까지 나만의 MVC 프레임워크를 구현해 잘 활용해 왔다. 그런데 새로운 컨트롤러가 추가될 때마다 매번 RequestMapping 클래스에 요청 URL과 컨트롤러를 추가하는 작업이 귀찮다. 귀찮지만 이 정도는 그래도 참을 수 있다. 하지만 유지보수 차원에서 봤을 때 컨트롤러의 수가 계속해서 증가하고 있으며, 각 컨트롤러의 execute() 메소드를 보니 10라인이 넘어가는 경우도 거의 없다. 새로운 기능이 추가될 때마다 매번 컨트롤러를 추가하는 것이 아니라 메소드를 추가하는 방식이면 좋겠다. 또 한 가지 아쉬운 점은 요청 URL을 매핑할 때 HTTP 메소드(GET, POST, PUT, DELETE 등)도 매핑에 활용할 수 있으면 좋겠다. HTTP 메소드에 대한 지원이 가능하다면 URL은 같지만 다른 메소드로 매핑하는 것도 가능할 것이다.
> 이 같은 단점을 보완하기 위해 다음과 같이 Controller를 구현할 수 있도록 지원하는 프레임워크 구현하려고 한다. @RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method를 지원해야 한다.
