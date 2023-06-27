package codekoi.apiserver.domain.auth.controller;

import codekoi.apiserver.domain.auth.service.UserTokenCommand;
import codekoi.apiserver.domain.auth.service.UserTokenQuery;
import codekoi.apiserver.domain.user.service.UserQuery;
import codekoi.apiserver.global.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith({RestDocumentationExtension.class, MockitoExtension.class})
@WebMvcTest(controllers = {AuthController.class})
public abstract class AuthControllerTestSupport {

    @MockBean
    protected UserQuery userQuery;
    @MockBean
    protected UserTokenQuery userTokenQuery;
    @MockBean
    protected UserTokenCommand userTokenCommand;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    protected MockMvc mvc;

    protected String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODYxMTk1NjN9.ZmNReTkQ0pZMXBsdaNDuri5xFQiSYEMYPggt3zj6P-k";
    protected String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODY3MjQwNjN9.3elRxRRR4Moa6U5TLHd2lC0yvN6TLiLu7on37Kadb2o";

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.mvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .addFilter(new CharacterEncodingFilter("UTF-8", true))
                        .apply(documentationConfiguration(provider)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint()))
                        .alwaysDo(print())
                        .build();
    }
}
