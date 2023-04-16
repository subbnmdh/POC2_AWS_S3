package com.mongodb.aws.demo.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection="Students")
public class Student {
	
	@Transient
    public static final String SEQUENCE_NAME = "rollno_sequence";
	
	@Id
	private long rollNo;
	
	private String firstName;
	private String lastName;
	private Date dob;
	private String imageLink;
}
