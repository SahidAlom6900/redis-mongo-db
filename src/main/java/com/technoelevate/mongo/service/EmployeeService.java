package com.technoelevate.mongo.service;

import java.util.List;

import com.technoelevate.mongo.dto.EmployeeDto;

/**
 * @author Sahid
 *
 */
public interface EmployeeService {

	/**
	 * @param employeeDto
	 * @return
	 */
	public EmployeeDto create(EmployeeDto employeeDto) ;
	
	/**
	 * @param employeeDto
	 * @return
	 */
	public EmployeeDto update(EmployeeDto employeeDto) ;

	/**
	 * @param empId
	 * @return
	 */
	public EmployeeDto getEmployee(Long empId);

	/**
	 * @param empId
	 * @return
	 */
	public boolean delete(Long empId) ;

	/**
	 * @return
	 */
	public List<EmployeeDto> getAllEmployee() ;

}
