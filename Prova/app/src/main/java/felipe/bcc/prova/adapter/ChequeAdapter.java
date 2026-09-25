package felipe.bcc.prova.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import felipe.bcc.prova.R;
import felipe.bcc.prova.entities.Cheque;

public class ChequeAdapter extends ArrayAdapter<Cheque> {
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ChequeAdapter(@NonNull Context context, @NonNull List<Cheque> cheques) {
        super(context, R.layout.item_cheque, cheques);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View linha = convertView;
        if (linha == null) {
            linha = LayoutInflater.from(getContext()).inflate(R.layout.item_cheque, parent, false);
        }

        Cheque cheque = getItem(position);
        if (cheque != null) {
            TextView tvValor = linha.findViewById(R.id.tvValorCheque);
            TextView tvData = linha.findViewById(R.id.tvDataCheque);
            TextView tvDias = linha.findViewById(R.id.tvDiasCheque);
            TextView tvJuros = linha.findViewById(R.id.tvJurosCheque);
            tvValor.setText(String.format(LOCALE_BR, "R$ %.2f", cheque.getValor()));
            tvData.setText(cheque.getDataVencimento().format(FORMATO_DATA));
            tvDias.setText(String.format(LOCALE_BR, "%d dias", cheque.getDiasParaDeposito()));
            tvJuros.setText(String.format(LOCALE_BR, "R$ %.2f", cheque.getValorJuros()));
        }
        return linha;
    }
}
