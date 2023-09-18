package com.semapaqr.db;

public class Constants {

    public static final String DB_NAME = "MY_SCANBARCODE_DB";
    //DB VERSION
    public static final int DB_VERSION = 1;
    //table name
    public static final String TABLE_NAME = "ACTIVOS_FIJOS";
    //    columns field of table
    public static final String C_ID = "ID";
    public static final String C_CODIGO_ACTIVO = "CODIGO_ACTIVO";
    public static final String C_NOMBRE_ACTIVO = "NOMBRE_ACTIVO";
    public static final String C_TIPO_ACTIVO = "TIPO_ACTIVO";
    public static final String C_DESCRIPCION_ACTIVO = "DESCRIPCION_ACTIVO";
    public static final String C_SECTOR_ACTIVO = "SECTOR_ACTIVO";
    public static final String C_ENCARGADO_ACTIVO = "ENCARGADO_ACTIVO";
    public static final String C_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";
    public static final String C_UPDATED_TIMESTAMP = "UPDATED_TIME_STAMP";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_CODIGO_ACTIVO + " TEXT UNIQUE,"
            + C_NOMBRE_ACTIVO + " TEXT,"
            + C_TIPO_ACTIVO + " TEXT,"
            + C_DESCRIPCION_ACTIVO + " TEXT,"
            + C_SECTOR_ACTIVO + " TEXT,"
            + C_ENCARGADO_ACTIVO + " TEXT,"
            + C_ADDED_TIMESTAMP + " TEXT,"
            + C_UPDATED_TIMESTAMP + " TEXT"
            + ")";

}
