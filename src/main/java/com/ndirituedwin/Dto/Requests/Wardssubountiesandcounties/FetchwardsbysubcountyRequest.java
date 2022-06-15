package com.ndirituedwin.Dto.Requests.Wardssubountiesandcounties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchwardsbysubcountyRequest {

    @NotNull(message = "subcounty id may not be null")
    private Long id;
}
