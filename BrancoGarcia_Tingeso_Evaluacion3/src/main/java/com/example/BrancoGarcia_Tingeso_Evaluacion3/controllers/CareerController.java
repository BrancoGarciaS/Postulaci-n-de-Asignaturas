package com.example.BrancoGarcia_Tingeso_Evaluacion3.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.CareerEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.PrerequisitesEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.services.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@CrossOrigin
@RequestMapping("/career")
public class CareerController {
    @Autowired
    CareerService careerService;

    @GetMapping("/get")
    public ArrayList<CareerEntity> getCareers(){
        return careerService.getCareers();
    }


}
