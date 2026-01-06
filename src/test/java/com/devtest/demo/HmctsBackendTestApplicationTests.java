package com.devtest.demo;

import com.devtest.demo.model.Task;
import com.devtest.demo.model.TaskStatus;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
class HmctsBackendTestApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void getAllTasks_ShouldReturnArray() throws Exception {
		mockMvc.perform(get("/api/tasks"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$", is(notNullValue())))
				.andExpect(jsonPath("$", is(instanceOf(java.util.List.class))));
	}

	@Test
	void createTask_ShouldReturnCreatedTask() throws Exception {
		Task task = new Task("Test Task", "Description", TaskStatus.PENDING, LocalDateTime.now().plusDays(1));
		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title", is("Test Task")))
				.andExpect(jsonPath("$.status", is("PENDING")));
	}

	@Test
	void updateTaskStatus_ShouldReturnUpdatedTask() throws Exception {
		// First create a task to ensure it exists
		Task task = new Task("Update Me", "Before Update", TaskStatus.PENDING, LocalDateTime.now().plusDays(1));
		String response = mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		Task createdTask = objectMapper.readValue(response, Task.class);
		Long id = createdTask.getId();

		mockMvc.perform(patch("/api/tasks/" + id + "/status")
				.contentType(MediaType.APPLICATION_JSON)
				.content("\"COMPLETED\""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("COMPLETED")));
	}

	@Test
	void deleteTask_ShouldReturnNoContent() throws Exception {
		// First create a task to ensure it exists
		Task task = new Task("Delete Me", "To be deleted", TaskStatus.PENDING, LocalDateTime.now().plusDays(1));
		String response = mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		Task createdTask = objectMapper.readValue(response, Task.class);
		Long id = createdTask.getId();

		mockMvc.perform(delete("/api/tasks/" + id))
				.andExpect(status().isNoContent());
	}

	@Test
	void getTaskById_WithBadId_ShouldReturnNotFound() throws Exception {
		mockMvc.perform(get("/api/tasks/999"))
				.andExpect(status().isNotFound());
	}

	@Test
	void createTask_WithInvalidBody_ShouldReturnBadRequest() throws Exception {
		mockMvc.perform(post("/api/tasks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\": \"Invalid JSON because it is missing status and closing brace\""))
				.andExpect(status().isBadRequest());
	}
}
