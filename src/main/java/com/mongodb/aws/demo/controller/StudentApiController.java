package com.mongodb.aws.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.mongodb.aws.demo.entity.Student;
import com.mongodb.aws.demo.services.SequenceGeneratorService;
import com.mongodb.aws.demo.services.StudentService;
import com.mongodb.aws.demo.utils.FileOperationUtils;

import exceptions.ResourceNotFoundException;
//import com.mongodb.aws.demo.utils.FileOperationUtils;
@CrossOrigin(origins =  "http://localhost:8089")
@RestController
@RequestMapping("/students")
public class StudentApiController {

	@Value("${bucketname}")
	private String bucketName;
	
	@Autowired
	private AmazonS3 s3Client;
	
	private String imagePath="";
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	@Autowired
	private StudentService studentService;
	@GetMapping({"/index"})
	public ResponseEntity<?> indexHandler()
	{
		System.out.println("\n\tIndex Handler Responded");
		return ResponseEntity.status(HttpStatus.OK).body("Index Page");
	}
	@PostMapping("/uploadFile/{rollno}")
	public ResponseEntity<String> uploadFile(@PathVariable("rollno") Long rollNo, @RequestParam(name="file") MultipartFile file)
	{
		Student std=null;
		try
		{
			std=studentService.fetchStudentByRollno(rollNo);
		
			System.out.println("\n\tBucketName: "+bucketName);
			
			String uploadImageUrl = FileOperationUtils.UploadImage(file, bucketName, s3Client, rollNo);
			System.out.println("uploadImageUrl: "+uploadImageUrl);
			if(uploadImageUrl.contains(".jpg")|| uploadImageUrl.contains(".png"))
			{
				//uploadImageUrl.split(":");
				std.setImageLink(uploadImageUrl);
				
				studentService.addNewStdudent(std);
			}
			return ResponseEntity.status(HttpStatus.OK).body("Image Uploaded into the s3Bucket: "+uploadImageUrl);
		
		}
		catch(ResourceNotFoundException ex)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
		
	}
//	@DeleteMapping("/deleteFile")
//	public ResponseEntity<String> deleteFileHandler(@RequestParam("file") String filename)
//	{
//		System.out.println("\n\tdeleteFileHandler invoked");
//		System.out.println("\n\tFilename: "+filename);
//		String deletemsg = FileOperationUtils.deleteImage(s3Client, bucketName, filename);
//		
//		return ResponseEntity.status(HttpStatus.OK).body(deletemsg);
//	}
	
	@PostMapping("/addnew")
	public ResponseEntity<Student> addStudentHandler(@RequestBody Student std)
	{
		System.out.println("\n\t addStudentHandler invoked");
	
		std.setRollNo(sequenceGeneratorService.generateSequence(Student.SEQUENCE_NAME));	
		Student addNewStdudent = studentService.addNewStdudent(std);
		return ResponseEntity.status(HttpStatus.OK).body(addNewStdudent);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<Student>> getAllStudentHandler()
	{
	 	System.out.println("\n\tgetAllStudentHandler Invoked");
	 	List<Student> AllStudents = studentService.fetchAllStudents();
	 	return ResponseEntity.status(HttpStatus.OK).body(AllStudents);
	 }
	
	@GetMapping("/{rollno}")
	public ResponseEntity<?> getStudentHandler(@PathVariable(value="rollno") Long rollNo)
	{
		System.out.println("\n\tgetStudentHandler Invoked");
		
		try
		{
			Student fetchedStudent = studentService.fetchStudentByRollno(rollNo);
			return ResponseEntity.status(HttpStatus.FOUND).body(fetchedStudent);
		}
		catch(ResourceNotFoundException ex)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
	}
	
	@DeleteMapping("/{rollno}")
	public ResponseEntity<?> deleteStudentHandler(@PathVariable("rollno") Long rollNo){
		
		System.out.println("\n\tdeleteStudentHandler Invoked");
		try {
			   Student fetchStudent = studentService.fetchStudentByRollno(rollNo);
			   String imageLink = fetchStudent.getImageLink();
			   String filename=imageLink.split("/")[3];
			   System.out.println("\n\tImage File Name: "+filename);
			  studentService.deleteStudentByRollno(rollNo);
			  String deleteImageMsg = FileOperationUtils.deleteImage(s3Client, bucketName, filename);
			  
			return ResponseEntity.status(HttpStatus.OK).body("Student Record Deleted Successfully!\n\t "+deleteImageMsg+"!!");
		
		}
		catch(ResourceNotFoundException ex){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
		}
		
	}
	@PutMapping("/update")
	public ResponseEntity<Student> updateStudentHandler(@RequestBody Student std)
	{
		System.out.println("\n\tUpdate Student Handler Invoked...");
		try
		{
			System.out.println("std: "+std);
			Student updatedStd = studentService.addNewStdudent(std);
			return ResponseEntity.status(HttpStatus.OK).body(updatedStd);
			
		}
		catch(Exception ex)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
