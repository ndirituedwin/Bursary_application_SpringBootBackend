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
public class AwardRequest {

    private String admissionnumber;
    private String applicationmonth;
    private String applicationyear;
    private String durationofcourse;
    private String fullname;
    private HigherEducationn higherEducationn;
    private Long id;
    private Long idnumber;
    private boolean isapproved;
    private String  phonenumber;
//    private Long higherlearningStudentId;
    private boolean selected;
   private int yearofadmission;
}
