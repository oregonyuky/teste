package com.example.appimc.entities;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;

public class Medicao implements Serializable {
    private LocalDate data;
    private char sexo;
    private double altura;
    private int peso;
    private String anotacao;
    private double imc;

    public Medicao(LocalDate data, char sexo, double altura, int peso, String anotacao, double imc) {
        this.data = data;
        this.sexo = sexo;
        this.altura = altura;
        this.peso = peso;
        this.anotacao = anotacao;
        this.imc = imc;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {
        this.anotacao = anotacao;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s | %.2f | %3d | %.1f | %s",data,altura,peso,imc,anotacao);
    }
}
