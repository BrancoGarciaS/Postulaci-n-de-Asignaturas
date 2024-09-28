package com.example.BrancoGarcia_Tingeso_Evaluacion3.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {
    // Atributos de la entidad
    @Id
    @Column(unique = true, nullable = false)
    private String rut;

    private String name;

    private String lastname;

    private String email;

    private Long id_career;

    private Integer level;

    private Integer max_subjects; // numero máximo de ramos a tomar

    private Integer stay_career; // Si permanece en la carrera

    private Integer enrolled_subjects; // Cantidad de ramos tomados

    private Integer opportunities; // Oportunidades sobre cantidad de reprobación de un ramo

    private List<Integer> inscribed; // Ramos inscritos

    private String name_career; // Nombre de la carrera

    private List<String> name_inscribed; // Nombre de datos inscritos
}
