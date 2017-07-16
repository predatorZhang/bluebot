package com.casic.bluebot.bean;

public class HCHCDevice {
	private String name;
	private String deviceID;
	private String IP;
	private int TCP_Port;
	private int UDP_Port;
	
	public HCHCDevice(){
		
	}
	
	public HCHCDevice(String name, String deviceID, String IP, int TCP_Port, int UDP_Port){
		this.name = name;
		this.deviceID = deviceID;
		this.IP = IP;
		this.TCP_Port = TCP_Port;
		this.UDP_Port = UDP_Port;
	}
	
	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getTCP_Port() {
		return TCP_Port;
	}

	public void setTCP_Port(int tCP_Port) {
		TCP_Port = tCP_Port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public int getUDP_Port() {
		return UDP_Port;
	}

	public void setUDP_Port(int uDP_Port) {
		UDP_Port = uDP_Port;
	}
	
	
}
