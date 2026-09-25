package com.example.lucaspanucciprova.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBTarefas extends SQLiteOpenHelper {
    private static final int VERSAO = 3;
    public DBTarefas(Context context) {
        super(context, "tarefas.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tarefa " +
                "(tar_id  INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tar_texto VARCHAR(30)," +
                "tar_data VARCHAR(20), " +
                "tar_temporal INTEGER, tar_concluida INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL ("DROP TABLE IF EXISTS tarefa");
        onCreate(db);

    }
}

