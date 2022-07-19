package com.technoelevate.mongo.service;

import java.util.List;

import com.technoelevate.mongo.dto.DepartmentDto;

/**
 * @author Sahid
 *
 */
public interface DepartmentService {

	/**
	 * @param departmentDto
	 * @return
	 */
	public DepartmentDto create(DepartmentDto departmentDto);
	
	/**
	 * @param departmentDto
	 * @return
	 */
	public DepartmentDto update(DepartmentDto departmentDto);
	
	/**
	 * @param deptId
	 * @return
	 */
	public DepartmentDto delete(Long deptId) ;
	
	/**
	 * @param deptId
	 * @return
	 */
	public DepartmentDto getDepartment(Long deptId);
	
	/**
	 * @return
	 */
	public List<DepartmentDto> getAllDepartment();

}
