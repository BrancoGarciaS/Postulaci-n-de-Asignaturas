package com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.PrerequisitesEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrerequisitesRepository extends CrudRepository<PrerequisitesEntity, Long> {

    @Query("SELECT p FROM PrerequisitesEntity p WHERE p.id_subject = :idSubject")
    List<PrerequisitesEntity> findBySubjectId(@Param("idSubject") Long idSubject);
}
