package com.ndirituedwin.Dto.Requests.Secschool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchSecSchoolByIdRequest {

    @NotNull(message = "school Id may not be null")
    private Long schoolId;
}
