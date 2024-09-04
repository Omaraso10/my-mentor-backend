package com.omar.backend.mymentor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.omar.backend.mymentor.models.entities.Professional;

@Repository
public interface ProfessionalRepository extends CrudRepository<Professional, Long>{

    @Query("select p from Professional p where p.area.id=?1")
    List<Professional> findByAreaId(Long area_id);
}
