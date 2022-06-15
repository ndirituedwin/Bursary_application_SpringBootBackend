package com.ndirituedwin.Dto.Responses.Wardssubountiesandcounties;

import com.ndirituedwin.Entity.SubCounty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WardResponse {

    private String ward;
    private SubCounty subCounty;
    private String message;
}
