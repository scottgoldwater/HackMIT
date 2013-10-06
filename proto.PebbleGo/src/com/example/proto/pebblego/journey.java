package com.example.proto.pebblego;

import java.util.ArrayList;

public class journey {
	
	
	@Override
	public String toString() {
		return "journey [distance=" + distance + ", startPoint=" + startPoint
				+ ", endPoint=" + endPoint + ", steps=" + steps + "]";
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public journey(String distance, String startPoint, String endPoint) {
		this.distance = distance;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.steps = new ArrayList<Step>();
	}

	public ArrayList<Step> getSteps() {
		return steps;
	}

	public void setSteps(ArrayList<Step> steps) {
		this.steps = steps;
	}

	public String distance;
	public String startPoint; 
	public String endPoint; 
	public ArrayList<Step> steps;
	
	public void addStep(Step a)
	{
		steps.add(a);
	}
	
	
}
