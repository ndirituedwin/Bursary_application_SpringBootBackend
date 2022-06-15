package com.ndirituedwin.Dto.Responses.SecSchool;

import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import com.ndirituedwin.Entity.SecondarySchool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Allbursaryapplications {

    private Long id;
    private String admissionnumber;
    private String fullname;
    private String formorclas;
    private String applicationyear;
    private String applicationmonth;
    private SecondarySchool secondaryschool;
//    private String secondaryschool;
    private StudentParent studentparent;
//    private String phonenumber;
//    private String county;
//    private String ward;
    private boolean isapproved;
    private Long secStudentId;
}
