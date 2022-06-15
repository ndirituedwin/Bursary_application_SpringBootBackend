package com.ndirituedwin.Dto.Requests.HigherEducation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class HigherEducationRequest {


    @NotNull(message = "name may not be null")
    @NotBlank(message = "name may not be bank")
    private String name;
    @NotNull(message = "Category may not be null")
    private Long higheEducationCategoryId;
}
