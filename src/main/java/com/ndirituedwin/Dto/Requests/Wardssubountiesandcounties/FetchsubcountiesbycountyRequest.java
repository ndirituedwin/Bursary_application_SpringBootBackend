package com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchsubcountiesbycountyRequest {

    @NotNull(message = "the code may not be null")
  private Long code;
}
