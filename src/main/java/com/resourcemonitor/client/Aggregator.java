package com.resourcemonitor.client;

import com.resourcemonitor.common.MonitorData;

import java.util.*;

/**
 * Create averages from the values and update the model
 */
public class Aggregator {
    // the raw message collection
    private DataCollection dataCollection;

    // this is the model we are going to render
    private GraphModel summery;

    // the time between two calculations
    private long window;

    /**
     * Create an aggregator
     * @param dataCollection row data coming from the broker
     * @param summery summery view
     * @param window the size of the window
     */
    public Aggregator(DataCollection dataCollection, GraphModel summery, long window) {
        this.dataCollection = dataCollection;
        this.summery = summery;
        this.window = window;
    }

    /**
     * Run through the messages in data collection and create a summery from this data. This method
     * is going to get called every milliseconds configured by "aggregate.time" property.
     * The middle of the current window can be found by "summery.getLastTime() + window". Student can
     * calculate the average for each of the values you need to display in this window by
     * going through the raw data collection.
     *
     * Make sure you delete the data from the row data collection that are already processed or that
     * are below the current window. If they are node deleted the data collection can grow.
     *
     * After the aggregate values are calculated you should add these values to the summery.
     *
     * In this method you may need to calculate values for multiple windows to compensate for the timing delays.
     *
     * ********* Student should implement this method ************
     */
    public void generate() {
        Map<Integer, ArrayList<MonitorData>> nodeMessageMap = dataCollection.getNodes();
        Map<Integer, ArrayList<MonitorData>> procMessageMap = dataCollection.getProcs();
        int procCount = 0;
        int count = 0;
        long time = 0;
        do {
            double cpu = 0;
            double mem = 0;
            double procCpu = 0;
            double procMem = 0;
            int totalMsgNode = 0;
            int totalMsgProc = 0;
            /* implement your code
                The following average values calculation cannot only aggregates the received messages without
                looking into their message weight, for instances, 5 messages collected in 10 seconds, 3 of
                them came from node A, 2 of them came from node B, the overall performance should be (0.6 * Perf_A + 0.4 * Perf_B).
                You could get the sender’s information from message.getWorkerId();
               1. Calculate the average values for overall cpu and mem usage on all nodes
               2. Calculate the average values for overall MPJ processes’ cpu and mem usage on all nodes
            */
            System.out.println(procCpu);
            count = nodeMessageMap.size();
            for(Integer key : nodeMessageMap.keySet()){
            	totalMsgNode += nodeMessageMap.get(key).size();
            }
            for(Integer key : nodeMessageMap.keySet()){
            	double subtotalCpu = 0;
            	double subtotalMem = 0;
            	for(MonitorData x : nodeMessageMap.get(key)){
            		subtotalCpu += x.getCpu();
            		subtotalMem += x.getMemory();
            	}
            	cpu += (subtotalCpu/totalMsgNode);
            	mem += (subtotalMem/totalMsgNode);
            }
            procCount = procMessageMap.size();
            for(Integer key : procMessageMap.keySet()){
            	totalMsgProc += procMessageMap.get(key).size();
            }
            System.out.println(totalMsgProc);
            for(Integer key : procMessageMap.keySet()){
            	double subtotalCpu = 0;
            	double subtotalMem = 0;
            	for(MonitorData x : procMessageMap.get(key)){
            		subtotalCpu += x.getCpu();
            		subtotalMem += x.getMemory();
            	}
            	procCpu += (subtotalCpu/totalMsgProc);
            	procMem += (subtotalMem/totalMsgProc);
            }
            System.out.println(procCpu);
            time = summery.getLastTime() + window;
            cpu = cpu * 100;
            //System.out.println(nodeMessageMap);
            if (procCount == 0 && count == 0) {
                summery.add(0, 0, 0, 0, System.currentTimeMillis());
            } else {
                summery.add(cpu, mem, procCpu, procMem, time);
                dataCollection.getNodes().clear();
                dataCollection.getProcs().clear();
            }
            
        } while (time < System.currentTimeMillis() - window);
    }
}
