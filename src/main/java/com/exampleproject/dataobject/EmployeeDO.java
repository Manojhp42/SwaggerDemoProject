package com.exampleproject.dataobject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "Employee")
public class EmployeeDO {
	
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;
	
	
	@Column(name = "employee_name")
	private String empName;
	
	
	@Column(name = "deparment")
	private String department;
	
	
	@Column(name = "employee_id")
	private Integer empId;

}
