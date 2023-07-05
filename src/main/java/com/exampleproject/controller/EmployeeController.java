package com.exampleproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exampleproject.DepartmentDTO;
import com.exampleproject.DepartmentDTOFilter;
import com.exampleproject.EmployeeDetailsDTO;
import com.exampleproject.ResultEntity;
import com.exampleproject.service.EmployeeService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/employee")
@Api(tags = "Employee Controller",description = "This api is related to Employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    
    
   
    @PostMapping("/save")
    public ResponseEntity<ResultEntity> saveEmployee(EmployeeDetailsDTO dto){
    	return new ResponseEntity<>(employeeService.saveEmp(dto),HttpStatus.OK);
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<ResultEntity> getEmployeeDetailsOnId(@PathVariable String id){
    	return new ResponseEntity<>(employeeService.getEmpById(id),HttpStatus.OK);
    }
    
    @GetMapping("/getAll/department")
    public ResponseEntity<ResultEntity> getAllEmployee(DepartmentDTO departmentDTO){
    	return new ResponseEntity<>(employeeService.getAllEmployee(departmentDTO),HttpStatus.OK);
    }
    
    
    @PutMapping("/update/{id}")
    public ResponseEntity<ResultEntity> updateEmployeeId(EmployeeDetailsDTO dto,@PathVariable String id){
    	return new ResponseEntity<>(employeeService.updateEmployee(dto,id),HttpStatus.OK);
    }
    
    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResultEntity> deleteEmployeeById(@PathVariable String id){
    	return new ResponseEntity<>(employeeService.deleteEmployee(id),HttpStatus.OK);
    }
    
}
