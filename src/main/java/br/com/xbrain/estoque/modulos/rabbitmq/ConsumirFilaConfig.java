package br.com.xbrain.estoque.modulos.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class ConsumirFilaConfig {

    public static void main(String[] args) {
        SpringApplication.run(ConsumirFilaConfig.class, args);
    }
}
