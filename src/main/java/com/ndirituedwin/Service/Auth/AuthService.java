package com.ndirituedwin.Service.Auth;

import com.ndirituedwin.Config.AppConfig;
import com.ndirituedwin.Dto.Requests.Auth.LoginRequest;
import com.ndirituedwin.Dto.Requests.Auth.RefreshTokenRequest;
import com.ndirituedwin.Dto.Requests.Auth.SignupRequest;
import com.ndirituedwin.Dto.Responses.Auth.UserSummary;
import com.ndirituedwin.Dto.Responses.ApiResponse;
import com.ndirituedwin.Dto.Responses.Auth.JwtAuthenticationResponse;
import com.ndirituedwin.Entity.Auth.VerificationToken;
import com.ndirituedwin.Entity.Enum.RoleName;
import com.ndirituedwin.Entity.Auth.NotificationEmail;
import com.ndirituedwin.Entity.Auth.Role;
import com.ndirituedwin.Entity.Auth.User;
import com.ndirituedwin.Exception.AppException;
import com.ndirituedwin.Exception.VerificationTokenNotFoundException;
import com.ndirituedwin.Repository.RoleRepository;
import com.ndirituedwin.Repository.UserRepository;
import com.ndirituedwin.Repository.VerificationTokenRepository;
import com.ndirituedwin.Security.JwtTokenProvider;
import com.ndirituedwin.Security.UserPrincipal;
import com.ndirituedwin.Service.Mail.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.ndirituedwin.Config.Constants.ApiUtils.*;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AppConfig appConfig;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public JwtAuthenticationResponse signin(LoginRequest loginRequest) {

        User user=userRepository.findByUsernameOrEmail(loginRequest.getUsernameoremail(),loginRequest.getUsernameoremail()).orElseThrow(() -> new UsernameNotFoundException("user with provided deails not found"));
        log.info("loginrequest {}",loginRequest);
        if (!(user.getIsEnabled())){
            return JwtAuthenticationResponse.builder().message("you shold activate your account first !").build();
        }
        log.info("logging authentication manager {}", authenticationManager);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameoremail(),
                loginRequest.getPassword()
        ));
        log.info("logging authentication {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        log.info("logging authentiction after it is set {}", SecurityContextHolder.getContext().getAuthentication());
        return JwtAuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .tokenType(TOKEN_TYPE)
                .username(loginRequest.getUsernameoremail())
                .expirationTime(Instant.now().plusMillis(jwtTokenProvider.jwtExpirationInMs()))
                .message("login successfull")
                .build();
            }

    @Transactional
    public Object registeruser(SignupRequest signupRequest) {
        log.info("gfhhffhhffhfhfhfhf");
        System.out.println("oihukgyjthdrgeswzxcvbnj;mkkk");
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username " + signupRequest.getUsername() + " is already taken!",HttpStatus.BAD_REQUEST),
                    HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "email " + signupRequest.getEmail() + " has already been taken",HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        //check if the role is set
        Role role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("user role not set !"));

        //try to register user
        User user = new User();
        user.setSurname(signupRequest.getSurname());
        user.setFirstname(signupRequest.getFirstname());
        user.setLastname(signupRequest.getLastname());
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setRoles(Collections.singleton(role));
        user.setIsEnabled(false);
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        log.info("logging the user object before saving {}",user);
        try {
            User saveduser = userRepository.save(user);
            log.info("logging saved user object {}",saveduser);
            String vToken = generateverificationToken(saveduser);
            mailService.SendMail(new NotificationEmail(SUBJECT, user.getEmail(), BODY + "\n " + appConfig.getUrl() + "api/auth/accountverification/" + vToken));
            log.info("Logging the token before sending email to the user {} ", vToken);

            return new ResponseEntity<>(new ApiResponse(true, "user with username " + saveduser.getUsername() + " has been registered",HttpStatus.CREATED), HttpStatus.CREATED);

        } catch (Exception exception) {
            log.error("an error has occurred while trying to save the user {}", exception.getStackTrace());
            return new ResponseEntity(new ApiResponse(false, "An exception has occurred while trying to register user " + exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    private String generateverificationToken(User saveduser) {
        log.info("saved in user {}", saveduser);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(saveduser);
        verificationToken.setExpiryDate(Instant.now().plusMillis(appConfig.getVerificationTokenExpirationInMs()));
        verificationTokenRepository.save(verificationToken);
        log.info("loggig generated token {}", token);
        log.info("logging saved token object {}", verificationToken);

        return token;
    }


    public void verifyAccount(String vtoken) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(vtoken);
        verificationToken.orElseThrow(() -> new VerificationTokenNotFoundException("verification token not found"));
        log.info("checking what verification token.get contains {}", verificationToken.get());
        fetchuserandenable(verificationToken.get());

    }


    @Transactional
    private void fetchuserandenable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        log.info("logging the token user {}", verificationToken.getUser());
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username " + username + " not found"));
        user.setIsEnabled(true);
        userRepository.save(user);

    }

    public UserSummary currentuser(UserPrincipal userPrincipal) {
        log.info("logging current user {}",userPrincipal);
        UserSummary userSummary=new UserSummary();
        userSummary.setId(userPrincipal.getId());
        userSummary.setSurname(userPrincipal.getSurname());
        userSummary.setFirstname(userPrincipal.getFirstname());
        userSummary.setLastname(userPrincipal.getLastname());
        userSummary.setUsername(userPrincipal.getUsername());
        userSummary.setEmail(userPrincipal.getEmail());
        userSummary.setAvatar(userPrincipal.getAvatar());
        return userSummary;
    }
    public JwtAuthenticationResponse refreshToken(UserPrincipal currentUser, RefreshTokenRequest refreshTokenRequest) {
       log.info("logging currentUser inside refreshToken method {}",currentUser);
        refreshTokenService.validateRefreshtoken(refreshTokenRequest.getRefreshToken());
        String token=jwtTokenProvider.generatetokenwithusername(currentUser.getId());
        log.info("logging token in refreshToken function {}",token);
        return JwtAuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .tokenType(TOKEN_TYPE)
                .expirationTime(Instant.now().plusMillis(jwtTokenProvider.jwtExpirationInMs()))
                .username(currentUser.getUsername())
                .message("Log in successful!")
                .build();
    }
}
