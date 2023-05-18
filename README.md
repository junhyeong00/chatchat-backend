# chatchat

## 프로젝트 소개
새로운 기술을 적용하고 이전 프로젝트에서 불편했던 점을 개선하기 위해 개발 중인 채팅 어플리케이션입니다.

<br>

## 기술 스택
### Backend
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"></a>
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">

### Database
<img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgreSQL&logoColor=white"/>

<br>

## 아키텍쳐
Layered Architecture

1. UI Layer
2. Application Layer
3. Domain Layer
4. Infrastructure Layer

상위 레이어는 하위 레이어에 의존하며, 하위 레이어는 상위 레이어를 모릅니다.

각 계층은 책임에 맞는 동작 수행 후 다른 계층과 상호작용, 계층 간 데이터 교환에는 DTO(Data Transfer Object)를 이용해 정의하여 사용하였습니다.

<br>

## 👊 기술적 도전 및 문제 해결 과정
- [EC2 Ubuntu 스왑파일을 활용한 메모리 할당](https://velog.io/@jhbae0420/AWS-EC2-Ubuntu-스왑파일을-활용한-메모리-할당)
- [JWT 단독사용으로 발생할 수 있는 보안 문제 해결하기](https://velog.io/@jhbae0420/JWT-단독사용으로-발생할-수-있는-보안-문제-해결하기Refresh-Token)

<br>

### 목적에 맞는 인스턴스 사용
서버구축 초기에는 EC2 인스턴스 하나에 웹서버 컨테이너와 db서버 컨테이너를 함께 띄웠습니다.

중간에 서버가 다운되는 현상이 발생해서 인스턴스를 재부팅하니 db 데이터가 전부 소실되는 이슈가 발생했습니다.

이를 해결하기 위해 영속화를 보장하는 RDS인스턴스를 활용하여 db서버를 분리한 결과 다음과 같은 이점이 있었습니다.

- 웹서버가 다운되는 일이 발생해도 분리된 환경에서 db서버를 안정적으로 운영할 수 있었습니다.
- 웹서버와 db서버가 분리되어 EC2인스턴스에 걸리는 부하가 감소하였습니다.

<br>

### 스왑 메모리 적용
AWS 프리티어(무료계정)에서 제공하는 인스턴스의 메모리가 1GB밖에 되지 않아 스프링부트 서버를 실행하면 서버가 다운되는 현상이 발생했습니다.

이를 해결하기 위해 디스크 용량을 메모리로 활용할 수 있는 스왑 메모리 기술을 활용하여 가용 메모리를 1GB -> 3GB로 증가시켜 서버 다운문제를 해결했습니다.

<br>

### 빌드, 테스트를 위한 github Actions CI 파이프라인 구축
프로젝트에서 작성한 모든 테스트가 통과하는지 체크하고, 프로젝트 서버에서 사용하는 도커이미지를 자동으로 빌드하고 푸쉬해주는 CI 파이프라인을 구축했습니다.

파이프라인 구축 결과 다음과 같은 이점이 있었습니다.

- PR단계에서 테스트와 이미지 빌드 중 발생하는 문제를 파악할 수 있어 안전한 브랜치만 main 브랜치에 merge시킬 수 있었습니다.
- 자동화로 인해 비즈니스로직 작성에 집중할 수 있었습니다.

<br>

### 보안을 위한 환경변수 관리
이미지 빌드에 필요한 도커허브 ID와 PW가 프로젝트 레포지토리에 노출 될 경우 다른사람이 악용할 수 있기 때문에 안전하게 관리할 필요가 있었습니다.

이러한 값들을 안전하게 관리하기 위해 환경변수로 설정하고 사용했습니다.

- 컨테이너 실행시 환경변수를 지정해 안전하게 secret관리
- github Actions의 CI 단계에서 필요한 환경변수를 관리하기 위해 github Secrets 이용
