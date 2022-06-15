package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationn;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationnCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HigherEducationRepository extends JpaRepository<HigherEducationn,Long> {
    boolean existsByName(String name);

    Page<HigherEducationn> findByHigherEducationnCategory(HigherEducationnCategory secondarySchoolCategory, Pageable pageable);
}
