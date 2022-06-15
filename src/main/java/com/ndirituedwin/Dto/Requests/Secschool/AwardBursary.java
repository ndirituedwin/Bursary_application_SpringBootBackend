package com.ndirituedwin.Dto.Requests.Secschool;

import com.fasterxml.jackson.annotation.JsonValue;
import com.ndirituedwin.Entity.Bursary.Secskuls.StudentParent;
import com.ndirituedwin.Entity.SecondarySchool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardBursary {
    private BigDecimal amount;
    private List<AwardRequest> awardrequest;
    @JsonValue
    private List<String> awardasindividual;
}

