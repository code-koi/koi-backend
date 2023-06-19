package codekoi.apiserver.global.config;

import codekoi.apiserver.global.token.AuthenticationPrincipalArgumentResolver;
import codekoi.apiserver.global.token.JwtTokenProvider;
import codekoi.apiserver.global.token.NullablePrincipalArgumentResolver;
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
