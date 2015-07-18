package com.resourcemonitor.daemon;

import com.resourcemonitor.common.Configuration;
import com.resourcemonitor.common.MonitorBroker;

/**
 * Run the node data collector and process data collector.
 */
public class MonitorDaemon {
    private int workerId = 1;
    // the broker
    private MonitorBroker broker;
    // sample time
    private long sampleTime;

    /**
     * Create the monitor daemon
     * @param workerId every daemon should have a unique id
     */
    public MonitorDaemon(int workerId) {
        broker = new MonitorBroker(true);
        this.workerId = workerId;
        this.sampleTime = Configuration.getInstance().getSampleTime();
    }

    // start collecting data in separate threads for process and node
    public void start() {
        NodeDataCollector nodeDataCollector = new NodeDataCollector(workerId, broker, sampleTime);
        ProcDataCollector procDataCollector = new ProcDataCollector(workerId, broker, sampleTime);
        // start a node data collector
        Thread t = new Thread(nodeDataCollector);
        t.start();
        // start a process data collector
        Thread t2 = new Thread(procDataCollector);
        t2.start();
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("Worker ID must be specified...");
            System.exit(1);
        }
        System.out.println("Starting Monitor daemon with ID: " + args[0]);
        MonitorDaemon daemon = new MonitorDaemon(Integer.parseInt(args[0]));
        daemon.start();
    }
}

