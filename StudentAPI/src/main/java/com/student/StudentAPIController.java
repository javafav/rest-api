package com.student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.student.repository.Student;
import com.student.repository.StudentRepository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/students")
@Validated
public class StudentAPIController {

	@Autowired
	private StudentRepository repo;

	@GetMapping
	public ResponseEntity<?> getAll(
			@RequestParam("pageSize") @Min(value = 5, message = "Minimun page size is 5") int pageSize,
			@Max(value = 10, message = "Maximum page size is 10") int pageNum) {

		List<Student> listStudents = repo.findAll();

		System.out.println("Page Size " + pageSize);
		System.out.println("Page Num " + pageNum);

		if (listStudents.isEmpty()) {

			return ResponseEntity.noContent().build();

		}

		return new ResponseEntity<>(listStudents, HttpStatus.ACCEPTED);

	}

	@PostMapping
	public ResponseEntity<?> addStudent(@RequestBody Student student) {

		repo.save(student);

		return new ResponseEntity<>(student, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<?> updateStudent(@RequestBody Student student) {
		Student studentInDB = repo.findById(student.getId()).get();
		if (studentInDB == null) {
			return ResponseEntity.notFound().build();

		}
		Student updatedStudnet = repo.save(student);
		return new ResponseEntity<>(updatedStudnet, HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") @Positive Integer id) {
		Student student = new Student(id);

		if (repo.findById(id).isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repo.delete(student);
		return ResponseEntity.noContent().build();

	}
}
