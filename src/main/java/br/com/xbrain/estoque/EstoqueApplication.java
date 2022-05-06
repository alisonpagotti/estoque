package br.com.xbrain.estoque;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EstoqueApplication {

	@Value("${queue.produto.nome}")
	private String produtoFila;

	public static void main(String[] args) {
		SpringApplication.run(EstoqueApplication.class, args);
	}

	@Bean
	public Queue queue() {
		return new Queue(produtoFila, true, false, false);
	}
}
