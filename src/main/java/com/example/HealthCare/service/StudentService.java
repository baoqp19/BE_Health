package com.example.HealthCare.service;

import com.example.HealthCare.dto.StudentLocation;
import com.example.HealthCare.model.Student;

public interface StudentService {

    StudentLocation getStudentLocation(Long id);

    Student getStudentById(Long id);

    Student createNewStudent(Student student);
}
