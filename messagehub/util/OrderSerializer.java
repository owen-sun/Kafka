/**
 * 
 */
package com.ibm.marketplace.checkout.orderhub.ordersubmit.messagehub.util;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.marketplace.checkout.orderhub.ordersubmit.messagehub.OrderMessage;

/**
 * @author zgsun
 * @param <T>
 *
 */
public class OrderSerializer<T> implements Serializer<OrderMessage>{

	/**
	 * 
	 */
	public OrderSerializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] serialize(String topic, OrderMessage data) {
		
		if (data == null){
			return null;
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(data).getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;		
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
