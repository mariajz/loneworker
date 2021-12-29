package com.incture.loneworker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incture.loneworker.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByUsername(String username);
	Optional<Employee> findByMobileno(String mob);
	List<Employee> findByOrderByIdDesc();
	
	@Query(value = "SELECT COUNT(*) FROM Employee E WHERE E.empStatus = true AND E.admin LIKE 'no' ")
	long empCount();
	
	@Query(value = "SELECT COUNT(*) FROM Employee E WHERE E.dept LIKE ?1 AND E.empStatus = true ")
	long empCount(String dept);



}
