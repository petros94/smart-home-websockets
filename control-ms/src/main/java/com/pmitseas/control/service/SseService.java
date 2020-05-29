package com.pmitseas.control.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SseService {

  private static Map<UUID, SseEmitter> currentEmitters = new ConcurrentHashMap<>();

  public SseEmitter createPaymentEmitter(UUID transactionID) {
    log.info("Start createPaymentEmitter with transactionID {}", transactionID);
    SseEmitter emitter = new SseEmitter(1800000L);
    emitter.onCompletion(() -> currentEmitters.remove(transactionID));
    currentEmitters.put(transactionID, emitter);
    return emitter;
  }

  public Map<UUID, SseEmitter> getEmitterMap() {
    return currentEmitters;
  }
}
