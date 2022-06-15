package com.ndirituedwin.Repository;

import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentParentRepo extends JpaRepository<StudentParent,Long> {
    boolean existsByPhonenumberOrIdnumber(String parentphonenumber, String parentidnumber);

    Optional<StudentParent> findByPhonenumberOrIdnumber(String parentphonenumber, String parentidnumber);


    StudentParent findByIdnumberContaining(String idnumber);

    StudentParent findByIdnumberLike(String idnumber);

    boolean existsByIdnumber(String idnumber);

    Optional<StudentParent> findByIdnumber(String idnumber);
}
