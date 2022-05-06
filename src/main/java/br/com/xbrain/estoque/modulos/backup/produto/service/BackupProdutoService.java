package br.com.xbrain.estoque.modulos.backup.produto.service;

import br.com.xbrain.estoque.modulos.backup.produto.model.BackupProduto;
import br.com.xbrain.estoque.modulos.backup.produto.repository.BackupProdutoRepository;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import br.com.xbrain.estoque.modulos.rabbitmq.EnviarFilaProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackupProdutoService {

    @Autowired
    private BackupProdutoRepository repository;

    @Autowired
    private EnviarFilaProduto enviarFilaProduto;

    public void insert(BackupProduto backupProduto) {
        repository.save(backupProduto);
    }

    public void gerarBackupProduto(Produto produto) {

        var backupProduto = BackupProduto.builder()
                .id(produto.getId())
                .nome(produto.getNomeDoProduto())
                .valorDoProduto(produto.getValorDoProduto())
                .quantidade(produto.getQuantidade())
                .build();

        enviarFilaProduto.send(backupProduto);
    }
}
