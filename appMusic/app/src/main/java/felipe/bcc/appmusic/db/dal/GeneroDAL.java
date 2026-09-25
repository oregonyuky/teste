package felipe.bcc.appmusic.db.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import java.util.ArrayList;

import felipe.bcc.appmusic.db.bean.Genero;
import felipe.bcc.appmusic.db.util.Conexao;


public class GeneroDAL
{   private Conexao con;
    private final String TABLE="genero";

    public GeneroDAL(Context context) {
        con = new Conexao(context);
        try {
            con.conectar();
        }
        catch(Exception e)
        {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public boolean salvar(Genero o)
    {
        if (corExiste(o.getCor(), 0) || nomeExiste(o.getNome(), 0))
            return false;

        ContentValues dados=new ContentValues();
        dados.put("gen_nome",o.getNome());
        dados.put("gen_cor",o.getCor());

        return con.inserir(TABLE,dados)>0;
    }
    public boolean alterar(Genero o)
    {
        if (corExiste(o.getCor(), o.getId()) || nomeExiste(o.getNome(), o.getId()))
            return false;

        ContentValues dados=new ContentValues();
        dados.put("gen_nome",o.getNome());
        dados.put("gen_cor",o.getCor());
        return con.alterar(TABLE,dados,"gen_id="+o.getId())>0;
    }
    public boolean apagar(long chave)
    {
        return con.apagar(TABLE,"gen_id="+chave)>0;
    }

    public boolean corExiste(int cor, int idIgnorado)
    {
        Cursor cursor = con.consultar(
                "select 1 from " + TABLE + " where gen_cor = ? and gen_id <> ? limit 1",
                new String[]{String.valueOf(cor), String.valueOf(idIgnorado)});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    public boolean nomeExiste(String nome, int idIgnorado)
    {
        Cursor cursor = con.consultar(
                "select 1 from " + TABLE +
                        " where lower(trim(gen_nome)) = lower(trim(?)) and gen_id <> ? limit 1",
                new String[]{nome, String.valueOf(idIgnorado)});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    public Genero get(int id)
    {   Genero o = null;
        Cursor cursor=con.consultar("select * from "+TABLE+" where gen_id="+id);
        if(cursor.moveToFirst())
            o=new Genero(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
        cursor.close();
        return o;
    }
    public ArrayList <Genero> get(String filtro)
    {   ArrayList <Genero> objs = new ArrayList<>();
        String sql="select * from "+TABLE;
        if (!filtro.equals(""))
            sql+=" where "+filtro;

        Cursor cursor=con.consultar(sql);
        if(cursor.moveToFirst())
            while (!cursor.isAfterLast()) {
                objs.add(new Genero(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
                cursor.moveToNext();
            }
        cursor.close();
        return objs;
    }
}
