package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HigherEducationRepo extends JpaRepository<HigherEducationn,Long> {
}
