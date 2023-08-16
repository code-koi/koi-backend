package com.codekoi.apiserver.config;

import com.codekoi.coreweb.jwt.AuthenticationPrincipalArgumentResolver;
import com.codekoi.coreweb.jwt.JwtTokenProvider;
import com.codekoi.coreweb.jwt.NullablePrincipalArgumentResolver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class ApiConfig implements WebMvcConfigurer {

    @PostConstruct
    public void setTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider));
        resolvers.add(new NullablePrincipalArgumentResolver(jwtTokenProvider));
    }
}
