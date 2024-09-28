package com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.StudentEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.SubjectsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<StudentEntity, String> {
    @Query("SELECT s FROM StudentEntity s WHERE s.id_career = :idCareer " +
            "ORDER BY s.level DESC")
    List<StudentEntity> findByCareer(@Param("idCareer") Long idCareer);
}
