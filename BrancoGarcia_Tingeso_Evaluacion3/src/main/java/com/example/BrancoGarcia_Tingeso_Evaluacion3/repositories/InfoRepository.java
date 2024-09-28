package com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.InfoEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface InfoRepository extends CrudRepository<InfoEntity, Long> {
    @Query("SELECT s FROM InfoEntity s " +
            "WHERE s.rut = :rut")
    List<InfoEntity> getInfoByRut(@Param("rut") String rut);

    @Query("SELECT s FROM InfoEntity s WHERE s.rut = :rut AND s.id_subject = :id_subject")
    List<InfoEntity> getInfoByRutAndSubject(@Param("rut") String rut, @PathVariable("id_subject") Long id_subject);

    @Query("SELECT s FROM InfoEntity s WHERE s.state = 1 AND s.rut = :rut")
    List<InfoEntity> approved_subjects(@Param("rut") String rut);

    @Query("SELECT s FROM InfoEntity s WHERE s.is_inscribed = 1 AND s.rut = :rut")
    List<InfoEntity> inscribed_subjects(@Param("rut") String rut);


    @Query("SELECT s FROM InfoEntity s WHERE s.canRegister = 1 AND s.rut = :rut AND s.is_inscribed = 0")
    List<InfoEntity> can_register(@Param("rut") String rut);

}
