package com.ndirituedwin.Dto.Requests.Secschool;

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
    private String formorclas;
    private String fullname;
    private Long id;
    private boolean isapproved;
    private Long secStudentId;
    private SecondarySchool secondaryschool;
    private boolean selected;
    private StudentParent studentparent;
}
