package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationnCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HigherEducationCategoryRepository extends JpaRepository<HigherEducationnCategory,Long> {



    boolean existsByCategory(String category);
}
