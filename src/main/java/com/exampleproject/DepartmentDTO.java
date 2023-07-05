package com.exampleproject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data

public class DepartmentDTO {

	@ApiModelProperty(dataType = "String",  allowableValues = "Mechanical,Civil,IT,ElectronicsCommunication,Electrical")
	private String department;
	
	@ApiModelProperty(dataType = "String")
	private String name;
}
