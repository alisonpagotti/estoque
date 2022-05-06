package br.com.xbrain.estoque.modulos.config.exception;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {

    private String mensagem;
    private List<String> detalhes;

    public ErrorResponse(String mensagem, List<String> detalhes) {
        super();
        this.mensagem = mensagem;
        this.detalhes = detalhes;
    }

    public ErrorResponse(String mensagem) {
        this.mensagem = mensagem;
    }
}
