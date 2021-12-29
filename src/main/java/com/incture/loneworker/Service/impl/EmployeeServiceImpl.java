package com.incture.loneworker.Service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;


import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.incture.loneworker.Service.EmployeeService;
import com.incture.loneworker.dto.GraphDTO;
import com.incture.loneworker.exception.ExistingMobileNumberException;
import com.incture.loneworker.exception.ExistingUsernameException;
import com.incture.loneworker.exception.IncorrectPasswordException;
import com.incture.loneworker.exception.ResourceNotFoundException;
import com.incture.loneworker.model.Employee;
import com.incture.loneworker.model.EmployeeActivity;
import com.incture.loneworker.model.EmployeeAlert;
import com.incture.loneworker.repository.ActivityRepository;
import com.incture.loneworker.repository.AlertRepository;
import com.incture.loneworker.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	private EmployeeRepository employeeRepository;
	private ActivityRepository activityRepository;
	private AlertRepository alertRepository;
	private JavaMailSender mailSender;
	
	public EmployeeServiceImpl(EmployeeRepository employeeRepository, ActivityRepository activityRepository,
			AlertRepository alertRepository, JavaMailSender mailSender) {
		super();
		this.employeeRepository = employeeRepository;
		this.activityRepository = activityRepository;
		this.alertRepository = alertRepository;
		this.mailSender = mailSender;
	}



	@Override
	public Employee saveEmployee(Employee employee) {
		
		String email = employee.getUsername();
		String mob = employee.getMobileno();
		Optional<Employee> emp_username = employeeRepository.findByUsername(email);
		Optional<Employee> emp_mob = employeeRepository.findByMobileno(mob);
		if(emp_username.isPresent()){

			throw new ExistingUsernameException(email);
		}
		else if (emp_mob.isPresent()) {
			throw new ExistingMobileNumberException(mob);
		}
		else {
			employee.setEmpStatus(true);
			employeeRepository.save(employee);
			EmployeeActivity obj = new EmployeeActivity();
			obj.setId(employee.getId());
			activityRepository.save(obj);
			return employee;
			
		}
		
	}



	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepository.findByOrderByIdDesc();
	}

	@Override
	public Employee getEmployeeById(long id) {
		Optional<Employee> employee = employeeRepository.findById(id);
		if(employee.isPresent()){
			return employee.get();
		}else {
			throw new ResourceNotFoundException("Employee", "id", id);
		}
		
	}

	@Override
	public Employee updateEmployee(Employee employee, long id) {
		
		//check if employee exists
		Employee existingEmployee = employeeRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Employee", "id", id));
		existingEmployee.setFirstname(employee.getFirstname());
		existingEmployee.setLastname(employee.getLastname());
		existingEmployee.setPassword(employee.getPassword());
		existingEmployee.setMobileno(employee.getMobileno());
		
		//save to db
		employeeRepository.save(existingEmployee);
		
		return existingEmployee;
	}

	@Override
	public void deleteEmployee(long id) {
		Employee existingEmployee = employeeRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Employee", "id", id));
		//employeeRepository.deleteById(id);
		existingEmployee.setEmpStatus(false);
		employeeRepository.save(existingEmployee);
		activityRepository.deleteById(id);
		
	}
	


	@Override
	public Employee loginEmployee(Employee employee) {
		
		String username = employee.getUsername();
		String password = employee.getPassword();
		Optional<Employee> existingEmployee = employeeRepository.findByUsername(username);
		  if(existingEmployee.isPresent()){
			  Employee loggedIn = existingEmployee.get();
			  String pw = loggedIn.getPassword();
			  if(pw.equals(password)){
				  loggedIn.setLoggedin(true);
	              employeeRepository.save(loggedIn);
			      return loggedIn;
			  }
			   
			  else {
				  throw new IncorrectPasswordException(username);
				}
		  }else {
			  throw new ResourceNotFoundException("Employee", "username", username);
		  }

	}

	@Override
	public void logout(Employee employee) {
		String username = employee.getUsername();
		Optional<Employee> existingEmployee = employeeRepository.findByUsername(username);
		  if(existingEmployee.isPresent()){
			  Employee loggedOut = existingEmployee.get();
			  loggedOut.setLoggedin(false);
			  employeeRepository.save(loggedOut);			  			  
		  }

	}



	@Override
	public EmployeeActivity updateLocation(EmployeeActivity employee, long employeeId) {
		EmployeeActivity existingEmployee = activityRepository.findById(employeeId).orElseThrow( () -> new ResourceNotFoundException("Employee", "id", employeeId));
		existingEmployee.setLastLocation(employee.getLastLocation());
		activityRepository.save(existingEmployee);
		return existingEmployee;
	}


	@Override
	public List<EmployeeActivity> getAllActivity() {
		return activityRepository.findByOrderById();
	}

	@Override
	public void checkInEmployee(long employeeId) {
		EmployeeActivity existingEmployee = activityRepository.findById(employeeId).orElseThrow( () -> new ResourceNotFoundException("Employee", "id", employeeId));
		Instant nowUtc = Instant.now();
        ZoneId asiaSingapore = ZoneId.of("Asia/Kolkata");

        ZonedDateTime nowAsiaSingapore = ZonedDateTime.ofInstant(nowUtc, asiaSingapore);
        System.out.println("now: " + nowAsiaSingapore );
        
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String formatDateTime = nowAsiaSingapore.format(format);
        existingEmployee.setCheckInTime(formatDateTime);
        existingEmployee.setCheckOutTime(null);
		activityRepository.save(existingEmployee);
		
	}

	@Override
	public void checkOutEmployee(long employeeId) {
		EmployeeActivity existingEmployee = activityRepository.findById(employeeId).orElseThrow( () -> new ResourceNotFoundException("Employee", "id", employeeId));
		Instant nowUtc = Instant.now();
        ZoneId asiaSingapore = ZoneId.of("Asia/Kolkata");
        ZonedDateTime nowAsiaSingapore = ZonedDateTime.ofInstant(nowUtc, asiaSingapore);
        System.out.println("now: " + nowAsiaSingapore );
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String formatDateTime = nowAsiaSingapore.format(format);
        existingEmployee.setCheckOutTime(formatDateTime);
		activityRepository.save(existingEmployee);	
	}


	@Override
	public EmployeeActivity getActivityById(long employeeId) {
		Optional<EmployeeActivity> employee = activityRepository.findById(employeeId);
		if(employee.isPresent()){
			return employee.get();
		}else {
			throw new ResourceNotFoundException("Employee", "id", employeeId);
		}
	}


	@Override
	public void sendAlert(EmployeeAlert employeeAlert) {
		Instant nowUtc = Instant.now();
        ZoneId asiaSingapore = ZoneId.of("Asia/Kolkata");
        ZonedDateTime nowAsiaSingapore = ZonedDateTime.ofInstant(nowUtc, asiaSingapore);
        System.out.println("now: " + nowAsiaSingapore );
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String formatDateTime = nowAsiaSingapore.format(format);
		employeeAlert.setAlertTime(formatDateTime);
        
        alertRepository.save(employeeAlert);
        
        Employee existingEmployee = employeeRepository.findById(employeeAlert.getEmpId()).orElseThrow( () -> new ResourceNotFoundException("Employee", "id", employeeAlert.getEmpId()));
		
        sendEmail(employeeAlert, existingEmployee);
    }
	
	@Override
	public String sendEmail(EmployeeAlert employeeAlert, Employee emp) {
		
		String from = "employeesthaan@gmail.com";
		String to = "admindanny98@protonmail.com";
		 
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject("Emergency Alert!");
		message.setText("Emergency Alert raised by "+ employeeAlert.getEmpId()+" "+emp.getFirstname()+" "+emp.getLastname()+
				"\nAlert Details: \nAlert ID : "+employeeAlert.getId()+"\nTime:"+employeeAlert.getAlertTime()
				+"\nLocation:"+employeeAlert.getAlertLocation()+"\nDescription:"+employeeAlert.getAlertDescription()+"\n \nContact Employee:"+emp.getMobileno()
		);
		 
		mailSender.send(message);
		return "Mail Sent to Admin";
	}


	@Override
	public List<EmployeeAlert> getAllAlerts() {
		return alertRepository.findByOrderByIdDesc();
	}


	@Override
	public void deleteAlert(long alertId) {

		EmployeeAlert existingAlert = alertRepository.findById(alertId).orElseThrow( () -> new ResourceNotFoundException("Alert", "id", alertId));

		Instant nowUtc = Instant.now();
        ZoneId asiaSingapore = ZoneId.of("Asia/Kolkata");
        ZonedDateTime nowAsiaSingapore = ZonedDateTime.ofInstant(nowUtc, asiaSingapore);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
        String formatDateTime = nowAsiaSingapore.format(format);
             
        existingAlert.setResolvedDate(formatDateTime);
		alertRepository.save(existingAlert);

	}

	@Override
	public long uploadImage(MultipartFile multipartImage, long employeeId) throws Exception {
		Employee existingEmployee = employeeRepository.findById(employeeId).orElseThrow( () -> new ResourceNotFoundException("Employee", "id", employeeId));
		existingEmployee.setImageName(multipartImage.getName());
		existingEmployee.setImageContent(multipartImage.getBytes());

        return employeeRepository.save(existingEmployee)
            .getId();
	}


	@Override
	public ByteArrayResource downloadImage(Long imageId) {
		byte[] image = employeeRepository.findById(imageId)
			      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
			      .getImageContent();

		return new ByteArrayResource(image);
	}


	
	@Override
	public GraphDTO stats() {

		GraphDTO obj=new GraphDTO();    
 
		long tot_emp = employeeRepository.empCount();
		obj.setTot_emp(tot_emp);
		
		long d1_tot_emp = employeeRepository.empCount("Petroleum Engineer");
		long d2_tot_emp = employeeRepository.empCount("Mining");
		long d3_tot_emp = employeeRepository.empCount("Drilling");
		long d4_tot_emp = employeeRepository.empCount("Mudlogger");
		obj.setTot_d1(d1_tot_emp);
		obj.setTot_d2(d2_tot_emp);
		obj.setTot_d3(d3_tot_emp);
		obj.setTot_d4(d4_tot_emp);
	
		long tot_alert_count = alertRepository.count();
		long tot_alert_pending_count = alertRepository.alertNotResolved();
		long tot_alert_resolved_count = alertRepository.alertResolved();
		obj.setTot_alert_count(tot_alert_count);
		obj.setTot_alert_pending_count(tot_alert_pending_count);
		obj.setTot_alert_resolved_count(tot_alert_resolved_count);
		
		long checkin_count = activityRepository.checkinCount();
		obj.setTot_checkin_count(checkin_count);
		
		long d1_dept_checkin_count = activityRepository.deptCheckinCount("Petroleum Engineer");
		long d2_dept_checkin_count = activityRepository.deptCheckinCount("Mining");
		long d3_dept_checkin_count = activityRepository.deptCheckinCount("Drilling");
		long d4_dept_checkin_count = activityRepository.deptCheckinCount("Mudlogger");
		obj.setD1_dept_checkin_count(d1_dept_checkin_count);
		obj.setD2_dept_checkin_count(d2_dept_checkin_count);
		obj.setD3_dept_checkin_count(d3_dept_checkin_count);
		obj.setD4_dept_checkin_count(d4_dept_checkin_count);

		//obj.setAlert_count(alert_count);    
		//obj.setAlert_pending_count(pet_checkin_count); 
		
		
		//dept1
		long d1_dept_alert_count = alertRepository.deptTotalAlerts("Petroleum Engineer");
		long d1_alert_pending_count = alertRepository.deptAlertNotResolved("Petroleum Engineer");
		long d1_alert_resolved_count = alertRepository.deptAlertResolved("Petroleum Engineer");
		obj.setD1_dept_alert_count(d1_dept_alert_count);
		obj.setD1_dept_alert_pending_count(d1_alert_pending_count);
		obj.setD1_dept_alert_resolved_count(d1_alert_resolved_count);
		
		//dept2
		long d2_dept_alert_count = alertRepository.deptTotalAlerts("Mining");
		long d2_alert_pending_count = alertRepository.deptAlertNotResolved("Mining");
		long d2_alert_resolved_count = alertRepository.deptAlertResolved("Mining");
		obj.setD2_dept_alert_count(d2_dept_alert_count);
		obj.setD2_dept_alert_pending_count(d2_alert_pending_count);
		obj.setD2_dept_alert_resolved_count(d2_alert_resolved_count);
		
		//dept3
		long d3_dept_alert_count = alertRepository.deptTotalAlerts("Drilling");
		long d3_alert_pending_count = alertRepository.deptAlertNotResolved("Drilling");
		long d3_alert_resolved_count = alertRepository.deptAlertResolved("Drilling");
		obj.setD3_dept_alert_count(d3_dept_alert_count);
		obj.setD3_dept_alert_pending_count(d3_alert_pending_count);
		obj.setD3_dept_alert_resolved_count(d3_alert_resolved_count);
		
		//dept4
		long d4_dept_alert_count = alertRepository.deptTotalAlerts("Mudlogger");
		long d4_alert_pending_count = alertRepository.deptAlertNotResolved("Mudlogger");
		long d4_alert_resolved_count = alertRepository.deptAlertResolved("Mudlogger");
		obj.setD4_dept_alert_count(d4_dept_alert_count);
		obj.setD4_dept_alert_pending_count(d4_alert_pending_count);
		obj.setD4_dept_alert_resolved_count(d4_alert_resolved_count);
		
		System.out.print(obj);		
		
		return obj ;
	}


	//@Autowired
    
	



	







}
	

