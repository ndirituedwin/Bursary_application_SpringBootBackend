package com.ndirituedwin.Dto.Responses;

import com.ndirituedwin.Entity.Bursary.HigherLearning.HigherEducationnCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HigherEducationResponse {


    private String name;
    private HigherEducationnCategory higherEducationnCategory;
    private long id;
    private String message;

}
