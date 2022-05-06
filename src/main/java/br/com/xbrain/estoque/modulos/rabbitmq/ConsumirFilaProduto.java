package br.com.xbrain.estoque.modulos.rabbitmq;

import br.com.xbrain.estoque.modulos.backup.produto.model.BackupProduto;
import br.com.xbrain.estoque.modulos.backup.produto.service.BackupProdutoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ConsumirFilaProduto {

    @Autowired
    private BackupProdutoService service;

    @RabbitListener(queues = {"${queue.produto.nome}"})
    public void consumirFilaProduto(@Payload BackupProduto backupProduto) {
        service.insert(backupProduto);
    }
}
