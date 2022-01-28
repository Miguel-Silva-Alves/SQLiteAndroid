package com.miguelstudiodeveloper.sqliteandroid.utils;

/*

    Classe criada para facilitar a manipulação dos dados
    -> nome referece-se ao nome do atributo na tabela
    -> tipo referece-se ao tipo do atributo na tabela
    -> valor temporário refere-se ao valor do atributo na tabela

 */

public class Atributo {
    private String nome;
    private String tipo;
    private String valor_temporario;

    //construtor
    public Atributo(String nome, String tipo){
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome(){
        return this.nome;
    }

    public void setValor_temporario(String value){
        this.valor_temporario = value;
    }

    public String getValor_temporario() {
        return valor_temporario;
    }

    public String getTipo(){ return this.tipo; }

    public String toString(){
        return this.nome + " " + this.tipo + " " + this.valor_temporario;
    }
}
