package com.exampleproject.service;


import com.exampleproject.DepartmentDTO;
import com.exampleproject.DepartmentDTOFilter;
import com.exampleproject.EmployeeDetailsDTO;
import com.exampleproject.ResultEntity;

public interface EmployeeService {

	ResultEntity saveEmp(EmployeeDetailsDTO dto);

	ResultEntity getEmpById(String id);

	ResultEntity getAllEmployee(DepartmentDTO departmentDTO);

	ResultEntity updateEmployee(EmployeeDetailsDTO dto, String id);

	ResultEntity deleteEmployee(String id);





}
