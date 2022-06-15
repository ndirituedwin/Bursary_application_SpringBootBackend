package com.ndirituedwin.Dto.Requests.HigherEducation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class HigherEducationCategoryRequest {


    @NotBlank(message = "category cannot be blank")
    @NotNull
    @NotEmpty
    private String category;
}
