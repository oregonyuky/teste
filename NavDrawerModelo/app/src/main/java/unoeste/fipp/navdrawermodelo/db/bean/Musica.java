package unoeste.fipp.navdrawermodelo.db.bean;


public class Musica {

    private int id, ano;
    private String titulo, autor;
    private Genero genero;
    private double duracao;

    public Musica() {
        this(0,0,"","",null,0);
    }

    public Musica(int ano, String titulo, String autor, Genero genero, double duracao) {
        this(0,ano,titulo, autor,genero,duracao);
    }

    public Musica(int id, int ano, String titulo, String autor, Genero genero, double duracao) {
        this.id = id;
        this.ano = ano;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.duracao = duracao;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public double getDuracao() {
        return duracao;
    }

    public void setDuracao(double duracao) {
        this.duracao = duracao;
    }

    @Override
    public String toString() {
        return  titulo+" ("+genero.getNome()+")";
    }


}
