package com.android.appforlanguage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataBase  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Database.db";

    public DataBase(Context context) {
        super(context, Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void createTable(SQLiteDatabase db, String tableName) {
        String createTable = "CREATE TABLE [" + tableName + "] (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Word TEXT, " +
                "Translate TEXT)";
        db.execSQL(createTable);
    }
    public boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean tableExists = cursor.getCount() > 0;
        cursor.close();
        return tableExists;
    }
    public List<String> getTableNames(SQLiteDatabase db) {
        List<String> tableNames = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT IN ('android_metadata', 'sqlite_sequence')", null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                tableNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return tableNames;
    }
    public void insertWord(SQLiteDatabase db, String tableName, String word, String translate) {
        ContentValues values = new ContentValues();
        values.put("Word", word);
        values.put("Translate", translate);
        db.insert("[" + tableName + "]", null, values);
    }
    public List<Word> getWordsFromTable(SQLiteDatabase db, String tableName) {
        List<Word> words = new ArrayList<>();
        String query = "SELECT ID, Word, Translate FROM [" + tableName + "]";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String word = cursor.getString(cursor.getColumnIndexOrThrow("Word"));
                String translation = cursor.getString(cursor.getColumnIndexOrThrow("Translate"));
                words.add(new Word(id, word, translation));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return words;
    }

    public List<Word> getAllWords(SQLiteDatabase db) {
        List<Word> words = new ArrayList<>();
        List<String> tableNames = getTableNames(db);
        for (String tableName : tableNames) {
            words.addAll(getWordsFromTable(db, tableName));
        }
        return words;
    }
}
