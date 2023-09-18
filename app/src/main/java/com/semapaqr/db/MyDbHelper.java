package com.semapaqr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.semapaqr.activities.ModelBusinessAssets;

import java.util.ArrayList;

public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Constants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade database (if there any structure change the change db version)

        //drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        //create table again
        onCreate(db);
    }

    public long insertBusinessAssets(String codigo_activo, String nombre_activo, String tipo_activo, String descripcion_activo,
                                     String sector_activo, String encargado_activo, String addedTime, String updateTime ){

        //get writable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //id will be automatically as we set AUTOINCREMENT in query

        //insert data
        values.put(Constants.C_CODIGO_ACTIVO, codigo_activo);
        values.put(Constants.C_NOMBRE_ACTIVO, nombre_activo);
        values.put(Constants.C_TIPO_ACTIVO, tipo_activo);
        values.put(Constants.C_DESCRIPCION_ACTIVO, descripcion_activo);
        values.put(Constants.C_SECTOR_ACTIVO, sector_activo);
        values.put(Constants.C_ENCARGADO_ACTIVO, encargado_activo);
        values.put(Constants.C_ADDED_TIMESTAMP, addedTime);
        values.put(Constants.C_UPDATED_TIMESTAMP, updateTime);


        //insert row, it will return record id of saved record
        long id = db.insert(Constants.TABLE_NAME, null, values);

        //close db connection
        db.close();
        //return id of inserted busines asset
        return id;

    }

    public void updateBusinessAsset(String id, String codigo_activo, String nombre_activo, String tipo_activo, String descripcion_activo,
                             String sector_activo, String encargado_activo, String addedTime, String updateTime ){
        //get writable database because we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //id will be automatically as we set AUTOINCREMENT in query

        //insert data
        values.put(Constants.C_CODIGO_ACTIVO, codigo_activo);
        values.put(Constants.C_NOMBRE_ACTIVO, nombre_activo);
        values.put(Constants.C_TIPO_ACTIVO, tipo_activo);
        values.put(Constants.C_DESCRIPCION_ACTIVO, descripcion_activo);
        values.put(Constants.C_SECTOR_ACTIVO, sector_activo);
        values.put(Constants.C_ENCARGADO_ACTIVO, encargado_activo);
        values.put(Constants.C_ADDED_TIMESTAMP, addedTime);
        values.put(Constants.C_UPDATED_TIMESTAMP, updateTime);

        //insert row, it will return record id of saved record
        db.update(Constants.TABLE_NAME, values, Constants.C_ID + " = ?", new String[]{id});

        //close db connection
        db.close();
    }

    public ArrayList<ModelBusinessAssets> getAllBusinessAssets(String orderBy){

        ArrayList<ModelBusinessAssets> recordsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                ModelBusinessAssets modelRecord = new ModelBusinessAssets(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CODIGO_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_NOMBRE_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIPO_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_DESCRIPCION_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_SECTOR_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_ENCARGADO_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP))
                );

                //add record to list
                recordsList.add(modelRecord);
            }while (cursor.moveToNext());
        }
        db.close();

        return recordsList;
    }

    public ArrayList<ModelBusinessAssets> searchBusinessAsset(String query){

        ArrayList<ModelBusinessAssets> recordsList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_CODIGO_ACTIVO + " LIKE '%" + query + "%'" + " OR " + Constants.C_ENCARGADO_ACTIVO + " LIKE '%" + query + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                ModelBusinessAssets modelRecord = new ModelBusinessAssets(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CODIGO_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_NOMBRE_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_TIPO_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_DESCRIPCION_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_SECTOR_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_ENCARGADO_ACTIVO)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP))
                );

                //add record to list
                recordsList.add(modelRecord);
            }while (cursor.moveToNext());
        }
        db.close();

        return recordsList;
    }

    //delete data using id
    public void deleteData(String id, String codigo){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.C_ID + " = ?", new String[]{id});
        db.close();
    }

    //delete all data from table
    public  void deleteAllData(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.TABLE_NAME);
        db.close();
    }

    public int getBusinessAssetCount(){
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }
}
