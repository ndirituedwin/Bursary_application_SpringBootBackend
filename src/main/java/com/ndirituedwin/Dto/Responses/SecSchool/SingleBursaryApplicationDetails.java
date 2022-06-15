package com.ndirituedwin.Dto.Responses.SecSchool;

import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import com.ndirituedwin.Entity.County;
import com.ndirituedwin.Entity.SchoolType;
import com.ndirituedwin.Entity.SecondarySchool;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleBursaryApplicationDetails {


    private String admissionnumber;
    private Long studentId;
    private String fullname;
    private String applicationYear;
    private String applicationMonth;
    private SecondarySchool school;
    private StudentParent studentParent;
    private String  formorclass;
}
