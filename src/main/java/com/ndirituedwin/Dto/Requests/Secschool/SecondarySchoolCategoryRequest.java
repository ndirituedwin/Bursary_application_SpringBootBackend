package com.ndirituedwin.Dto.Requests.Secschool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecondarySchoolCategoryRequest {


    @NotBlank
    private String category;
}
