package com.ndirituedwin.Dto.Requests.Secschool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Awardbursaryrequest {

    @NotNull
    private Long applicationId;
    @NotNull
    private Long studentId;
    @NotNull
    private Long schoolId;
    @NotNull(message = "amount may not be null")
   private BigDecimal amount;


}
