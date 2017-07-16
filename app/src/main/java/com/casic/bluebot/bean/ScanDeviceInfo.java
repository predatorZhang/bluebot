package com.casic.bluebot.bean;

/**
 * Created by admin on 2015/5/1.
 */
public class ScanDeviceInfo
{
    private String devId;

    private String sensorId;

    private String ip;

    private String currentValue;

    public String getCurrentValue()
    {
        return currentValue;
    }

    public void setCurrentValue(String currentValue)
    {
        this.currentValue = currentValue;
    }

    public String getDevId()
    {
        return devId;
    }

    public void setDevId(String devId)
    {
        this.devId = devId;
    }

    public String getSensorId()
    {
        return sensorId;
    }

    public void setSensorId(String sensorId)
    {
        this.sensorId = sensorId;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }
}
