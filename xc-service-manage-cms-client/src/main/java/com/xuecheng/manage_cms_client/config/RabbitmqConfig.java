package com.xuecheng.manage_cms_client.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hh
 * @create 2020/2/5
 * @description
 **/
@Configuration
public class RabbitmqConfig {
    //队列bean名称
    public static final String QUEUE_CMS_POSTPAGE= "queue_cms_postpage";

    //交换机的名称
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    //队列名称
    @Value("${xuecheng.mq.queue}")
    public String queue_cms_postpage_name;

    //routingKey 即站点id
    @Value("${xuecheng.mq.routingKey}")
    public String routingKey;

    /**
     * 交换机配置使用direct类型
     *
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange getExchangeTopicsInform() {
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    /**
     * 声明队列
     */
    @Bean(QUEUE_CMS_POSTPAGE)
    public Queue getQueueCmsPostpage() {
        Queue queue = new Queue(QUEUE_CMS_POSTPAGE);
        return queue;
    }

    /**
     * 绑定队列到交换机
     */
    public Binding bindingQueueToExchange(@Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange,
                                          @Qualifier(QUEUE_CMS_POSTPAGE) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }
}
