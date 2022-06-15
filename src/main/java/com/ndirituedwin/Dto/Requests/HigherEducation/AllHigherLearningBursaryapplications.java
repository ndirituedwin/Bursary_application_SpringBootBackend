package com.ndirituedwin.Dto.Requests.HigherEducation;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationn;
import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import com.ndirituedwin.Entity.SecondarySchool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllHigherLearningBursaryapplications {

    private Long id;
    private String admissionnumber;
    private String fullname;
    private String applicationyear;
    private String applicationmonth;
    private HigherEducationn higherEducationn;
    private String phonenumber;
    private Long idnumber;
    private int yearofadmission;
    private String durationofcourse;
    private boolean isapproved;
}
