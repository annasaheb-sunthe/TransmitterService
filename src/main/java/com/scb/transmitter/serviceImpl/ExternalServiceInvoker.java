package com.scb.transmitter.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.scb.transmitter.model.MsErrorLog;
import com.scb.transmitter.model.RequestData;
import com.scb.transmitter.model.ResponseMessage;
import com.scb.transmitter.utils.ServiceUtil;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ExternalServiceInvoker {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ServiceUtil commonMethods;
	
	public ResponseEntity<ResponseMessage> serviceApiCall(RequestData requestData, String serviceURL) {
		log.info("RequestData in serviceApiCall: " + requestData);
		log.info("RequestData in serviceURL: " + serviceURL);

		ResponseMessage responseMessage = new ResponseMessage();
		ResponseEntity<ResponseMessage> re = null;
		
		try {
			HttpEntity<String> entity = new HttpEntity<String>(requestData.getPayload());
			log.info("calling restTemplate...");
			//re = restTemplate.exchange(serviceURL, HttpMethod.POST, entity, ResponseMessage.class);
			entity = restTemplate.exchange(serviceURL, HttpMethod.POST, entity, String.class);
			log.info("reponseMessage : " + entity.getBody());
			responseMessage.setResponseCode(200);
			responseMessage.setResponseMessage(entity.getBody());
			re = new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.OK);
		} catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerEx) {
			log.info("HttpClientErrorException | HttpServerErrorException occured.... : " + httpClientOrServerEx.getMessage());
			httpClientOrServerEx.printStackTrace();
			MsErrorLog msErrorLog = commonMethods.getErrorLogDetails(httpClientOrServerEx);
			msErrorLog.setErrorCode(httpClientOrServerEx.getStatusCode().toString());
			msErrorLog.setUuid(requestData.getTransactionID());
			msErrorLog.setTimeStamp(requestData.getCreatedOn());
//			if (HttpStatus.INTERNAL_SERVER_ERROR.equals(httpClientOrServerEx.getStatusCode())) {
//				//msErrorLogApiCall(msErrorLog);
//				
//				return new ResponseEntity<BalanceEnquiryResponse>( commonMethods.getErrorResponse("Problem While calling serviceApiCall api"), HttpStatus.BAD_GATEWAY);
//			} else {
//				return new ResponseEntity<BalanceEnquiryResponse>( commonMethods.getErrorResponse("Problem While calling serviceApiCall api"), HttpStatus.BAD_GATEWAY);
//			}
			responseMessage.setResponseCode(502);
			responseMessage.setResponseMessage("Problem While calling downstream service");
			re = new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.BAD_GATEWAY);
		} catch (Exception e) {
			log.info("Exception occured.... : " + e.getMessage());
			e.printStackTrace();
			MsErrorLog msErrorLog = commonMethods.getErrorLogDetails(e);
			msErrorLog.setUuid(requestData.getTransactionID());
			msErrorLog.setTimeStamp(requestData.getCreatedOn());
			//msErrorLogApiCall(msErrorLog);
			//return new ResponseEntity<BalanceEnquiryResponse>( commonMethods.getErrorResponse("Problem While calling serviceApiCall api"), HttpStatus.BAD_GATEWAY);
			responseMessage.setResponseCode(502);
			responseMessage.setResponseMessage("Problem While calling downstream service");
			re = new ResponseEntity<ResponseMessage>(responseMessage, HttpStatus.BAD_GATEWAY);
		}
		return re;
	}
}