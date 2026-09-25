package com.example.appimc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appimc.entities.Medicao;
import com.example.appimc.repository.MedidasRepository;
import com.example.appimc.util.ImcCalculos;
import com.example.appimc.util.MyStorage;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    private RadioButton rbMasc, rbFem;
    private SeekBar sbAltura, sbPeso;
    private TextView tvAltura, tvPeso, tvIMC, tvCondicao;
    private Button btArmazenar;

    private EditText etObs;
    //atributos de apoio
    private double altura=0, imc=0;
    private int peso=0;
    private char sexo;

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
        rbMasc=findViewById(R.id.rbMasc);
        rbFem=findViewById(R.id.rbFem);
        sbAltura=findViewById(R.id.sbAltura);
        sbPeso=findViewById(R.id.sbPeso);
        tvAltura=findViewById(R.id.tvAltura);
        tvPeso=findViewById(R.id.tvPeso);
        tvIMC=findViewById(R.id.tvIMC);
        tvCondicao=findViewById(R.id.tvCondicao);
        etObs=findViewById(R.id.etObs);
        btArmazenar=findViewById(R.id.btArmazenar);
        ajustarInformacoes();
        definirEventos();
        if(!MyStorage.recuperarMedicoes(this)){
            Toast.makeText(this,"Problemas ao carregar medicoes anteriores",Toast.LENGTH_SHORT).show();
        }
        Log.i("IMC",MedidasRepository.medicaoList.size()+" medições realizadas");
        recuperarUltimasMedidas();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!MyStorage.salvarMedicoes(this)){
            Toast.makeText(this,"Problemas ao carregar medicoes anteriores",Toast.LENGTH_SHORT).show();
        }
        salvarMedidas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.imTabela) trocarActivity();
        if(item.getItemId()==R.id.imGrafico) trocarActivity2();
        if(item.getItemId()==R.id.imFechar) finish();
        return true;
    }

    private void recuperarUltimasMedidas() {
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("sexo",true)) {
            sexo='m';
            rbMasc.setChecked(true);
        }
        else{
            sexo='f';
            rbFem.setChecked(true);
        }
        altura=sharedPreferences.getFloat("altura",0);
        peso=sharedPreferences.getInt("peso",0);
        ajustarInformacoes();
        calcularIMC();
    }
    private void salvarMedidas() {
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("sexo",rbMasc.isChecked());
        sharedPreferences.edit().putFloat("altura",(float)altura);
        sharedPreferences.edit().putInt("peso",peso);
        sharedPreferences.edit().commit();
    }

    private void definirEventos() {
        sbAltura.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                altura=sbAltura.getProgress()/100.;
                tvAltura.setText(""+altura);
                calcularIMC();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                alertarPercaPeso();
            }
        });
        sbPeso.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                peso=sbPeso.getProgress();
                tvPeso.setText(""+peso);
                calcularIMC();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                alertarPercaPeso();
            }
        });
        rbMasc.setOnCheckedChangeListener((compoundButton, b) -> {
            altura=0;
            peso=0;
            ajustarInformacoes();
        });
        btArmazenar.setOnClickListener(e->{armazenarMedicao();});
    }

    private void armazenarMedicao() {
        Medicao medicao;
        medicao=new Medicao(LocalDate.now(),sexo,altura,peso,etObs.getText().toString(),imc);
        MedidasRepository.medicaoList.add(medicao);
        btArmazenar.setEnabled(false);
        trocarActivity();
    }
    private void trocarActivity(){
        //trocando a activity
        Intent intent=new Intent(this,TableActivity.class);
        intent.putExtra("sexo",sexo);
        intent.putExtra("altura",altura);
        startActivity(intent);
    }
    private void trocarActivity2(){
        //trocando a activity
        Intent intent=new Intent(this,GraficoLinhasActivity.class);
        startActivity(intent);
    }

    private void alertarPercaPeso(){
        int pesoPerder=ImcCalculos.pesoPerder(imc, sexo, altura, peso);
        if(pesoPerder>0){
            String mens="Você deve perder pelo menos "+pesoPerder+" kg para chegar ao peso ideal";
            View view=getLayoutInflater().inflate(R.layout.alert_peso,  null);
            ((TextView)view.findViewById(R.id.tvMens)).setText(mens);
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setView(view);
            AlertDialog dialog=builder.create();
            dialog.show();
        }
    }

    private void calcularIMC() {
        btArmazenar.setEnabled(true);
        sexo=rbMasc.isChecked()?'m':'f';
        imc= ImcCalculos.calcular(peso,altura);
        tvIMC.setText(String.format("%.1f",imc));
        tvCondicao.setText(ImcCalculos.getCondicaoFisica(imc,sexo));
    }

    private void ajustarInformacoes() {
        sbAltura.setProgress((int)(altura*100));
        tvAltura.setText(""+altura);
        sbPeso.setProgress(peso);
        tvPeso.setText(""+peso);
        tvIMC.setText(""+imc);
    }

}