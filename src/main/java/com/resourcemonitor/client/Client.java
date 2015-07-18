package com.resourcemonitor.client;

import javax.jms.JMSException;

import com.resourcemonitor.common.Configuration;
import com.resourcemonitor.common.MonitorBroker;
import com.resourcemonitor.common.MonitorData;
import com.resourcemonitor.common.ReceiveHandler;

/**
 * The main program to run the client
 */
public class Client {
    // the raw data collection
    private DataCollection dataCollection;
    // the aggregator to create the summery
    private Aggregator aggregator;
    // UI
    private MonitorUI monitorUI;
    // Broker
    private MonitorBroker broker;
    // aggregator running interval
    private long aggregatorTime;
    // ui update interval
    private long uiUpdateTime;

    public Client() {
        aggregatorTime = Configuration.getInstance().getAggregatorInterval();
        uiUpdateTime = Configuration.getInstance().getUiUpdateInterval();
        long averageWindow = Configuration.getInstance().getAverageWindow();
        // create the collections
        dataCollection = new DataCollection();
        // summery
        GraphModel graphModel = new GraphModel(Configuration.getInstance().getNoOfPoints());
        // aggregator with offset 1 sec
        aggregator = new Aggregator(dataCollection, graphModel, averageWindow);
        // the UI
        monitorUI = new MonitorUI("Monitor", graphModel);
        // the broker
        broker = new MonitorBroker(false);
    }

    public void start() throws JMSException {
        // a thread to update the UI
        Thread t = new Thread(new UIUpdateWorker());
        t.start();

        // start the aggregate thread
        Thread aggreateThread = new Thread(new AggregateWorker());
        aggreateThread.start();

        // start receiving
        broker.startReceive(new ReceiveHandler() {
            public void onMessage(MonitorData m) {
                dataCollection.addMessage(m);
            }
        });
    }

    // aggregate the results
    private class AggregateWorker implements Runnable {
        public void run() {
            while (true) {
                aggregator.generate();
                try {
                    Thread.sleep(aggregatorTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // we are going to generate every 1 sec and update the UI every 1 sec
    private class UIUpdateWorker implements Runnable {
        public void run() {
            while (true) {
                monitorUI.update();
                try {
                    Thread.sleep(uiUpdateTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Run the client
     * @param args nothing
     * @throws JMSException 
     */
    public static void main(String[] args) throws JMSException {
        Client c = new Client();
        c.start();
    }
}
