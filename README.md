# Android-Mini-Game
💡 [ Protfolio Project 007] 모바일 미니 게임 제작 프로젝트


## 📌 프로젝트 소개
이 프로젝트는 모바일 기기의 가속도 센서를 사용하여 공을 움직여 목표 지점인 네모에 들어가는 게임입니다. 사용자는 핸드폰을 기울여 공을 조작하고, 각 단계를 클리어할수록 게임의 난이도가 점차 올라가며, 배경 음악과 다양한 시각적 요소가 포함되어 있어 게임 플레이를 더욱 흥미롭게 합니다.

## 📌 폴더 구조
    📂 mobile-sensor-game/  
     ┣ 📂 /src/main/java/com/example/b2203098_0927  
     ┃ ┣ 📂 /res/drawable                   # 배경 이미지 파일  
     ┃ ┣ 📂 /assets                         # 게임에 필요한 이미지 및 리소스 파일  
     ┃ ┣ 📜 MainActivity.java               # 공 움직임과 레벨 관리 코드  
     ┃ ┣ 📜 MusicService.java               # 배경 음악 제어 서비스  
     ┃ ┣ 📜 /res/layout/activity_main.xml   # 게임 화면 레이아웃 설정  
     ┃ ┣ 📜 /res/raw/game_bgm.mp3           # 배경 음악 파일  

 
## 📌 주요 기능
### - 가속도 센서를 통한 공 움직임:
사용자가 핸드폰을 기울일 때, 가속도 센서를 통해 공이 해당 방향으로 움직이며, 목표 네모에 들어가면 다음 단계로 넘어갑니다.

### - 레벨별 난이도 증가:
각 레벨에서 공의 속도와 네모의 크기가 달라지며, 네모가 숨거나 가짜 네모가 등장하는 등의 방식으로 난이도가 증가합니다.

### - 배경 음악과 배경 이미지:
배경 음악이 게임 시작부터 종료까지 지속적으로 재생되며, 사용자가 게임을 종료하면 음악도 함께 종료됩니다. 추가로, 배경 이미지를 통해 더욱 몰입감 있는 플레이가 가능합니다.

### - 레벨 구성:
#### - 튜토리얼 모드:
- 레벨 0: 기본 설정으로 게임이 시작되며, 목표는 네모에 공을 넣는 것입니다.  
- 레벨 1: 네모의 크기가 줄어듭니다.  
- 레벨 2: 공의 속도가 증가합니다.  
  
#### - 일반 모드:
- 레벨 3: 네모의 크기가 더 작아지고, 공의 속도가 다시 증가합니다.  
- 레벨 4: 공의 속도가 더욱 빨라집니다.  
- 레벨 5: 네모의 위치가 20초마다 변경됩니다.  
- 레벨 6: 공의 위치가 20초마다 변경됩니다.  
  
#### - 하드 모드:
- 레벨 7: 여러 개의 가짜 네모가 등장하고, 오직 하나만이 진짜 네모입니다.  
- 레벨 8: 네모가 하얗게 변하고, 10초마다 한 번씩만 나타납니다.  
- 레벨 9: 공이 하얗게 변하고, 10초마다 한 번씩만 보입니다.  
- 레벨 10: 최종 단계로, 공과 네모가 모두 하얗게 변하고, 10초마다 한 번씩만 보입니다.  
  
### - 타이머 및 레벨 클리어:
각 레벨에는 타이머가 있어, 일정 시간이 지나면 네모나 공의 위치가 변경되거나 사라지며, 이를 통해 사용자는 전략적인 플레이가 필요합니다. 최종 단계까지 완료하면 축하 메시지가 표시됩니다.

## 📌 구현 상세
### - MainActivity.java:
핸드폰의 가속도 센서를 사용하여 사용자가 핸드폰을 기울이는 각도에 따라 공의 움직임이 실시간으로 반영되고, 네모에 도달하면 다음 레벨로 넘어갑니다.  
각 레벨마다 시간이 지나면 네모와 공의 위치가 변경되거나 숨겨지는 동작을 구현하였습니다.  
게임 난이도는 레벨에 따라 공의 속도와 네모의 크기, 위치 갱신 등이 점차적으로 변경되며, 마지막 단계에서는 공과 네모가 사라졌다 나타나는 기능이 추가되었습니다.  

### - MusicService.java:
게임 시작 시 배경 음악을 재생하고, 사용자가 앱을 종료하거나 백그라운드로 전환할 때 음악이 종료되도록 설정되었습니다.  

## 📌 개발 환경
  Java  
  Android Studio  
