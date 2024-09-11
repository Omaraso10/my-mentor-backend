package com.omar.backend.mymentor.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.omar.backend.mymentor.models.entities.User;

@Repository
public interface UserRepository
        extends CrudRepository<User, Long> {

        //@EntityGraph(attributePaths = {"mentors", "mentors.professional"}) //Sirve
        Optional<User> findByUuid(String uuid);

        //Tambien sirve
        // @Query("SELECT u FROM User u LEFT JOIN FETCH u.mentors m LEFT JOIN FETCH m.professional WHERE u.uuid = :uuid")
        // Optional<User> findByUuidWithProfessionals(String uuid);

        @Query("select u from User u where u.email=?1")
        Optional<User> getUserByEmail(String email);

        Page<User> findAll(Pageable pageable);

        @Modifying
        @Query(value = "DELETE FROM users_roles WHERE uuid = :uuid", nativeQuery = true)
        void deleteUserRolesByUuid(@Param("uuid") String uuid);

        @Modifying
        @Query("DELETE FROM User u WHERE u.uuid=?1")
        void deleteByUuid(String uuid);

        public boolean existsByEmail(String email);
}
