package com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WardRequest {

//    @NotBlank
//    private String ward;
//    @NotNull
//    private Long subcountyId;
//
    @NotNull
    private String ward;
    @NotNull
    private Long subcountyId;
}
