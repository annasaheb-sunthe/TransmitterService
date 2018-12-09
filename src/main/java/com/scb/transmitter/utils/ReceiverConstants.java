package com.scb.transmitter.utils;

public interface ReceiverConstants {
	
	public static final String TRANSMITTER_SERVICE_URL= "/transmitter";
	public static final String TRANSMIT_MESSAGE_URL = "/transmitMessage";
	public static final String JMS_SERVICE_REQUEST_HANDLE_URL="/publishMessage";
	public static final String HTTPS_SERVICE_REQUEST_HANDLE_URL="/httpspublishMessage";
	public static final String ADD_TRANSMITTER_URL="/addTransmitter";
	public static final String MODIFY_TRANSMITTER_URL="/modifyTransmitter";
	public static final String GET_TRANSMITTER_BY_TYPE_URL="/transmitterByType";
	public static final String GET_ALL_TRANSMITTER_URL="/allTransmitters";
	public static final String GET_TRANSMITTER_BY_ID_URL="/transmitterById/{transmitterId}";
}
