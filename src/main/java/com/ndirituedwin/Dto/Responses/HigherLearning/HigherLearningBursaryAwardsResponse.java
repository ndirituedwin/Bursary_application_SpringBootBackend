package com.ndirituedwin.Dto.Responses.HigherLearning;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HigherLearningBursaryAwardsResponse {
    private String admissionnumber;
    private String applicationmonth;
    private String applicationyear;
    private String durationofcourse;
    private String fullname;
    private HigherEducationn higherEducationn;
    private String college;
    private Long id;
    private Long idnumber;
    private boolean isapproved;
    private String  phonenumber;
    private int yearofadmission;
    private BigDecimal amount;


}
