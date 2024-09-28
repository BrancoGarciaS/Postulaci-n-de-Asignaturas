package com.example.BrancoGarcia_Tingeso_Evaluacion3.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    StudentRepository studentRepository;

    public List<StudentEntity> getAllStudents(){
        return (List<StudentEntity>) studentRepository.findAll();
    }

    public List<StudentEntity> getStudentsByIdCareer(long id_career){
        return (List<StudentEntity>) studentRepository.findByCareer(id_career);
    }



    public Optional<StudentEntity> getStudentByRut(String rut){
        return studentRepository.findById(rut);
    }

    public StudentEntity saveStudent(StudentEntity student){
        return studentRepository.save(student);
    }


}
