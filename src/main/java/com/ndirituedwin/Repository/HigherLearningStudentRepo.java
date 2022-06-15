package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationn;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherLearningStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HigherLearningStudentRepo extends JpaRepository<HigherLearningStudent,Long> {
    boolean existsByAdmissionnumberAndCollege(String admissionnumber, HigherEducationn higherEducationn);

    Optional<HigherLearningStudent> findByAdmissionnumberAndCollege(String admissionnumber, HigherEducationn higherEducationn);
}
