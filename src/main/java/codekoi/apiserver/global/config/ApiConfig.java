package codekoi.apiserver.global.config;

import codekoi.apiserver.global.token.AuthenticationPrincipalArgumentResolver;
import codekoi.apiserver.global.token.JwtTokenProvider;
import codekoi.apiserver.global.token.NullablePrincipalArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApiConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider));
        resolvers.add(new NullablePrincipalArgumentResolver(jwtTokenProvider));
    }
}
