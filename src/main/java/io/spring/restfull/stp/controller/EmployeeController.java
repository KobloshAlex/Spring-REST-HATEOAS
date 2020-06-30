package io.spring.restfull.stp.controller;

import io.spring.restfull.stp.exception.EmployeeNotFoundEcxeption;
import io.spring.restfull.stp.model.Employee;
import io.spring.restfull.stp.model.assembler.EmployeeModelAssembler;
import io.spring.restfull.stp.model.repo.EmployeeRepo;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

  private final EmployeeRepo employeeRepo;
  private final EmployeeModelAssembler assembler;

  public EmployeeController(EmployeeRepo employeeRepo, EmployeeModelAssembler assembler) {
    this.employeeRepo = employeeRepo;
    this.assembler = assembler;
  }

  // Aggregete root

  @GetMapping("/employees")
  public CollectionModel<EntityModel<Employee>> all() {

    List<EntityModel<Employee>> employees =
        employeeRepo.findAll().stream().map(assembler::toModel).collect(toList());

    return CollectionModel.of(
        employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
  }

  @PostMapping("/employees")
  public ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {

    EntityModel<Employee> entityModel = assembler.toModel(employeeRepo.save(newEmployee));

    return ResponseEntity //
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
        .body(entityModel);
  }

  // Single item

  @GetMapping("/employees/{id}")
  public EntityModel<Employee> one(@PathVariable Long id) {

    final Employee employee =
        employeeRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundEcxeption(id));

    return assembler.toModel(employee);
  }

  @PutMapping("/employees/{id}")
  public ResponseEntity<?> replaceEmployee(
      @RequestBody Employee newEmployee, @PathVariable Long id) {

    Employee updatedEmployee =
        employeeRepo
            .findById(id)
            .map(
                employee -> {
                  employee.setName(newEmployee.getName());
                  employee.setRole(newEmployee.getRole());
                  return employeeRepo.save(employee);
                })
            .orElseGet(
                () -> {
                  newEmployee.setId(id);
                  return employeeRepo.save(newEmployee);
                });

    EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @DeleteMapping("/employees/{id}")
  public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

    employeeRepo.deleteById(id);

    return ResponseEntity.noContent().build();
  }
}
