# ChatChat

## 프로젝트 소개
기업-고객 간 자주 묻는 질문이나 1:1 상담 등의 소통을 간편하게 할 수 있도록 도와주는 채팅 서비스입니다.

<br>

### API 설계 
- https://chatchat.gitbook.io/api/

<br>

## 기술 스택
### Backend
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"></a>
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">

### Database
<img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgreSQL&logoColor=white"/>

<br>

## 아키텍쳐
<p align="center">
  <br>
  <img width="800" alt="아키텍쳐" 
    src="https://github.com/junhyeong00/Trend-up-backend/assets/107493122/d144515f-bf48-46d1-b358-bef773c84734">
  <br>
</p>

<br>

## 👊 기술적 도전 및 문제 해결 과정
- [EC2 Ubuntu 스왑파일을 활용한 메모리 할당](https://velog.io/@jhbae0420/AWS-EC2-Ubuntu-스왑파일을-활용한-메모리-할당)
- [JWT 단독사용으로 발생할 수 있는 보안 문제 해결하기](https://velog.io/@jhbae0420/JWT-단독사용으로-발생할-수-있는-보안-문제-해결하기Refresh-Token)
- [쿠키를 활용한 인증에서 SameSite 문제 해결을 위해 서버의 서브도메인으로 프론트엔드 배포](https://velog.io/@jhbae0420/쿠키를-활용한-인증에서-SameSite-문제-해결하기)
  
<br>

### 쿠키를 활용한 인증에서 SameSite 문제 해결을 위해 서버의 서브도메인으로 프론트엔드 배포
포스트맨은 정상적으로 토큰이 재발급되나, 브라우저에서는 정상 작동하지 않는 이슈가 발생했습니다.

- 원인 파악 :
  - 서버 로그를 확인하여 Refresh Token이 전달되지 않은 것을 확인
  - [개발자 도구 - Network]에서 확인 시 로그인 과정에서 쿠키에 Refresh Token이 담긴 것을 확인, 해당 내용 우측에서 (Set-Cookie 속성 중) SameSite 관련 경고 확인
  - => 서버에서 할 수 있는 SameSite 설정에는 3가지가 있는데, 이 때 적용했었던 Lax는 보통의 경우 GET 요청에서는 문제가 없으나 cross site라고 판단된 출처에서 POST 요청 시엔 쿠키가 전송되지 않기 때문
- 해결 방안 :
  1. SameSite 설정을 none으로 변경 → 단, cross site 요청의 경우에도 쿠키를 항상 전송하기 때문에 보안에 좋지 않아 Secure 속성과 함께 사용
  2. SameSite 규칙에 위배되지 않도록 프론트를 서버의 서브 도메인으로 변경 → 채택

- 대응 지연 사유 : 포스트맨에서는 기능에 문제가 없었으므로 당연히 프론트엔드 문제일 것이라고 예상
   → 포스트맨은 브라우저와는 다른 방식을 갖기 때문에 보안 제약을 따르지 않는다는 사실을 간과함
    
- 향후 대책 :
    - 프론트에서 문제가 생기더라도 단순히 프론트의 문제가 아닐 수 있음을 인지하고, 서버 로그와 Network 탭을 활용하여 문제해결에 적극적으로 나설 것

<br>

### 목적에 맞는 인스턴스 사용
서버구축 초기에는 EC2 인스턴스 하나에 웹서버 컨테이너와 db서버 컨테이너를 함께 띄웠습니다.

중간에 서버가 다운되는 현상이 발생해서 인스턴스를 재부팅하니 db 데이터가 전부 소실되는 이슈가 발생했습니다.

이를 해결하기 위해 영속화를 보장하는 RDS인스턴스를 활용하여 db서버를 분리한 결과 다음과 같은 이점이 있었습니다.

- 웹서버가 다운되는 일이 발생해도 분리된 환경에서 db서버를 안정적으로 운영할 수 있었습니다.
- 웹서버와 db서버가 분리되어 EC2인스턴스에 걸리는 부하가 감소하였습니다.

<br>

### 빌드, 테스트, 배포 자동화를 위한 github Actions CI/CD 파이프라인 구축
프로젝트에서 작성한 모든 테스트가 통과하는지 체크하고, 프로젝트 서버에서 사용하는 도커이미지를 자동으로 빌드하고 푸쉬해주는 CI/CD 파이프라인을 구축했습니다.

파이프라인 구축 결과 다음과 같은 이점이 있었습니다.

- PR단계에서 테스트와 이미지 빌드 중 발생하는 문제를 파악할 수 있어 안전한 브랜치만 main 브랜치에 merge시킬 수 있었습니다.
- 자동화로 인해 비즈니스로직 작성에 집중할 수 있었습니다.

