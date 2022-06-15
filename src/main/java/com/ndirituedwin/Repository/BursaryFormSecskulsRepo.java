package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryApplication;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryFormSecskuls;
import com.ndirituedwin.Entity.SecondarySchool;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BursaryFormSecskulsRepo extends JpaRepository<BursaryFormSecskuls,Long> {

    boolean existsByStudentid(Long id);

    Optional<BursaryFormSecskuls> findByStudentid(Long id);

//    List<BursaryFormSecskuls> findAllByStudentid(Long id);

    boolean existsByBursaryApplication(BursaryApplication bursaryApplication);

    Optional<BursaryFormSecskuls> findByBursaryApplication(BursaryApplication bursaryApplication);

    @Query("SELECT V FROM BursaryFormSecskuls V WHERE V.bursaryApplication.secStudent.secondaryschool =:secondarySchool ")
    Page<BursaryFormSecskuls> findAllBySecondaryShool(SecondarySchool secondarySchool, Pageable pageable);
    @Query("SELECT V FROM BursaryFormSecskuls V WHERE V.bursaryApplication.secStudent.secondaryschool.category =:secondarySchoolCategory ")
    Page<BursaryFormSecskuls> findAllBySecondaryShoolCategory(SecondarySchoolCategory secondarySchoolCategory, Pageable pageable);

    @Query("SELECT V FROM BursaryFormSecskuls V WHERE V.bursaryApplication.applicationPeriod.year =:applicationPeriodYear")
    Page<BursaryFormSecskuls> findAllByApplicationYear(String applicationPeriodYear, Pageable pageable);

    @Query("SELECT V FROM BursaryFormSecskuls V WHERE V.bursaryApplication.applicationPeriod.year =:year and V.bursaryApplication.applicationPeriod.month =:month ")
    Page<BursaryFormSecskuls> findAllByApplicationYearandMonth(String year, String month, Pageable pageable);

    Page<BursaryFormSecskuls> findAllByStudentid(Long id, Pageable pageable);
}
