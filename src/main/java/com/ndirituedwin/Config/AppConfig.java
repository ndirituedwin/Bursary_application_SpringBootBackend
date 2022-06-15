package com.ndirituedwin.Config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix="app")
@EnableAutoConfiguration
public class AppConfig {

    @NotNull
    private String url;
    @NotNull
    private Long verificationTokenExpirationInMs;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getVerificationTokenExpirationInMs() {
        return verificationTokenExpirationInMs;
    }

    public void setVerificationTokenExpirationInMs(Long verificationTokenExpirationInMs) {
        this.verificationTokenExpirationInMs = verificationTokenExpirationInMs;
    }
}
