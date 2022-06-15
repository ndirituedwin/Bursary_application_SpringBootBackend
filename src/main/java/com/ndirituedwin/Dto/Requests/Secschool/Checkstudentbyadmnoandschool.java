package com.ndirituedwin.Dto.Requests.Secschool;

import com.ndirituedwin.Entity.Bursary.Secskuls.SecStudent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Checkstudentbyadmnoandschool {

 private long schoolId;
 private String studentAdmno;

 private String  fullname;
}
