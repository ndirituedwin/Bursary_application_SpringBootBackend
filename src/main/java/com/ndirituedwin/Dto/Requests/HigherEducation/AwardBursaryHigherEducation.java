package com.ndirituedwin.Dto.Requests.HigherEducation;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AwardBursaryHigherEducation {

    private BigDecimal amount;
    private List<AwardRequest> awardrequest;
    @JsonValue
    private List<String> awardasindividual;
}
