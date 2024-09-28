package com.example.BrancoGarcia_Tingeso_Evaluacion3.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.InfoEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.ScoresEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.services.ScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    ScoresService scoresService;

    @GetMapping("/subjectsByRut/{rut}")
    public List<InfoEntity> studentInfoSubjects(@PathVariable String rut){
        scoresService.loadAll(rut);
        return scoresService.saveSubjectsStudentInfo(rut);
    }

    @GetMapping("/subjects/{rut}")
    public List<Map<String, Object>> studentSubjects(@PathVariable String rut){
        scoresService.loadAll(rut);
        return scoresService.subjectsStudent(rut);
    }

    @GetMapping("/prerequisitesApproved/{rut}/{id_subject}")
    public int aprobo(@PathVariable String rut, @PathVariable long id_subject){
        scoresService.loadAll(rut);
        return scoresService.approved_prerequisites(rut, id_subject);
    }

    @GetMapping("/getSub_semester/{rut}")
    public List<Object[]> getSubjectsBySemester(@PathVariable String rut){
        scoresService.loadAll(rut);
        return scoresService.countScoresBySemesterAndYear(rut);
    }

    @GetMapping("/ola/{rut}")
    public int ola(@PathVariable String rut){
        scoresService.loadAll(rut);
        return scoresService.subjects_3(rut);
    }

    @GetMapping("/getLevel/{rut}")
    public int getLevel(@PathVariable String rut){
        scoresService.loadAll(rut);
        return scoresService.getLevel(rut);
    }

    @GetMapping("/loadLevel")
    public void loadLevels(){
        scoresService.loadLevels();
    }

    @GetMapping("/loadMax")
    public void loadMax(){
        scoresService.loadMaxLevels();
    }
    @GetMapping("/loadStay")
    public void loadStay(){
        scoresService.stayInTheCareer();
    }

    @GetMapping("/loadOp")
    public void loadOportunities(){
        scoresService.load_nFailure();
    }

    @GetMapping("/loadListStudent")
    public void loadListS(){
        scoresService.load_schedule();
    }

    @GetMapping("/enrollSub/{rut}/{id_subject}")
    public int enroll_Subject(@PathVariable String rut, @PathVariable Long id_subject){
        scoresService.loadAll(rut);
        return scoresService.enroll_subject(rut, id_subject);
    }

    @GetMapping("/unsubscribe/{rut}/{id_subject}")
    public int unsubscribe(@PathVariable String rut, @PathVariable Long id_subject){
        scoresService.loadAll(rut);
        return scoresService.unsubscribe(rut, id_subject);
    }

    @GetMapping("/history/{rut}/{id_subject}")
    public List<ScoresEntity> history(@PathVariable String rut, @PathVariable Long id_subject){
        scoresService.loadAll(rut);
        return scoresService.history_scores(rut, id_subject);
    }

    @GetMapping("/loadCareerName")
    public void loadCareerNmae(){
        scoresService.load_nameCareer();
    }

    @GetMapping("/loadListNameInscribed")
    public void loadListNmaeInscribed(){
        scoresService.load_name_inscribed();
    }

    @GetMapping("/loadAll/{rut}")
    public void loadAll(){
        scoresService.load_name_inscribed();
    }

    @GetMapping("/count_inscriptions")
    public List<Object[]> count_inscriptions(){
        return scoresService.count_inscriptions();
    }

    @GetMapping("/enrolled_students/{id_subject}")
    public List<ScoresEntity> enrolled_students(@PathVariable Long id_subject) {
        return scoresService.enrolled_students(id_subject);
    }

    @GetMapping("/score_history/{rut}")
    public List<ScoresEntity> score_history_student(@PathVariable String rut){
        scoresService.loadAll(rut);
        return scoresService.score_history_student(rut);
    }

    @PostMapping("/addSchedules/{id_subject}")
    public Integer addSchedulesToSubject(@PathVariable Long id_subject, @RequestBody List<Integer> schedules){
        return scoresService.addSchedulesToSubject(id_subject, schedules);
    }
}
