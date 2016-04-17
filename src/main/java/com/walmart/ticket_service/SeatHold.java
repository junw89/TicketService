package com.walmart.ticket_service;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SeatHold {
	private int id;
	private int numSeats;
	private int level;
	private String customerEmail;
	private long createdOn;
	
	public SeatHold() {}
	
	public SeatHold(int id, int numSeats, int level, String customerEmail) {
		this.id = id;
		this.numSeats = numSeats;
		this.level = level;
		this.customerEmail = customerEmail;
		this.createdOn = System.currentTimeMillis();
	}
	
	public long getCreatedOn() { return createdOn; }
	
	public int getId() { return id; }
	
	public int getNumSeats() { return numSeats; }
	
	public int getLevel() { return level; }
	
	public String getCustomerEmail() { return customerEmail; }
}
