package com.example.BrancoGarcia_Tingeso_Evaluacion3.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "subject")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectsEntity {
    // Atributos de la entidad
    @Id
    @Column(unique = true, nullable = false)
    private Long id_subject;

    private String name;

    private Long id_career;

    private String id_plan;

    private Integer level;

    private Integer capacity;

    private Integer inscribed; // personas inscritas

    private List<Integer> s_schedule; // horarios del ramo
}
