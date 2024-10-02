package com.omar.backend.mymentor.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.omar.backend.mymentor.models.entities.Area;

@Repository
public interface AreaRepository extends CrudRepository<Area, Long>{

    Optional<Area> findByNameIgnoreCase(String name);

}
