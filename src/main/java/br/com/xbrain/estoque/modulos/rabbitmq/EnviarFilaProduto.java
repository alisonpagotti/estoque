package br.com.xbrain.estoque.modulos.rabbitmq;

import br.com.xbrain.estoque.modulos.backup.produto.model.BackupProduto;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnviarFilaProduto {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    public void send(BackupProduto backupProduto) {
        this.rabbitTemplate.convertAndSend(this.queue.getName(), backupProduto);
    }
}
