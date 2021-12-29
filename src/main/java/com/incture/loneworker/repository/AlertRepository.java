package com.incture.loneworker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.incture.loneworker.model.EmployeeAlert;


public interface AlertRepository extends JpaRepository<EmployeeAlert, Long> {


	List<EmployeeAlert> findByOrderByIdDesc();

	@Query(value = "SELECT COUNT(*) FROM EmployeeAlert E WHERE E.resolvedDate IS NOT NULL")
	long alertResolved();
	
	@Query(value = "SELECT COUNT(*) FROM EmployeeAlert E WHERE E.resolvedDate IS NULL")
	long alertNotResolved();
	
	//dept-wise 
	
	@Query(value = "SELECT COUNT(*) FROM EmployeeAlert E, Employee D  WHERE D.dept LIKE ?1  AND D.id = E.empId")
	long deptTotalAlerts(String dept);
		
	@Query(value = "SELECT COUNT(*) FROM EmployeeAlert E, Employee D  WHERE E.resolvedDate IS NOT NULL AND D.dept LIKE ?1  AND D.id = E.empId")
	long deptAlertResolved(String dept);
	
	@Query(value = "SELECT COUNT(*) FROM EmployeeAlert E, Employee D  WHERE E.resolvedDate IS NULL AND D.dept LIKE ?1  AND D.id = E.empId")
	long deptAlertNotResolved(String dept);
	

}
