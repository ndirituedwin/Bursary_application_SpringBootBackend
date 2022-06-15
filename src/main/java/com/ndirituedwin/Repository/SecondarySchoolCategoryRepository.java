package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.SecondarySchoolCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondarySchoolCategoryRepository extends JpaRepository<SecondarySchoolCategory,Long> {

    boolean existsByCategory(String category);
}
