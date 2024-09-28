package com.example.BrancoGarcia_Tingeso_Evaluacion3.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id_info;
    private String rut;
    private Integer canRegister;
    private Integer is_inscribed;
    private List<Integer> schedule; // Horarios del ramo
    private String name_subject;
    private Integer level;
    private Integer state;
    private Long id_subject;
    private Integer capacity;
    private Integer students_inscribed;
}
