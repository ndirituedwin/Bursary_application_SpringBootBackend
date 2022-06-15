package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.County;
import com.ndirituedwin.Entity.SubCounty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubCountyRepository extends JpaRepository<SubCounty,Long> {
//    SubCounty findByCountyAndsubcounty(Long county_code, String subcounty);
     Optional<SubCounty> findById(Long id);
    @Query("SELECT COUNT(S.id) from SubCounty S where S.county.code = :county_code AND S.subcounty=:subcounty")
    long countByCountyAndSubcounty(@Param("county_code") Long county_code, @Param("subcounty") String subcounty);

    Page<SubCounty> findAllByCounty(County county, Pageable pageable);
}
