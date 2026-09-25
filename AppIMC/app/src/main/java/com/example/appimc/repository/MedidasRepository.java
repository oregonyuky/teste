package com.example.appimc.repository;

import com.example.appimc.entities.Medicao;

import java.util.ArrayList;
import java.util.List;

//Singleton 'simplificado'
public class MedidasRepository {
    static public List<Medicao> medicaoList=new ArrayList<>();
}
