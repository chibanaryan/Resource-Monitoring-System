package com.resourcemonitor.client;

import com.resourcemonitor.common.MonitorData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps the raw information coming from the daemons. The aggregator use
 * this information to create a aggregated view. Then aggregator clears the used information.
 */
public class DataCollection {
    // messages for nodes
    private Map<Integer, ArrayList<MonitorData>> nodes = new HashMap<Integer, ArrayList<MonitorData>>();

    // messages for processes
    private Map<Integer, ArrayList<MonitorData>> procs = new HashMap<Integer, ArrayList<MonitorData>>();

    /**
     * add a message. If process id != null message is a process message
     */
    public void addMessage(MonitorData m) {
        if (!m.isProcess()) {
            Integer id = m.getWorkerId();
            ArrayList<MonitorData> messages = nodes.get(id);
            if (messages == null) {
                messages = new ArrayList<MonitorData>();
                nodes.put(id, messages);
            }
            synchronized (messages) {
                messages.add(m);
            }
        } else {
            Integer id = m.getWorkerId();
            ArrayList<MonitorData> messages = procs.get(id);
            if (messages == null) {
                messages = new ArrayList<MonitorData>();
                procs.put(id, messages);
            }
            synchronized (messages) {
                messages.add(m);
            }
        }
    }

    /**
     * Get the map containing the node messages
     * @return map containing the node messages
     */
    public Map<Integer, ArrayList<MonitorData>> getNodes() {
        return nodes;
    }

    /**
     * Get the map containing the process messages
     * @return map containing the process messages
     */
    public Map<Integer, ArrayList<MonitorData>> getProcs() {
        return procs;
    }
}
