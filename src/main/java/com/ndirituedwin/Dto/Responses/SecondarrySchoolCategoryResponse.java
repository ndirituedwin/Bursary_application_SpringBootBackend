package com.ndirituedwin.Dto.Responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecondarrySchoolCategoryResponse {


    private String category;
    private String message;
}
