package com.vidasnoaltar.celulas.Dados;

import java.io.Serializable;

public class Aviso implements Serializable {

    private int id;
    private int avisos_celula_id;
    private String titulo;
    private String conteudo;
    private Boolean ativo;
    private String created;
    private String modified;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvisos_celula_id() {
        return avisos_celula_id;
    }

    public void setAvisos_celula_id(int avisos_celula_id) {
        this.avisos_celula_id = avisos_celula_id;
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

    @Override
    public String toString() {
        return getTitulo();
    }
}
