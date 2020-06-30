package io.spring.restfull.stp.model.repo;

import io.spring.restfull.stp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {}
