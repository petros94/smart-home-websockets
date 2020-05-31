package com.pmitseas.control.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AMQMessagingService {

	private final JmsTemplate jmsQueueTemplate;

	@Value("${broker.queue}")
	private String actionsQueue;

	public void send(ActionEvent message) {
		jmsQueueTemplate.convertAndSend(actionsQueue, message);
	}
}
