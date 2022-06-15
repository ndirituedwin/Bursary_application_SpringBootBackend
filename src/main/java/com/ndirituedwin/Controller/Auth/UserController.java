package com.ndirituedwin.Controller.Auth;

import com.ndirituedwin.Dto.Responses.Auth.UserSummary;
import com.ndirituedwin.Security.CurrentUser;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.Auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final AuthService authService;

    @GetMapping("/currentuser")
//    @PreAuthorize("hasRole('ADMIN')or hasRole('USER')or hasRole('SUPER_ADMIN')")
    public UserSummary getcurrentuser(@CurrentUser UserPrincipal userPrincipal){
        return authService.currentuser(userPrincipal);
    }

}
