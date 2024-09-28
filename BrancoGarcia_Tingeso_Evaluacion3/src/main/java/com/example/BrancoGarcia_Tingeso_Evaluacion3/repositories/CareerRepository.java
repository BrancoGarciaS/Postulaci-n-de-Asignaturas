package com.example.BrancoGarcia_Tingeso_Evaluacion3.repositories;
import com.example.BrancoGarcia_Tingeso_Evaluacion3.entities.CareerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerRepository extends CrudRepository<CareerEntity, Long> {
}
