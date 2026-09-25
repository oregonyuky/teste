package com.example.appimc.util;

import android.app.Activity;
import android.content.Context;

import com.example.appimc.entities.Medicao;
import com.example.appimc.repository.MedidasRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MyStorage {
    public static boolean salvarMedicoes(Activity activity){
        FileOutputStream fout  = null;
        ObjectOutputStream out;
        try {
            fout = activity.openFileOutput("medicoes.dad", Context.MODE_PRIVATE);
            out = new ObjectOutputStream(fout);
            out.writeObject(MedidasRepository.medicaoList);
            out.close();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean recuperarMedicoes(Activity activity){
        FileInputStream fin  = null;
        ObjectInputStream in;
        try
        {
            fin = activity.openFileInput("medicoes.dad");
            in = new ObjectInputStream(fin);
            MedidasRepository.medicaoList = (List<Medicao>) in.readObject();
            in.close();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
