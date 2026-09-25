package com.example.lucaspanucciprova.db.bean;

public class TarefaTemporal extends Tarefa{

    private String data;

    public TarefaTemporal(int id, String texto, int concluida, String data) {
        super(id, concluida, texto);
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
