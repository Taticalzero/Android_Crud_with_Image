package com.example.crud;

public class ConstBanco {

    //Identificaçao do ConstBanco
    public static final String DB_NAME = "MY_RECORDS_DB";
    public static final int DB_VERSION = 1;
    public static final String  TABLE_NAME = "MY_RECORDS_TABLE";

    //Dados inseridos na tabela

    public static final String C_ID = "ID";
    public static final String C_NAME = "NAME";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_EMAIL = "EMAIL";
    public static final String C_SENHA = "SENHA";
    public static final String C_IDADE = "IDADE";
    public static final String C_TELEFONE = "TELEFONE";
    public static final String C_ENDERECO = "ENDERECO";
    public static final String C_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";
    public static final String C_UPDATED_TIMESTAMP = "UPDATED_TIME_STAMP";

    // Busca e persistencia dos dados

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY,"
            + C_NAME + " TEXT,"
            + C_IMAGE + " TEXT,"
            + C_EMAIL + " TEXT,"
            + C_SENHA + " TEXT,"
            + C_IDADE + " TEXT,"
            + C_TELEFONE + " TEXT,"
            + C_ENDERECO + " TEXT,"
            + C_ADDED_TIMESTAMP + " TEXT,"
            + C_UPDATED_TIMESTAMP + " TEXT"
            +")";

}
