package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.County;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountyRepository extends JpaRepository<County,Long> {
    Optional<County> findByCode(Long county_code);

    boolean existsByCode(Long code);
}
