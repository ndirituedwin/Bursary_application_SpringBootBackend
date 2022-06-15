package com.ndirituedwin.Dto.Responses.SecSchool;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherLearningBursaryForm;
import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryFormSecskuls;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class AwardBursaryResponse {

    private HttpStatus status;
    private BursaryFormSecskuls bursaryFormSecskuls;
    private HigherLearningBursaryForm higherLearningBursaryForm;
    private String message;
    private String amountmaynotbenull;
}
