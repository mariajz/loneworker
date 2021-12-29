package com.incture.loneworker.Service;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.incture.loneworker.dto.GraphDTO;
import com.incture.loneworker.model.Employee;
import com.incture.loneworker.model.EmployeeActivity;
import com.incture.loneworker.model.EmployeeAlert;

public interface EmployeeService {
	
	Employee saveEmployee(Employee employee);
	
	List<Employee> getAllEmployees();
	
	Employee getEmployeeById(long id);
	
	Employee updateEmployee(Employee employee, long id);
	
	void deleteEmployee(long id);

	Employee loginEmployee(Employee employee);
	
	void logout(Employee employee);

	EmployeeActivity updateLocation(EmployeeActivity employee, long employeeId);
	
	List<EmployeeActivity> getAllActivity();

	void checkInEmployee(long employeeId);

	void checkOutEmployee(long employeeId);

	EmployeeActivity getActivityById(long employeeId);

	void sendAlert(EmployeeAlert employeeAlert);

	List<EmployeeAlert> getAllAlerts();

	void deleteAlert(long alertId);
	
	long uploadImage(MultipartFile multipartImage, long employeeId) throws Exception;
	
	ByteArrayResource downloadImage(@PathVariable Long imageId);

	GraphDTO stats();

	//String sendEmail(long alertId, long empId, String time, String location, String description);

	String sendEmail(EmployeeAlert employeeAlert, Employee employee);

	//String sendEmail(EmployeeAlert employeeAlert, Optional<Employee> existingEmployee);
	
}
