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
@Table(name = "EmployeeDetails")
public class EmployeeDetailsDO {


	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;
	
	@Column(name = "parent_Id")
	private String parentId;
	
	@Column(name = "option_name")
	private String optionName;

	@Column(name = "option_value")
	private String optionValue;
}
