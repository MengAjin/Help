package com.example.contactList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmergencyContactsDB extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "EmergencyContacts.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "EmergencyContacts_table";
    public final static String EC_ID = "EmergencyContacts_id";
    public final static String EC_ACCOUNT = "EmergencyContacts_account";
    public final static String EC_NICKNAME = "EmergencyContacts_name";
    public final static String EC_IMAGE = "EmergencyContacts_image";

    public EmergencyContactsDB(Context context) {
// TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //fa创建table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + EC_ID
                + " INTEGER primary key autoincrement, " + EC_ACCOUNT + " long, "+
                EC_NICKNAME + " varchar(45), "+ EC_IMAGE +" varchar(200));";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public Cursor select() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db
                .query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }
    //增加操作
    public long insert(long account,String name,String image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
/* ContentValues */
        ContentValues cv = new ContentValues();
        cv.put(EC_ACCOUNT, account);
        cv.put(EC_NICKNAME, name);
        cv.put(EC_IMAGE, image);
        long row = db.insert(TABLE_NAME, null, cv);
        return row;
    }
    //删除操作
    public void delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = EC_ID + " = ?";
        String[] whereValue ={ Integer.toString(id) };
        db.delete(TABLE_NAME, where, whereValue);
    }
    //修改操作
    public void update(int id, String account,String name,String image)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = EC_ACCOUNT + " = ?";
        String[] whereValue = { Integer.toString(id) };

        ContentValues cv = new ContentValues();
        cv.put(EC_ACCOUNT, account);
        cv.put(EC_NICKNAME, name);
        cv.put(EC_IMAGE, image);
        db.update(TABLE_NAME, cv, where, whereValue);
    }
}
