package com.exampleproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exampleproject.dataobject.EmployeeDetailsDO;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetailsDO, String> {

	List<EmployeeDetailsDO> findByParentId(String parentId);

}
