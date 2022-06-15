package com.ndirituedwin.Dto.Responses.SecSchool;

import com.ndirituedwin.Entity.County;
import com.ndirituedwin.Entity.SchoolType;
import com.ndirituedwin.Entity.SecondarySchool;
import com.ndirituedwin.Entity.SecondarySchoolCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolResponse {

    private Long id;
    private Long code;
    private String school;
    private SecondarySchoolCategory category;
    private SchoolType type;
    private County county;
}
