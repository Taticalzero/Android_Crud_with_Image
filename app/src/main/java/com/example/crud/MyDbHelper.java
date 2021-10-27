package com.example.crud;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDbHelper extends SQLiteOpenHelper {


    public MyDbHelper(@Nullable Context context) {
        super(context, ConstBanco.DB_NAME, null, ConstBanco.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ConstBanco.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ConstBanco.TABLE_NAME);

        onCreate(db);

    }

    public long insertRecord(String name , String image, String email, String senha, String idade,
                             String telefone, String endereco,String addTime, String updatedTime){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ConstBanco.C_NAME, name);
        values.put(ConstBanco.C_IMAGE, image);
        values.put(ConstBanco.C_EMAIL, email);
        values.put(ConstBanco.C_SENHA, senha);
        values.put(ConstBanco.C_IDADE, idade);
        values.put(ConstBanco.C_TELEFONE, telefone);
        values.put(ConstBanco.C_ENDERECO, endereco);
        values.put(ConstBanco.C_ADDED_TIMESTAMP, addTime);
        values.put(ConstBanco.C_UPDATED_TIMESTAMP, updatedTime);

        long id = db.insert(ConstBanco.TABLE_NAME, null,values);

        db.close();

        return id;
    }

    public void updateRecord(String id , String name , String image, String email, String senha, String idade,
                             String telefone, String endereco,String addTime, String updatedTime){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ConstBanco.C_NAME, name);
        values.put(ConstBanco.C_IMAGE, image);
        values.put(ConstBanco.C_EMAIL, email);
        values.put(ConstBanco.C_SENHA, senha);
        values.put(ConstBanco.C_IDADE, idade);
        values.put(ConstBanco.C_TELEFONE, telefone);
        values.put(ConstBanco.C_ENDERECO, endereco);
        values.put(ConstBanco.C_ADDED_TIMESTAMP, addTime);
        values.put(ConstBanco.C_UPDATED_TIMESTAMP, updatedTime);

        db.update(ConstBanco.TABLE_NAME,values,ConstBanco.C_ID +" = ?", new String[]{id});

        db.close();

    }

    public ArrayList<ModelRecord> getAllRecords(String orderBy){
        ArrayList<ModelRecord> recordsList = new ArrayList<>();

        String selectQuery = " SELECT * FROM " + ConstBanco.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") ModelRecord modelRecord = new ModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(ConstBanco.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_ENDERECO)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_TELEFONE)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_IDADE)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_UPDATED_TIMESTAMP))
                );

                recordsList.add(modelRecord);
            }while (cursor.moveToNext());
        }
        db.close();
        return recordsList;
    }

    public ArrayList<ModelRecord> searchAllRecords(String query){
        ArrayList<ModelRecord> recordsList = new ArrayList<>();

        String selectQuery = " SELECT * FROM " + ConstBanco.TABLE_NAME + " WHERE " + ConstBanco.C_NAME + " LIKE '%" + query + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") ModelRecord modelRecord = new ModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(ConstBanco.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_ENDERECO)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_TELEFONE)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_IDADE)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(ConstBanco.C_UPDATED_TIMESTAMP))
                );

                recordsList.add(modelRecord);
            }while (cursor.moveToNext());
        }
        db.close();
        return recordsList;
    }

    public void deleteData(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ConstBanco.TABLE_NAME,ConstBanco.C_ID + " = ?",new String[]{id});
        db.close();
    }
    public void deleteAllData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" DELETE FROM " + ConstBanco.TABLE_NAME);
        db.close();
    }
    public int getRecordsCount(){
        String countQuery = " SELECT * FROM " + ConstBanco.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }
}
