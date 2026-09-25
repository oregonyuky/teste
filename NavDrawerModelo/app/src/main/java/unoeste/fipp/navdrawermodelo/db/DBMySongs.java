package unoeste.fipp.navdrawermodelo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBMySongs extends SQLiteOpenHelper {
    private static final int VERSAO = 2;
    public DBMySongs(Context context) {
        super(context, "mysongs.db", null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE genero( gen_id INTEGER PRIMARY KEY AUTOINCREMENT, gen_nome VARCHAR (15), gen_cor INTEGER);");

        db.execSQL("CREATE TABLE musica " +
           "(mus_id  INTEGER PRIMARY KEY AUTOINCREMENT, " +
           "mus_ano INTEGER, mus_titulo VARCHAR (40), " +
           "mus_interprete VARCHAR (30), " +
           "mus_genero INTEGER REFERENCES genero (gen_id), " +
           "mus_duracao NUMERIC (4, 1) );");
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE genero ADD COLUMN gen_cor INTEGER NOT NULL DEFAULT 0");
        }
    }
}

