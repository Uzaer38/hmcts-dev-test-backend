package com.devtest.demo.config;

import com.devtest.demo.model.Task;
import com.devtest.demo.model.TaskStatus;
import com.devtest.demo.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(TaskRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Task(
                    "Setup Project", 
                    "Initialize Spring Boot and H2", 
                    TaskStatus.COMPLETED, 
                    LocalDateTime.now().minusDays(1)
                ));
                repository.save(new Task(
                    "Implement API", 
                    "Create CRUD endpoints for tasks", 
                    TaskStatus.IN_PROGRESS, 
                    LocalDateTime.now().plusDays(2)
                ));
                repository.save(new Task(
                    "Write Tests", 
                    "Add unit and integration tests", 
                    TaskStatus.PENDING, 
                    LocalDateTime.now().plusDays(5)
                ));
                System.out.println("Sample data initialized in H2 database.");
            }
        };
    }
}

