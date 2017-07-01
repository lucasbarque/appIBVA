package com.vidasnoaltar.celulas.Dados;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Programacao implements Serializable {

    public static String DIRETORIO_IMAGENS_PROGRAMACAO = "/programacao";
    public static String NOME_PADRAO_IMAGEM_PROGRAMACAO = "progImg.png";
    public static String NOME_PADRAO_IMAGEM_PROGRAMACAO_ENVIAR = "progImgEnv.png";

    private int id;
    private int programacoes_celula_id;
    private String nome;
    private String data;
    private String horario;
    private String local;
    private String telefone;
    private String valor;
    private Boolean ativo;
    private Bitmap imagem;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgramacoes_celula_id() {
        return programacoes_celula_id;
    }

    public void setProgramacoes_celula_id(int programacoes_celula_id) {
        this.programacoes_celula_id = programacoes_celula_id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
