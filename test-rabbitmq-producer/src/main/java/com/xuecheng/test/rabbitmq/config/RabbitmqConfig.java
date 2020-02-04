package com.xuecheng.test.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hh
 * @create 2020/2/4
 * @description
 **/

@Configuration
public class RabbitmqConfig {
    //队列
    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    public static final String ROUTINGKEY_EMAIL = "inform.#.email.#";//inform.email
    public static final String ROUTINGKEY_SMS = "inform.#.sms.#";

    //声明交换机
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange getTopicsExchange() {
        //durable(true) 持久化，mq重启后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    //声明队列 QUEUE_INFORM_EMAIL
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue getQueueInformEmail() {
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    //声明队列 QUEUE_INFORM_SMS
    @Bean(QUEUE_INFORM_SMS)
    public Queue getQueueInformSms() {
        return new Queue(QUEUE_INFORM_SMS);
    }

    //绑定交换机和email队列,指定routingKey
    @Bean
    public Binding bindingQueueInformEmail(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,
                                           @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL).noargs();
    }

    //绑定交换机和sms队列,指定routingKey
    @Bean
    public Binding bindingQueueInformSms(@Qualifier(QUEUE_INFORM_SMS) Queue queue,
                                           @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_SMS).noargs();
    }
}
