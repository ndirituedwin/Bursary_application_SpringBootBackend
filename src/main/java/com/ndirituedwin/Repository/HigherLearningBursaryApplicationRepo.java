package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.ApplicationPeriod;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationn;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherLearningBursaryApplication;
import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherLearningStudent;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryApplication;
import com.ndirituedwin.Entity.SecondarySchool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HigherLearningBursaryApplicationRepo extends JpaRepository<HigherLearningBursaryApplication,Long> {
    boolean existsByHigherLearningStudentAndApplicationPeriod(HigherLearningStudent existinghigherLearningStudent, ApplicationPeriod applicationPeriod);

    Page<HigherLearningBursaryApplication> findAllByIsapprovedEqualsAndApplicationPeriodYearAndApplicationPeriodMonth(boolean b, String curYear, String curMonth, Pageable pageable);

    Page<HigherLearningBursaryApplication> findAllByIsapprovedEqualsAndApplicationPeriod(boolean b, ApplicationPeriod applicationPeriod, Pageable pageable);


    @Query("SELECT V FROM HigherLearningBursaryApplication V where V.higherLearningStudent.college.id in :secoids")
    Page<HigherLearningBursaryApplication> findByHigherEducationnIn(List<Long> secoids, Pageable pageable);



    @Query("SELECT V FROM HigherLearningBursaryApplication V  where V.higherLearningStudent.college =:secondarySchool")
    Page<HigherLearningBursaryApplication> findAllByHigherEducationn(HigherEducationn secondarySchool, Pageable pageable);
}
