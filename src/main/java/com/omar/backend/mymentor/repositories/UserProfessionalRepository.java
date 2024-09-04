package com.omar.backend.mymentor.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.omar.backend.mymentor.models.entities.UserProfessional;

@Repository
public interface UserProfessionalRepository extends CrudRepository<UserProfessional, Long> {

}
