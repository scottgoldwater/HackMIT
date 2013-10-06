package com.example.proto.pebblego;

import org.jsoup.Jsoup;

public class Step {
	public float latStart; 
	public float logStart;
	public float latEnd; 
	public float logEnd;
	public String descr; 
	public int duration; 
	
	
	public float getLatStart() {
		return latStart;
	}
	public void setLatStart(long latStart) {
		this.latStart = latStart;
	}
	public Step(float latStart, float logStart, float latEnd, float logEnd,
			String descr, int duration) {
		super();
		this.latStart = latStart;
		this.logStart = logStart;
		this.latEnd = latEnd;
		this.logEnd = logEnd;
		this.descr = descr.replaceAll("\\<.*?>","");
		this.duration = duration;
	}
	
	public float getLogStart() {
		return logStart;
	}
	public void setLogStart(long logStart) {
		this.logStart = logStart;
	}
	public float getLatEnd() {
		return latEnd;
	}
	public void setLatEnd(long latEnd) {
		this.latEnd = latEnd;
	}
	public float getLogEnd() {
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
