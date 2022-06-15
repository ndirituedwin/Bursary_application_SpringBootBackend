package com.ndirituedwin.Dto.Requests.HigherEducation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckhigherStudentByadmnoandschool {
    private long schoolId;
    private String studentAdmno;

    private boolean flag=false;
    private String fullname;
    private Long idnumber;
    private String voterscard;
    private int yearofadmission;
    private String phonenumber;
    private String durationofcourse;
}
