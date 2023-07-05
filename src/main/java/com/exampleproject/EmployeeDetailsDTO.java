package com.exampleproject;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailsDTO {
	
	@ApiModelProperty(hidden = true)
	private String id;
	private String empName;
	@ApiModelProperty(hidden = true)
	private String empId;
	@ApiModelProperty(dataType = "String",  allowableValues = "Mechanical,Civil,IT,ElectronicsCommunication,Electrical")
	private String department;
	@ApiModelProperty(dataType = "String",  allowableValues = "Bangalore,Chennai,Hyderabed,Mumbai,Delhi,Trivendrum,Noida,Others")
	private String address;
	@ApiModelProperty(dataType = "String",  allowableValues = "TCS,ZKTECO,HP,DELL,INFOSYS,ZOHO,ACCENTURE,Other")
	private String company;
	@ApiModelProperty(dataType = "String",  allowableValues = "male,female,other")
	private String gender;

}
