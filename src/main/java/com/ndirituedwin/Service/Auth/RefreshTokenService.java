   package com.ndirituedwin.Service.Auth;


import com.ndirituedwin.Dto.Responses.Auth.LogoutResponse;
import com.ndirituedwin.Entity.Auth.RefreshToken;
import com.ndirituedwin.Exception.AppException;
import com.ndirituedwin.Repository.RefreshTokenRepository;
import com.ndirituedwin.Security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class RefreshTokenService {
 private final RefreshTokenRepository refreshTokenRepository;
    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken =new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());
       return refreshTokenRepository.save(refreshToken);
    }

    public void validateRefreshtoken(String token){
        refreshTokenRepository.findByToken(token).orElseThrow(() -> new AppException("token not found"));
    }
   public LogoutResponse deleteToken(UserPrincipal currentUser, String token){
        log.info("logging passed token {}",token);
        String c=currentUser.getUsername();
        log.info("logging currentUser inside deleteToken {}",currentUser);
        if (c.isEmpty()){
        }
       try{
           refreshTokenRepository.deleteByToken(token);
           return LogoutResponse.builder()
                   .message("Logged out successfully")
                   .build();

          }catch(Exception e)
       {
        throw new RuntimeException("an exception has occurred while logiig out the user "+e.getMessage());
       }
   }
}
