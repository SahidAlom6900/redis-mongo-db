package com.technoelevate.mongo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection  = "department_details")
@JsonInclude(value = Include.NON_DEFAULT)
public class Department implements Serializable {

	@Id
	private Long deptId;

	private String deptName;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private List<Long> empIds;

}
