package com.ndirituedwin.Dto.Requests.Secschool;

import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import com.ndirituedwin.Entity.SecondarySchool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardbyseschoolCategory {

    private Long id;
    private String admissionnumber;
    private String applicationmonth;
    private String applicationyear;
    private Instant createdAt;
    private String fullname;
    private SecondarySchool secondaryschool;
    private StudentParent studentparent;
    private String formorclas;
    private boolean isapproved;
    private long secStudentId;

}
