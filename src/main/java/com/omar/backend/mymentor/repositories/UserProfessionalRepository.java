package com.omar.backend.mymentor.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.omar.backend.mymentor.models.entities.UserProfessional;

@Repository
public interface UserProfessionalRepository extends CrudRepository<UserProfessional, Long> {

    @Modifying
    @Query("DELETE FROM UserProfessional up WHERE up.uuid=?1")
    void deleteByUuid(String uuid);

}
