package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.SchoolType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Schooltyperepository extends JpaRepository<SchoolType, Long> {
    long countByType(String type);
}
