package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Bursary.Secskuls.SecStudent;
import com.ndirituedwin.Entity.SecondarySchool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecStudentRepo extends JpaRepository<SecStudent,Long> {

    @Query("SELECT v FROM SecStudent v WHERE v.admissionnumber =:admissionnumber AND v.secondaryschool.id=:secondaryschool")
    boolean existsByAdmissionnumberAndSecondaryschooll(@Param("admissionnumber") String admissionnumber, @Param("secondaryschool") Long secondaryschool);

//    Boolean existsByAdmissionumberAndSecondaryschool(String admissionnumber, Long secondaryschool);

    Boolean existsByAdmissionnumberAndSecondaryschool(String admissionnumber,SecondarySchool secondarySchool);
    long countByAdmissionnumberAndSecondaryschool(String admissionnumber,SecondarySchool secondarySchool);

    Page<SecStudent> findAllBySecondaryschool(Pageable pageable, SecondarySchool secondarySchool);


    @Query("SELECT v FROM SecStudent v where  v.secondaryschool.id in :secoids")
    Page<SecStudent> findBySecondaryschoolIn(List<Long> secoids, Pageable pageable);

    Optional<SecStudent> findByAdmissionnumberAndSecondaryschool(String admissionnumber, SecondarySchool secondarySchool);
}
