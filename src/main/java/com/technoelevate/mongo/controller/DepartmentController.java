package com.technoelevate.mongo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.technoelevate.mongo.dto.DepartmentDto;
import com.technoelevate.mongo.response.DepartmentResponse;
import com.technoelevate.mongo.service.DepartmentService;

import lombok.AllArgsConstructor;

/**
 * @author Sahid
 *
 */
@RestController
@RequestMapping(value = "/api/v1")
@AllArgsConstructor
public class DepartmentController {

	private final DepartmentService departmentService;

	/**
	 * @param departmentDto
	 * @return
	 */
	@PostMapping(path = "department")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody DepartmentDto departmentDto) {
		return ResponseEntity.ok(DepartmentResponse.builder().error(false).message("")
				.data(departmentService.create(departmentDto)).build());
	}

	/**
	 * @param departmentDto
	 * @return
	 */
	@PutMapping(path = "department")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<DepartmentResponse> updateDepartment(@RequestBody DepartmentDto departmentDto) {
		return ResponseEntity.ok(DepartmentResponse.builder().error(false).message("")
				.data(departmentService.update(departmentDto)).build());
	}

	/**
	 * @param deptId
	 * @return
	 */
	@GetMapping(path = "department/{deptId}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<DepartmentResponse> getDepartment(@PathVariable(name = "deptId") Long deptId) {
		return ResponseEntity.ok(DepartmentResponse.builder().error(false).message("")
				.data(departmentService.getDepartment(deptId)).build());
	}

	/**
	 * @param deptId
	 * @return
	 */
	@DeleteMapping(path = "department/{deptId}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<DepartmentResponse> deleteDepartment(@PathVariable(name = "deptId") Long deptId) {
		return ResponseEntity.ok(
				DepartmentResponse.builder().error(false).message("").data(departmentService.delete(deptId)).build());
	}

	/**
	 * @return
	 */
	@GetMapping(path = "departments")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<DepartmentResponse> getAllDepartment() {
		return ResponseEntity.ok(DepartmentResponse.builder().error(false).message("")
				.data(departmentService.getAllDepartment()).build());
	}

}
