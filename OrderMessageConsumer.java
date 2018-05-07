package com.ibm.marketplace.checkout.orderhub.ordersubmit.messagehub;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderMessageConsumer {
	private static Properties PROP = new Properties(); // kafka config
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderMessageConsumer.class.getName());

	public OrderMessageConsumer() {
		
		LOGGER.debug("start initiazing message consumer");

		if (PROP.size() <= 0) {
			try {
				InputStream input = OrderMessageProducer.class.getResourceAsStream("consumer.properties");

				// load a properties file
				PROP.load(input);

			} catch (IOException ex) {
				LOGGER.error("failed to initiaze message consumer");
				ex.printStackTrace();
			}
		}
	}
	
	public void readOrder() {
		String topicName = "OrderSubmission";
		String groupName = "UOR";
		KafkaConsumer<String, OrderMessage>  consumer =  new KafkaConsumer<String, OrderMessage>(PROP);
		consumer.subscribe(Arrays.asList(topicName));
		
		while(true) {
			ConsumerRecords<String, OrderMessage> crs = consumer.poll(1000);
			if(crs !=null) {
				LOGGER.debug("*******************************");
				for(ConsumerRecord<String, OrderMessage> cr: crs) {
					LOGGER.debug("Offset: " + cr.offset());
					LOGGER.debug("Order source: " + cr.value().getMsgSrc());
					LOGGER.debug("Order type: " + cr.value().getMsgType());
					LOGGER.debug("Order doc ID: " + cr.value().getOrderDocID());
					consumer.commitSync();
				}
			}else {
				break;
			}
		}
		
		consumer.close();
	}

	public static void main(String[] args) {
		OrderMessageConsumer c = new OrderMessageConsumer();
		c.readOrder();

	}

}
