package com.dinesh.rabbitMqBatch.config;

import com.dinesh.rabbitMqBatch.model.GenericRequest;
import com.dinesh.rabbitMqBatch.model.TxnRequest;
import com.dinesh.rabbitMqBatch.writer.Writer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.amqp.AmqpItemReader;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    public final static String queueName = "rabbitmq.batch.listener";
    @Autowired
    private ConnectionFactory rabbitConnectionFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ObjectMapper mapper;

    // this will create a new queue if it doesn't exist; otherwise, it'll use the existing one of the same name
// ...the second argument means to make the queue 'durable'
    @Bean
    public Queue myQueue() {
        return new Queue(queueName, true);
    }

    // this is necessary for operations with Spring AMQP
    @Bean
    public RabbitTemplate getMyQueueTemplate() {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public Step getMyJobStep() {
        return this.stepBuilderFactory.get("myJobStep")
                .<String, GenericRequest<TxnRequest>>chunk(1)
                .reader(getMyReader())
                .writer(getMyWriter())
                .build();
    }
    @Bean
    public ItemReader<String> getMyReader() {
        return new AmqpItemReader<String>(getMyQueueTemplate());
    }
    @Bean
    public Writer getMyWriter() {
        return new Writer();
    }
}
