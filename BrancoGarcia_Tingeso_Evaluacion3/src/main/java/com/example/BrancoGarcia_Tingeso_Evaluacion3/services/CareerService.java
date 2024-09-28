package com.example.BrancoGarcia_Tingeso_Evaluacion3.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.CareerEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.PrerequisitesEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories.CareerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CareerService {
    @Autowired
    CareerRepository careerRepository;

    public ArrayList<CareerEntity> getCareers(){
        return (ArrayList<CareerEntity>) careerRepository.findAll();
    }

    public Optional<CareerEntity> getCareerById(long id_career){
        return careerRepository.findById(id_career);
    }

}
