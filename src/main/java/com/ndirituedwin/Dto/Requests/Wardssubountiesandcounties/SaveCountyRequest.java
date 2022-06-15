package com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveCountyRequest {


  @NotNull
  @NotBlank
 private String county;

  @NotNull
  private Long code;
  @NotBlank
  @NotNull
    private String region;
    @NotBlank
    @NotNull
    private String  area;
    @NotBlank
    @NotNull
    private String capital;
}
