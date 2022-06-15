package com.ndirituedwin.Controller.Auth;

import com.ndirituedwin.Dto.Requests.Auth.LoginRequest;
import com.ndirituedwin.Dto.Requests.Auth.RefreshTokenRequest;
import com.ndirituedwin.Dto.Requests.Auth.SignupRequest;
import com.ndirituedwin.Dto.Responses.Auth.JwtAuthenticationResponse;
import com.ndirituedwin.Dto.Responses.Auth.LogoutResponse;
import com.ndirituedwin.Repository.RefreshTokenRepository;
import com.ndirituedwin.Security.CurrentUser;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.Auth.AuthService;
import com.ndirituedwin.Service.Auth.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshToken;


    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateuser(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.signin(loginRequest));
    }
    @PostMapping("/register")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest, BindingResult result){

        return new ResponseEntity<>(authService.registeruser(signupRequest),HttpStatus.CREATED);
    }
    @GetMapping("/accountverification/{vtoken}")
    public ResponseEntity<String> accountverification(@PathVariable("vtoken") String vtoken){

        authService.verifyAccount(vtoken);
        return new ResponseEntity("Account activation successful",HttpStatus.OK);    }

    @PostMapping("/refreshToken")
    public JwtAuthenticationResponse authenticationResponse(@CurrentUser UserPrincipal currentUser,@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(currentUser,refreshTokenRequest);
    }
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logoutanddeleterefreshtoken(@CurrentUser UserPrincipal currentUser, @Valid @RequestBody  RefreshTokenRequest refreshTokenRequest){

        return  ResponseEntity.ok(refreshToken.deleteToken(currentUser,refreshTokenRequest.getRefreshToken()));
    }
}
