package com.pmitseas.control.controller;

import com.pmitseas.control.dto.ActionDTO;
import com.pmitseas.control.event.AMQMessagingService;
import com.pmitseas.control.event.ActionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/device")
public class DeviceController {

	private final AMQMessagingService messagingService;

	@PostMapping
	public ResponseEntity sendCommandToDevice(@RequestBody ActionDTO actionDTO) {
		log.info("Received POST request with body: {}", actionDTO.toString());

		/* Create Action event*/
		ActionEvent event = ActionEvent.builder()
				.destination(actionDTO.getDestination())
				.command(actionDTO.getCommand())
				.args(actionDTO.getArgs())
				.id(UUID.randomUUID())
				.build();

		messagingService.send(event);
		return ResponseEntity.ok().build();
	}
}
