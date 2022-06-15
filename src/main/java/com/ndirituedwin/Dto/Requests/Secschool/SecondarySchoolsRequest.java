package com.ndirituedwin.Dto.Requests.Secschool;

import com.ndirituedwin.Entity.County;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SecondarySchoolsRequest {

    @NotNull(message = "school code may not be null")
    private Long code;

    @NotNull(message = "school is required")
    @NotBlank(message = "school may not be null")
    private String school;

    @NotNull(message = "school category is required")
    private Long categoryId;

    @NotNull(message = "school type is required")
    private Long typeId;
    @NotNull(message = "county is required")
    private Long countyCode;


}
