package com.resourcemonitor.daemon;

import com.resourcemonitor.common.MonitorBroker;
import com.resourcemonitor.common.MonitorData;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;


/**
 * Worker thread per node that sends
 * cpu and mem usage data.
 */
public class NodeDataCollector extends AbstractDataCollector {
    /**
     * Constructor
     * @param workerId daemon id
     * @param broker broker
     * @param sampleTime sampling time
     */
    public NodeDataCollector(int workerId, MonitorBroker broker, long sampleTime) {
        super(workerId, broker, sampleTime);
    }

    /**
     * Get machine data using Sigar APIs. This function will be called time to time. This time is configured by
     * sample.time property.
     *
     * The student needs to get the machine CPU usage and Memory usage using Sigar APIs,
     * create a MonitorData object and return.
     *
     * The Sigar instance is defined in <code>AbstractDataCollector</code>.
     *
     * @return a monitor data
     */
    public MonitorData getMonitorData() {
        MonitorData message = new MonitorData();
        Mem mem = null;
        CpuPerc cpuPerc = null;
        /*  implement your code here
	Get machine data using Sigar APIs. This function will be called time to time. This time is configured by sample.time in the monitor.properties.
        */
        
        //Get CPU percentage and memory with Sigar API
        try {
			cpuPerc = sigar.getCpuPerc();
			mem = sigar.getMem();
			//System.out.println("NODE:" + mem);
		} catch (SigarException e) {
			e.printStackTrace();
		}
        
        //Get system CPU percentage and set in MonitorData
        double sysCpu = cpuPerc.getSys();
        message.setCpu(sysCpu * 100);
 
        //Get memory used percentage and set in MonitorData
        double memUsed = mem.getUsedPercent();
        message.setMemory(memUsed);
        
        return message;
    }
}
