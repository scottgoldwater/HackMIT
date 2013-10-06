package com.example.proto.pebblego;

public class Step {
	public long latStart; 
	public long logStart;
	public long latEnd; 
	public long logEnd;
	public String descr; 
	public int duration; 
	
	
	public long getLatStart() {
		return latStart;
	}
	public void setLatStart(long latStart) {
		this.latStart = latStart;
	}
	public Step(long latStart, long logStart, long latEnd, long logEnd,
			String descr, int duration) {
		super();
		this.latStart = latStart;
		this.logStart = logStart;
		this.latEnd = latEnd;
		this.logEnd = logEnd;
		this.descr = descr;
		this.duration = duration;
	}
	
	public long getLogStart() {
		return logStart;
	}
	public void setLogStart(long logStart) {
		this.logStart = logStart;
	}
	public long getLatEnd() {
		return latEnd;
	}
	public void setLatEnd(long latEnd) {
		this.latEnd = latEnd;
	}
	public long getLogEnd() {
		return logEnd;
	}
	public void setLogEnd(long logEnd) {
		this.logEnd = logEnd;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
