package com.technoelevate.mongo.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sahid
 *
 */
@SuppressWarnings("serial")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(value = Include.NON_DEFAULT)
public class DepartmentDto implements Serializable{
	
	private Long deptId;

	private String deptName;
	
	private List<Long> empIds;

	private List<EmployeeDto> employeeDtos;
}
