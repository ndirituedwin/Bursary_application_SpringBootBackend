package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.SecondarySchool;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecondarySchoolRepository extends JpaRepository<SecondarySchool,Long> {
    boolean existsByCode(Long code);

    boolean existsBySchoolAndCountyCode(String school, Long countyId);


    Page<SecondarySchool> findAllByCategory(Pageable pageable, SecondarySchoolCategory secondarySchoolCategory);

    Page<SecondarySchool> findByCategory(SecondarySchoolCategory id, Pageable pageable);

}
