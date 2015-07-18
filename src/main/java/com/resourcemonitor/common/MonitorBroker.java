package com.resourcemonitor.common;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Encapsulate the broker interactions.
 */
public class MonitorBroker {
    // session
    private Session session;
    // producer
    private MessageProducer producer;
    // consumer
    private MessageConsumer consumer;
    private String url;
    private String topicName;
    
    /**
     * Create the broker
     */
    public MonitorBroker(boolean sender) {
        url = Configuration.getInstance().getBrokerUrl();
        topicName = Configuration.getInstance().getTopicName();
        try {
            init(sender);
        } catch (MonitorException e) {
            e.printStackTrace();
            System.out.println("Failed to initialize the system..");
            System.exit(1);
        }
    }

    /**
     * Initialize the connections. Create the session, producer and consumer
     * @param sender weather this is a producer or a consumer, student can choose
     *               not to create the producer, when sender = false and student can
     *               choose not to create the consumer when sender = true
     * @throws MonitorException
     */
    private void init(boolean sender) throws MonitorException {

        /** implement your code
        It’s similar to the ActiveMQ example in lab
        1. create topic/queue connection session
        2. If it’s sender, create a producer, otherwise, create a consumer
        */
    	
    	try {
	    	// Create a ConnectionFactory
	        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
	
	        // Create a Connection
	        Connection connection = connectionFactory.createConnection();
	        connection.start();
	
	        // Create a Session
	        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	
	        // Create the destination (Topic or Queue)
	        Destination destination = session.createTopic(topicName);
	
	        //Creates producer if sender, creates consumer if otherwise
	        if (sender) {
	        	// Create a MessageProducer from the Session to the Topic or Queue
		        producer = session.createProducer(destination);
		        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	        } else {
	        	// Create a MessageConsumer from the Session to the Topic or Queue
                consumer = session.createConsumer(destination);
	        }
    	} catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
    
    /**
     * Create a JMS message using the Monitor Data and send using the producer
     * @param data MonitorData instance
     * @throws JMSException 
     */
    public void send(MonitorData data) throws JMSException {
        /** implement your code
           Producer send message to ActiveMQ broker
           1. Construct/set message body by using javax.jms.MapMessage
           2. Send the message
        */
    	Message message = session.createMessage();
    	message.setDoubleProperty("cpu", data.getCpu());
    	message.setDoubleProperty("memory", data.getMemory());
    	message.setIntProperty("workerId", data.getWorkerId());
    	message.setLongProperty("time", data.getTime());
    	message.setBooleanProperty("process", data.isProcess());
    	
    	//Send message
    	producer.send(message);
    }
    
    /**
     * Receive a JMS message and convert it to MonitorData. After the monitor data is created call
     * the handler.onMessage(monitorData).
     * @param handler the handler to call with the monitor message
     * @throws JMSException 
     */
    public void startReceive(final ReceiveHandler handler) throws JMSException {
        /** implement your code
           Consumer receive messages from ActiveMQ broker
           1. If receive any message, deserialize it and fill them into MonitorData object
           2. Using handler.onMessage
        */

    	consumer.setMessageListener(new MessageListener(){
    	    public void onMessage(Message message){
    	    	
    	    	MonitorData monitorData = new MonitorData();
    	    	try {
    	    		
					monitorData.setCpu(message.getDoubleProperty("cpu"));
					System.out.println("Success");
					monitorData.setMemory(message.getDoubleProperty("memory"));
					monitorData.setWorkerId(message.getIntProperty("workerId"));
					monitorData.setTime(message.getLongProperty("time"));
					monitorData.setProcess(message.getBooleanProperty("process"));
					System.out.println("Success");
					
				} catch (JMSException e) {
					System.out.println("Failure");
					e.printStackTrace();
				}
    	    	
    	    	handler.onMessage(monitorData);
    	    }
    	  }
    	);
    	
    }
    
}
