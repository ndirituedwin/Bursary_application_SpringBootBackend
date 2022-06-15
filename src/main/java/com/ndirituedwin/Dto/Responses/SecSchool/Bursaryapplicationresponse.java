package com.ndirituedwin.Dto.Responses.SecSchool;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class Bursaryapplicationresponse {


//  private Double amount;
  private HttpStatus status;
  private String message;
  private boolean issuccess;
}
