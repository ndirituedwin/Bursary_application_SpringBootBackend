package com.ndirituedwin.Dto.Responses;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class Schooltyperesponse {

  private String type;
  private String message;
  private HttpStatus status;
}
