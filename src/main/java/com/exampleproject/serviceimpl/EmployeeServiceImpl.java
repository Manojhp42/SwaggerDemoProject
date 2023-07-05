package com.exampleproject.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.exampleproject.DepartmentDTO;
import com.exampleproject.DepartmentDTOFilter;
import com.exampleproject.EmployeeConstants;
import com.exampleproject.EmployeeDTO;
import com.exampleproject.EmployeeDetailsDTO;
import com.exampleproject.ResultEntity;
import com.exampleproject.WebClientUtil;
import com.exampleproject.dataobject.EmployeeDO;
import com.exampleproject.dataobject.EmployeeDetailsDO;
import com.exampleproject.repository.EmployeeDetailsRepository;
import com.exampleproject.repository.EmployeeRepository;
import com.exampleproject.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository repo;

	@Autowired
	EmployeeDetailsRepository repodetails;

	@Autowired
	WebClientUtil web;

	@Value("${local.base-url}")
	public String webUrl;

	@Override
	public ResultEntity saveEmp(EmployeeDetailsDTO detailsDTO) {
		Map<String, Object> map = new LinkedHashMap<>();
		EmployeeDetailsDTO detailsDTO1 = null;
		if (detailsDTO.getDepartment().equalsIgnoreCase(EmployeeConstants.MECHANICAL)) {
			map.put(EmployeeConstants.MECHANICAL, detailsDTO);
			detailsDTO1 = (EmployeeDetailsDTO) map.get(EmployeeConstants.MECHANICAL);

		} else if (detailsDTO.getDepartment().equalsIgnoreCase(EmployeeConstants.CIVIL)) {
			map.put(EmployeeConstants.CIVIL, detailsDTO);
			detailsDTO1 = (EmployeeDetailsDTO) map.get(EmployeeConstants.CIVIL);

		} else if (detailsDTO.getDepartment().equalsIgnoreCase(EmployeeConstants.IT)) {
			map.put(EmployeeConstants.IT, detailsDTO);
			detailsDTO1 = (EmployeeDetailsDTO) map.get(EmployeeConstants.IT);

		} else if (detailsDTO.getDepartment().equalsIgnoreCase(EmployeeConstants.ELECTRONICS_COMMUNICATION)) {
			map.put(EmployeeConstants.ELECTRONICS_COMMUNICATION, detailsDTO);
			detailsDTO1 = (EmployeeDetailsDTO) map.get(EmployeeConstants.ELECTRONICS_COMMUNICATION);

		} else if (detailsDTO.getDepartment().equalsIgnoreCase(EmployeeConstants.ELECTRICAL)) {
			map.put(EmployeeConstants.ELECTRICAL, detailsDTO);
			detailsDTO1 = (EmployeeDetailsDTO) map.get(EmployeeConstants.ELECTRICAL);
		}

		return saveEmployeeDetails(detailsDTO1);

	}

	public ResultEntity saveEmployeeDetails(EmployeeDetailsDTO detailsDTOs) {
		Integer value = repo.getMaxId(detailsDTOs.getDepartment());
		if (value == null) {
			value = 0;
		}
		var empDo = new EmployeeDO();
		empDo.setEmpName(detailsDTOs.getEmpName());
		empDo.setDepartment(detailsDTOs.getDepartment());
		empDo.setEmpId(value + 1);
		empDo = repo.save(empDo);
		saveEmpDetails(detailsDTOs, empDo);
		// webclient
		String url = webUrl + "/" + "save";
		EmployeeDTO dtos = new EmployeeDTO();
		dtos.setEmpName(detailsDTOs.getEmpName());
		dtos.setDepartment(detailsDTOs.getDepartment());
		dtos.setEmpId(empDo.getId());
		ResultEntity entity = web.prepareClientForCreateEmpl(dtos, url).block();
		if (entity != null && (entity.getCode().equalsIgnoreCase(EmployeeConstants.SUCCESS))) {
			return new ResultEntity(EmployeeConstants.SUCCESS, EmployeeConstants.SUCCESSFULL, empDo);
		} else {
			EmployeeDO do1 = repo.findById(empDo.getId()).get();
			List<EmployeeDetailsDO> detailsDo = repodetails.findByParentId(do1.getId());
			repo.delete(do1);
			repodetails.deleteAll(detailsDo);
			return entity;
		}
	}

	public void saveEmpDetails(EmployeeDetailsDTO detailsDTOs, EmployeeDO empDo) {
		List<EmployeeDetailsDO> detailsDTOss = new ArrayList<>();
		Map<String, String> mapParams = new HashMap<>();
		mapParams.put(EmployeeConstants.EMP_NAME, empDo.getEmpName());
		mapParams.put(EmployeeConstants.DEPARTMENT, empDo.getDepartment());
		mapParams.put(EmployeeConstants.ADDRESS, detailsDTOs.getAddress());
		mapParams.put("id", Integer.toString(empDo.getEmpId()));
		mapParams.put(EmployeeConstants.COMPANY, detailsDTOs.getCompany());
		List<String> gender = Arrays.asList("male", "female");
		if (gender.contains(detailsDTOs.getGender())) {
			mapParams.put(EmployeeConstants.GENDER, detailsDTOs.getGender());
		} else {
			mapParams.put(EmployeeConstants.GENDER, "other");
		}
		mapParams.forEach((key, value1) -> {
			var detailsDo = new EmployeeDetailsDO();
			detailsDo.setOptionName(key);
			detailsDo.setOptionValue(value1);
			detailsDo.setParentId(empDo.getId());
			detailsDTOss.add(detailsDo);
		});

		repodetails.saveAll(detailsDTOss);
	}

	@Override
	public ResultEntity getEmpById(String id) {
		EmployeeDO do1 = repo.findById(id).get();
		if (do1 != null) {
			List<EmployeeDetailsDO> detailsDo = repodetails.findByParentId(do1.getId());
			if (!detailsDo.isEmpty()) {
				List<EmployeeDetailsDTO> setlist = set(detailsDo);
				return new ResultEntity(EmployeeConstants.SUCCESS, EmployeeConstants.SUCCESSFULL, setlist);
			} else {
				return new ResultEntity(EmployeeConstants.ERROR, "Employee details does not exists", null);
			}

		} else {
			return new ResultEntity(EmployeeConstants.ERROR, "Employee does not exists", null);
		}
	}

	public List<EmployeeDetailsDTO> set(List<EmployeeDetailsDO> detailsDo) {
		List<EmployeeDetailsDTO> detailsDTOs = new ArrayList<>();
		Map<String, HashMap<String, String>> map = new HashMap<>();
		detailsDo.stream().forEach(details -> {
			HashMap<String, String> hashMap = map.get(details.getParentId());
			if (hashMap == null) {
				hashMap = new HashMap<>();
			}
			hashMap.put(details.getOptionName(), details.getOptionValue());
			map.put(details.getParentId(), hashMap);
		});
		map.forEach((empid, empObj) -> {

			EmployeeDetailsDTO detailsDTO = setMethod(empid, empObj);
			detailsDTOs.add(detailsDTO);
		});
		return detailsDTOs;

	}

	public EmployeeDetailsDTO setMethod(String id, Map<String, String> hashMap1) {
		EmployeeDetailsDTO detailsDTO = new EmployeeDetailsDTO();
		detailsDTO.setEmpName(hashMap1.get("empName"));
		detailsDTO.setAddress(hashMap1.get("address"));
		detailsDTO.setCompany(hashMap1.get("company"));
		detailsDTO.setDepartment(hashMap1.get("department"));
		detailsDTO.setId(id);
		detailsDTO.setEmpId(hashMap1.get("id"));
		detailsDTO.setGender(hashMap1.get("gender"));
		return detailsDTO;
	}

	@Override
	public ResultEntity getAllEmployee(DepartmentDTO departmentDTO) {
		if ((departmentDTO.getDepartment() != null) && (departmentDTO.getName() != null)) {
			List<EmployeeDO> list = repo.findByDepartmentAndEmpName(departmentDTO.getDepartment(),
					departmentDTO.getName());
			if (!list.isEmpty()) {
				List<EmployeeDetailsDTO> detailsDTOs = new ArrayList<>();
				list.stream().forEach(lists -> {
					List<EmployeeDetailsDO> detailsDo = repodetails.findByParentId(lists.getId());
					if (!detailsDo.isEmpty()) {
						List<EmployeeDetailsDTO> detailsDTOs1 = set(detailsDo);
						detailsDTOs.addAll(detailsDTOs1);
					}
				});
				if (!detailsDTOs.isEmpty()) {
					return new ResultEntity(EmployeeConstants.SUCCESS, EmployeeConstants.SUCCESSFULL, detailsDTOs);
				} else {
					return new ResultEntity(EmployeeConstants.ERROR, EmployeeConstants.EMPLOYEE_LIST_DOES_EXISTS, null);
				}
			} else {
				return new ResultEntity(EmployeeConstants.ERROR, EmployeeConstants.EMPLOYEE_LIST_DOES_EXISTS, null);
			}
		} else if (departmentDTO.getDepartment() != null) {
			List<EmployeeDO> list = repo.findByDepartment(departmentDTO.getDepartment());
			if (!list.isEmpty()) {
				List<EmployeeDetailsDTO> detailsDTOs = new ArrayList<>();
				list.stream().forEach(lists -> {
					List<EmployeeDetailsDO> detailsDo = repodetails.findByParentId(lists.getId());
					if (!detailsDo.isEmpty()) {
						List<EmployeeDetailsDTO> detailsDTOs1 = set(detailsDo);
						detailsDTOs.addAll(detailsDTOs1);
					}
				});
				if (!detailsDTOs.isEmpty()) {
					return new ResultEntity(EmployeeConstants.SUCCESS, EmployeeConstants.SUCCESSFULL, detailsDTOs);
				} else {
					return new ResultEntity(EmployeeConstants.ERROR, EmployeeConstants.EMPLOYEE_LIST_DOES_EXISTS, null);
				}
			} else {
				return new ResultEntity(EmployeeConstants.ERROR, EmployeeConstants.EMPLOYEE_LIST_DOES_EXISTS, null);
			}
		}

		else if (departmentDTO.getName() != null) {
			List<EmployeeDO> list = repo.findByEmpName(departmentDTO.getName());
			if (!list.isEmpty()) {
				List<EmployeeDetailsDTO> detailsDTOs = new ArrayList<>();
				list.stream().forEach(lists -> {
					List<EmployeeDetailsDO> detailsDo = repodetails.findByParentId(lists.getId());
					if (!detailsDo.isEmpty()) {
						List<EmployeeDetailsDTO> detailsDTOs1 = set(detailsDo);
						detailsDTOs.addAll(detailsDTOs1);
					}
				});
				if (!detailsDTOs.isEmpty()) {
					return new ResultEntity(EmployeeConstants.SUCCESS, "Successfull", detailsDTOs);
				} else {
					return new ResultEntity(EmployeeConstants.ERROR, EmployeeConstants.EMPLOYEE_LIST_DOES_EXISTS, null);
				}
			} else {
				return new ResultEntity(EmployeeConstants.ERROR, EmployeeConstants.EMPLOYEE_LIST_DOES_EXISTS, null);
			}
		}
		return new ResultEntity(EmployeeConstants.ERROR, "Fields are empty", null);
	}

	@Override
	public ResultEntity updateEmployee(EmployeeDetailsDTO dto, String id) {
		if (repo.existsById(id)) {
			String url = webUrl + "/" + "update" + "/" + id;
			EmployeeDTO dto2 = new EmployeeDTO();
			dto2.setEmpName(dto.getEmpName());
			dto2.setDepartment(dto.getDepartment());
			ResultEntity entity = web.prepareClientForUpdateEmpl(dto2, url).block();
			if (entity != null && (entity.getCode().equalsIgnoreCase(EmployeeConstants.SUCCESS))) {
				EmployeeDO employeeDO = repo.findById(id).get();
				List<EmployeeDetailsDO> do1 = repodetails.findByParentId(employeeDO.getId());
				employeeDO.setDepartment(dto.getDepartment());
				employeeDO.setEmpName(dto.getEmpName());
				repo.save(employeeDO);
				List<EmployeeDetailsDO> do2 = new ArrayList<>();
				if (!do1.isEmpty()) {
					do1.forEach(listDetails -> {
						if (listDetails.getOptionName().equals("department")) {
							listDetails.setOptionValue(dto.getDepartment());
						}
						if (listDetails.getOptionName().equals("address")) {
							listDetails.setOptionValue(dto.getAddress());
						}
						if (listDetails.getOptionName().equals("company")) {
							listDetails.setOptionValue(dto.getCompany());
						}
						if (listDetails.getOptionName().equals("gender")) {
							listDetails.setOptionValue(dto.getGender());
						}
						if (listDetails.getOptionName().equals("empName")) {
							listDetails.setOptionValue(dto.getEmpName());
						}
						do2.add(listDetails);
					});
					repodetails.saveAll(do2);
				} else {
					return new ResultEntity(EmployeeConstants.ERROR, EmployeeConstants.EMPLOYEE_LIST_DOES_EXISTS, null);
				}
				return new ResultEntity(EmployeeConstants.SUCCESS, "successfully updated", set(do2));
			} else {
				return entity;
			}
		}
		return new ResultEntity(EmployeeConstants.ERROR, "employee does not exists", null);
	}

	@Override
	public ResultEntity deleteEmployee(String id) {
		String url = webUrl + "/" + "delete" + "/" + id;
		ResultEntity entity = web.prepareClientForDeleteEmpl(url).block();
		if (entity!=null  && entity.getCode().equalsIgnoreCase(EmployeeConstants.SUCCESS)) {
			EmployeeDO employeeDO = repo.findById(id).get();
			if (employeeDO != null) {
				List<EmployeeDetailsDO> do1 = repodetails.findByParentId(employeeDO.getId());
				if (!do1.isEmpty()) {
					repo.delete(employeeDO);
					repodetails.deleteAll(do1);
					return new ResultEntity(EmployeeConstants.SUCCESS, "successfully deleted", null);
				} else {
					repo.delete(employeeDO);
					return new ResultEntity(EmployeeConstants.SUCCESS, "successfully deleted", null);
				}
			} else {
				return new ResultEntity(EmployeeConstants.ERROR, "employee does not exists", null);
			}
		}
		return new ResultEntity(EmployeeConstants.ERROR, "employee does not exists", null);
	}



}
