package com.example.votingproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class sqlmanager extends SQLiteOpenHelper {

    Context co ;
    SQLiteDatabase db = this.getWritableDatabase();
    public sqlmanager(@Nullable Context context)
    {
        super(context, "DocMart.db",null, 1);
        this.co = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {}
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    public void login(String email,String password)
    {
        try
        {
            db.execSQL("create table if not exists login(email text,password text)");
            ContentValues cv = new ContentValues();
            cv.put("email",email);
            cv.put("password",password);
            db.insert("login",null,cv);
        }
        catch(Exception e )
        {
            System.out.println("Error at sqlmanager 35"+e);
        }

    }
    public int checklog()
    {
        db.execSQL("create table if not exists login(email text,password text)");
        Cursor c =db.rawQuery("select * from login",null);
        if(c.getCount()==0)
        {
            return 0;
        }
        else{
            return 1;
        }
    }
    public String getemail(){
        try
        {
            Cursor c =db.rawQuery("select * from login",null);
            c.moveToNext();
            return c.getString(0);
        }
        catch(Exception e)
        {
            System.out.println("Error at 54 sqlmanager "+e);
        }
        return "";
    }
    public String getpassword(){
        try
        {
            Cursor c =db.rawQuery("select * from login",null);
            c.moveToNext();
            return c.getString(1);
        }
        catch(Exception e){
            System.out.println("Error at 70 sqlmanager "+e);
        }
        return "";
    }
    public void droplogin(){
        try
        {
            db.delete("login",null,null);
        }
        catch(Exception e){
            System.out.println("Error at 82 sql "+e);
        }
    }
}
