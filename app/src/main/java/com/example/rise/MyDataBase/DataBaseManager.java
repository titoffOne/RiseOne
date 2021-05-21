package com.example.rise.MyDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rise.Adapter.ListItem;

import java.util.ArrayList;
import java.util.List;

// Вспомогательный класс, который управлет значениеями БД
public class DataBaseManager {

    // Создаем контекст и объект класса DataBaseHelper
    private Context context;
    private DataBaseHelper dataBaseHelper;
    // База данных, которую будем открывать
    private SQLiteDatabase db;

    // Конструктор для context
    public DataBaseManager(Context context) {
        this.context = context;
        // Передаем context в конструктор класса DataBaseHelper
        dataBaseHelper = new DataBaseHelper(context);
    }

    // Метод, открывающий БД
    public void openDb(){
        db = dataBaseHelper.getWritableDatabase();
    }

    // Метод, добавдяющий в БД имя, время и параметры (описание) будильника
    // Получет в себя время, имя, параметры будильника
    public void insertToDb(int hour, int minute, String time, String title, String description,
                           int sound, int vibration){
        // Создаём объект класса ContentValues
        ContentValues cv = new ContentValues();
        // С помощью объекта класса ContentValues добавляем в него значениия
        cv.put(ConstantsDB.HOUR,hour);
        cv.put(ConstantsDB.MINUTE, minute);
        cv.put(ConstantsDB.TIME, time);
        cv.put(ConstantsDB.TITLE, title);
        cv.put(ConstantsDB.DESCRIPTION, description);
        cv.put(ConstantsDB.SOUND, sound);
        cv.put(ConstantsDB.VIBRATION, vibration);

        // Добавляем в БД значения, которые уже лежат в ContentValues
        db.insert(ConstantsDB.TABLE_NAME, null, cv);
    }

    // Метод, возвращающий значения врмени, названия и описания будильника из БД
    public List<ListItem> getInformAlarmFromDb(){
        // Массив в котором будут содержаться все значения будильника
        List<ListItem> listItems = new ArrayList<>();

        // Создаем объект класса курсор, которому указываем имя таблицы БД,
        // из которой будем считывать данные будильника
        Cursor cursor = db.query(ConstantsDB.TABLE_NAME, null, null, null,
                null, null, null, null);

        // Цикл, который проёдется по всем элементам, лежащим в курсоре и вернет нам их значения
        while (cursor.moveToNext()){
            // Создаем объект класса ListItem
            ListItem item = new ListItem();
            // В переменные добавляем значения врмени(time), названия(title) и описания(description) будильника
            int _id = cursor.getInt(cursor.getColumnIndex(ConstantsDB._ID));
            int hour = cursor.getInt(cursor.getColumnIndex(ConstantsDB.HOUR));
            int minute = cursor.getInt(cursor.getColumnIndex(ConstantsDB.MINUTE));
            String time = cursor.getString(cursor.getColumnIndex(ConstantsDB.TIME));
            String title = cursor.getString(cursor.getColumnIndex(ConstantsDB.TITLE));
            String description = cursor.getString(cursor.getColumnIndex(ConstantsDB.DESCRIPTION));
            // Передаём значения будильника в сеттеры класса ListItem
            item.setId(_id);
            item.setHour(hour);
            item.setMinute(minute);
            item.setTime(time);
            item.setTitle(title);
            item.setDescription(description);
            // Добавляем в listItems все значения будильника
            listItems.add(item);
        }
        // Закрываем курсор, чтобы не потреблял ресурсы приложения
        cursor.close();

        // Возвращаем массив значений будильника
        return listItems;
    }

