package com.ndirituedwin.Dto.Responses.Auth;

import lombok.Data;

@Data
public class UserSummary {

    private Long id;
    private String surname;
    private String firstname;
    private String lastname;
    private String  username;
    private String  email;
    private String avatar;
//    private Set<Role> roles;

}
