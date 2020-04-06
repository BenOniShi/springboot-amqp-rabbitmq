package com.benonshi.springbootamqprabbitmq.controller;

import com.benonshi.springbootamqprabbitmq.producer.RabbitmqProducer;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author BenOniShi
 * @date 2020/4/4 20:19
 */
@Controller
public class RabbitmqController {

    @Autowired
    private RabbitmqProducer producer;

    @RequestMapping("/send")
    @ResponseBody
    public void index() {
        producer.send();
    }

}
