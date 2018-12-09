package com.scb.transmitter.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.scb.transmitter.model.MsAuditLog;
import com.scb.transmitter.model.MsErrorLog;
import com.scb.transmitter.model.RequestData;

@Component
public class ServiceUtil {

	public String getCurrentDateTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime.toString();
	}

	public long getTransactionId() {
		Random random = new Random(System.nanoTime() % 100000);
		long uniqueTansactionId = random.nextInt(1000000000);
		return uniqueTansactionId;
	}

	public MsAuditLog getAuditLogDetails(RequestData requestData) {
		MsAuditLog auditLog = MsAuditLog.builder().msComponent("TransmitterService").payload(toByteArray(requestData.getPayload()))
				.timeStamp(requestData.getCreatedOn()).uuid(requestData.getTransactionID())
				.logMessage("Calling from TransmitterService").build();
		return auditLog;
	}

	// convert object to toByteArray
	public static byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream oos = null;
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}

	public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
		Object obj = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (ois != null) {
				ois.close();
			}
		}
		return obj;
	}

	public MsErrorLog getErrorLogDetails(Exception e) {
		MsErrorLog errorLog = MsErrorLog.builder().errorMessage(e.getMessage()).msComponent("Router")
				.stackTrace(toByteArray(e)).build();

		return errorLog;
	}
}