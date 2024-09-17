package com.example.b2203098_0927;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static int cx,  dx = 0, r, rx, ry, w;
    static int cy, dy = 0, layoutWidth, layoutHeight;
    static int rx1, rx2, rx3, rx4, ry1, ry2, ry3, ry4;
    static int level_num = 0, goal = 0, stop_x = 100, stop_y = 100, speed = 4;
    static long timenum = 0;
    MyGraphicView myGraphicView;
    LinearLayout layout;
    static TextView textView, textView2, textView3, textView4, textView5;
    Button button1, button2;
    SensorManager sensorManager;
    Sensor accelerometerSensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myGraphicView = new MyGraphicView(this);
        layout = (LinearLayout) findViewById(R.id.ViewLayout);
        layout.addView(myGraphicView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);

        startService(new Intent(getApplicationContext(), MusicService.class));      // 음악 재생
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {                                                     // 앱 종료 시 음악 멈춤
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), MusicService.class));
    }

    @Override
    protected void onUserLeaveHint() {                                                 // 앱 종료 시 음악 멈춤
        super.onUserLeaveHint();
        stopService(new Intent(getApplicationContext(), MusicService.class));
    }

    @Override
    public void onBackPressed() {                                                     // 앱 종료 시 음악 멈춤
        super.onBackPressed();
        stopService(new Intent(getApplicationContext(), MusicService.class));
    }

    public void onWindowFocusChanged(boolean hasFocus) {                    // 기본 코드
        super.onWindowFocusChanged(hasFocus);
        textView.setText("width = " + layout.getWidth() + ", height : " + layout.getHeight());
        layoutWidth = layout.getWidth();
        layoutHeight = layout.getHeight();
        cx = layout.getWidth() / 2;
        cy = layout.getHeight() / 2;
        r = (int)(layoutHeight * 0.02);

        Random random = new Random();
        w = r * 4;
        rx = random.nextInt(layoutWidth - w);
        ry = random.nextInt(layoutHeight - w);

        myGraphicView.invalidate();
    }

    private static class MyGraphicView extends View {
        private boolean levelSuccess = false;           // 레벨 통과 체크
        private boolean SquareAppears = true;           // 사각형 나타남 체크
        private boolean CircleAppears = true;           // 원 나타남 체크
        private CountDownTimer timer;                   // 카운트다운 타이머

        public MyGraphicView(MainActivity context) {
            super(context);
            timer = null;
        }
        public void checkIn(){                          // 공이 네모에 들어왔는지 체크, goal 변수에 저장
            if (cx - r > rx && cx + r < rx + w && cy - r > ry && cy + r < ry + w) {
                if (stop_x == 100 && stop_y == 100) {   // stop x,y 좌표가 초기화 상태이면 현재 좌표 저장하기
                    stop_x = cx;
                    stop_y = cy;
                    goal = 1;
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();      // 기본 공
            paint.setColor(Color.RED);
            Paint paint1 = new Paint();     // 기본 네모
            paint1.setColor(Color.BLUE);
            Paint paint2 = new Paint();     // 정지된 하얀 공
            paint2.setColor(Color.WHITE);

            Paint paint11 = new Paint();    // 랜덤 네모 4개
            paint11.setColor(Color.BLUE);
            Paint paint12 = new Paint();
            paint12.setColor(Color.BLUE);
            Paint paint13 = new Paint();
            paint13.setColor(Color.BLUE);
            Paint paint14 = new Paint();
            paint14.setColor(Color.BLUE);

            Handler mHandler = new Handler();
            Random random = new Random();

            if (level_num == 7){        // 7레벨 : 네모 4개 생성
                canvas.drawRect(rx1, ry1, rx1+w, ry1+w, paint11);
                canvas.drawRect(rx2, ry2, rx2+w, ry2+w, paint12);
                canvas.drawRect(rx3, ry3, rx3+w, ry3+w, paint13);
                canvas.drawRect(rx4, ry4, rx4+w, ry4+w, paint14);
            }
            if (SquareAppears && (level_num == 8 || level_num == 10)) {          // 8레벨과 10레벨 : 네모 사라지기
                paint1.setColor(Color.BLUE);
            } else if (!SquareAppears && (level_num == 8 || level_num == 10)) {
                paint1.setColor(Color.parseColor("#00000000"));         // 투명하게
            }

            if (CircleAppears && (level_num == 9 || level_num == 10)) {          // 9레벨과 10레벨 : 네모 사라지기
                paint.setColor(Color.RED);
            } else if (!CircleAppears && (level_num == 9 || level_num == 10)) {
                paint.setColor(Color.parseColor("#00000000"));
            }
            canvas.drawRect(rx, ry, rx+w, ry+w, paint1);

            switch (level_num) {
                case 0:                                                             // 레벨 0 : 튜토리얼, 네모에 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;                           // 기본 설정 : goal, stop공 좌표 초기화, 레벨 및 목표 표시, textview GONE
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 네모에 들어가기");
                    textView4.setVisibility(View.GONE);
                    checkIn();
                    if (goal == 1 && !levelSuccess) {                              // 공이 네모에 들어온 경우
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);             // 현재 좌표에 멈춘 공 표시 후 textview 들어옴으로 변경
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과!");      // 레벨 통과 화면 띄우고 딜레이
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);             // 다음 레벨 준비 : textview 가리기, 레벨 올리기, 네모 크기 줄이기, 랜덤 좌표 생성, 변수 초기화
                                        level_num += 1;
                                        w = w - 25;
                                        rx = random.nextInt(layoutWidth - w);
                                        ry = random.nextInt(layoutHeight - w);
                                        levelSuccess = false;
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                case 1:                                                           // 레벨 1 : 조금 작아진 네모에 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 조금 작아진 네모에 들어가기");
                    textView4.setVisibility(View.GONE);
                    checkIn();
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과!");
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        speed = 6;                                    // 다음 레벨 준비 : speed 올리기
                                        rx = random.nextInt(layoutWidth - w);
                                        ry = random.nextInt(layoutHeight - w);
                                        levelSuccess = false;
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                case 2:                                                           // 레벨 2 : 조금 빨라진 공으로 네모에 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 조금 빨라진 공으로 들어가기");
                    textView4.setVisibility(View.GONE);
                    checkIn();
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과!");
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        w = w - 25;                                    // 다음 레벨 준비 : 네모 더 줄이기
                                        rx = random.nextInt(layoutWidth - w);
                                        ry = random.nextInt(layoutHeight - w);
                                        levelSuccess = false;
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                case 3:                                                           // 레벨 3 : 더 작아진 네모에 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 더 작아진 네모에 들어가기");
                    textView4.setVisibility(View.GONE);
                    checkIn();
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과!");
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        speed = 8;                                    // 다음 레벨 준비 : speed 더 올리기
                                        rx = random.nextInt(layoutWidth - w);
                                        ry = random.nextInt(layoutHeight - w);
                                        levelSuccess = false;
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                case 4:                                                           // 레벨 4 : 더 빨라진 공으로 네모에 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 더 빨라진 공으로 들어가기");
                    textView4.setVisibility(View.GONE);
                    checkIn();
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과! \n 지금부터 현재 속도와 크기가 유지됩니다.");        // 기본 속도 및 크기 안내
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        rx = random.nextInt(layoutWidth - w);       // 다음 레벨 준비 : 타이머 초기화
                                        ry = random.nextInt(layoutHeight - w);
                                        levelSuccess = false;
                                        timer = null;
                                        invalidate();
                                    }
                                }, 3000);
                            }
                        }, 1000);
                    }
                    break;
                case 5:                                                           // 레벨 5 : 10초마다 위치가 바뀌는 네모에 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 10초마다 위치가 바뀌는 네모에 들어가기");
                    textView4.setVisibility(View.GONE);
                    textView5.setVisibility(View.VISIBLE);
                    checkIn();
                    if (timer == null) {                                            // 타이머가 초기상태면 함수 실행
                        startCountdownTimer();
                    }
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과!");
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        rx = random.nextInt(layoutWidth - w);
                                        ry = random.nextInt(layoutHeight - w);
                                        levelSuccess = false;
                                        if (timer != null) {                        // 다음 레벨 준비 : 타이머 취소 후 초기화
                                            timer.cancel();
                                            timer = null;
                                        }
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                case 6:                                                           // 레벨 6 : 조금 작아진 네모에 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 10초마다 달라지는 공과 네모로 통과하기");
                    textView4.setVisibility(View.GONE);
                    textView5.setVisibility(View.VISIBLE);
                    checkIn();
                    if (timer == null) {
                        startCountdownTimer();
                    }
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과! \n 지금부터 하드모드가 시작됩니다.");
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        rx = random.nextInt(layoutWidth - w);
                                        ry = random.nextInt(layoutHeight - w);
                                        randomSquarePosition();                 // 다음 레벨 준비 : 사각형 4개 위치 랜덤으로 뽑아서 준비
                                        levelSuccess = false;
                                        if (timer != null) {
                                            timer.cancel();
                                            timer = null;
                                        }
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                case 7:                                                           // 레벨 7 : 20초마다 달라지는 진짜 네모에 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 20초마다 달라지는 진짜 네모에 들어가기");
                    textView4.setVisibility(View.GONE);
                    textView5.setVisibility(View.VISIBLE);
                    checkIn();
                    if (timer == null) {
                        startCountdownTimer();
                    }
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과!");
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        rx = random.nextInt(layoutWidth - w);
                                        ry = random.nextInt(layoutHeight - w);
                                        new Handler().postDelayed(new Runnable() {      // 다음 레벨 준비 : 네모 보여주고 0.5초 딜레이 후 투명화
                                            @Override
                                            public void run() {toggleSquareColor();}
                                        }, 500);
                                        levelSuccess = false;
                                        if (timer != null) {
                                            timer.cancel();
                                            timer = null;
                                        }
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                case 8:                                                           // 레벨 8 : 10초마다 한번 등장하는 네모에 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 10초마다 한번 등장하는 네모에 들어가기");
                    textView4.setVisibility(View.GONE);
                    textView5.setVisibility(View.VISIBLE);
                    checkIn();
                    if (timer == null) {
                        startCountdownTimer();
                    }
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과!");
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        rx = random.nextInt(layoutWidth - w);
                                        ry = random.nextInt(layoutHeight - w);
                                        new Handler().postDelayed(new Runnable() {          // 다음 레벨 준비 : 공 보여주고 0.8초 딜레이 후 투명화
                                            @Override
                                            public void run() {toggleCircleColor();}
                                        }, 800);
                                        levelSuccess = false;
                                        if (timer != null) {
                                            timer.cancel();
                                            timer = null;
                                        }
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                case 9:                                                           // 레벨 9 : 10초마다 한번 등장하는 공으로 들어가기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 10초마다 한번 등장하는 공으로 들어가기");
                    textView4.setVisibility(View.GONE);
                    textView5.setVisibility(View.VISIBLE);
                    checkIn();
                    if (timer == null) {
                        startCountdownTimer();
                    }
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과!");
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        rx = random.nextInt(layoutWidth - w);
                                        ry = random.nextInt(layoutHeight - w);
                                        SquareAppears = true;
                                        CircleAppears = true;
                                        new Handler().postDelayed(new Runnable() {          // 다음 레벨 준비 : 네모, 공 보여주고 0.8초 딜레이 후 투명화
                                            @Override
                                            public void run() {
                                                toggleSquCirColor();
                                            }
                                        }, 800);
                                        levelSuccess = false;
                                        if (timer != null) {
                                            timer.cancel();
                                            timer = null;
                                        }
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                case 10:                                                           // 레벨 10 : 10초마다 한번 등장하는 공과 네모로 통과하기
                    goal = 0; stop_x = 100; stop_y = 100;
                    textView2.setText("레벨 " + level_num);
                    textView3.setText("목표 : 10초마다 한번 등장하는 공과 네모로 통과하기");
                    textView4.setVisibility(View.GONE);
                    textView5.setVisibility(View.VISIBLE);
                    checkIn();
                    if (timer == null) {
                        startCountdownTimer();
                    }
                    if (goal == 1 && !levelSuccess) {
                        levelSuccess = true;
                        canvas.drawCircle(stop_x, stop_y, r, paint2);
                        textView.setText("들어옴");
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                textView4.setText("레벨 " + level_num + " 통과!");
                                textView4.setVisibility(View.VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        textView4.setVisibility(View.GONE);
                                        level_num += 1;
                                        levelSuccess = false;
                                        if (timer != null) {
                                            timer.cancel();
                                            timer = null;
                                        }
                                        invalidate();
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                    break;
                default:            // 게임 종료 후
                    goal = 1;
                    textView4.setText("모든 단계를 통과했습니다! \n 축하합니다!");      // textview 표시
                    textView4.setVisibility(View.VISIBLE);
                    break;
            }
            if (goal == 0 && !levelSuccess){        // 공이 들어오지 않은 경우 원 그리기 계속
                textView.setText("안 들어옴");
                canvas.drawCircle(cx,cy,r, paint);
            }
        }

        private void startCountdownTimer() {                     // 카운트다운 타이머 함수
            if (level_num == 5 || level_num == 6 || level_num == 8 || level_num == 9 || level_num == 10) {
                timenum = 10000;
            } else if (level_num == 7){                          // 5,6,8,9,10레벨에서는 10초, 7레벨에서 20초 제한
                timenum = 20000;
            }
            timer = new CountDownTimer(timenum, 1000) {     // 1초씩 카운트다운
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    long hours = seconds / 3600;
                    long minutes = (seconds % 3600) / 60;
                    seconds = seconds % 60;

                    String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);   // 초 계산 후 표시
                    textView5.setText("Timer: " + timeString);
                }
                @Override
                public void onFinish() {
                    textView5.setText("Timer: 0 seconds");
                    switch (level_num){
                        case 5:
                            updateSquarePosition();     // 사각형 랜덤 위치 업데이트
                            break;
                        case 6:
                            updateCirclePosition();     // 원 랜덤 위치 업데이트
                            break;
                        case 7:
                            randomSquarePosition();     // 4개 사각형 랜덤 위치
                            break;
                        case 8:
                            toggleSquareColor();        // 사각형 색 전환 - 0.5초 딜레이 - 색 전환
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {toggleSquareColor();}
                            }, 500);
                            break;
                        case 9:
                            toggleCircleColor();        // 원 색 전환 - 0.8초 딜레이 - 색 전환
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {toggleCircleColor();}
                            }, 800);
                            break;
                        case 10:
                            toggleSquCirColor();        // 사각형과 원 색 전환 - 0.8초 딜레이 - 색 전환
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toggleSquCirColor();
                                }
                            }, 800);
                            break;
                    }
                    startCountdownTimer();
                }
            }.start();
        }
        private void updateSquarePosition() {
            Random random = new Random();
            rx = random.nextInt(layoutWidth - w);       // 사각형 랜덤 좌표
            ry = random.nextInt(layoutHeight - w);
            invalidate();
        }
        private void updateCirclePosition() {
            Random random = new Random();
            cx = random.nextInt(layoutWidth - w);       // 원 랜덤 좌표
            cy = random.nextInt(layoutHeight - w);
            rx = random.nextInt(layoutWidth - w);       // 사각형 랜덤 좌표
            ry = random.nextInt(layoutHeight - w);
            invalidate();
        }

        private void randomSquarePosition() {
            Random random = new Random();
            rx = random.nextInt(layoutWidth - w);       // 기본 사각형 랜덤 좌표
            ry = random.nextInt(layoutHeight - w);
            rx1 = random.nextInt(layoutWidth - w);       // 가짜 사각형 랜덤 좌표
            ry1 = random.nextInt(layoutHeight - w);
            rx2 = random.nextInt(layoutWidth - w);
            ry2 = random.nextInt(layoutHeight - w);
            rx3 = random.nextInt(layoutWidth - w);
            ry3 = random.nextInt(layoutHeight - w);
            rx4 = random.nextInt(layoutWidth - w);
            ry4 = random.nextInt(layoutHeight - w);
            invalidate();
        }
        private void toggleSquareColor() {
            SquareAppears = !SquareAppears;
            invalidate();
        }
        private void toggleCircleColor() {
            CircleAppears = !CircleAppears;
            invalidate();
        }

        private void toggleSquCirColor() {
            SquareAppears = !SquareAppears;
            CircleAppears = !CircleAppears;
            invalidate();
        }
    }

    final SensorEventListener sensorEventListener = new SensorEventListener() {         // 기본 코드
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                dx = -(int)(sensorEvent.values[0] * speed);
                dy = (int)(sensorEvent.values[1] * speed);

                if (cx - r + dx > 0 && cx + r + dx < layoutWidth) {
                    cx = cx + dx;
                }
                if (cy - r + dy > 0 && cy + r + dy < layoutHeight) {
                    cy = cy + dy;
                }
                myGraphicView.invalidate();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
}