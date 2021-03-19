package view;

import java.io.Serializable;


    //classe dos equipamentos,  possui os dados com get/set/toString/Construtor 

public class Equipamentos implements Serializable {

    
    private String nomePeça;
    private String nomeFabricante;
    private double precoAquisicao;
    private int numeroSerie;
    private String dataAquisicao;

    public String getNomePeça() {
        return nomePeça;
    }

    @Override
    public String toString() {
        return "Produtos{" + "nomePe\u00e7a=" + nomePeça + ", nomeFabricante=" + nomeFabricante + ", precoAquisicao=" + precoAquisicao + ", numeroSerie=" + numeroSerie + ", dataAquisicao=" + dataAquisicao + '}';
    }

    public void setNomePeça(String nomePeça) {
        this.nomePeça = nomePeça;
    }

    public String getNomeFabricante() {
        return nomeFabricante;
    }

    public void setNomeFabricante(String nomeFabricante) {
        this.nomeFabricante = nomeFabricante;
    }

    public double getPrecoAquisicao() {
        return precoAquisicao;
    }

    public void setPrecoAquisicao(double precoAquisicao) {
        this.precoAquisicao = precoAquisicao;
    }

    public int getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(int numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(String dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public Equipamentos(String nomePeça, String nomeFabricante, double precoAquisicao, int numeroSerie, String dataAquisicao) {
        this.nomePeça = nomePeça;
        this.nomeFabricante = nomeFabricante;
        this.precoAquisicao = precoAquisicao;
        this.numeroSerie = numeroSerie;
        this.dataAquisicao = dataAquisicao;
    }

}
