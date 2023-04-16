package com.mongodb.aws.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mongodb.aws.demo.entity.Student;

public interface StudentRepository extends MongoRepository<Student, Long> {

}
