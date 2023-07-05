package com.exampleproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exampleproject.dataobject.EmployeeDO;

@Repository
public interface EmployeeRepository  extends JpaRepository<EmployeeDO,String>{
	
	@Query(value = "SELECT MAX(empId) from EmployeeDO d where d.department= ?1")
	Integer getMaxId(String  department);

	List<EmployeeDO> findByDepartment(String department);

	List<EmployeeDO> findByDepartmentAndEmpName(String department, String name);

	List<EmployeeDO> findByEmpName(String name);

}
