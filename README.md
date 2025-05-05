# 1. 프로젝트 개요
Java, JDBC를 이용한 영화 예매 시스템
## 1.1 주요 기능

- 회원 기능
    - /signup
    - /login
    - /logout
- 영화 예매
    - /movie - 상영중인 영화 확인
    - /book - 영화 예매
- 예매 내역 확인
    - /reservation
    - /cancel

## 1.2 기술 스택

- Java 21
- AWS RDS (PostgreSQL)

## 1.3 프로젝트 구조

```
src/main/java
├── util
├── command
├── controller
├── exception
├── member
├── movie
├── pay
├── reservation
└── Main.java
```

**주요 흐름**

- Main → Controller → Command → Service → Repository

**주요 패키지**

- **util**: 데이터베이스 연결 설정 정보를 가지고 있습니다.
- **command**: 커맨드 인터페이스와 구현체가 있습니다.
- **controller**: 사용자 요청에 맞는 커맨드를 호출하는 MainController가 있습니다.
- **exception**: 프로젝트 전반에서 발생할 수 있는 예외를 정의하고 처리합니다.
- **member**: 멤버 도메인을 처리하는 클래스가 있습니다.
- **movie**: 영화 도메인을 처리하는 클래스가 있습니다.
- **pay**: 결제 처리 및 관련 기능을 담당합니다.
- **reservation**: 예약 도메인을 처리하는 클래스가 있습니다.
- **Main.java**: 프로그램의 시작점으로, 전체 애플리케이션을 실행하는 메인 클래스입니다.

# 2. 역할

| 팀원 | 역할 분담 |
| --- | --- |
| 이재용 | 프로젝트 관리, 예외 처리 |
| 이휘 | 영화 예매 |
| 유훈종 | 테이블 설계, 샘플 데이터 추가, 예매 내역 |
| 한제이 | 회원 기능, 결제, 문서 작성 |