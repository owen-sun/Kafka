package com.ibm.marketplace.checkout.orderhub.ordersubmit.messagehub;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.marketplace.checkout.orderhub.ordersubmit.messagehub.admin.CreateTopicConfig;
import com.ibm.marketplace.checkout.orderhub.ordersubmit.messagehub.admin.CreateTopicParameters;

public class OrderMessageProducer {

	private static Properties PROP = new Properties(); // kafka config
	private static final long _24H_IN_MILLISECONDS = 3600000L * 24; // topic retention period
	private final KafkaProducer<String, String> kafkaProducer;
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderMessageProducer.class.getName());

	public OrderMessageProducer() {

		LOGGER.debug("start initiazing message producer");

		if (PROP.size() <= 0) {
			try {
				InputStream input = OrderMessageProducer.class.getResourceAsStream("messagehub.properties");

				// load a properties file
				PROP.load(input);

				// get the property value and print it out
				System.out.println(PROP.values());

			} catch (IOException ex) {
				LOGGER.error("failed to initiaze message producer");
				ex.printStackTrace();
			}
		}

		kafkaProducer = new KafkaProducer<String, String>(PROP);

	}

	public boolean sendOrder() {

		String topicName = "OrderSubmission";
		try {
			createTopic(topicName);
		} catch (Exception e) {
			LOGGER.error("failed to create topic");
			e.printStackTrace();
			return false;
		}

		OrderMessage msg = new OrderMessage();

		try {
			msg.setMsgID(System.currentTimeMillis());
			msg.setMsgSrc("SS");
			msg.setMsgType("DSW");
			msg.setOrderDocID("aaa");

			ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName,
					String.valueOf(msg.getMsgType()), msg.getOrderDocID());

			LOGGER.debug("start sending order submission msg: " + msg.getMsgID());
			
			Future<RecordMetadata> future = kafkaProducer.send(record);
			
			RecordMetadata recordMetadata = future.get(5000, TimeUnit.MILLISECONDS);
			
			LOGGER.error("end sending order submission msg:" + String.valueOf(recordMetadata.offset()));
//			while (!future.isDone()) {
//				RecordMetadata recordMetadata = future.get();
//				
//			}
//			
//			msg.setMsgID(System.currentTimeMillis());
//			msg.setMsgSrc("SS");
//			msg.setMsgType("GAIA");
//			msg.setOrderDocID("bbb");
//			
//			record = new ProducerRecord<String, String>(topicName,
//					String.valueOf(msg.getMsgType()), msg.getOrderDocID());
//			
//			kafkaProducer.send(record);
			
			kafkaProducer.close();

		} catch (Exception e) {
			LOGGER.error("error on sending order to message hub.");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Creates a topic or ignores an 'Already Exists' response
	 * <p/>
	 * 
	 * @param topicName
	 *            Name of the topic
	 * @return the body of the HTTP response
	 * @throws Exception
	 *             if an unexpected error occurs
	 */
	public static String createTopic(String topicName) throws Exception {

		RESTRequest restApi = new RESTRequest(PROP.getProperty("kafka_admin_url"), PROP.getProperty("api_key"));

		// Create a topic, ignore a 422 response - this means that the topic name already exists.
		return restApi.post("/admin/topics",
				new CreateTopicParameters(topicName, 2, new CreateTopicConfig(_24H_IN_MILLISECONDS)).toString(),
				new int[] { 422 });
	}

	public static void main(String[] args) {
		OrderMessageProducer producer = new OrderMessageProducer();
		producer.sendOrder();
	}

}
