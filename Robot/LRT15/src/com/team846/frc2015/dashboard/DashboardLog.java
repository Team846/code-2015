package com.team846.frc2015.dashboard;

public abstract class DashboardLog<T> {
	private String id;
	private T value;
	
	public DashboardLog(String id, T value) {
		this.id = id;
		this.value = value;
	}
	
	public String getID() {
		return id;
	};
	
	public T getValue() {
		return value;
	};
	
	abstract String valueJSON();
	
	public String json() {
		return "{ \"type\": \"" + getID() + "\", \"value\": " + valueJSON() + " }";
	};
}