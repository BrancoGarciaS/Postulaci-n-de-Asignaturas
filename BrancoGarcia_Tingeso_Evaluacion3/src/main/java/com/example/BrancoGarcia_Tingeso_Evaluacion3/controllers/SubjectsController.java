package com.example.BrancoGarcia_Tingeso_Evaluacion3.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.SubjectsEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.services.SubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/subject")
public class SubjectsController {
    @Autowired
    SubjectsService subjectsService;

    @GetMapping("/getByCareer/{id_career}")
    public List<SubjectsEntity> getSubjectsByIdCareer(@PathVariable Long id_career){
        return subjectsService.getSubjectsByCareerId(id_career);
    }

    @GetMapping("/subjectsByLevel/{id_career}/{level}")
    public long getSubjectsByLevel(@PathVariable Long id_career, @PathVariable Integer level){
        return subjectsService.getSubjectsByLevelAndCareer(id_career, level);
    }

    @GetMapping("/schedule/{id_subject}/{schedule}")
    public SubjectsEntity loadSchedule(@PathVariable Long id_subject, @PathVariable Integer schedule){
        return subjectsService.addSchedule(id_subject, schedule);
    }

    @PostMapping("/addSchedules/{id_subject}")
    public Integer addSchedulesToSubject(@PathVariable Long id_subject, @RequestBody List<Integer> schedules) {
        return subjectsService.addManySchedules(id_subject, schedules);
    }

    @GetMapping("/getById/{id_subject}")
    public SubjectsEntity getByIdSubject(@PathVariable Long id_subject){
        Optional<SubjectsEntity> subs = subjectsService.getSubjectsById(id_subject);
        if(subs.isPresent()){
            return subs.get();
        }
        return null;
    }
}
