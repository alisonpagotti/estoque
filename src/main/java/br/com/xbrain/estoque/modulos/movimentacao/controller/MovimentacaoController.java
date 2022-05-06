package br.com.xbrain.estoque.modulos.movimentacao.controller;

import br.com.xbrain.estoque.modulos.movimentacao.dto.AtualizarMovimentacaoRequest;
import br.com.xbrain.estoque.modulos.movimentacao.dto.MovimentacaoRequest;
import br.com.xbrain.estoque.modulos.movimentacao.dto.MovimentacaoResponse;
import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.movimentacao.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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