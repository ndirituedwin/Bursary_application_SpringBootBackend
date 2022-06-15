package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationn;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationnCategory;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherLearningBursaryApplication;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherLearningBursaryForm;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryApplication;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryFormSecskuls;
import com.ndirituedwin.Entity.SecondarySchool;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HigherLearningBursaryFormRepo extends JpaRepository<HigherLearningBursaryForm,Long> {
    boolean existsByHigherLearningBursaryApplication(HigherLearningBursaryApplication bursaryApplication);


    Optional<HigherLearningBursaryForm> findByHigherLearningBursaryApplication(HigherLearningBursaryApplication bursaryApplication);

//    @Query("SELECT V FROM HigherLearningBursaryForm V WHERE V.higherLearningBursaryApplication.applicationPeriod.year =:applicationPeriodYear")
//    Page<HigherLearningBursaryForm> findAllByApplicationYear(String year, Pageable pageable);
    @Query("SELECT V FROM HigherLearningBursaryForm V WHERE V.higherLearningBursaryApplication.applicationPeriod.year =:applicationPeriodYear")
    Page<HigherLearningBursaryForm> findAllByApplicationYear(String applicationPeriodYear, Pageable pageable);

    @Query("SELECT V FROM HigherLearningBursaryForm V WHERE V.higherLearningBursaryApplication.applicationPeriod.year =:year and V.higherLearningBursaryApplication.applicationPeriod.month =:month ")
    Page<HigherLearningBursaryForm> findAllByApplicationYearandMonth(String year, String month, Pageable pageable);



    @Query("SELECT V FROM HigherLearningBursaryForm V WHERE V.higherLearningBursaryApplication.higherLearningStudent.college.higherEducationnCategory =:secondarySchoolCategory ")
    Page<HigherLearningBursaryForm> findAllByHigherEducationCategory(HigherEducationnCategory secondarySchoolCategory, Pageable pageable);



    @Query("SELECT V FROM HigherLearningBursaryForm V WHERE V.higherLearningBursaryApplication.higherLearningStudent.college =:secondarySchool ")
    Page<HigherLearningBursaryForm> findAllByHigherEducationn(HigherEducationn secondarySchool, Pageable pageable);

    Page<HigherLearningBursaryForm> findAllByStudentid(Long id, Pageable pageable);
}

