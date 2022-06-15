package com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties;

import com.ndirituedwin.Entity.County;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCountyRequest {

    @NotNull
    private String subcounty;
    @NotNull
    private Long county_code;
    private Long id;
}
