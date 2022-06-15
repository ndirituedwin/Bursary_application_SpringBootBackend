package com.ndirituedwin.Dto.Requests.Secschool;

import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import com.ndirituedwin.Entity.SecondarySchool;
import com.ndirituedwin.Entity.Ward;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bursaryapplicationrequest {

    //parent details
    @NotNull
    private String parentidnumber;
    @NotBlank
    private String parentfullname;

    @NotBlank
    private String parentphonenumber;

    private long parentward;

    //student details
    @NotBlank
    private String admissionnumber;

    @NotNull
    private String formorclass;
    @NotBlank
    private String fullname;
    @NotBlank
    private String applicationperiodyear;

    @NotBlank
    private String applicationperiodmonth;

    private Long secondarySchool;
//    private Long studentParent;




}
