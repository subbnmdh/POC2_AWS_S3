package com.mongodb.aws.demo.services;

import java.util.List;

import org.springframework.data.domain.Example;

import com.mongodb.aws.demo.entity.Student;

import exceptions.ResourceNotFoundException;

public interface StudentService {

	public Student addNewStdudent(Student s);
	public Student fetchStudentByRollno(Long id) throws ResourceNotFoundException;
	public List<Student> fetchAllStudents();
	//public Student updateStudent(Student s);
	public void deleteStudentByRollno(Long rollNo) throws ResourceNotFoundException;
}
