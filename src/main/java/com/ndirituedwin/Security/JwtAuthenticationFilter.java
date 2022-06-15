package com.ndirituedwin.Security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private  CustomUserDetailsService customUserDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        try {
            String jwt=getJwtFromRequest(request);
             if (StringUtils.hasText(jwt)&& jwtTokenProvider.validateToken(jwt)){
             Long userId=jwtTokenProvider.getuserIdfromJwt(jwt);
             log.info("logging the userId from the jwt {}",userId);
                 UserDetails userDetails=customUserDetailsService.loadUserById(userId);
                 log.info("logging user details {}", userDetails);
                 UsernamePasswordAuthenticationToken  authentication=new UsernamePasswordAuthenticationToken(
                         userDetails,null,userDetails.getAuthorities());
                 log.info("logging authentication before{}",authentication);

                 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                 log.info("logging authentication {}",authentication);
                 SecurityContextHolder.getContext().setAuthentication(authentication);
                 log.info("logging security context {}",SecurityContextHolder.getContext().getAuthentication().getDetails());
             }
        }catch (Exception e){
            log.error("could not set authentication in the security context {}",e);
        }
        filterChain.doFilter(request,response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
      log.info("logging Httpservlet request {}",request);
      String bearerToken=request.getHeader("Authorization");
      log.info("logging bererToken {}",bearerToken);
      if (StringUtils.hasText(bearerToken)&& bearerToken.startsWith("Bearer")){
          log.info("logging jwt {}",bearerToken.substring(7));
          return bearerToken.substring(7,bearerToken.length());
      }
      return null;
    }
}
