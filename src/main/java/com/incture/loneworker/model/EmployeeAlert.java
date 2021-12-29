package com.incture.loneworker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Entity
@Table(name="EMPLOYEE_ALERTS")

@SequenceGenerator(name="alert_seq", initialValue=100, allocationSize = 1)

public class EmployeeAlert {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="alert_seq")
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name="emp_id", nullable=false)
	@JsonProperty(value = "empId" )
	private long empId;
	
	@Column(name="alert_time" , nullable=false)
	@JsonProperty(value = "alertTime")
	private String alertTime;
	
	@Column(name="alert_location" , nullable=false)
	@JsonProperty(value = "alertLocation")
	private String alertLocation;
	
	@Column(name="alert_description")
	@JsonProperty(value = "alertDescription")
	private String alertDescription;

	@Column(name="resolved_date")
	@JsonProperty(value = "resolvedDate")
	private String resolvedDate;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}

	public String getAlertLocation() {
		return alertLocation;
	}

	public void setAlertLocation(String alertLocation) {
		this.alertLocation = alertLocation;
	}

	public String getAlertDescription() {
		return alertDescription;
	}

	public void setAlertDescription(String alertDescription) {
		this.alertDescription = alertDescription;
	}

	public String getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(String resolvedDate) {
		this.resolvedDate = resolvedDate;
	}
	
	

	public EmployeeAlert(long id, long empId, String alertTime, String alertLocation, String alertDescription,
			String resolvedDate) {
		super();
		this.id = id;
		this.empId = empId;
		this.alertTime = alertTime;
		this.alertLocation = alertLocation;
		this.alertDescription = alertDescription;
		this.resolvedDate = resolvedDate;
	}

	public EmployeeAlert() {
		
	}

	
	
}
