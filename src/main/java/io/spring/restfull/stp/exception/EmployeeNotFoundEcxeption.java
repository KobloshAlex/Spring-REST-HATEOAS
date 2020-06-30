package io.spring.restfull.stp.exception;

public class EmployeeNotFoundEcxeption extends RuntimeException {

  public EmployeeNotFoundEcxeption(Long id) {
    super("Could not find employee " + id);
  }
}
