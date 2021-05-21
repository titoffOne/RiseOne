package com.example.rise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rise.AlarmSettings.MyAlarm;
import com.example.rise.MyDataBase.DataBaseManager;

public class DisableAlarm extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;        // Объект для работы с датчиком
    private boolean color = false;              // Индикатор текущего цвета: false-зеленый, true - красный.
    private long lastUpdate;                    // Время последнего изменения состояния датчика
    private TextView tv_inform, tv_name;        // Надпись "Тряси", имя будильника
    private ConstraintLayout cl;                // ConstraintLayout - фон приложения
    private ProgressBar progressBar;            // ProgressBar - строка состояния тряски
    private int progressMax = 5;                // Максимальный прогресс
    int indProgress = 0;                        // Состояние прогресса
    private Button bt_off;                      // Кнопка отключения
    int shakingForce = 2;                       // Сила тряски

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disable_alarm);

        // Убрать ActionBar (Строка сверху с названием приложения)
        getSupportActionBar().hide();

        // Находим элементы
        tv_inform = findViewById(R.id.tv_inform);
        tv_name = findViewById(R.id.tv_name);
        cl = findViewById(R.id.cl);
        progressBar = findViewById(R.id.progressBar);
        bt_off = findViewById(R.id.bt_off);
        // Утсанавливаем начальный фон экрана
        cl.setBackgroundColor(Color.BLUE);
        // Устанавливаем максимальный прогресс
        progressBar.setMax(progressMax);

        // Создаем объект для работы с датчиками
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Регистрируем класс, где будет реализован метод, вызываемый при изменении
        // состояния датчика
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();

        // Звук
        MediaPlayer mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();

        // Вибрация
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100000);

        // Слушатель на кноку отключения
        bt_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancelAlarm();
                mediaPlayer.stop();
                vibrator.cancel();
                finish();
            }
        });
    }

    // Выключить будильник
    private void cancelAlarm(){
        DataBaseManager dataBaseManager = null;
        MyAlarm myAlarm = new MyAlarm(this, dataBaseManager.getHour(), dataBaseManager.getMinute());
        myAlarm.cancelAlarm();
    }

    /*private int setSound(){
        return dataBaseManager.getSound();
    }

    private int setVibration(){
        return dataBaseManager.getVibration();
    }*/




    @Override
    protected void onResume() {
        super.onResume();
        // регистрируем прослушивание на датчики ориентации и акселерометра
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // отмена регистрации слушателя
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // отмена регистрации слушателя
        sensorManager.unregisterListener((SensorEventListener) this);
    }


    // Регагирование датчиков движения
    @SuppressLint("ResourceAsColor")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            // проекции ускорения на оси системы координат
            float x = values[0];
            float y = values[1];
            float z = values[2];

            // Квадрат модуля ускорения телефона, деленный на квадрат
            // ускорения свободного падения
            float accelationSquareRoot = (x * x + y * y + z * z)
                    / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            // Текущее время
            long actualTime = System.currentTimeMillis();


            // Если тряска сильная
            if (accelationSquareRoot >= shakingForce) {

                // Если с момента начала тряски прошло меньше 1000
                // миллисекунд (1 секунды) - выходим из обработчика
                if (actualTime - lastUpdate < 1000) {
                    return;
                }
                lastUpdate = actualTime;

                // Меняем цвет, увеличиваем индикатор прогресса
                if (color) {
                    cl.setBackgroundColor(Color.BLUE);
                    indProgress++;
                } else {
                    cl.setBackgroundColor(Color.BLACK);
                    indProgress++;
                }
                color = !color;
                progressBar.setProgress(indProgress);

                if (progressBar.getProgress() == progressMax){
                    //Toast.makeText(this, "Выполнено!", Toast.LENGTH_LONG).show();
                    bt_off.setVisibility(View.VISIBLE);
                    changeShakingForce();
                }
            }

        }
    }

    // Метод,
    private void changeShakingForce(){
        shakingForce = 100;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}