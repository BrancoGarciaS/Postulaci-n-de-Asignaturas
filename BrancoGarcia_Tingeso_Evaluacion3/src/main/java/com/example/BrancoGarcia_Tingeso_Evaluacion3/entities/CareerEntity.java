package com.example.BrancoGarcia_Tingeso_Evaluacion3.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "career")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerEntity {
    // Atributos de la entidad
    @Id
    @Column(unique = true, nullable = false)
    private Long id_career;

    private String name;
}