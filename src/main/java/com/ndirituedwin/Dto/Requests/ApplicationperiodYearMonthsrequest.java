package com.ndirituedwin.Dto.Requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationperiodYearMonthsrequest {

 @NotNull(message = "year may not be null")
 @NotBlank(message = "year may not be blank")
 private String year;
}
