package com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories;

import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.ScoresEntity;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.SubjectsEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoresRepository extends CrudRepository<ScoresEntity, Long> {
    @Query("SELECT s.semester, s.year, count(s) FROM ScoresEntity s " +
            "WHERE s.rut = :rut GROUP BY s.semester, s.year")
    List<Object[]> subjectsBySemesterAndYear(@Param("rut") String rut);

    @Query("SELECT count(s) FROM ScoresEntity s " +
            "WHERE s.rut = :rut AND s.id_subject = :idSubject AND s.score >= 4")
    long countByRutAndIdSubjectAndScore(@Param("rut") String rut,
                                        @Param("idSubject") Long idSubject);

    @Query("SELECT count(s), s.id_subject FROM ScoresEntity s " +
            "WHERE s.rut = :rut AND s.score < 4 GROUP BY s.id_subject ORDER BY count(s) DESC")
    List<Object[]> countApprovals(@Param("rut") String rut);

    @Query("SELECT count(s) FROM ScoresEntity s " +
            "WHERE s.rut = :rut AND s.id_subject = :idSubject AND s.year = 2024")
    long isInscribed(@Param("rut") String rut,
                     @Param("idSubject") Long idSubject);

    /*
    @Modifying
    @Query("DELETE FROM ScoresEntity s WHERE s.id_subject = :idSubject AND s.rut = :rut AND s.year = 2024")
    void deleteSubjectByRut(@Param("idSubject") Long idSubject,
                                      @Param("rut") String rut);

     */

    @Query("SELECT s FROM ScoresEntity s WHERE s.rut = :rut " +
            "AND s.id_subject = :idSubject AND s.year = 2024")
    List<ScoresEntity> getScoresAndSubjects2024(@Param("rut") String rut,
            @Param("idSubject") Long idSubject);

    @Query("SELECT s.id_subject, count(s) FROM ScoresEntity s " +
            "WHERE s.year = 2024 AND s.semester = 1 " +
            "GROUP BY s.id_subject")
    List<Object[]> count_inscriptions();

    @Query("SELECT s FROM ScoresEntity s " +
            "WHERE s.year = 2024 AND s.semester = 1 AND s.id_subject = :idSubject")
    List<ScoresEntity> enrolled_students(@Param("idSubject") Long idSubject);

    @Query("SELECT s FROM ScoresEntity s " +
            "WHERE s.year != 2024 AND s.rut = :rut " +
            "ORDER BY s.year, s.semester")
    List<ScoresEntity> score_history_student(@Param("rut") String rut);

    @Query("SELECT s FROM ScoresEntity s WHERE s.rut = :rut AND s.id_subject = :id_subject")
    List<ScoresEntity> history_score(@Param("rut") String rut, @Param("id_subject") Long id_subject);
}
