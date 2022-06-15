package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.SubCounty;
import com.ndirituedwin.Entity.Ward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface WardRepository extends JpaRepository<Ward,Long>{

//    @Query("SELECT COUNT(S.id) from SubCounty S where S.county.code = :county_code AND S.subcounty=:subcounty")
    @Query("SELECT COUNT (w.id) from Ward w where w.ward=:ward AND w.subcounty.id=:subcountyId")
    long countBySubCountyIdAndWard(String ward, Long subcountyId);

    Page<Ward> findAllBySubcounty(SubCounty subCounty, Pageable pageable);
}
