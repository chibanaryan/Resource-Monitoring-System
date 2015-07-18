package com.resourcemonitor.client;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;

/**
 * The UI
 */
public class MonitorUI extends ApplicationFrame {
    private static final long serialVersionUID = 1L;

    // cpu usage series
    private XYSeries nodeCpu;
    // mem usage series
    private XYSeries nodeMem;

    private XYSeries procCPU;
    private XYSeries procMem;

    // the model we are going to render
    private GraphModel model;

    /**
     * Create the charts using JFree charts
     * @param title ui title
     */
    public MonitorUI(String title) {
        super(title);
        XYSeriesCollection cpuDataSet = new XYSeriesCollection();
        XYSeriesCollection memDataSet = new XYSeriesCollection();

        JFreeChart cpuChart = ChartFactory.createXYLineChart("CPU Node", "Time", "CPU %", cpuDataSet,
                PlotOrientation.VERTICAL, true, true, false);
        JFreeChart memChart = ChartFactory.createXYAreaChart("Memory Node", "Time", "Memory Usage %", memDataSet,
                PlotOrientation.VERTICAL, true, true, false);

        nodeCpu = new XYSeries("Overall CPU usage");
        nodeMem = new XYSeries("Overall Memory usage");

        cpuDataSet.addSeries(nodeCpu);

        memDataSet.addSeries(nodeMem);


        cpuChart.getXYPlot().setDataset(cpuDataSet);
        ChartPanel chartPanel = new ChartPanel(cpuChart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        XYPlot plot = cpuChart.getXYPlot();
        plot.setDomainAxis(0, new DateAxis());

        memChart.getXYPlot().setDataset(memDataSet);
        ChartPanel memPanel = new ChartPanel(memChart);
        memPanel.setPreferredSize(new Dimension(500, 270));
        plot = memChart.getXYPlot();
        plot.setDomainAxis(0, new DateAxis());


        XYSeriesCollection procCpuDataSet = new XYSeriesCollection();
        XYSeriesCollection procMemDataSet = new XYSeriesCollection();

        JFreeChart procCpuChart = ChartFactory.createXYLineChart("CPU Process", "Time", "CPU %", procCpuDataSet,
                PlotOrientation.VERTICAL, true, true, false);
        JFreeChart procMemChart = ChartFactory.createXYAreaChart("Memory Process", "Time", "Memory Usage MB", procMemDataSet,
                PlotOrientation.VERTICAL, true, true, false);

        procCPU = new XYSeries("CPU usage Process");
        procMem = new XYSeries("Memory usage Process");

        procCpuDataSet.addSeries(procCPU);
        procMemDataSet.addSeries(procMem);


        procCpuChart.getXYPlot().setDataset(procCpuDataSet);
        ChartPanel procChartPanel = new ChartPanel(procCpuChart);
        procChartPanel.setPreferredSize(new Dimension(500, 270));
        XYPlot procPlot = procCpuChart.getXYPlot();
        procPlot.setDomainAxis(0, new DateAxis());

        procMemChart.getXYPlot().setDataset(procMemDataSet);
        ChartPanel procMemPanel = new ChartPanel(procMemChart);
        procMemPanel.setPreferredSize(new Dimension(500, 270));
        procPlot = procMemChart.getXYPlot();
        procPlot.setDomainAxis(0, new DateAxis());


        JPanel jpanel = new JPanel(new GridLayout(2, 2));
        jpanel.setPreferredSize(new Dimension(1200, 600));

        jpanel.add(chartPanel);
        jpanel.add(memPanel);

        jpanel.add(procChartPanel);
        jpanel.add(procMemPanel);

        setContentPane(jpanel);

        this.pack();
        this.setVisible(true);
    }

    public MonitorUI(String title, GraphModel model) {
        this(title);
        this.model = model;
    }

    /**
     * Render the model
     */
    public void update() {
        // clear the current charts
        nodeMem.clear();
        nodeCpu.clear();

        // add the values again
        for (int i = 0; i < model.getCurrentSize(); i++) {
            nodeCpu.add(model.getTime().get(i), model.getNodeCpu().get(i));

            nodeMem.add(model.getTime().get(i), model.getNodeMemory().get(i));
        }

        // clear the charts
        procMem.clear();
        procCPU.clear();

        // add the values again
        for (int i = 0; i < model.getCurrentSize(); i++) {
            procCPU.add(model.getTime().get(i), model.getProcCpu().get(i));

            procMem.add(model.getTime().get(i), model.getProcMemory().get(i));
        }

        // repaint the UI
        repaint();
    }
}
 


