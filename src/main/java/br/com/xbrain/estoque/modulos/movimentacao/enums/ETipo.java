package br.com.xbrain.estoque.modulos.movimentacao.enums;

public enum ETipo {

    ENTRADA("entrada"),
    SAIDA("saída");

    private String valor;

    ETipo(String valor) {
        this.valor = valor;
    }
}
