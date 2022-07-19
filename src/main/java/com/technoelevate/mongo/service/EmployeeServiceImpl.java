package com.technoelevate.mongo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
//@RequiredArgsConstructor
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

	private MongoTemplate template;

//	@Caching(evict = { @CacheEvict(value = "employee", allEntries = true) })
	@Cacheable(value = "employee", key = "#employeeDto.firstName")
	public EmployeeDto create(EmployeeDto employeeDto) {
		Employee employee = Employee.builder().createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
		BeanUtils.copyProperties(employeeDto, employee);
		Query query1 = new Query();
		query1.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(employee.getDeptId())));

		Department department = template.findOne(query1, Department.class);
		if (department == null)
			employee.setDeptId(null);
		else {
			List<Long> empIds = department.getEmpIds() == null ? new ArrayList<>() : department.getEmpIds();
			if (!empIds.contains(employee.getEmpId())) {
				empIds.add(employee.getEmpId());
				department.setEmpIds(empIds);
			}
			template.save(department);
		}

		Employee employee2 = template.save(employee);

		return getEmp(employee2);
	}

	@Caching(evict = { @CacheEvict(value = "employee", allEntries = true) }, put = {
			@CachePut(value = "employee", key = "#employeeDto.empId") })
	public EmployeeDto update(EmployeeDto employeeDto) {
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(employeeDto.getEmpId())));

		Employee employee = template.findOne(query, Employee.class);
		if (employee != null) {
			Query query1 = new Query();
			query1.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(employeeDto.getDeptId())));

			Department department = template.findOne(query1, Department.class);
			List<Long> empIds = department == null ? new ArrayList<>() : department.getEmpIds();

			if (!empIds.contains(employee.getEmpId()) && department != null) {
				empIds.add(employee.getEmpId());
				department.setEmpIds(empIds);
			}
			template.save(department);

		}
		BeanUtils.copyProperties(employeeDto, employee);
		employee = template.save(employee);
		return getEmp(employee);
	}

	@Cacheable(value = "employee", key = "#empId")
	public EmployeeDto getEmployee(Long empId) {
		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(empId)));
		Employee employee = template.findOne(query, Employee.class);
		return getEmp(employee);
	}

	private EmployeeDto getEmp(Employee employee) {
		EmployeeDto employeeDto = EmployeeDto.builder().build();
		DepartmentDto departmentDto = DepartmentDto.builder().build();
		if (employee != null) {
			Query query = new Query();
			query.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(employee.getDeptId())));
			Department department = template.findOne(query, Department.class);
			if (department != null)
				BeanUtils.copyProperties(department, departmentDto);
			departmentDto.setEmpIds(null);
			BeanUtils.copyProperties(employee, employeeDto);
		}
		employeeDto.setDepartment(departmentDto);
		employeeDto.setDeptId(null);
		return employeeDto;
	}

	@Caching(evict = { @CacheEvict(value = "employee", key = "#empId"),
			@CacheEvict(value = "employee", allEntries = true) })
	public boolean delete(Long empId) {

		Query query = new Query();
		query.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(empId)));

		Employee employee = template.findAndRemove(query, Employee.class);
		if (employee != null) {
			Query query1 = new Query();
			query1.addCriteria(new Criteria().andOperator(Criteria.where("_id").is(employee.getDeptId())));

			Department department = template.findOne(query1, Department.class);

			if (department != null) {
				List<Long> ids = department.getEmpIds() == null ? new ArrayList<>() : department.getEmpIds();
				if (ids.contains(empId))
					ids.remove(empId);

				department.setEmpIds(ids);

				template.save(department);
			}
			return true;
		}
		return false;
	}

	@Cacheable(value = "employees")
	public List<EmployeeDto> getAllEmployee() {
		List<Employee> emps = template.findAll(Employee.class);
		return emps.stream().map(emp -> getEmp(emp)).collect(Collectors.toList());
	}

}
