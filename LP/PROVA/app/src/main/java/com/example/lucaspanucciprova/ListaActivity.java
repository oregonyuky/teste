package com.example.lucaspanucciprova;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;


import androidx.appcompat.app.AppCompatActivity;

import com.example.lucaspanucciprova.db.bean.Tarefa;
import com.example.lucaspanucciprova.db.dal.TarefaDAL;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends AppCompatActivity {
    TarefaAdapter adapter;   // muda aqui para o seu adapter
    ListView listView;
    TarefaDAL dal;
    List<Tarefa> listaTar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        carregarComponentes();

        View header = getLayoutInflater().inflate(R.layout.header_sample, listView, false);
        listView.addHeaderView(header);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Tarefa tar = (Tarefa) parent.getItemAtPosition(position);
            if (tar.getConcluida() == 0) {
                AlertDialog.Builder dialogBuilderExclusaoElemento = criaDialogConclusaoElemento(tar);

                dialogBuilderExclusaoElemento.setPositiveButton("Concluir", (dialog, which) -> {
                    concluiElemento(tar);
                    carregarComponentes();
                });

                dialogBuilderExclusaoElemento.setNegativeButton("Cancelar", null);
                dialogBuilderExclusaoElemento.show();
            }
        });


        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Tarefa tar = (Tarefa) parent.getItemAtPosition(position);
            AlertDialog.Builder dialogBuilderExclusaoElemento = criaDialogExclusaoElemento(tar);

            dialogBuilderExclusaoElemento.setPositiveButton("Excluir", (dialog, which) -> {
                excluiElemento(tar);
                carregarComponentes();
            });

            dialogBuilderExclusaoElemento.setNegativeButton("Cancelar", null);
            dialogBuilderExclusaoElemento.show();

            return true;
        });
    }

    private AlertDialog.Builder criaDialogConclusaoElemento(Tarefa item) {
        return new AlertDialog.Builder(this)
                .setTitle("Concluir tarefa")
                .setMessage("Deseja realmente concluir \"" + item.getTexto() + "\"?");
    }

    private AlertDialog.Builder criaDialogExclusaoElemento(Tarefa item) {
        return new AlertDialog.Builder(this)
                .setTitle("Concluir tarefa")
                .setMessage("Deseja realmente excluir \"" + item.getTexto() + "\"?");
    }

    private AlertDialog.Builder criaDialogLimparLista() {
        return new AlertDialog.Builder(this)
                .setTitle("Confirmar limpar")
                .setMessage("Deseja realmente limpar a lista?");
    }

    private void excluiElemento(Tarefa item) {
        dal.apagar(item.getId());

        adapter.notifyDataSetChanged();
        Toast.makeText(this, item.getTexto() + " removido", Toast.LENGTH_SHORT).show();
    }

    private void concluiElemento(Tarefa item) {
        item.setConcluida(1);
        if (dal.alterar(item))
            Toast.makeText(this, item.getTexto() + " concluida", Toast.LENGTH_SHORT).show();
    }

    private void carregarComponentes() {
        dal = new TarefaDAL(getApplicationContext());
        this.listaTar = dal.get("");

        adapter = new TarefaAdapter(
                this,
                R.layout.item_lista,   // seu layout customizado
                R.id.tvData,         // qualquer TextView do seu layout (obrigatório no construtor do ArrayAdapter)
                listaTar
        );

        listView = findViewById(R.id.tarefaListView);

        listView.setAdapter(adapter);
    }
}
