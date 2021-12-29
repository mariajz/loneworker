package com.incture.loneworker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name="EMPLOYEE_ACTIVITY")

public class EmployeeActivity {

	@Id
	@Column(name="emp_id")
	@JsonProperty(value = "emp_id")
	private long id;
	
	@Column(name="checkin_time")
	@JsonProperty(value = "checkin_time")
	private String checkInTime;
	
	@Column(name="checkout_time")
	@JsonProperty(value = "checkout_time")
	private String checkOutTime;
	
	@Column(name="last_location")
	@JsonProperty(value = "lastLocation")
	private String lastLocation;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public String getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}

	public EmployeeActivity(long id, String checkInTime, String checkOutTime, String lastLocation) {
		super();
		this.id = id;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.lastLocation = lastLocation;
	}

	public EmployeeActivity() {
		
	}
	
	
}
