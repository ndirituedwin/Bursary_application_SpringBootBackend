package com.ndirituedwin.Dto.Requests.HigherEducation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HigherEducationApplRequest {
    @NotBlank
    private String applicationperiodyear;

    @NotBlank
    private String applicationperiodmonth;
    @NotBlank
    private String admissionnumber;
    @NotNull
    private Long school;

    @NotBlank
    private String fullname;

    @NotNull
    private Long idnumber;

    @NotBlank
    private String phonenumber;

    private String voterscard;
    @NotNull
    private int yearofadmission;

    @NotBlank
    @NotEmpty
    @NotNull
    private String durationofcourse;
}
