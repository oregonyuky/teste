package felipe.bcc.prova;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.Locale;

import felipe.bcc.prova.adapter.ChequeAdapter;
import felipe.bcc.prova.entities.Cheque;
import felipe.bcc.prova.repositories.ChequeRepository;

public class MainActivity extends AppCompatActivity {
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private SeekBar sbJuros;
    private TextView tvJuros;
    private EditText etValor;
    private DatePicker datePicker;
    private ChequeAdapter adapter;
    private final ChequeRepository repository = new ChequeRepository();
    private double jurosMensais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sbJuros = findViewById(R.id.sbJuros);
        tvJuros = findViewById(R.id.tvJuros);
        etValor = findViewById(R.id.etValor);
        datePicker = findViewById(R.id.datePicker);
        Button btIncluir = findViewById(R.id.btIncluir);
        ListView lvCheques = findViewById(R.id.lvCheques);
        TextView tvListaVazia = findViewById(R.id.tvListaVazia);

        adapter = new ChequeAdapter(this, repository.listar());
        lvCheques.setAdapter(adapter);
        lvCheques.setEmptyView(tvListaVazia);
        configurarSeekBar();
        reiniciarData();

        btIncluir.setOnClickListener(view -> incluirCheque());
        lvCheques.setOnItemClickListener((parent, view, position, id) -> mostrarTotalLiquido());
        lvCheques.setOnItemLongClickListener((parent, view, position, id) -> {
            repository.remover(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.cheque_removido, Toast.LENGTH_SHORT).show();
            return true;
        });
    }

    private void configurarSeekBar() {
        sbJuros.setMax(1000);
        sbJuros.setProgress(0);
        atualizarJuros(0);
        sbJuros.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atualizarJuros(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void atualizarJuros(int progresso) {
        jurosMensais = progresso / 100.0;
        tvJuros.setText(String.format(LOCALE_BR, getString(R.string.juros_formatado), jurosMensais));
    }

    private void incluirCheque() {
        String textoValor = etValor.getText().toString().trim().replace(',', '.');
        if (textoValor.isEmpty()) {
            etValor.setError(getString(R.string.erro_valor_obrigatorio));
            etValor.requestFocus();
            return;
        }

        final double valor;
        try {
            valor = Double.parseDouble(textoValor);
        } catch (NumberFormatException erro) {
            etValor.setError(getString(R.string.erro_valor_invalido));
            etValor.requestFocus();
            return;
        }

        if (valor <= 0) {
            etValor.setError(getString(R.string.erro_valor_positivo));
            etValor.requestFocus();
            return;
        }

        LocalDate hoje = LocalDate.now();
        LocalDate vencimento = LocalDate.of(
                datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
        if (vencimento.isBefore(hoje)) {
            Toast.makeText(this, R.string.erro_data_passada, Toast.LENGTH_SHORT).show();
            return;
        }

        repository.incluir(new Cheque(valor, vencimento, jurosMensais, hoje));
        adapter.notifyDataSetChanged();
        etValor.setText("0");
        reiniciarData();
    }

    private void reiniciarData() {
        LocalDate hoje = LocalDate.now();
        datePicker.updateDate(hoje.getYear(), hoje.getMonthValue() - 1, hoje.getDayOfMonth());
        datePicker.setMinDate(System.currentTimeMillis() - 1000L);
    }

    private void mostrarTotalLiquido() {
        String total = String.format(
                LOCALE_BR, getString(R.string.total_liquido_formatado), repository.calcularTotalLiquido());
        Toast.makeText(this, total, Toast.LENGTH_LONG).show();
    }
}
