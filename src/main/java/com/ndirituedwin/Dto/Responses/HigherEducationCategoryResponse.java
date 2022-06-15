package com.ndirituedwin.Dto.Responses;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class HigherEducationCategoryResponse {


    private String category;
    private HttpStatus httpStatus;
    private String message;
}
