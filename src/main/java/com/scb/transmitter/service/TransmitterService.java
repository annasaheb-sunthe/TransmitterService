package com.scb.transmitter.service;

import java.util.List;

import com.scb.transmitter.model.RequestData;
import com.scb.transmitter.model.ResponseMessage;
import com.scb.transmitter.model.TransmitterModel;

public interface TransmitterService {
	public ResponseMessage transmitMessage(RequestData requestData);
	
	public TransmitterModel addTransmitterData(TransmitterModel transmitterData);

	public ResponseMessage modifyTransmitterData(TransmitterModel transmitterData);
	
	public List<TransmitterModel> getTransmitterByType(TransmitterModel transmitterData);
	
	public List<TransmitterModel> getTransmitterByMethod(TransmitterModel transmitterData);
	
	public List<TransmitterModel> getAllTransmitterData();
	
	public TransmitterModel getTransmitterById(long transmitterId);

	public void DeleteTransmitterModel(long transmitterId);
}