package unoeste.fipp.navdrawermodelo.db.bean;


public class Genero {
    private int id;
    private String nome;

    private int cor;
    public Genero() {
        this(0,"", 0);
    }

    public Genero(String nome) {
        this(0,nome, 0);
    }

    public Genero(int id, String nome) {
        this(id, nome, 0);
    }

    public Genero(String nome, int cor) {
        this(0, nome, cor);
    }

    public Genero(int id, String nome, int cor) {
        this.id = id;
        this.nome = nome;
        this.cor = cor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    @Override
    public String toString() {
        return nome+"("+id+")";
    }
}
