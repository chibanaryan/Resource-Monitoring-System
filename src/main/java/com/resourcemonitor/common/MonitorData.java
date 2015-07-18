package com.resourcemonitor.common;

/**
 * The monitor data coming from process and node
 */
public class MonitorData {
    // cpu
    private double cpu;
    // memory
    private double memory;
    // daemon id
    private int workerId;
    // timestamp
    private long time;
    // weather these are process data or cpu data
    private boolean process = false;

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public boolean isProcess() {
        return process;
    }

    public void setProcess(boolean processId) {
        this.process = processId;
    }
}
