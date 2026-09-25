package com.example.lucaspanucciprova;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lucaspanucciprova.db.bean.Tarefa;
import com.example.lucaspanucciprova.db.bean.TarefaTemporal;

import java.util.List;

public class TarefaAdapter extends ArrayAdapter <Tarefa> {

    private int resource;

    public TarefaAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Tarefa> tarefa) {
        super(context, resource, textViewResourceId, tarefa);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource,parent,false);
        }

        if (position % 2 == 0)
            convertView.setBackgroundColor(Color.parseColor("#66A69B9E"));
        else
            convertView.setBackgroundColor(Color.parseColor("#1AB71C1C"));


        TextView tvTexto=convertView.findViewById(R.id.tvTexto);
        TextView tvConcluida=convertView.findViewById(R.id.tvConcluida);
        TextView tvData=convertView.findViewById(R.id.tvData);

        tvTexto.setText(""+getItem(position).getTexto());

        if (getItem(position).getConcluida() == 1) {
            tvConcluida.setText("Sim");
        } else {
            tvConcluida.setText("Nao");
        }

        if (getItem(position) instanceof TarefaTemporal) {
            TarefaTemporal ttAt = (TarefaTemporal) getItem(position);
            tvData.setText(""+ttAt.getData());
        } else {
            tvData.setText("-");
        }

        return convertView;
    }
}
