package com.ndirituedwin.Dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationPeriodRequest {

    @NotBlank(message = "year may not be blank")
    @NotNull(message = "year may not be null")
    private String year;

    @NotBlank(message = "month may not be blank")
    @NotNull(message = "month may not be null")
    private String month;
}
