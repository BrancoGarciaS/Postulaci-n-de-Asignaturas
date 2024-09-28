package com.example.BrancoGarcia_Tingeso_Evaluacion3.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.services.ScoresService;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentService studentService;

    @Autowired
    ScoresService scoresService;

    @GetMapping("/get")
    public List<StudentEntity> getStudents(){
        return studentService.getAllStudents();
    }

    @GetMapping("/getByCareer/{id}")
    public List<StudentEntity> getStudentsByCareer(@PathVariable Long id){
        return studentService.getStudentsByIdCareer(id);
    }

    @GetMapping("/getByRut/{rut}")
    public StudentEntity getStudentByRut(@PathVariable String rut){
        scoresService.loadAll(rut);
        return studentService.getStudentByRut(rut).get();
    }

    @GetMapping("/getByRut2/{rut}")
    public StudentEntity getStudentByRut2(@PathVariable String rut){
        Optional<StudentEntity> s = studentService.getStudentByRut(rut);
        if(s.isPresent()){
            if(s.get().getStay_career() == null){
                scoresService.loadAll(rut);
            }
            return s.get();
        }
        else{
            return null;
        }
    }

}
