package com.incture.loneworker.Controller;


import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.loneworker.Service.EmployeeService;
import com.incture.loneworker.dto.GraphDTO;
import com.incture.loneworker.model.Employee;
import com.incture.loneworker.model.EmployeeActivity;
import com.incture.loneworker.model.EmployeeAlert;

@RestController
@RequestMapping("/")
public class EmployeeController {
	
	private EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}
	
	@PostMapping("register")
	public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee){
		return new ResponseEntity<Employee>(employeeService.saveEmployee(employee), HttpStatus.CREATED);
	}
	
	@GetMapping("users/all")
	public List<Employee> getAllEmployees(){
		return employeeService.getAllEmployees();
	}
	
	@GetMapping("users/{id}")
	public ResponseEntity<Employee> getElementById(@PathVariable("id") long employeeId){
		return new ResponseEntity<Employee>(employeeService.getEmployeeById(employeeId), HttpStatus.OK);
	}
	
	@PutMapping("users/edit/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Employee employee){
		
		return new ResponseEntity<Employee>(employeeService.updateEmployee(employee,employeeId), HttpStatus.OK);
	}
	
	@PutMapping("users/delete/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") long employeeId){
		employeeService.deleteEmployee(employeeId);
		return new ResponseEntity<String>("Employee Deleted", HttpStatus.OK);
	}
	

	@PostMapping("login")
	public ResponseEntity<Employee> loginEmployee(@RequestBody Employee employee){
		return new ResponseEntity<Employee>(employeeService.loginEmployee(employee),HttpStatus.OK);
	}
	
	@PostMapping("logout")
	public ResponseEntity<String> logoutEmployee(@RequestBody Employee employee){
		employeeService.logout(employee);
		return new ResponseEntity<String>("Successfully Logged Out!",HttpStatus.OK);
	}
	
	@PutMapping("updatelocation")
	public ResponseEntity<EmployeeActivity> updateLocation(@RequestParam long employeeId, @RequestBody EmployeeActivity employee){
		
		return new ResponseEntity<EmployeeActivity>(employeeService.updateLocation(employee,employeeId), HttpStatus.OK);
	}
	
	@GetMapping("activity/all")
	public List<EmployeeActivity> getAllActivity(){
		return employeeService.getAllActivity();
	}
	
	@GetMapping("activity/{id}")
	public ResponseEntity<EmployeeActivity> getActivityById(@PathVariable("id") long employeeId){
		return new ResponseEntity<EmployeeActivity>(employeeService.getActivityById(employeeId), HttpStatus.OK);
	}
	
	@PutMapping("checkin/{id}")
	public ResponseEntity<String> checkInEmployee(@PathVariable("id") long employeeId){
		employeeService.checkInEmployee(employeeId);
		return new ResponseEntity<String>("CheckIn successful!", HttpStatus.OK);
	}
	
	@PutMapping("checkout/{id}")
	public ResponseEntity<String> checkOutEmployee(@PathVariable("id") long employeeId){
		employeeService.checkOutEmployee(employeeId);
		return new ResponseEntity<String>("CheckOut successful!", HttpStatus.OK);
	}
	
	@PostMapping("alert")
	public ResponseEntity<String> sendAlert(@RequestBody EmployeeAlert employeeAlert){
		employeeService.sendAlert(employeeAlert);
		return new ResponseEntity<String>("Alert send to admin!", HttpStatus.CREATED);
	}
	
	@GetMapping("fetchalert/all")
	public List<EmployeeAlert> getAllAlerts(){
		return employeeService.getAllAlerts();
	}
	
	@PutMapping("deletealert/{id}")
	public ResponseEntity<String> deleteAlert(@PathVariable("id") long alertId){
		employeeService.deleteAlert(alertId);
		return new ResponseEntity<String>("Alert Resolved", HttpStatus.OK);
	}
	
    @PutMapping("uploadimage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile multipartImage,@RequestParam("id") long employeeId) throws Exception {
    	employeeService.uploadImage(multipartImage,employeeId);
    	return new ResponseEntity<String>("Image Uploaded!", HttpStatus.OK);
    }
    
    @GetMapping(value = "/getimage/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    ByteArrayResource downloadImage(@PathVariable Long imageId) {
        return employeeService.downloadImage(imageId);
    }
    
    @GetMapping("stats")
    GraphDTO stats(){
		return employeeService.stats();
    } 
    
}
