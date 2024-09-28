package com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.SubjectsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectsRepository extends CrudRepository<SubjectsEntity, Long>  {
    @Query("SELECT s FROM SubjectsEntity s WHERE s.id_career = :idCareer ORDER BY s.level")
    List<SubjectsEntity> findAllByCareerOrderedByLevel(@Param("idCareer") Long idCareer);

    @Query("SELECT COUNT(s) FROM SubjectsEntity s WHERE s.id_career = :idCareer AND s.level = :level")
    long subjectsByLevelAndCareer(@Param("idCareer") Long idCareer, @Param("level") Integer level);

    @Query("SELECT s FROM SubjectsEntity s WHERE s.id_career = :idCareer")
    List<SubjectsEntity> findByCareer(@Param("idCareer") Long idCareer);
}
