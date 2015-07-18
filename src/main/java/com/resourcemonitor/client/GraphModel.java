package com.resourcemonitor.client;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This is the model we are going to render
 */
public class GraphModel {
    // time at which we have the values, all four of the following values correspond to these times
    private ArrayList<Long> time = new ArrayList<Long>();
    // node memory aggregated together
    private ArrayList<Double> memory = new ArrayList<Double>();
    // node cpu aggregated together
    private ArrayList<Double> cpu = new ArrayList<Double>();
    // memory for the process
    private ArrayList<Double> procMemory = new ArrayList<Double>();
    // cpu for the process
    private ArrayList<Double> procCpu = new ArrayList<Double>();
    // number of element we are going to keep
    private int size;
    // the last time we recorded an element
    private long lastTime = System.currentTimeMillis();


    public GraphModel(int size) {
        this.size = size;
    }

    /**
     * Number of data points
     * @return number of data points
     */
    public int getCurrentSize() {
        return time.size();
    }

    /**
     * Add a new time and corresponding values. If we have more elements than size remove them
     * @param c node cpu
     * @param m node memory
     * @param pc process cpu
     * @param pm process memory
     * @param t time
     */
    public void add(double c, double m, double pc, double pm, long t) {
        if (t > System.currentTimeMillis()) {
            lastTime = System.currentTimeMillis();
        } else {
            lastTime = t;
        }
        // we are going to use some crude synchronization
        synchronized (this) {
            memory.add(m);
            cpu.add(c);
            procMemory.add(pm);
            procCpu.add(pc);


            time.add(t);
            while (time.size() >= size) {
                time.remove(0);
            }

            while (cpu.size() >= size) {
                cpu.remove(0);
            }

            while (memory.size() >= size) {
                memory.remove(0);
            }

            while (procCpu.size() >= size) {
                procCpu.remove(0);
            }

            while (procMemory.size() >= size) {
                procMemory.remove(0);
            }
        }
    }

    /**
     * Node memory
     * @return node memory value list
     */
    public ArrayList<Double> getNodeMemory() {
        synchronized (this) {
            ArrayList<Double> copy = new ArrayList<Double>(memory);
            Collections.copy(copy, memory);
            return copy;
        }
    }

    /**
     * Node cpu list
     * @return node cpu value list
     */
    public ArrayList<Double> getNodeCpu() {
        synchronized (this) {
            ArrayList<Double> copy = new ArrayList<Double>(cpu);
            Collections.copy(copy, cpu);
            return copy;
        }
    }

    /**
     * X axis, time
     * @return time list
     */
    public ArrayList<Long> getTime() {
        synchronized (this) {
            ArrayList<Long> copy = new ArrayList<Long>(time);
            Collections.copy(copy, time);
            return copy;
        }
    }

    /**
     * The last time we record a data point
     * @return the last time
     */
    public long getLastTime() {
        return lastTime;
    }

    /**
     * Process memory
     * @return list of process memory
     */
    public ArrayList<Double> getProcMemory() {
        synchronized (this) {
            ArrayList<Double> copy = new ArrayList<Double>(procMemory);
            Collections.copy(copy, procMemory);
            return copy;
        }

    }

    /**
     * Process cpu
     * @return list of process cpu
     */
    public ArrayList<Double> getProcCpu() {
        synchronized (this) {
            ArrayList<Double> copy = new ArrayList<Double>(procCpu);
            Collections.copy(copy, procCpu);
            return copy;
        }
    }
}
