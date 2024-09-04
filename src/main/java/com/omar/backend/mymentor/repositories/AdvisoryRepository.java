package com.omar.backend.mymentor.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.omar.backend.mymentor.models.entities.Advisory;

@Repository
public interface AdvisoryRepository extends CrudRepository<Advisory, Long>{

    @Query("SELECT a FROM Advisory a WHERE a.userProfessional.id=?1")
    List<Advisory> findByUserProfessionalId(Long userProfessionalId);

    @Modifying
    @Query("DELETE FROM Advisory a WHERE a.userProfessional.id=?1")
    void deleteByUserProfessionalId(Long userProfessionalId);

}
