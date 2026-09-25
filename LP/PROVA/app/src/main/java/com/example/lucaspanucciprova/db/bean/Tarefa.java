package com.example.lucaspanucciprova.db.bean;

public class Tarefa {
    private int id, concluida;
    private String texto;

    public Tarefa(int id, int concluida, String texto) {
        this.id = id;
        this.concluida = concluida;
        this.texto = texto;
    }

    public int getConcluida() {
        return concluida;
    }

    public void setConcluida(int concluida) {
        this.concluida = concluida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return texto+"("+id+")";
    }
}
