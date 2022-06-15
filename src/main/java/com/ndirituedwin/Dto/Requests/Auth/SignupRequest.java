package com.ndirituedwin.Dto.Requests.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    private String surname;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String password;

}
