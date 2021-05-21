package com.example.rise.MyDataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper{

        // Конструктор
        public DataBaseHelper(@Nullable Context context) {
            // Передаём context от activity, название БД, версию БД
            super(context, ConstantsDB.DB_NAME, null, ConstantsDB.DB_VERSION);
        }

        // Метод, создающий таблицу в БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Передаём методу onCreate функцию создания таблицы
            // бд.добавитьБД(структура таблицы)
            db.execSQL(ConstantsDB.TABLE_STRUCTURE);
        }

        // Метод, обновляющий таблицу в БД
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Передаём методу onUpgrade функцию удаления таблицы
            // бд.добавитьБД(удаление таблицы)
            db.execSQL(ConstantsDB.DELETE_TABLE);
            // Снова создать БД
            onCreate(db);
        }

}
