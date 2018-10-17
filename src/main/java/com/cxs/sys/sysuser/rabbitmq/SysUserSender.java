package com.cxs.sys.sysuser.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysUserSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void getUserSender(String loginName) {
        amqpTemplate.convertAndSend("getUser", loginName);
    }

    public void getUserDelaySender(String loginName) {
        amqpTemplate.convertAndSend("getUser_delay", loginName);
    }
}
