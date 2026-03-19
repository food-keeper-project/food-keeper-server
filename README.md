# 🥬 Kitchen Log (키친로그) — AI 기반 식재료 관리 서비스

> 냉장고 속 식재료를 등록하면 **AI가 최적의 레시피를 추천**해주고, 유통기한이 임박하면 **FCM 푸시 알림**을 보내주는 식재료 관리 플랫폼

---

## 📌 프로젝트 개요

"냉장고에 뭐가 있는데 뭘 해먹을지 모르겠다"는 일상의 문제에서 시작한 프로젝트입니다.
단순 식재료 기록을 넘어, **AI 레시피 추천**과 **유통기한 알림**으로 식재료 낭비를 줄이는 것을 목표로 했습니다.
최신 Spring Boot 4.0 기반으로 구축하며, 인증·알림·AI 추천까지 다양한 도메인을 직접 설계하고 구현했습니다.

| 항목 | 내용 |
|------|------|
| 개발 기간 | 2025.12 ~ 2026.01 |
| 핵심 목표 | AI 레시피 추천 / 유통기한 FCM 알림 / 다중 인증 방식 지원 |
| 특징 | Spring Boot 4.0 + Spring AI 2.x 최신 스택 적용 |

---

## 🛠️ 기술 스택

| 분류 | 기술 |
|------|------|
| Language / Runtime | Java 21 |
| Backend Framework | **Spring Boot 4.0** |
| AI | **Spring AI 2.x (OpenAI)**, Naver Clova Studio |
| Database | MySQL 8.0, QueryDSL |
| Push Notification | **Firebase Cloud Messaging (FCM)** |
| Storage | AWS S3 (프로필 이미지) |
| Infra / Cloud | NCP(Naver Cloud Platform), GCP(Google Cloud Platform) |
| DevOps | Docker, NGINX |
| Monitoring | Prometheus, Grafana, Loki, Discord 서버 알람 |
| 인증 | JWT, OAuth 2.0 (카카오), 이메일 인증 |

---

## 🏗️ 아키텍처

<img width="1246" height="690" alt="architecture" src="https://github.com/user-attachments/assets/bd51f28f-ab84-4d5a-98d5-8e456075c81a" />

---

## 🗃️ ERD

<img width="2490" height="1192" alt="Foodkeeper-erd" src="https://github.com/user-attachments/assets/50c3cdec-ab8c-4a4c-a900-70cb07fe16f8" />

---

## 📦 도메인 구조

레이어드 아키텍처를 기반으로, 각 도메인을 독립적으로 관리합니다.

```
src/main/java/com/foodkeeper/foodkeeperserver/
├── auth/          # 로컬 회원가입·로그인, 카카오 OAuth, 이메일 인증
├── member/        # 회원 정보 조회·수정·탈퇴
├── food/          # 식재료 CRUD, 카테고리 관리
├── bookmarkedfood/ # 자주 쓰는 식재료 북마크
├── recipe/        # 레시피 저장·조회, AI 추천
├── notification/  # FCM 토큰 관리, 유통기한 푸시 알림
├── ai/            # Spring AI 기반 레시피 추천 구현체
├── mail/          # 이메일 인증 발송
└── support/       # Rate Limiter, 로그, 공통 예외 처리
```

**패키지 내부는 `controller → business → implement → dataaccess → domain` 계층으로 명확히 분리**하여
비즈니스 로직과 인프라 코드가 섞이지 않도록 설계했습니다.

---



## 🔔 모니터링

| 도구 | 역할 |
|------|------|
| Prometheus | 메트릭 수집 |
| Grafana | 대시보드 시각화 (JVM, HTTP, DB 쿼리) |
| Loki | 애플리케이션 로그 집계 |
| Discord Webhook | 서버 이상 지표 실시간 알람 |

---

## 🧪 테스트 전략

각 도메인별로 단위 테스트와 통합 테스트를 구분하여 작성했습니다.

| 테스트 종류 | 범위 | 예시 |
|------------|------|------|
| 단위 테스트 | Service, Implement 계층 | `RecipeServiceTest`, `FcmSenderTest` |
| 레포지토리 테스트 | QueryDSL 쿼리 검증 | `RecipeRepositoryTest`, `FcmRepositoryTest` |
| 통합·E2E 테스트 | 전체 흐름 검증 | `AuthE2ETest`, `RecipeE2ETest` |
| 동시성 테스트 | 동시 요청 정합성 검증 | `AuthConcurrencyTest` |

---

## 🚀 실행 방법

### 1. 환경변수 설정

AWS Secrets Manager를 통해 민감한 정보를 관리합니다.
로컬 실행 시 아래 환경변수가 필요합니다.

```dotenv
# DB
DB_URL=jdbc:mysql://localhost:3306/foodkeeper?serverTimezone=Asia/Seoul
DB_USERNAME=root
DB_PASSWORD=[DB 비밀번호]

# JWT
JWT_SECRET=[JWT 시크릿 키]
JWT_ACCESS_EXPIRY=3600
JWT_REFRESH_EXPIRY=86400

# Firebase (FCM)
FIREBASE_CONFIG_PATH=[firebase-adminsdk.json 경로]

# Mail
MAIL_USERNAME=[발신 이메일]
MAIL_PASSWORD=[앱 비밀번호]

# AWS S3
AWS_ACCESS_KEY=[액세스 키]
AWS_SECRET_KEY=[시크릿 키]
AWS_S3_BUCKET=[버킷 이름]

# Kakao OAuth
KAKAO_CLIENT_ID=[카카오 앱 키]
```

### 2. Docker로 실행

```bash
docker-compose -f docker-compose.dev.yml up -d
```

### 3. 배포 환경

```bash
chmod +x deploy.sh
./deploy.sh
```

---

## 📐 API 설계 원칙

- 버전 관리: `/v1/api/` 접두사 사용
- URI는 **복수형 명사** 기준 (`/foods`, `/recipes`, `/members`)
- Cursor 기반 페이지네이션: `?cursor={lastId}&size={pageSize}`

| 기능 | Method | URI |
|------|--------|-----|
| 식재료 목록 조회 (커서 기반) | `GET` | `/v1/api/foods?cursor=&size=20` |
| 식재료 등록 | `POST` | `/v1/api/foods` |
| 식재료 삭제 | `DELETE` | `/v1/api/foods/{foodId}` |
| AI 레시피 추천 | `POST` | `/v1/api/recipes/ai-recommend` |
| 레시피 저장 | `POST` | `/v1/api/recipes` |
| 로컬 회원가입 | `POST` | `/v1/api/auth/local/sign-up` |
| 카카오 로그인 | `POST` | `/v1/api/auth/oauth/kakao` |
| 이메일 인증 코드 발송 | `POST` | `/v1/api/auth/email/verification` |
| FCM 토큰 등록 | `POST` | `/v1/api/notifications/fcm-token` |

---

