package com.ndirituedwin.Dto.Responses;

import com.ndirituedwin.Entity.County;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class SecondarySchoolResponse {


    private Long code;
    private String school;
    private SecondarySchoolCategory category;
    private String type;
    private County county;
    private HttpStatus status;
    private String message;
    private boolean saved;
}
