package com.example.lucaspanucciprova;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Context;

import com.example.lucaspanucciprova.db.bean.Tarefa;
import com.example.lucaspanucciprova.db.bean.TarefaSimples;
import com.example.lucaspanucciprova.db.bean.TarefaTemporal;
import com.example.lucaspanucciprova.db.dal.TarefaDAL;

public class MainActivity extends AppCompatActivity {
    private EditText etTexto, etDate;
    private Button button, button2, buttonFinalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etTexto = findViewById(R.id.etTexto);
        etDate = findViewById(R.id.etDate);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.buttonLista);
        buttonFinalizar = findViewById(R.id.buttonFinalizar);

        buttonFinalizar.setOnClickListener(e->{this.finish();});

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tarefa novaTarefa;

                if (etDate.getText().toString().isEmpty()) {
                    // nao temporal
                    novaTarefa = new TarefaSimples(0, etTexto.getText().toString(), 0);
                } else {
                    // temporal
                    novaTarefa = new TarefaTemporal(0, etTexto.getText().toString(), 0, etDate.getText().toString());
                }

                TarefaDAL dal = new TarefaDAL(getBaseContext());
                dal.salvar(novaTarefa);

                limparCampos();
            }
        });

        button2.setOnClickListener(e-> {trocarActivity();});
    }

    private void limparCampos() {
        etTexto.setText("");
        etDate.setText("");
    }

    private void trocarActivity() {
        Intent intent = new Intent(this, ListaActivity.class);

        startActivity(intent);
    }


}