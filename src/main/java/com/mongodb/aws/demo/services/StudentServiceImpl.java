package com.mongodb.aws.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.mongodb.aws.demo.entity.Student;
import com.mongodb.aws.demo.repository.StudentRepository;

import exceptions.ResourceNotFoundException;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	StudentRepository studentRepository;
	
	@Override
	public Student addNewStdudent(Student s) {
		// TODO Auto-generated method stub
		
		return studentRepository.save(s);
	}

	@Override
	public Student fetchStudentByRollno(Long rollno)  throws ResourceNotFoundException {
		// TODO Auto-generated method stub
			Student std=studentRepository.findById(rollno).orElseThrow(()->new ResourceNotFoundException("Student Record Not Found For this Rollno. :: "+rollno));
			return std;
	}	

	@Override
	public List<Student> fetchAllStudents() {
		// TODO Auto-generated method stub
		return studentRepository.findAll();
		
	}

//	@Override
//	public Student updateStudent(Student s) {
//		// TODO Auto-generated method stub
//		return studentRepository.save(s);
//		
//	}

	@Override
	public void deleteStudentByRollno(Long rollNo){
		// TODO Auto-generated method stub
		   
			studentRepository.deleteById(rollNo);
		
	}
	

}
