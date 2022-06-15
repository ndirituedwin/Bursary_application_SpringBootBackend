package com.ndirituedwin.Dto.Responses.SecSchool;

import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import com.ndirituedwin.Entity.SecondarySchool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Viewbursaryapplications {



    private Long id;
    private String admissionnumber;
    private String fullname;
    private String formorclas;
    private SecondarySchool secondaryschool;
    private StudentParent studentparent;
    private Instant createdAt;
    private boolean isapproved;

//    private Long idnumber;
//    private String parentname;
//    private String phonenumber;
}
