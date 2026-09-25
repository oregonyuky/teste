package com.example.appimc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appimc.entities.Medicao;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MedicaoAdapter extends ArrayAdapter<Medicao> {
    private int resource;
    public MedicaoAdapter(@NonNull Context context, int resource, @NonNull List<Medicao> objects) {
        super(context, resource, objects);
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(this.resource,parent,false);
        }
        TextView tvData = convertView.findViewById(R.id.tvData);
        TextView tvPeso2 = convertView.findViewById(R.id.tvPeso2);
        TextView tvIMC2 = convertView.findViewById(R.id.tvIMC2);
        TextView tvAnot = convertView.findViewById(R.id.tvAnot);
        Medicao medicao=this.getItem(position);
        tvData.setText(medicao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tvPeso2.setText(""+medicao.getPeso());
        tvIMC2.setText(String.format("%.1f",medicao.getImc()));
        tvAnot.setText(medicao.getAnotacao());
        return convertView;
    }
}