    // Метод, возвращающий значения врмени (минут) послденего установленного будильника из БД
    public int getHour(){
        // int в котором будут содержаться утсановленный час будильника
        int hour = 0;

        // Создаем объект класса курсор, которому указываем имя таблицы БД,
        // из которой будем считывать данные будильника
        Cursor cursor = db.query(ConstantsDB.TABLE_NAME, null, null, null,
                null, null, null, null);

        // Цикл, который проёдется по всем элементам, лежащим в курсоре и вернет нам их значения
        while (cursor.moveToNext()){
            hour = cursor.getInt(cursor.getColumnIndex(ConstantsDB.HOUR));
        }
        // Закрываем курсор, чтобы не потреблял ресурсы приложения
        cursor.close();

        // Возвращаем  утсановленный час будильника
        return hour;
    }

    // Метод, возвращающий значения врмени (минут) послденего установленного будильника из БД
    public int getMinute(){
        // int в котором будут содержаться установленные минуты будильника
        int minute = 0;

        // Создаем объект класса курсор, которому указываем имя таблицы БД,
        // из которой будем считывать данные будильника
        Cursor cursor = db.query(ConstantsDB.TABLE_NAME, null, null, null,
                null, null, null, null);

        // Цикл, который проёдется по всем элементам, лежащим в курсоре и вернет нам их значения
        while (cursor.moveToNext()){
            minute = cursor.getInt(cursor.getColumnIndex(ConstantsDB.MINUTE));
        }
        // Закрываем курсор, чтобы не потреблял ресурсы приложения
        cursor.close();

        // Возвращаем установленные минуты будильника
        return minute;
    }

    // Метод, возвращающий имя послденего установленного будильника из БД
    public String getName(){
        // String в котором будет содержаться имя будильника
        String name = "";

        // Создаем объект класса курсор, которому указываем имя таблицы БД,
        // из которой будем считывать данные будильника
        Cursor cursor = db.query(ConstantsDB.TABLE_NAME, null, null, null,
                null, null, null, null);

        // Цикл, который проёдется по всем элементам, лежащим в курсоре и вернет нам их значения
        while (cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndex(ConstantsDB.TITLE));
        }
        // Закрываем курсор, чтобы не потреблял ресурсы приложения
        cursor.close();

        // Возвращаем установленное имя будильника
        return name;
    }

    // Метод, возвращающий значения звука послденего установленного будильника из БД
    public int getSound(){
        // int в котором будут содержаться звук будильника
        int sound = 0;

        // Создаем объект класса курсор, которому указываем имя таблицы БД,
        // из которой будем считывать данные будильника
        Cursor cursor = db.query(ConstantsDB.TABLE_NAME, null, null, null,
                null, null, null, null);

        // Цикл, который проёдется по всем элементам, лежащим в курсоре и вернет нам их значения
        while (cursor.moveToNext()){
            sound = cursor.getInt(cursor.getColumnIndex(ConstantsDB.SOUND));
        }
        // Закрываем курсор, чтобы не потреблял ресурсы приложения
        cursor.close();

        // Возвращаем звук будильника
        return sound;
    }

    // Метод, возвращающий значения звука послденего установленного будильника из БД
    public int getVibration(){
        // int в котором будут содержаться звук будильника
        int vibration = 0;

        // Создаем объект класса курсор, которому указываем имя таблицы БД,
        // из которой будем считывать данные будильника
        Cursor cursor = db.query(ConstantsDB.TABLE_NAME, null, null, null,
                null, null, null, null);

        // Цикл, который проёдется по всем элементам, лежащим в курсоре и вернет нам их значения
        while (cursor.moveToNext()){
            vibration = cursor.getInt(cursor.getColumnIndex(ConstantsDB.VIBRATION));
        }
        // Закрываем курсор, чтобы не потреблял ресурсы приложения
        cursor.close();

        // Возвращаем звук будильника
        return vibration;
    }


    // Метод, удаляющий из базы данных элементы по id
    public void deleteFromDb(int id){
        // Переменая, в которой содержится id элементов, которые нужно будет удалить
        String selection = ConstantsDB._ID + "=" + id;
        // функция, удаляющая из БД элементы по id
        db.delete(ConstantsDB.TABLE_NAME, selection, null);
    }

    // Метод, закрывающий БД
    public void closeDb(){
        dataBaseHelper.close();
    }
}
