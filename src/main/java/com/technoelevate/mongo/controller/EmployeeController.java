package com.technoelevate.mongo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technoelevate.mongo.dto.EmployeeDto;
import com.technoelevate.mongo.response.EmployeeResponse;
import com.technoelevate.mongo.service.EmployeeService;

import lombok.AllArgsConstructor;

/**
 * @author Sahid
 *
 */
@RestController
@RequestMapping(value = "/api/v1")
@AllArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;

	/**
	 * @param employeeDto
	 * @return
	 */
	@PostMapping("employee")
	public ResponseEntity<EmployeeResponse> createEmployee(@RequestBody EmployeeDto employeeDto) {
		return ResponseEntity.ok().body(
				EmployeeResponse.builder().error(false).message("").data(employeeService.create(employeeDto)).build());

	}

	/**
	 * @param empId
	 * @return
	 */
	@GetMapping("employee/{empId}")
	public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable(name = "empId") Long empId) {
		return ResponseEntity.ok().body(
				EmployeeResponse.builder().error(false).message("").data(employeeService.getEmployee(empId)).build());

	}

	/**
	 * @param employeeDto
	 * @return
	 */
	@PutMapping("employee")
	public ResponseEntity<EmployeeResponse> updateEmployee(
			@RequestBody EmployeeDto employeeDto) {
		return ResponseEntity.ok().body(EmployeeResponse.builder().error(false).message("")
				.data(employeeService.update( employeeDto)).build());
	}

	/**
	 * @param empId
	 * @return
	 */
	@DeleteMapping(value = "/employee/{empId}")
	public ResponseEntity<EmployeeResponse> deleteEmployee(@PathVariable(name = "empId") Long empId) {
		boolean delete = employeeService.delete(empId);
		return ResponseEntity.ok()
				.body(EmployeeResponse.builder().error(!delete).message("").data(delete).build());
	}

	/**
	 * @return
	 */
	@GetMapping(value = "/employees")
	public ResponseEntity<EmployeeResponse> getAllEmployee() {
		return ResponseEntity.ok().body(
				EmployeeResponse.builder().error(false).message("").data(employeeService.getAllEmployee()).build());
	}

}
