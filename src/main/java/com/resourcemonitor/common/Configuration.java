package com.resourcemonitor.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration read from the monitor.properties
 */
public class Configuration {
    // singleton
    private static Configuration configuration = new Configuration();
    // broker url
    private String brokerUrl;
    // topic name
    private String topicName;
    // daemons collect information with this granuality
    private long sampleTime = 100;
    // aggregator runs
    private long aggregatorInterval = 1000;
    // ui is refreshed
    private long uiUpdateInterval = 1000;
    // the values get averaged
    private long averageWindow = 1000;
    // number of points to display
    private int noOfPoints = 60;

    private Configuration() {
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream("monitor.properties");
        } catch (FileNotFoundException e) {
            handleConfigurationError("monitor.properties file cannot be found in the class path..");
        }

        if (in == null) {
            handleConfigurationError("monitor.properties file cannot be found in the class path..");
        } else {
            try {
                prop.load(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (prop.getProperty(Constants.BROKER_URL) != null) {
            brokerUrl = prop.getProperty(Constants.BROKER_URL).trim();
        } else {
            handleConfigurationError("Broker URL must be specified");
        }

        if (prop.getProperty(Constants.TOPIC_NAME) != null) {
            topicName = prop.getProperty(Constants.TOPIC_NAME).trim();
        } else {
            handleConfigurationError("Topic name must be specified");
        }

        String timeProp = prop.getProperty("sample.time", Long.toString(sampleTime));
        sampleTime = Long.parseLong(timeProp);

        String aggregateProp = prop.getProperty("aggregate.time", Long.toString(aggregatorInterval));
        aggregatorInterval = Long.parseLong(aggregateProp);

        String uiUpdateProp = prop.getProperty("ui.update.time", Long.toString(uiUpdateInterval));
        uiUpdateInterval = Long.parseLong(uiUpdateProp);

        String averageWindowProp = prop.getProperty("average.window", Long.toString(averageWindow));
        averageWindow = Long.parseLong(averageWindowProp);

        String numPointsProp = prop.getProperty("summery.points", Long.toString(noOfPoints));
        noOfPoints = Integer.parseInt(numPointsProp);
    }

    public static Configuration getInstance() {
        return configuration;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public String getTopicName() {
        return topicName;
    }

    public long getSampleTime() {
        return sampleTime;
    }

    public long getAggregatorInterval() {
        return aggregatorInterval;
    }

    public long getUiUpdateInterval() {
        return uiUpdateInterval;
    }

    public long getAverageWindow() {
        return averageWindow;
    }

    public int getNoOfPoints() {
        return noOfPoints;
    }

    private void handleConfigurationError(String msg) {
        System.out.println(msg);
        System.exit(1);
    }
}
