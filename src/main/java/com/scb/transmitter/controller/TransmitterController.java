package com.scb.transmitter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.scb.transmitter.model.AuditLog;
import com.scb.transmitter.model.RequestData;
import com.scb.transmitter.model.ResponseMessage;
import com.scb.transmitter.model.TransmitterModel;
import com.scb.transmitter.service.TransmitterService;
import com.scb.transmitter.serviceImpl.InternalApiInvoker;
import com.scb.transmitter.utils.ReceiverConstants;
import com.scb.transmitter.utils.ServiceUtil;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(ReceiverConstants.TRANSMITTER_SERVICE_URL)
public class TransmitterController {

	// @Autowired
	// private JMSService jmsService;

	@Autowired
	private TransmitterService transmitterService;
	
	@Autowired
	private ServiceUtil commonMethods;
	
	@Autowired
	private InternalApiInvoker internalApiInvoker;

	/*
	 * @Autowired private HTTPSService httpsService;
	 */

	// @RequestMapping(value = ReceiverConstants.JMS_SERVICE_REQUEST_HANDLE_URL,
	// method = RequestMethod.POST, produces = { "application/xml",
	// "application/json"})
	// public ResponseEntity<Void> publishMessage(@RequestBody CustomerRequestData
	// customerRequestData){
	// jmsService.sendMessage(customerRequestData);
	// log.info("JmsServicekRequestController - Request data : " +
	// customerRequestData);
	// return new ResponseEntity<Void>(HttpStatus.OK);
	// }
	/*
	 * // @RequestMapping(value = "/service2/{query}", method = {RequestMethod.GET})
	 * 
	 * @RequestMapping(value = ReceiverConstants.HTTPS_SERVICE_REQUEST_HANDLE_URL,
	 * method = RequestMethod.POST, produces = { "application/xml",
	 * "application/json"}) public ResponseEntity<Void>
	 * httpspublishMessage(@RequestBody CustomerRequestData customerRequestData){
	 * httpsService.sendMessage(customerRequestData);
	 * log.info("HttpsServicekRequestController - Request data : " +
	 * customerRequestData); return new ResponseEntity<Void>(HttpStatus.OK); }
	 */

	@RequestMapping(value = ReceiverConstants.TRANSMIT_MESSAGE_URL, method = RequestMethod.POST, produces = {"application/xml", "application/json" })
	public ResponseEntity<ResponseMessage> httpspublishMessage(@RequestBody RequestData requestData) {
		log.info("Request data : " + requestData);
		AuditLog auditLog = commonMethods.getAuditLog(requestData, "INITIATED", "Message transmission request initiated");
		ResponseEntity<AuditLog> responseAuditLog = internalApiInvoker.auditLogApiCall(auditLog);
		
		ResponseMessage responseMessage = transmitterService.transmitMessage(requestData);
		
		if (responseMessage.getResponseCode() != 200) {
			auditLog = commonMethods.getAuditLog(requestData, "FAILED", responseMessage.getResponseMessage());
		} else {
			auditLog = commonMethods.getAuditLog(requestData, "COMPLETED", "Message transmission for transaction type: " + requestData.getTransactionType() + " successfully");
		}

		responseAuditLog = internalApiInvoker.auditLogApiCall(auditLog);
		log.info("Response message from Transmitter Service : " + responseMessage);
		return new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
	}

	@RequestMapping(value = com.scb.transmitter.utils.ReceiverConstants.ADD_TRANSMITTER_URL, method = RequestMethod.POST, produces = {
			"application/xml", "application/json" })
	public ResponseEntity<TransmitterModel> addTransmitterData(@RequestHeader Map<String, String> requestMap,
			@RequestBody TransmitterModel transmitterData) {
		log.info("Received RequestHeader : " + requestMap);
		log.info("Received transmitterModel : " + transmitterData);

		TransmitterModel transmitterModel = transmitterService.addTransmitterData(transmitterData);

		if (transmitterModel == null) {
			log.info("Data not able to add into DB - transmitterModel : " + transmitterModel);
			return new ResponseEntity<TransmitterModel>(transmitterModel, HttpStatus.CONFLICT);
		}

		log.info("Data added into DB - transmitterModel : " + transmitterModel);
		return new ResponseEntity<TransmitterModel>(transmitterModel, HttpStatus.OK);
	}

