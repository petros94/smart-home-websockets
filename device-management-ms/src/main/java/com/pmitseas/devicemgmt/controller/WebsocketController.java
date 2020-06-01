package com.pmitseas.devicemgmt.controller;

import com.pmitseas.devicemgmt.model.ResponseMessage;
import com.pmitseas.devicemgmt.service.DeviceMgmtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebsocketController {

	private final DeviceMgmtService deviceMgmtService;

	@MessageMapping("/device")
	public void handleMessageFromDevice(@Payload ResponseMessage message, Principal principal) {
		log.info("Received message from device: {}", principal.getName());
		deviceMgmtService.handleMessageFromDevice(message);
	}

}
