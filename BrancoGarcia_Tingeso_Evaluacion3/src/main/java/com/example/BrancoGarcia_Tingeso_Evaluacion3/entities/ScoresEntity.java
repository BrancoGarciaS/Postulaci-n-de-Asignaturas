package com.example.BrancoGarcia_Tingeso_Evaluacion3.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "score")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoresEntity {
    // Atributos de la entidad
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_score_student;

    private Integer year;

    private Integer semester;

    private String rut;

    private Long id_subject;

    private float score;

    private Integer level;
}

