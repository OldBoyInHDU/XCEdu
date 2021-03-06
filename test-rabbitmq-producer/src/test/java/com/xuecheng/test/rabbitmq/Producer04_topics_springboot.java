package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hh
 * @create 2020/2/4
 * @description
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer04_topics_springboot {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendEmail() {
        String message = "给email发送消息";
        //参数
        /**
         * 1.交换机的名称
         * 2.routingKey
         * 3.消息内容
         */
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.email", message);
        System.out.println(message);
    }
}
