# AWS 초급 강의 V4 엘라스틱 빈스톡 배포
- 엘라스틱빈스톡 + RDS (MariaDB)
- prod 배포 설정 파일 5000번 포트
- dev 개발 설정 파일 8081 포트 + h2 인메모리 디비 사용

# 순서
- 엘라스틱빈스톡 생성 (보안키 설정)
- 환경변수 생성
- RDS_HOSTNAME
- RDS_DB_NAME
- RDS_PORT
- RDS_USERNAME
- RDS_PASSWORD
- RDS 생성 : 퍼블릭 엑세스 허용
- 로컬 PC ip와 내부 시큐리티 그룹으로만 연결 허용해주자.
- 이번에는 로드밸런서 연결안함

```txt
엘라스틱빈스톡으로 생성하는 환경변수는 jar파일을 직접 배포해서 실행시키면 적용되지 않는다. 무조건 엘라스틱 빈스톡으로 정상적인 배포를 할 때만 환경변수가 작동한다.

RDS_HOSTNAME 의 경우 스프링 yml 파일에서 접근할 때 3가지 방법이 있다.
여기서 점(dot)을 쓸 수 있는 이유는 스프링에서 .으로도 _를 찾아주기 때문이다.
1. rds.hostname
2. rds_hostname
3. RDS_HOSTNAME

자바로 접근할 떄는 아래와 같이 찾을 수 있다.
System.getProperty("RDS_HOSTNAME");
```