package com.exampleproject;

import lombok.Data;

@Data
public class DepartmentDTOFilter {

	private Integer pageNumber;

	private Integer pageSize;

	private String id;

	private String empName;

	private String department;

	private Integer empId;

}
