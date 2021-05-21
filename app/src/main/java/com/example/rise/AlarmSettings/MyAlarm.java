package com.example.rise.AlarmSettings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyAlarm {
    Context context;
    final static int RQS_TIME = 1;
    int hour;
    int minute;
    List<Integer> hourList = new ArrayList<>();
    List<Integer> minuteList = new ArrayList<>();

    public MyAlarm(Context context, int hour, int minute) {
        this.context = context;
        this.hour = hour;
        this.minute = minute;

        //hourList.add(hour);
        //minuteList.add(minute);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createAlarm(){

        // Время сейчас
        Calendar calNow = Calendar.getInstance();
        // Время будильника
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hour);
        calSet.set(Calendar.MINUTE, minute);

        // Если выбранное время на сегодня прошло, то переносим на завтра
        if (calSet.compareTo(calNow) <= 0) {
            calSet.add(Calendar.DATE, 1);
        }

        setAlarm(calSet);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setAlarm(Calendar targetCal) {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, RQS_TIME, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
                pendingIntent);


        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                targetCal.getTimeInMillis(), pendingIntent);

    }


    public void cancelAlarm() {

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, RQS_TIME, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public String informForFragmentString(){
        String time = "";
        String getHour = "";
        String getMinute = "";
        int razn = 0;

        // Время сейчас
        Calendar calNow = Calendar.getInstance();
        int nowHour = calNow.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calNow.get(Calendar.MINUTE);

        if (nowHour < hour){
            razn = hour - nowHour;
            getHour = Integer.toString(razn);
        }

        if (nowHour > hour){
            razn = 24 - (nowHour - hour);
            getHour = Integer.toString(razn);
        }

        if (nowHour == hour){
            getHour = "23";
        }

        if (nowMinute < minute){
            razn = minute - nowMinute;
            getMinute = Integer.toString(razn);
        }

        if (nowMinute > minute){
            razn = 60 - (nowMinute - minute);
            getMinute = Integer.toString(razn);
        }

        if (nowMinute == minute){
            getMinute = "59";
        }

        if (nowHour == hour && nowMinute < minute){
            getHour = "0";
        }

        time = getHour + " ч " + getMinute + " мин";

        return time;
    }

    public int informForFragmentInt(){
        int getHour = 0;

        // Время сейчас
        Calendar calNow = Calendar.getInstance();
        int nowHour = calNow.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calNow.get(Calendar.MINUTE);

        if (nowHour < hour){
            getHour = hour - nowHour;
        }

        if (nowHour > hour){
            getHour = 24 - (nowHour - hour);
        }

        if (nowHour == hour){
            getHour = 23;
        }

        if (nowHour == hour && nowMinute < minute){
            getHour = 0;
        }


        return getHour;
    }

}