	// @RequestMapping(value = ReceiverConstants.MODIFY_TRANSMITTER_URL, method =
	// RequestMethod.POST, produces = {"application/xml", "application/json" })
	@PutMapping("/modifyTransmitter")
	public ResponseEntity<ResponseMessage> modifyDupcheckRule(@RequestHeader Map<String, String> requestMap,
			@RequestBody TransmitterModel transmitterData) {
		log.info("Received RequestHeader : " + requestMap);
		log.info("Received DupcheckRuleModel : " + transmitterData);

		ResponseMessage rm = transmitterService.modifyTransmitterData(transmitterData);

		if (rm.getResponseCode() != 200) {
			log.info("Data not able to udpate into DB - ResponseMessage : " + rm);
			return new ResponseEntity<ResponseMessage>(rm, HttpStatus.CONFLICT);
		}

		log.info("Data updated into DB - ResponseMessage : " + rm);
		return new ResponseEntity<ResponseMessage>(rm, HttpStatus.OK);
	}

	@RequestMapping(value = ReceiverConstants.GET_TRANSMITTER_BY_TYPE_URL, method = RequestMethod.GET, produces = {
			"application/xml", "application/json" })
	public ResponseEntity<List<TransmitterModel>> getTransmitterByType(@RequestHeader Map<String, String> requestMap,
			@RequestBody TransmitterModel transmitterData) {
		log.info("Received RequestHeader : " + requestMap);
		log.info("Request dupcheckRuleModel: " + transmitterData);
		List<TransmitterModel> transmitterList = transmitterService.getTransmitterByType(transmitterData);
		log.info("DupcheckRules recieved from db : " + transmitterList);
		return new ResponseEntity<List<TransmitterModel>>(transmitterList, HttpStatus.OK);
	}

	@RequestMapping(value = ReceiverConstants.GET_ALL_TRANSMITTER_URL, method = RequestMethod.GET, produces = {
			"application/xml", "application/json" })
	public ResponseEntity<List<TransmitterModel>> getAllTransmitters(@RequestHeader Map<String, String> requestMap) {
		log.info("Received RequestHeader : " + requestMap);
		List<TransmitterModel> list = transmitterService.getAllTransmitterData();
		log.info("Transmitter rules received from DB : " + list);

		return new ResponseEntity<List<TransmitterModel>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = ReceiverConstants.GET_TRANSMITTER_BY_ID_URL, method = RequestMethod.GET, produces = {
			"application/xml", "application/json" })
	public ResponseEntity<TransmitterModel> getTransmitterById(@RequestHeader Map<String, String> requestMap,
			@PathVariable("transmitterId") long transmitterId) {
		log.info("RequestHeader received " + requestMap);
		log.info("Request Body : " + transmitterId);
		TransmitterModel transmitterModel = transmitterService.getTransmitterById(transmitterId);
		log.info("Transmitter received from DB : " + transmitterModel);
		return new ResponseEntity<TransmitterModel>(transmitterModel, HttpStatus.OK);
	}
	
	@RequestMapping(value = ReceiverConstants.DELETE_TRANSMITTER_URL, method = RequestMethod.DELETE, produces = {"application/xml", "application/json"})
    public ResponseEntity<Void> deleteTransmitterRule(@PathVariable("transmitterId") long transmitterId) {
		transmitterService.DeleteTransmitterModel(transmitterId);
        return new ResponseEntity<Void>(HttpStatus.OK);  	
    }
}