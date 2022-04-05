
### 2022-04-01
# 코드를 짤 때 명심할 원칙들.

## S 단일 책임 원칙 (Single responsibility principle)
- 하나의 클래스에 하나의 책임만 가져야 된다는 원칙으로 여러 책임을 가질 경우에
각 클래스의 정체성에서 문제가 생기므로 Login 만 하는 객체는 Login만 담당하게 코드를 설계하는 것이 심신건강에 좋다.



## O 개방-폐쇄 원칙 (Open/closed principle)
- 간단히 하드 코딩을 최소화 하자는 원칙이다.
- 하드코딩으로 인해 추후 코드 수정이 필요할 때 바꿔야할 코드의 수가 늘어날 경우
    의도치 않은 문제가 생길 수 있다.
-   이에 대비하여 parameter값으로 넘겨주어서 최대한 하드코딩을 안 하는 것이 좋다.

## L 리스코프 치환 원칙 (Liskov substitution principle)
- 결국에 프로그램의 객체는 프로그램의 책임을 깨지 않는 선에서 하위 타입의 인스턴스 (자식 클래스)로 바꿀 수 있어야 한다.
- 이 말은 즉, 연결된 클래스끼리도 책임을 공유해야 한다는 의미기도 하다.
## I 인터페이스 분리 원칙 (Interface segregation principle)
- 인터페이스의 의도를 이해해야한다. 인터페이스는 서로 다른 클래스 간의 공유하는 것들로 하나의 타입을 만들어주는 것이다.
- 인터페이스의 의도를 바탕으로 객체 타입을 분리하여서 입력받는 파라미터를 최소화할 수 있다.
## D 의존관계 역전 원칙 (Dependency inversion principle)
- 강한 결합을 줄이기위해 부모 클래스에서 먼저 만들어 주고 이를 활용하는 것이
추후에 유지보수가 쉽다.
### 문제점
정체성을 잃은 클래스의 경우 추후 후임자가 코드를 고칠 때 클래스 명을 보고 찾을 수 없다.
즉, 가독성이 안 좋다. == 유지보수가 안 좋다. == 지금은 몰라도 추후 위험하다.
또한 마찬가지로 유지보수 관점에서 위험도가 높다.
하드코딩한 부분을 고칠때 안 고쳐준 값으로 인해 다수의 디버깅을 해야한다.
확장성 또한 안 좋다.

1줄 요약
쓰지마라.





## 아쉬운 점
비교적 간단한 코드에서 너무 인터페이스와 클래스화 시키면서 코드가 비약적으로 길어지는 문제가 발생할 수도 있다.


# 2022-04-05
## JVM,  JRE, JDK, 개념 간단 정리.
![](https://media.vlpt.us/images/soe8192/post/84268872-865c-4706-8727-9d79f6ac3113/image.png)


## JVM : Java Virtual Machine

### 자바 가상머신이다.

.java로 부터 만들어지는 바이너리 파일. 즉, .class파일을 실행할 수 있다.
유일하게 플랫폼에 의존적인 부분인데
그럼에도 불구하고 바이너리 파일의 생성이 리눅스인지 윈도우인지는 관계없이
어느 바이너리 파일이든 jvm을 통과할 수 있다.
단지 번역되는 결과물이 다를 뿐이다.
이 부분은 리눅스와 윈도우의 기계어가 다르게 구성되어지기 때문에 단점이 아닌 어쩔 수 없는 부분이다.

## JRE : Java Runtime Environment

### 자바 실행환경이다.

자바도 파이썬처럼 프로그램을 실행할 때 필요한 패키지(라이브러리)를 가지고 있어야 실행이 된다.
이러한 환경을 구성하는 부분을 JRE라 하며 JVM이 실행되기 위한 실행환경이기도 하다.

## JDK : Java Development Kit

### 자바 개발 도구이다.

실행환경과 달리 자바로 개발하는 데 사용한 도구들로
javac(자바 컴파일러), java, JRE 또한 이곳에 포함된다.

