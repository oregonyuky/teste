package com.example.lucaspanucciprova.db.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;


import com.example.lucaspanucciprova.db.util.Conexao;
import com.example.lucaspanucciprova.db.bean.Tarefa;
import com.example.lucaspanucciprova.db.bean.TarefaSimples;
import com.example.lucaspanucciprova.db.bean.TarefaTemporal;

import java.util.ArrayList;
import java.util.Date;


public class TarefaDAL
{   private Conexao con;
    private Context context;
    private final String TABLE="tarefa";

    public TarefaDAL(Context context) {
        this.context=context;
        con = new Conexao(context);
        try {
            con.conectar();
        }
        catch(Exception e)
        {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public boolean salvar(Tarefa o)
    {
        ContentValues dados=new ContentValues();
        dados.put("tar_texto",o.getTexto());
        dados.put("tar_concluida", o.getConcluida());
        if (o instanceof TarefaTemporal) {
            dados.put("tar_temporal", 1);
            dados.put("tar_data", ((TarefaTemporal) o).getData());
        } else {
            dados.put("tar_temporal", 0);
            dados.put("tar_data", "");
        }

        return con.inserir(TABLE,dados)>0;
    }
    public boolean alterar(Tarefa o)
    {
        ContentValues dados=new ContentValues();
        dados.put("tar_id",o.getId());
        dados.put("tar_texto",o.getTexto());
        dados.put("tar_concluida", o.getConcluida());
        if (o instanceof TarefaTemporal) {
            dados.put("tar_temporal", 1);
            dados.put("tar_data", ((TarefaTemporal) o).getData());
        } else {
            dados.put("tar_temporal", 0);
            dados.put("tar_data", "");

            dados.put("tar_concluida", o.getConcluida());
        }

        return con.alterar(TABLE,dados,"tar_id="+o.getId())>0;
    }
    public boolean apagar(long chave)
    {
        return con.apagar(TABLE,"tar_id="+chave)>0;
    }

    public Tarefa get(int id)
    {
        Cursor cursor=con.consultar("select * from "+TABLE+" where tar_id="+id);
        Tarefa o = null;
        if(cursor.moveToFirst()) {
            int idAtual = cursor.getInt(0);
            String texto = cursor.getString(1);
            int isTemporal = cursor.getInt(3);
            int concluida = cursor.getInt(4);
            String data;

            if (isTemporal == 1) {
                data = cursor.getString(2);
                o = new TarefaTemporal(idAtual, texto, concluida, data);
            } else {
                o = new TarefaSimples(idAtual, texto, concluida);
            }
        }

        cursor.close();
        return o;
    }
    public ArrayList <Tarefa> get(String filtro)
    {
        ArrayList <Tarefa> objs = new ArrayList();
        String sql="select * from "+TABLE;
        if (!filtro.equals(""))
            sql+=" where "+filtro;

        Cursor cursor=con.consultar(sql);
        if(cursor.moveToFirst())
            while (!cursor.isAfterLast()) {
                Tarefa tarAtual;
                int isTemporal = cursor.getInt(3);
                if (isTemporal == 1)
                    tarAtual = new TarefaTemporal(cursor.getInt(0), cursor.getString(1), cursor.getInt(4), cursor.getString(2));
                else
                    tarAtual = new TarefaSimples(cursor.getInt(0), cursor.getString(1), cursor.getInt(4));
                objs.add(tarAtual);
                cursor.moveToNext();
            }
        cursor.close();
        return objs;
    }
}
