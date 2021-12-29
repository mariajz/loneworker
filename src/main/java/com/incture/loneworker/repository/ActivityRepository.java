package com.incture.loneworker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incture.loneworker.model.EmployeeActivity;

public interface ActivityRepository extends JpaRepository<EmployeeActivity, Long> {

	List<EmployeeActivity> findByOrderById();

	@Query(value = "SELECT COUNT(*) FROM EmployeeActivity E WHERE E.checkOutTime IS NULL AND E.checkInTime IS NOT NULL")
	long checkinCount();

	@Query(value = "SELECT COUNT(*) FROM EmployeeActivity E, Employee D  WHERE E.checkOutTime IS NULL AND E.checkInTime IS NOT NULL AND D.dept LIKE ?1  AND D.id = E.id ")
	long deptCheckinCount(String dept);
	

}
