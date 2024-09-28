package com.example.BrancoGarcia_Tingeso_Evaluacion3.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.InfoEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoService {
    @Autowired
    InfoRepository infoRepository;

    public InfoEntity saveInfo(InfoEntity infoEntity){
        return infoRepository.save(infoEntity);
    }

    public List<InfoEntity> getInfoByRut(String rut){

        return  infoRepository.getInfoByRut(rut);
    }

    public InfoEntity getInfoByRutAndSubject(String rut, long id_subject){
        List<InfoEntity> l = infoRepository.getInfoByRutAndSubject(rut, id_subject);
        if(l == null || l.isEmpty()){
            return null;
        }
        return l.get(0);
    }

    public List<InfoEntity> approved_subjects(String rut){
        return infoRepository.approved_subjects(rut);
    }

    public List<InfoEntity> inscribed_subjects(String rut){
        return infoRepository.inscribed_subjects(rut);
    }


    public List<InfoEntity> can_register(String rut){
        return infoRepository.can_register(rut);
    }


}
