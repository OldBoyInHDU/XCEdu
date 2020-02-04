package com.xuecheng.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author hh
 * @create 2020/2/4
 * @description
 **/
@Component
public class ReceiveHandler {
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void send_email(String message, Message message2, Channel channel) {
        System.out.println("接收到的消息" + message);
        System.out.println("接收到的消息" + message2.getBody());
    }
}
