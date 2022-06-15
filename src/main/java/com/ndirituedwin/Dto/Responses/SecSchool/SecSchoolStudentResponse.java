package com.ndirituedwin.Dto.Responses.SecSchool;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationn;
import com.ndirituedwin.Entity.SecondarySchool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SecSchoolStudentResponse {

    private Long id;
    private String admissionnumber;
    private String fullname;
    private SecondarySchool secondaryschool;
    private HigherEducationn higherEducationn;
}
