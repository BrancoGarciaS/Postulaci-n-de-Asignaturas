package com.example.BrancoGarcia_Tingeso_Evaluacion3.services;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.PrerequisitesEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories.PrerequisitesRepository;
import lombok.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrerequisitesService {
    @Autowired
    PrerequisitesRepository prerequisitesRepository;

    public List<PrerequisitesEntity> getPrerequisitesBySubject(long id_subject){
        return prerequisitesRepository.findBySubjectId(id_subject);
    }

    public ArrayList<PrerequisitesEntity> getPrerequisites(){
        return (ArrayList<PrerequisitesEntity>) prerequisitesRepository.findAll();
    }

    // Para guardar un examen en la base de datos
    public PrerequisitesEntity saveDataDB(String id_subject, String id_prerequisite){
        Long id_s = Long.parseLong(id_subject);
        Long id_r = Long.parseLong(id_prerequisite);
        PrerequisitesEntity prerequisitesEntity = new PrerequisitesEntity();
        prerequisitesEntity.setId_prerequisite(id_r);
        prerequisitesEntity.setId_subject(id_s);
        return prerequisitesRepository.save(prerequisitesEntity);
    }

    public void readCsv(String filename){
        String texto = ""; // para almacenar el contenido del texto
        BufferedReader bf = null; // Objeto para leer
        try{
            bf = new BufferedReader(new FileReader(filename)); // abro el archivo csv para lectura
            String temp = ""; // para acumular las líneas leidas
            String bfRead; // para almacenar cada línea de archivo
            int count = 1; // desde que fila va a leer (omite la primera línea del archivo csv que
            // tiene los nombres de las variables)
            while((bfRead = bf.readLine()) != null){ // mientras hayan lineas por leer
                if (count == 1){ // si el contador es 1
                    count = 0; // omite la primera fila del csv
                }
                else{ // sino, significa que se están leyendo las otras líneas
                    // guardo los datos de cada celda de la fila en la base de datos de examenes
                    saveDataDB(bfRead.split(";")[0],
                            bfRead.split(";")[1]);
                    temp = temp + "\n" + bfRead; // acumulo la linea leida
                }
            }
            texto = temp;
            System.out.println("Archivo leido exitosamente");
        }catch(Exception e){
            System.err.println("No se encontro el archivo");
        }finally{
            if(bf != null){
                try{
                    bf.close(); // me encargo que se cierre BufferedReader
                }catch(IOException e){

                }
            }
        }
    }

    // Para guardar archivo csv
    public String saveFile(MultipartFile file){
        // obtengo el nombre del archivo
        String filename = file.getOriginalFilename();
        if(filename != null){ // si el nombre no es nulo y el archivo no está vacío
            if(!file.isEmpty()){
                try{
                    // Obtengo los bytes del archivo
                    byte [] bytes = file.getBytes();
                    // creo un archivo path del archivo
                    Path path  = Paths.get(file.getOriginalFilename());
                    // escribo los bytes en el archivo correspondiente
                    Files.write(path, bytes);
                    System.out.printf("archivo guardado");
                }
                catch (IOException e){ // en caso de error
                    System.out.printf("error");
                }
            }
            return "Archivo guardado con exito";
        }
        else{
            return "No se pudo guardar el archivo";
        }
    }
}
