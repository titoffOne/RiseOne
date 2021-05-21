package com.example.rise;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rise.Adapter.ListItem;
import com.example.rise.MyDataBase.ConstantsDB;
import com.example.rise.MyDataBase.DataBaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Create_Alarm extends AppCompatActivity implements View.OnClickListener{
    private FloatingActionButton fab_on;
    private TimePicker timePicker;
    private EditText alarmName;
    private DataBaseManager dataBaseManager;
    private boolean EditState = true;
    private Switch sw_sound, sw_vibration;
    private String soundVibration = "";
    String sound = "Звук";
    String vibration = "Вибрация";
    int soundOn = 1;
    int vibrationOn = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_alarm);

        // Находим элементы
        initById();

        dataBaseManager = new DataBaseManager(this);

        fab_on.setVisibility(View.VISIBLE);
        fab_on.setOnClickListener(this);
        TimePickerSettings();
        getIntents();


        // Слушатель на звук
        sw_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sound = "";
                    sound = "Звук";
                    soundOn = 1;
                } else {
                    sound = "";
                    sound = "Без звука";
                    soundOn = 0;
                }
            }
        });

        // Слушатель на вибрацию
        sw_vibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    vibration = "";
                    vibration = "Вибрация";
                    vibrationOn = 1;
                } else {
                    vibration = "";
                    vibration = "Без вибрации";
                    vibrationOn = 0;
                }
            }
        });





        // Убрать ActionBar (Строка сверху с названием приложения)
        getSupportActionBar().hide();

    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBaseManager.openDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBaseManager.closeDb();
    }

    // Инициализация элементов активности по id
    private void initById() {
        fab_on = findViewById(R.id.fab_on);
        timePicker = findViewById(R.id.timePicker);
        alarmName = findViewById(R.id.alarmName);
        sw_sound = findViewById(R.id.sw_sound);
        sw_vibration = findViewById(R.id.sw_vibration);
    }

    // Слушатель на кнопкИ
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        // Создаем Intent
        Intent intent = new Intent();

        // Получаем данные будильника
        int getHour = timePicker.getHour();
        int getMinute = timePicker.getMinute();
        String getTime = getTime();
        String getName = alarmName.getText().toString();
        String getDesc = sound + ", " + vibration;

        switch (v.getId()) {
            case R.id.fab_on:
                setResult(RESULT_OK, intent);
                dataBaseManager.insertToDb(getHour, getMinute, getTime, getName, getDesc, soundOn, vibrationOn);
                Toast.makeText(this, "Будильник установлен на " + getTime, Toast.LENGTH_LONG).show();
                ConstantsDB.PB_TIME = getTime;
                finish();
                dataBaseManager.closeDb();
                break;
        }


    }



    // Настройка timepicker
    public void TimePickerSettings(){
        // Задаем timepicker настоящее время
        timePicker.setIs24HourView(true);
        Calendar now = Calendar.getInstance();
        timePicker.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(now.get(Calendar.MINUTE));


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getTime() {
        String hour = String.valueOf(timePicker.getHour());
        String min = String.valueOf(timePicker.getMinute());
        switch (hour) {
            case "0":
                hour = "00";
                break;
            case "1":
                hour = "01";
                break;
            case "2":
                hour = "02";
                break;
            case "3":
                hour = "03";
                break;
            case "4":
                hour = "04";
                break;
            case "5":
                hour = "05";
                break;
            case "6":
                hour = "06";
                break;
            case "7":
                hour = "07";
                break;
            case "8":
                hour = "08";
                break;
            case "9":
                hour = "09";
                break;
        }

        switch (min) {
            case "0":
                min = "00";
                break;
            case "1":
                min = "01";
                break;
            case "2":
                min = "02";
                break;
            case "3":
                min = "03";
                break;
            case "4":
                min = "04";
                break;
            case "5":
                min = "05";
                break;
            case "6":
                min = "06";
                break;
            case "7":
                min = "07";
                break;
            case "8":
                min = "08";
                break;
            case "9":
                min = "09";
                break;
        }

        String time = hour + ":" + min;

        return time;
    }

    // Метод, проверяющий какое действие мы хотим провести с активностью:
    // посмотреть информацию о уже созданном будильнике или создать новый
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getIntents(){
        
        Intent intent = getIntent();
        // Проверяем, пустой ли intent пришёл
        if (intent != null){
            // Получем инофрмацию из класса ListItem
            ListItem listItem = (ListItem) intent.getSerializableExtra("LIST_RV_INTENT");
            EditState = intent.getBooleanExtra("EDIT_STATE", true);

            // Если EditState == false, зачит мы открыли активность для просмотра данных
            if(EditState == false){
                alarmName.setText(listItem.getTitle());
                timePicker.setHour(listItem.getHour());
                timePicker.setMinute(listItem.getMinute());
                fab_on.setVisibility(View.INVISIBLE);
            }

        }
    }
}
