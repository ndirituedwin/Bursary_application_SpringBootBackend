package com.ndirituedwin.Dto.Requests.Secschool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardBursarybysecschoolCategoryrequest {

    private List<AwardbyseschoolCategory> bursarybysecschoolcategory;
    private BigDecimal amount;

}
