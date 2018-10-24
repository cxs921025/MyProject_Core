package com.cxs.sys.sysuser.rabbitmq;

import com.cxs.core.utils.LogUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SysUserSender {
    private final RabbitTemplate rabbitTemplate;

    final ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        LogUtil.info("[RabbitMQ]: 消息送达确认");
        if (ack) {
            LogUtil.info("[RabbitMQ]: 消息成功送达");
            // 成功后处理......
        } else {
            LogUtil.error("[RabbitMQ]: 消息发送失败! 原因: " + cause);
            // 异常处理......
        }
    };

    @Autowired
    public SysUserSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void getUserSender(String loginName) {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        // 测试 消息发送失败
        //rabbitTemplate.convertAndSend("no_had_exchange","getUser11", loginName);
        rabbitTemplate.convertAndSend("getUser11", loginName);
    }

    public void getUserDelaySender(String loginName) {
        rabbitTemplate.convertAndSend("getUser_delay", loginName);
    }
}
