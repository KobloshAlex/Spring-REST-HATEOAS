package io.spring.restfull.stp;

import io.spring.restfull.stp.model.Employee;
import io.spring.restfull.stp.model.repo.EmployeeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadData {

  public static final Logger log = LoggerFactory.getLogger(LoadData.class);

  @Bean
  CommandLineRunner initDatabase(EmployeeRepo employeeRepo) {
    return args -> {
      log.info("Preloading " + employeeRepo.save(new Employee("Bilbo", "Baggins", "burglar")));
      log.info("Preloading " + employeeRepo.save(new Employee("Frodo", "Baggins", "thief")));
    };
  }
}
