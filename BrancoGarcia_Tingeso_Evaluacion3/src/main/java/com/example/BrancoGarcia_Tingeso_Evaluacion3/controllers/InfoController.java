package com.example.BrancoGarcia_Tingeso_Evaluacion3.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.InfoEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.services.InfoService;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.services.ScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/info")
public class InfoController {
    @Autowired
    InfoService infoService;

    @Autowired
    ScoresService scoresService;


    @GetMapping("/approved/{rut}")
    public List<InfoEntity> approved(@PathVariable String rut){
        scoresService.loadAll(rut);
        if(infoService.getInfoByRut(rut) == null || infoService.getInfoByRut(rut).isEmpty()){
            scoresService.saveSubjectsStudentInfo(rut);
        }
        return infoService.approved_subjects(rut);
    }


    @GetMapping("/canRegister/{rut}")
    public List<InfoEntity> can_register(@PathVariable String rut){
        scoresService.loadAll(rut);
        if(infoService.getInfoByRut(rut) == null || infoService.getInfoByRut(rut).isEmpty()){
            scoresService.saveSubjectsStudentInfo(rut);
        }
        return infoService.can_register(rut);
    }

    @GetMapping("/inscribed/{rut}")
    public List<InfoEntity> inscribed(@PathVariable String rut){
        scoresService.loadAll(rut);
        if(infoService.getInfoByRut(rut) == null || infoService.getInfoByRut(rut).isEmpty()){
            scoresService.saveSubjectsStudentInfo(rut);
        }
        return infoService.inscribed_subjects(rut);
    }

    @GetMapping("/infoByRut/{rut}")
    public List<InfoEntity> getInfoByRut(@PathVariable String rut){
        scoresService.loadAll(rut);
        if(infoService.getInfoByRut(rut) == null || infoService.getInfoByRut(rut).isEmpty()){
            scoresService.saveSubjectsStudentInfo(rut);
        }
        return infoService.getInfoByRut(rut);
    }

    @GetMapping("/infoStudent/{rut}/{id_subject}")
    public InfoEntity getInfoByRutAndSubject(@PathVariable String rut, @PathVariable long id_subject){
        if(infoService.getInfoByRut(rut) == null || infoService.getInfoByRut(rut).isEmpty()){
            scoresService.saveSubjectsStudentInfo(rut);
        }
        return infoService.getInfoByRutAndSubject(rut, id_subject);
    }
}
