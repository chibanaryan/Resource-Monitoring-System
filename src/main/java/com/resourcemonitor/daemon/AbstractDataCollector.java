package com.resourcemonitor.daemon;

import javax.jms.JMSException;

import com.resourcemonitor.common.MonitorBroker;
import com.resourcemonitor.common.MonitorData;

import org.hyperic.sigar.Sigar;

/**
 * Common functionality for data collectors
 */
public abstract class AbstractDataCollector implements Runnable {
    // the sigar
    protected Sigar sigar;
    // the worker id
    protected int workerId;
    // the broker
    protected MonitorBroker broker;
    // time interval for collecting data
    private long time = 500;

    public AbstractDataCollector(int workerId, MonitorBroker broker, long sampleTime) {
        sigar = new Sigar();
        this.workerId = workerId;
        this.time = sampleTime;
        this.broker = broker;
    }

    // collect data and send to client
    public void run() {
        while (true) {
            MonitorData data = getMonitorData();
            try {
				sendToClient(data);
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The student should implement this method for Node data collector and Proc Data collector
     * @return monitor data
     */
    public abstract MonitorData getMonitorData();

    /**
     * Util function to send the data
     * @param data
     * @throws JMSException 
     */
    private void sendToClient(MonitorData data) throws JMSException {
    	//System.out.println(data.getCpu());
        if (data != null) {
            broker.send(data);
        }
    }
    
}
