package com.example.andorid.youandi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static  final String DB_Name = "Users.db";
    private static final String DB_Table = "Users_Table";

    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String CREATE_TABLE = "CREATE TABLE " +DB_Table +" ("+ ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +NAME +" TEXT " + ")";

    public DatabaseHelper(Context context){
        super(context, DB_Name, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int l) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_Table);

        onCreate(sqLiteDatabase);
    }
    public boolean insertDATA(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);

        long result = db.insert(DB_Table, null, contentValues);
        return result != -1;
    }

    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * from "+ DB_Table;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
