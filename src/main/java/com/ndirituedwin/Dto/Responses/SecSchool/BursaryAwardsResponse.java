package com.ndirituedwin.Dto.Responses.SecSchool;

import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import com.ndirituedwin.Entity.County;
import com.ndirituedwin.Entity.SecondarySchool;
import com.ndirituedwin.Entity.Ward;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BursaryAwardsResponse {

    private long id;
    private String fullname;
    private String admno;
    private String formorclass;
    private String secondarySchool;
    private String secondaryschoolcategory;
    private String schoollocation;
    private long schoollocationcountycode;
    private String applicationYear;
    private String applicationMonth;
    private String  studentParentname;
    private String  studentParentidnumber;
    private String  studentParentphonenumber;
    private String studentparentcounty;
    private long studentparentcountycode;
    private String studentparentward;
    private BigDecimal amount;
}
