package br.com.xbrain.estoque.modulos.movimentacao.controller;

import br.com.xbrain.estoque.modulos.movimentacao.dto.AtualizarMovimentacaoRequest;
import br.com.xbrain.estoque.modulos.movimentacao.dto.MovimentacaoRequest;
import br.com.xbrain.estoque.modulos.movimentacao.dto.MovimentacaoResponse;
import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.movimentacao.service.MovimentacaoService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("movimentacoes")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService service;

    @GetMapping("listar")
    public List<MovimentacaoResponse> listarTodas() {
        return service.listarTodas();
    }

    @GetMapping("listar/{tipo}")
    public List<MovimentacaoResponse> listarPorTipoMovimentacao(@PathVariable String tipo) {
        return service.listarPorTipoMovimentacao(tipo);
    }

    @GetMapping("listar/periodo")
    public List<MovimentacaoResponse> listarPorPeriodo(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime inicio,
                                                       @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime fim) {
        return service.listarPorPeriodo(inicio, fim);
    }

    @GetMapping("detalhar/{id}")
    public MovimentacaoResponse detalhar(@PathVariable Integer id) {
        return service.detalhar(id);
    }

    @PostMapping
    public MovimentacaoResponse movimentacao(@RequestBody @Valid MovimentacaoRequest request) {
        return service.movimentacao(request);
    }

    @PutMapping("{id}")
    public MovimentacaoResponse atualizar(@PathVariable Integer id,
                                          @RequestBody @Valid AtualizarMovimentacaoRequest request) {
        return service.atualizar(id, request);
    }
}