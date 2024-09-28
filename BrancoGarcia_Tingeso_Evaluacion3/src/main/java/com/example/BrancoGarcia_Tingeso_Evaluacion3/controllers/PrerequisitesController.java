package com.example.BrancoGarcia_Tingeso_Evaluacion3.controllers;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.PrerequisitesEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.services.PrerequisitesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/prerequisites")
public class PrerequisitesController {
    @Autowired
    PrerequisitesService prerequisitesService;

    @GetMapping("/get")
    public ArrayList<PrerequisitesEntity> mostrarPrerequisitos(){
        return prerequisitesService.getPrerequisites();
    }

    @PostMapping("/load_excel")
    public String subirExcel(@RequestParam("file") MultipartFile file) {
        try {
            System.out.print("Guardando");
            prerequisitesService.saveFile(file);
            String filename = file.getOriginalFilename();
            prerequisitesService.readCsv(filename);
            String m = "Archivo cargado con éxito";
            return m;
        } catch (Exception e) {
            // En caso de error, se manda mensaje de error
            String m = "Error, problema al cargar archivo";
            return m;
        }
    }

    @GetMapping("/getPre/{id_subject}")
    public List<PrerequisitesEntity> mostrarPrerequisitosDeUnRamo(@PathVariable("id_subject") long id_subject){
        return prerequisitesService.getPrerequisitesBySubject(id_subject);
    }
}
