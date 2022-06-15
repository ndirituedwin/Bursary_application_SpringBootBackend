package com.ndirituedwin.Dto.Requests.Secschool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetcBursaryApplicationsBy {


 @NotBlank(message = "year may not be blank")
 @NotNull(message = "Year may not be null" )
 private String year;

 @NotBlank(message = "month may not be blank")
 @NotNull(message = "month may not be null" )
 private String month;
}
