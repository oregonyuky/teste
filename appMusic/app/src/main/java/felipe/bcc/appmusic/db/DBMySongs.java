package felipe.bcc.appmusic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBMySongs extends SQLiteOpenHelper {
    private static final int VERSAO = 5;
    public DBMySongs(Context context) {
        super(context, "mysongs.db", null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE genero (" +
                "gen_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "gen_nome VARCHAR(40) NOT NULL, " +
                "gen_cor INTEGER NOT NULL DEFAULT -12303292);");

        db.execSQL("CREATE TABLE musica " +
           "(mus_id  INTEGER PRIMARY KEY AUTOINCREMENT, " +
           "mus_ano INTEGER, mus_titulo VARCHAR (40), " +
           "mus_interprete VARCHAR (30), " +
           "mus_genero INTEGER REFERENCES genero (gen_id), " +
           "mus_duracao NUMERIC (4, 1) );");

        db.execSQL("INSERT INTO genero VALUES (null, 'Sertanejo', -33010)");
        db.execSQL("INSERT INTO genero VALUES (null, 'Pop', -5635841)");
        db.execSQL("INSERT INTO genero VALUES (null, 'Rock', -1023342)");
        db.execSQL("INSERT INTO genero VALUES (null, 'MPB', -10944513)");

        criarProtecaoContraCoresRepetidas(db);
        criarProtecaoContraNomesRepetidos(db);

     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE genero ADD COLUMN gen_cor INTEGER NOT NULL DEFAULT -12303292");
        }
        if (oldVersion < 4) {
            criarProtecaoContraCoresRepetidas(db);
        }
        if (oldVersion < 5) {
            criarProtecaoContraNomesRepetidos(db);
        }
    }

    private void criarProtecaoContraCoresRepetidas(SQLiteDatabase db) {
        db.execSQL("CREATE TRIGGER IF NOT EXISTS genero_cor_unica_insert " +
                "BEFORE INSERT ON genero " +
                "WHEN EXISTS (SELECT 1 FROM genero WHERE gen_cor = NEW.gen_cor) " +
                "BEGIN SELECT RAISE(IGNORE); END;");

        db.execSQL("CREATE TRIGGER IF NOT EXISTS genero_cor_unica_update " +
                "BEFORE UPDATE OF gen_cor ON genero " +
                "WHEN EXISTS (SELECT 1 FROM genero " +
                "WHERE gen_cor = NEW.gen_cor AND gen_id <> OLD.gen_id) " +
                "BEGIN SELECT RAISE(IGNORE); END;");
    }

    private void criarProtecaoContraNomesRepetidos(SQLiteDatabase db) {
        db.execSQL("CREATE TRIGGER IF NOT EXISTS genero_nome_unico_insert " +
                "BEFORE INSERT ON genero " +
                "WHEN EXISTS (SELECT 1 FROM genero " +
                "WHERE lower(trim(gen_nome)) = lower(trim(NEW.gen_nome))) " +
                "BEGIN SELECT RAISE(IGNORE); END;");

        db.execSQL("CREATE TRIGGER IF NOT EXISTS genero_nome_unico_update " +
                "BEFORE UPDATE OF gen_nome ON genero " +
                "WHEN EXISTS (SELECT 1 FROM genero " +
                "WHERE lower(trim(gen_nome)) = lower(trim(NEW.gen_nome)) " +
                "AND gen_id <> OLD.gen_id) " +
                "BEGIN SELECT RAISE(IGNORE); END;");
    }
}

