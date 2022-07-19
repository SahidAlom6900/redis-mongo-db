package com.technoelevate.mongo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.technoelevate.mongo.dto.DepartmentDto;
import com.technoelevate.mongo.dto.EmployeeDto;
import com.technoelevate.mongo.entity.Department;
import com.technoelevate.mongo.entity.Employee;

import lombok.AllArgsConstructor;

/**
 * @author Sahid
 *
 */
@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

	private MongoTemplate template;

//	@Caching(evict = { @CacheEvict(value = "employee", allEntries = true) })
	/**
	 *
	 */
	@Cacheable(value = "mg_dept", key = "#departmentDto.deptId")
	public DepartmentDto create(DepartmentDto departmentDto) {
		Department department = Department.builder().createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
				.build();
		BeanUtils.copyProperties(departmentDto, department);
		Department department2 = template.save(department);

		Query query1 = new Query();
		query1.addCriteria(new Criteria().andOperator(
				Criteria.where("_id").in(department.getEmpIds() == null ? new ArrayList<>() : department.getEmpIds())));

		List<Employee> employees = template.find(query1, Employee.class);

		if (!employees.isEmpty()) {
			employees.stream().filter(emp -> emp != null).forEach(emp -> {
				emp.setDeptId(department.getDeptId());
				template.save(emp);
			});
		}

		return getDept(department2);
	}

	@Caching(evict = { @CacheEvict(value = "department", allEntries = true) }, put = {
			@CachePut(value = "mg_dept", key = "#departmentDto.deptId") })
	public DepartmentDto update(DepartmentDto departmentDto) {
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(departmentDto.getDeptId())));
		Department department = template.findOne(query, Department.class);
		if (department != null) {
			List<Long> empIds = department.getEmpIds() == null ? new ArrayList<>() : department.getEmpIds();
			for (Long id : departmentDto.getEmpIds()) {
				if (!empIds.contains(id))
					empIds.add(id);
			}
			BeanUtils.copyProperties(departmentDto, department);
			department.setEmpIds(empIds);
			Query query1 = new Query();
			query1.addCriteria(new Criteria().andOperator(Criteria.where("_id")
					.in(department.getEmpIds() == null ? new ArrayList<>() : department.getEmpIds())));

			List<Employee> employees = template.find(query1, Employee.class);
			employees.stream().forEach(emp -> {
				emp.setDeptId(department.getDeptId());
				template.save(emp);
			});
			template.save(department);
		} else
			BeanUtils.copyProperties(departmentDto, department);
		return getDept(department);
	}

	private DepartmentDto getDept(Department department) {
		DepartmentDto departmentDto = DepartmentDto.builder().build();
		if (department != null) {
			Query query = new Query();
			List<Long> empIds = department.getEmpIds() == null ? new ArrayList<>() : department.getEmpIds();
			query.addCriteria(new Criteria().andOperator(Criteria.where("_id").in(empIds)));
			List<Employee> employees = template.find(query, Employee.class);
			if (!employees.isEmpty()) {
				List<EmployeeDto> empList = employees.stream().filter(emp1 -> emp1.getDeptId() != null)
						.filter(emp -> emp.getDeptId().equals(department.getDeptId())).map(employee -> {
							EmployeeDto employeeDto = EmployeeDto.builder().build();
							BeanUtils.copyProperties(employee, employeeDto);
							employeeDto.setDeptId(null);
							return employeeDto;
						}).collect(Collectors.toList());
				departmentDto.setEmployeeDtos(empList);
			}
			BeanUtils.copyProperties(department, departmentDto);
			departmentDto.setEmpIds(null);
		}
		return departmentDto;
	}

	@Caching(evict = { @CacheEvict(value = "mg_dept", key = "#deptId"),
			@CacheEvict(value = "mg_dept", allEntries = true) })
	public DepartmentDto delete(Long deptId) {
		Query query1 = new Query();
		query1.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(deptId)));

		List<Employee> employees = template.find(query1, Employee.class);
		employees.stream().forEach(emp -> {
			emp.setDeptId(null);
			template.save(emp);
		});

		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(deptId)));

		Department department = template.findAndRemove(query, Department.class);

		return getDept(department);
	}

	@Cacheable(value = "mg_dept", key = "#deptId")
	public DepartmentDto getDepartment(Long deptId) {
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(deptId)));
		Department department = template.findOne(query, Department.class);
		return getDept(department);
	}

	@Cacheable(value = "mg_depts")
	public List<DepartmentDto> getAllDepartment() {
		List<Department> departments = template.findAll(Department.class);
		return departments.isEmpty() ? Collections.emptyList()
				: departments.stream().map(dept -> getDept(dept)).collect(Collectors.toList());
	}

}
