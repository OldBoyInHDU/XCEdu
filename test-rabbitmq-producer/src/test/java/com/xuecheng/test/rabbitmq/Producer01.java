package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author hh
 * @create 2020/2/4
 * @description
 **/
public class Producer01 {
    //队列
    private static final String QUEUE = "helloworld";

    public static void main(String[] args) {
        //通过连接工厂创建新的连接，和mq建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);//外部web管理端口时 15672，注意区分
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        //设置虚拟机,一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        connectionFactory.setVirtualHost("/");


        Connection connection = null;
        try {
            //建立新连接
            connection = connectionFactory.newConnection();
            //创建会话通道，生产者和mq服务所有通信都在channel通道中完成
            Channel channel = connection.createChannel();
            //声明队列：如果队列在mq中没有则要创建
            //参数：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            /**
             * 参数明细：
             *  1.queue 队列名称
             *  2.durable 是否持久化。如果持久化，mq重启后队列还在
             *  3.exclusive 是否独占连接，队列只允许在该连接中访问，
             *      如果连接关闭，队列自动删除；如果将此参数设置为true可用于临时队列的创建
             *  4.autoDelete 自动删除，队列不再是哟个时是否自动删除此队列
             *      如果将此参数和exclusive参数设置为true,就可以实现临时队列（队列不用了就自动删除）
             *  5.arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间等
             */
            channel.queueDeclare(QUEUE, true, false, false, null);

            //发送消息
            //参数：String exchange, String routingKey, BasicProperties props, byte[] body
            /**
             * 1.exchange 交换机，如果不指定将使用mq的默认交换机，设置为""
             * 2.routingKey 路由Key，交换机根据路由key来将消息转发到指定的队列
             *      如果使用默认交换机，routingKey设置为队列的名称
             * 3.props 消息的属性
             * 4.body 消息内容
             */
            String message = "hello rabbitmq";
            channel.basicPublish("", QUEUE, null, message.getBytes());
            System.out.println("发送给mq的信息为：" + message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}