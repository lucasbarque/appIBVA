package com.vidasnoaltar.celulas.Dados;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GrupoEvangelistico implements Serializable {

    private int id;
    private int ges_celula_id;
    private String nome;
    private String data;
    private Boolean ativo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGes_celula_id() {
        return ges_celula_id;
    }

    public void setGes_celula_id(int ges_celula_id) {
        this.ges_celula_id = ges_celula_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
        Date dataDias = null;
        long days = 0;
        try {
            dataDias = formatoEntrada.parse(getData());
            Date dataAtual = new Date();
            long diff = dataAtual.getTime() - dataDias.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            days = hours / 24;


        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        String retorno;
        if (days <= 1) {
            retorno = getNome() + " (" + days + " dia)";
        } else {
            retorno = getNome() + " (" + days + " dias)";
        }
        return retorno;
    }
}
