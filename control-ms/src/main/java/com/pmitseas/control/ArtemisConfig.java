package com.pmitseas.control;

import com.pmitseas.control.event.ActionEvent;
import com.pmitseas.control.event.ActionResultEvent;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;

@Configuration
public class ArtemisConfig {

	@Value("${broker.url}")
	private String brokerUrl;

	@Value("${broker.relay.user}")
	private String username;

	@Value("${broker.relay.password}")
	private String password;

	@Bean
	public ActiveMQConnectionFactory senderActiveMQConnectionFactory(){
		ActiveMQConnectionFactory connectionFactory =  new ActiveMQConnectionFactory(brokerUrl);
		connectionFactory.setUser(username);
		connectionFactory.setPassword(password);
		connectionFactory.setConnectionTTL(120000L);
		return connectionFactory;
	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		HashMap<String, Class<?>> typeIdMappings = new HashMap<>();
		typeIdMappings.put(ActionEvent.class.getSimpleName(), ActionEvent.class);
		typeIdMappings.put(ActionResultEvent.class.getSimpleName(), ActionResultEvent.class);
		converter.setTypeIdMappings(typeIdMappings);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(senderActiveMQConnectionFactory());
		factory.setMessageConverter(jacksonJmsMessageConverter());
		factory.setPubSubDomain(true);
		return factory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory(){
		return new CachingConnectionFactory(senderActiveMQConnectionFactory());
	}

	@Bean("jmsTemplate")
	public JmsTemplate jmsTemplate(){
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory());
		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
		return jmsTemplate;
	}
}