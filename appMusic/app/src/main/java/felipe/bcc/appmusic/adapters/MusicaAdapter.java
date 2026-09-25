package felipe.bcc.appmusic.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import felipe.bcc.appmusic.R;
import felipe.bcc.appmusic.db.bean.Musica;

public class MusicaAdapter extends ArrayAdapter<Musica> {
    private int resource;

    public MusicaAdapter(@NonNull Context context, int resource, @NonNull List<Musica> musicaList) {
        super(context, resource, musicaList);
        this.resource = resource;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource, parent, false);
        }

        Musica musica = getItem(position);

        TextView tv_titulo = convertView.findViewById(R.id.tv_titulo);
        TextView tv_interprete = convertView.findViewById(R.id.tv_interprete);
        TextView tv_genero = convertView.findViewById(R.id.tv_genero);
        TextView tv_duracao = convertView.findViewById(R.id.tv_duracao);
        TextView tv_ano = convertView.findViewById(R.id.tv_ano);

        // Preenche os TextViews com os dados do objeto Usuario
        if (musica != null) {
            MaterialCardView card = convertView.findViewById(R.id.card_musica);
            int corGenero = musica.getGenero().getCor();
            card.setCardBackgroundColor(ColorUtils.setAlphaComponent(corGenero, 90));
            card.setStrokeColor(corGenero);
            card.setStrokeWidth((int) (4 * getContext().getResources().getDisplayMetrics().density));

            View indicador = convertView.findViewById(R.id.indicador_cor_genero);
            GradientDrawable circulo = new GradientDrawable();
            circulo.setShape(GradientDrawable.OVAL);
            circulo.setColor(corGenero);
            indicador.setBackground(circulo);
            tv_titulo.setText(musica.getTitulo());
            tv_interprete.setText(musica.getInterprete());
            tv_ano.setText(String.valueOf(musica.getAno()));
            tv_genero.setText(musica.getGenero().getNome());
            int duracao = (int) musica.getDuracao();
            int minutos = (int) Math.round((musica.getDuracao() - duracao) * 100);
            tv_duracao.setText(duracao + ":" + (minutos > 9 ? minutos : "0" + minutos));
        }

        return convertView;
    }
}
