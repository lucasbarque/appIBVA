package com.vidasnoaltar.celulas.Dados;

import java.io.Serializable;

public class DadosNotification implements Serializable {

    private String titulo;
    private String conteudo;
    private String data;

    public DadosNotification(String titulo, String conteudo, String data) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.data = data;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "DadosNotification{" +
                "titulo='" + titulo + '\'' +
                ", conteudo='" + conteudo + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
