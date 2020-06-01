package com.pmitseas.devicemgmt.event;

import com.pmitseas.devicemgmt.service.DeviceMgmtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AMQMessagingService {
    private DeviceMgmtService deviceMgmtService;
    private JmsTemplate jmsTopicTemplate;

    public AMQMessagingService(JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }

    @Value("${broker.topic}")
    private String actionsTopic;

    public void send(ActionResultEvent event) {
        log.info("sending message='{}'", event);
        jmsTopicTemplate.convertAndSend(actionsTopic, event);
    }

    @JmsListener(destination = "${broker.queue}")
    public void receiveMessage(ActionEvent message) {
        log.info("Received event from Control MS: {}", message);
        deviceMgmtService.sendMessageToDevice(message);
    }

    @Autowired
    @Lazy
    public void setDeviceMgmtService(DeviceMgmtService deviceMgmtService) {
        //This must be lazily initiated because there is a circular dependency
        //between CDMService <-> AMQMessagingService
        this.deviceMgmtService = deviceMgmtService;
    }
}
