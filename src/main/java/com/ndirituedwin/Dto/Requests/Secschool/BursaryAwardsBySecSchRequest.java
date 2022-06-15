package com.ndirituedwin.Dto.Requests.Secschool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BursaryAwardsBySecSchRequest {

    private Long schoolId;
    private Long schoolCategoryId;
    private String year;
    private String month;

    private Long studentId;
}
