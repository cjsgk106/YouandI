package com.example.andorid.youandi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static  final String DB_Name = "mylist.db";
    public static final String DB_Table = "mylist_data";
    public static final String COL1 = "ID";
    public static final String COL2 = "ITEM1";
    public DatabaseHelper(Context context){ super(context, DB_Name, null, 1);}


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ DB_Table + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+ "ITEM1 TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + DB_Table);

    }
    public boolean addData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        long result = db.insert(DB_Table, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + DB_Table, null);
        return data;
    }
}
