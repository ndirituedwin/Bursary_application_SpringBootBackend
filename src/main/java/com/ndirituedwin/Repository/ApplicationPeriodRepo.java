package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.ApplicationPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ApplicationPeriodRepo extends JpaRepository<ApplicationPeriod,Long> {

    boolean existsByYearAndMonth(String year, String month);

    List<ApplicationPeriod> findAllByIsOpenEquals(boolean b);

    List<ApplicationPeriod> findAllByYearAndIsOpenEquals(String year, boolean b);

    Optional<ApplicationPeriod> findByYearAndMonth(String applicationYear, String applicationMonth);

    List<ApplicationPeriod> findAllByYear(String year);

    Optional<ApplicationPeriod> findByIsOpenEquals(boolean b);

    boolean existsByIsOpenEquals(boolean b);

    Boolean existsByIdAndIsOpenEquals(Long id, boolean b);

    @Query("SELECT V FROM ApplicationPeriod V ORDER BY createdAt DESC")
    List<ApplicationPeriod> findAllByCreatedAtDesc();






}
