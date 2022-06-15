package com.ndirituedwin.Dto.Requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationPeriodStatusrequest {


    @NotNull(message = "id may not be null")
    private Long id;

//    private String closeexisting;
}
