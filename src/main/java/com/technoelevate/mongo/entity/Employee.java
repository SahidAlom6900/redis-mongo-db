package com.technoelevate.mongo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sahid
 *
 */
@SuppressWarnings("serial")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection  = "employee_details")
@JsonInclude(value = Include.NON_DEFAULT)
public class Employee implements Serializable {

	@Id
	private Long empId;

	private String firstName;

	private String lastName;

	private String gender;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
	
	private Long deptId;
}
