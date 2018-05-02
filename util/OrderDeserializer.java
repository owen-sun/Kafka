/**
 * 
 */
package com.ibm.marketplace.checkout.orderhub.ordersubmit.messagehub.util;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.marketplace.checkout.orderhub.ordersubmit.messagehub.OrderMessage;

/**
 * @author zgsun
 * @param <T>
 *
 */
public class OrderDeserializer<T> implements Deserializer<OrderMessage>{

	/**
	 * 
	 */
	public OrderDeserializer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OrderMessage deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		OrderMessage order = null;
	    try {
	    	order = mapper.readValue(data, OrderMessage.class);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return order;
	}

}
