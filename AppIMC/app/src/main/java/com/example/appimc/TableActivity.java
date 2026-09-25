package com.example.appimc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appimc.entities.Medicao;
import com.example.appimc.repository.MedidasRepository;

public class TableActivity extends AppCompatActivity {
    private ListView listView;
    private TextView tvSexo, tvAltura2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_table);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(getSupportActionBar()!=null)
            getSupportActionBar().hide();
        listView=findViewById(R.id.listView);
        tvSexo=findViewById(R.id.tvSexo);
        tvAltura2=findViewById(R.id.tvAltura2);
        Intent intent=getIntent();
        char sexo=intent.getCharExtra("sexo",' ');
        double altura=intent.getDoubleExtra("altura",0);
        String str="Sexo: ";
        str+=(sexo=='m')?"Masculino":"Feminino";
        tvSexo.setText(str);
        str=String.format("Sua altura = %.2f",altura);
        tvAltura2.setText(str);
        carregarListView();
    }

    private void carregarListView() {
        MedicaoAdapter adapter =
                new MedicaoAdapter(this, R.layout.item_listtview, MedidasRepository.medicaoList);

        listView.setAdapter(adapter);
    }
}