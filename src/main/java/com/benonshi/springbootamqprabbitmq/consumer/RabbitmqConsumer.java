package com.benonshi.springbootamqprabbitmq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author BenOniShi
 * @date 2020/4/1 22:06
 */
@Component
@EnableRabbit
public class RabbitmqConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RabbitmqConsumer.class);

    @Autowired
    private Environment env;

    @RabbitListener(queues = "local.log.user.queue")
    public void getMsg(Message message, Channel channel) {
        byte[] body = message.getBody();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        logger.info("deliveryTag:" + deliveryTag);
        System.out.println(deliveryTag);
        try {
            Map map = new ObjectMapper().readValue(body, Map.class);
            if (deliveryTag > 3) {
                channel.basicNack(deliveryTag, false, false);
            } else {
                channel.basicAck(deliveryTag, false);
            }
            System.out.println("map = " + map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message.getMessageProperties().getHeader(\"msg\") = " + message.getMessageProperties().getHeader("msg"));
        System.out.println("receive.getBody() = " + message.getBody());
        System.out.println("receive.getMessageProperties() = " + message.getMessageProperties());
        System.out.println("receive.toString() = " + message.toString());
    }


}
