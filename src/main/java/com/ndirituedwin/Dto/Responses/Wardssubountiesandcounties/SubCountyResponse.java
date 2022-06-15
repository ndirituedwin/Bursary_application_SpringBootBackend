package com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties;

import com.ndirituedwin.Entity.County;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCountyResponse {

    private Long id;
    private String subcounty;
    private County county;
}
