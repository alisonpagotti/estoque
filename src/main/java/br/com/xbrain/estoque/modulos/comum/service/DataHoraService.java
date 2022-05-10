package br.com.xbrain.estoque.modulos.comum.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DataHoraService {

    public LocalDateTime DataHoraAtual() {
        return LocalDateTime.now()
                .withNano(0);
    }
}
