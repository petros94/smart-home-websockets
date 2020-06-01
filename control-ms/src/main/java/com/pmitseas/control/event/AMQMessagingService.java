package com.pmitseas.control.event;

import com.pmitseas.control.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AMQMessagingService {

	private final JmsTemplate jmsQueueTemplate;
	private final SseService sseService;

	@Value("${broker.queue}")
	private String actionsQueue;

	public void send(ActionEvent message) {
		jmsQueueTemplate.convertAndSend(actionsQueue, message);
	}

	@JmsListener(destination = "${broker.topic}")
	public void receive(ActionResultEvent message) {
		log.info("received message='{}'", message);
		var emitter = sseService.getEmitterMap().get(message.getTransactionId());
		if (emitter == null)
			log.info("No emitter found for this message: {}", message);
		else {
			try {
				emitter.send("OK");
			} catch (IOException e) {
				log.info("Error while sending emitter data", e);
			} finally {
				emitter.complete();
			}
		}
	}
}
