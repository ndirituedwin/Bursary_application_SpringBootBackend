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
public class ViewAllWards {


 private Long id;
 private String ward;
 private SubCounty subCounty;
}
