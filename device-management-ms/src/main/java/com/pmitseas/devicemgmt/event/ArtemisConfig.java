package com.pmitseas.devicemgmt.event;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;

@Configuration
@EnableJms
public class ArtemisConfig {

	@Value("${broker.url}")
	private String brokerUrl;

	@Value("${broker.relay.user}")
	private String username;

	@Value("${broker.relay.password}")
	private String password;

	@Bean
	public ActiveMQConnectionFactory receiverActiveMQConnectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		connectionFactory.setUser(username);
		connectionFactory.setPassword(password);
		connectionFactory.setConnectionTTL(120000L);

		return connectionFactory;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(receiverActiveMQConnectionFactory());
		factory.setMessageConverter(jacksonJmsMessageConverter());

		factory.setConcurrency("3-10");

		return factory;
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
	public CachingConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory(receiverActiveMQConnectionFactory());
	}

	@Bean("jmsTopicTemplate")
	public JmsTemplate jmsTopicTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory());
		jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}

}

