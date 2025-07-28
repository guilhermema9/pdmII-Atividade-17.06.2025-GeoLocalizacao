package com.example.atividade_17062025_geolocalizacao.model;

public class Local {

    private String nome;
    private String descricao;
    private double latitude;
    private double longitude;

    public Local(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Local(String nome, String descricao, double latitude, double longitude) {
        this.nome = nome;
        this.descricao = descricao;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}