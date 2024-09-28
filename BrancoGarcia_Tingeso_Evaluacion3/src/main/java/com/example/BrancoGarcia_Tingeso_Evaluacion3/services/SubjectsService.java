package com.example.BrancoGarcia_Tingeso_Evaluacion3.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.SubjectsEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories.SubjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectsService {

    @Autowired
    SubjectsRepository subjectsRepository;

    public List<SubjectsEntity> getSubjects(){
        return (List<SubjectsEntity>) subjectsRepository.findAll();
    }

    // Para añadir muchos bloques a un ramo
    // 0: fracaso, 1: exito
    public Integer addManySchedules(Long id_subject, List<Integer> schedules){
        Optional<SubjectsEntity> s = getSubjectsById(id_subject);
        if(s.isPresent()){
            SubjectsEntity subjectEntity = s.get();
            subjectEntity.setS_schedule(null); // elimino los horarios antiguos
            for(Integer sch : schedules) { // voy agregando los nuevos
                add_1_Schedule(subjectEntity, sch);
            }
            return 1; // exito
        }
        return 0; // fracaso
    }

    public SubjectsEntity saveSubject(SubjectsEntity subjects){
        return subjectsRepository.save(subjects);
    }


    // Para añadir un bloque a un ramo
    public SubjectsEntity add_1_Schedule(SubjectsEntity subjectEntity, Integer schedule){
        if(schedule > 54){ // Si el bloque pasa los horarios establecidos
            return subjectEntity;
        }
        List<Integer> sch = subjectEntity.getS_schedule();
        if(sch != null){
            if(!sch.contains(schedule)){
                // Si contiene ese bloque, no se vuelve agregar
                sch.add(schedule);
                subjectEntity.setS_schedule(sch);
            }
        }
        else{
            List<Integer> sch_n = new ArrayList<>();
            sch_n.add(schedule);
            subjectEntity.setS_schedule(sch_n);
            return subjectsRepository.save(subjectEntity);
        }
        return subjectsRepository.save(subjectEntity);
    }

    // Para añadir un bloque a un ramo
    public SubjectsEntity addSchedule(Long id_subject, Integer schedule){
        Optional<SubjectsEntity> s = getSubjectsById(id_subject);
        if(s.isPresent()){
            SubjectsEntity subjectEntity = s.get();
            if(schedule > 54){
                return subjectEntity;
            }
            List<Integer> sch = subjectEntity.getS_schedule();
            if(sch != null){
                if(!sch.contains(schedule)){
                    // Si contiene ese bloque, no se vuelve agregar
                    sch.add(schedule);
                    subjectEntity.setS_schedule(sch);
                }
            }
            else{
                List<Integer> sch_n = new ArrayList<>();
                sch_n.add(schedule);
                subjectEntity.setS_schedule(sch_n);
                return subjectsRepository.save(subjectEntity);
            }
            return subjectsRepository.save(subjectEntity);
        }
        return new SubjectsEntity();
    }

    public Optional<SubjectsEntity> getSubjectsById(Long id_subject){
        return subjectsRepository.findById(id_subject);
    }

    public List<SubjectsEntity> getSubjectsByCareerId(Long idCareer) {
        return subjectsRepository.findAllByCareerOrderedByLevel(idCareer);
    }

    public long getSubjectsByLevelAndCareer(Long id_career, Integer level){
        return subjectsRepository.subjectsByLevelAndCareer(id_career, level);
    }

    public List<SubjectsEntity> getByIdCareer(Long  idCareer){
        return subjectsRepository.findByCareer(idCareer);
    }


}
