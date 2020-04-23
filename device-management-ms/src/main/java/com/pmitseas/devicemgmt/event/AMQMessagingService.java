package com.pmitseas.devicemgmt.event;

import com.pmitseas.devicemgmt.service.DeviceMgmtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AMQMessagingService {
    private final DeviceMgmtService deviceMgmtService;

    @JmsListener(destination = "${broker.queue}")
    public void receiveMessage(ActionEvent message) {
        log.info("Received event from Control MS: {}", message.toString());
        deviceMgmtService.sendMessageToDevice(message);
    }

}
