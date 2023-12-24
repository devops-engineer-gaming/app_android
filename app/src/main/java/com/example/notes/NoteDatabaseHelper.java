package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;
import java.util.ArrayList;

public class NoteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE = "note";

    public NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTE + " TEXT)";
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public void addNote(String note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE, note);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    public ArrayList<String> getAllNotes() {
        ArrayList<String> notesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String note = cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));
                notesList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notesList;
    }
    public void updateNote(int position, String noteText) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("note_text", noteText);

        // Обновляем запись в базе данных по позиции
        db.update("notes", values, "id = ?", new String[]{String.valueOf(position + 1)});

        // Закрываем соединение с базой данных
        db.close();
    }
}