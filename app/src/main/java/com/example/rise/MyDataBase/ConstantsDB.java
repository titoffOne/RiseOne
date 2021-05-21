package com.example.rise.MyDataBase;

public class ConstantsDB {

        // Название базы данных
        public static final String DB_NAME = "mydb.db";
        // Версия базы данных
        public static final int DB_VERSION = 4;

        // Название таблицы
        public static final String TABLE_NAME = "mytable";
        // Параметры таблицы
        public static final String _ID = "_id";
        public static final String HOUR = "hour";
        public static final String MINUTE = "minute";
        public static final String TITLE = "title"; // название будильника
        public static final String TIME = "time"; // время будильника
        public static final String DESCRIPTION = "description"; // описание будильника
        public static final String SOUND = "sound"; // звук
        public static final String VIBRATION = "vibration"; // вибрация

        // Создать таблицу + структрура таблицы
        public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + HOUR + " INTEGER," + MINUTE + " INTEGER,"
                + TITLE + " TEXT," + TIME + " TEXT," +
                DESCRIPTION + " TEXT," + SOUND + " INTEGER," + VIBRATION + " INTEGER)";
        // Удалить таблицу
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


        public static String PB_TIME = "--:--";

}
