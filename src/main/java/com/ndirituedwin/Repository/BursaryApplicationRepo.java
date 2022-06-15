package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.ApplicationPeriod;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryApplication;
import com.ndirituedwin.Entity.Bursary.Secskuls.SecStudent;
import com.ndirituedwin.Entity.SecondarySchool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BursaryApplicationRepo extends JpaRepository<BursaryApplication,Long> {
//    boolean existsBySecStudentAndApplicationYearAndApplicationMonth(SecStudent savedsecstudent, String applicationperiodyear, String applicationperiodmonth);

//
//    @Query("SELECT v FROM SecStudent v where  v.secondaryschool.id in :secoids")
//    Page<SecStudent> findBySecondaryschoolIn(List<Long> secoids, Pageable pageable);

    @Query("SELECT V FROM BursaryApplication V where V.secStudent.secondaryschool.id in :secoids")
    Page<BursaryApplication> findBySecondarySchoolIn(List<Long> secoids, Pageable pageable);

    @Query("SELECT V FROM BursaryApplication V  where V.secStudent.secondaryschool =:secondarySchool")
    Page<BursaryApplication> findAllBySecondarySchool(SecondarySchool secondarySchool, Pageable pageable);


    boolean existsBySecStudentAndApplicationPeriod(SecStudent existingstudent, ApplicationPeriod applicationPeriod);


    Page<BursaryApplication> findAllByApplicationPeriodYearAndApplicationPeriodMonth(String curYear, String curMonth, Pageable pageable);

    Page<BursaryApplication> findAllByApplicationPeriod(ApplicationPeriod applicationPeriod, Pageable pageable);

    Page<BursaryApplication> findAllByIsapprovedEqualsAndApplicationPeriodYearAndApplicationPeriodMonth(boolean b, String curYear, String curMonth, Pageable pageable);

    Page<BursaryApplication> findAllByIsapprovedEqualsAndApplicationPeriod(boolean b, ApplicationPeriod applicationPeriod, Pageable pageable);
//    boolean existsBySecStudentAndApplicationYearAndApplicationMonth(Long id, String applicationperiodyear, String applicationperiodmonth);
}
