package com.benonshi.springbootamqprabbitmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author BenOniShi
 * @date 2020/4/1 21:57
 */
@Component
public class RabbitmqProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Environment env;
    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private Exchange exchange;

    public void send() {
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setExchange(env.getProperty("log.user.direct.exchange.name"));
        rabbitTemplate.setRoutingKey(Objects.requireNonNull(env.getProperty("log.user.routing.key.name")));
        rabbitAdmin.declareExchange(exchange);
        Map map = new HashMap(1);
        map.put("msg", "张三");
        Message message = null;
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("msg", "测试消息");
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        try {
            message = MessageBuilder.withBody(new ObjectMapper().writeValueAsBytes(map)).andProperties(messageProperties).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        int i = 0;
        while (i < 5) {
            rabbitTemplate.convertAndSend(Objects.requireNonNull(env.getProperty("log.user.routing.key.name")),
                    message, new CorrelationData(UUID.randomUUID().toString().substring(0, 8)));
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            i++;
        }
    }

}
