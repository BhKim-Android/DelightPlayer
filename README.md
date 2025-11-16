# DelightPlayer

DelightPlayer는 Clean Architecture 기반으로 개발된 안드로이드 음악 플레이어 애플리케이션입니다.

## 📱 프로젝트 개요

DelightPlayer는 Jetpack Compose를 사용하여 구축된 현대적인 음악 플레이어 앱으로, 멀티 모듈 아키텍처를 통해 확장 가능하고 유지보수가 용이한 구조를 제공합니다.

## 🏗️ 프로젝트 구조

이 프로젝트는 Clean Architecture 원칙을 따르며, 다음과 같은 모듈로 구성되어 있습니다:

### 모듈 구조

- **`app`**: 메인 애플리케이션 모듈
  - 앱의 진입점 및 설정
  - 네비게이션 구성
  - 테마 및 UI 설정

- **`domain`**: 도메인 레이어
  - 비즈니스 로직 및 엔티티
  - Repository 인터페이스
  - UseCase 정의

- **`data`**: 데이터 레이어
  - Repository 구현
  - 데이터 소스 관리

- **`feature`**: UI 모듈
  - List, Player 화면
  - ViewModel (UI 상태 관리)

- **`media`**: 미디어 모듈
  - 미디어 재생 서비스
  - Media3 통합
  - 미디어 관련 Repository 구현

- **`core-ui`**: 공통 UI 컴포넌트
  - 재사용 가능한 UI 컴포넌트
  - 공통 테마 요소

## 🛠️ 기술 스택

### 주요 기술

- **언어**: Kotlin
- **UI 프레임워크**: Compose
- **아키텍처**: Clean Architecture (멀티 모듈)
- **의존성 주입**: Hilt
- **비동기 처리**: Coroutines
- **미디어 재생**: Media3

## 🎯 기능 요구사항

- ✅ 오디오 타입의 미디어 파일을 검색하여 리스트로 표시
- ✅ 각 리스트 아이템(앨범 아트, 제목, 아티스트)
- ✅ 현재 재생 중인 음악은 리스트에서 재생 중임을 시각적으로 표시
- ✅ 아이템을 클릭하면 해당 음악을 재생하고 상세 화면으로 이동
- ✅ 현재 재생 중인 음악의 진행 상태(progress)가 실시간으로 업데이트
- ✅ 플레이어 컨트롤 버튼을 구현
- ✅ 뒤로가기 버튼을 통해 리스트 화면 이동

## 🏛️ 아키텍처

### Clean Architecture 계층

```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│  (Feature Module - UI/ViewModel)    │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│          Domain Layer               │
│  (UseCase, Repository Interface)    │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│           Data Layer                │
│  (Repository Implementation)        │
└─────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────┐
│         Media Layer                 │
│  (Media3, PlaybackService)          │
└─────────────────────────────────────┘
```

### 데이터 흐름

1. **UI Layer** (Feature Module)
   - 사용자 입력 처리
   - ViewModel을 통해 UseCase 호출
   - 상태 관리를 위한 StateFlow/Flow 사용

2. **Domain Layer**
   - 비즈니스 로직 처리
   - Repository 인터페이스 정의
   - UseCase로 비즈니스 규칙 캡슐화

3. **Data Layer**
   - Repository 구현
   - 데이터 소스 관리
   - 도메인 모델로 변환

4. **Media Layer**
   - Media3를 통한 미디어 재생
   - 백그라운드 서비스 관리
   - 미디어 세션 관리

