package com.scb.transmitter.config;

import org.springframework.context.annotation.Configuration;

@Configuration  
public class TransmitterConfig {
	
//	 private static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";
//     
//	    private static final String ORDER_QUEUE = "order-queue";
//	 
//	    @Bean
//	    public ActiveMQConnectionFactory connectionFactory(){
//	        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//	        connectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
//	        connectionFactory.setTrustedPackages(Arrays.asList("com.websystique.springmvc"));
//	        return connectionFactory;
//	    }
//	     
//	    @Bean
//	    public JmsTemplate jmsTemplate(){
//	        JmsTemplate template = new JmsTemplate();
//	        template.setConnectionFactory(connectionFactory());
//	        template.setDefaultDestinationName(ORDER_QUEUE);
//	        return template;
//	    }

}
