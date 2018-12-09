package com.scb.transmitter.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ExternalServiceConfig {
	@Value("${msbif.lti.downstreamServiceURL}")
	private String downstreamServiceURL;
}
