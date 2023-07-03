package codekoi.apiserver.utils;

import codekoi.apiserver.domain.auth.controller.AuthController;
import codekoi.apiserver.domain.auth.service.UserTokenCommand;
import codekoi.apiserver.domain.auth.service.UserTokenQuery;
import codekoi.apiserver.domain.code.comment.controller.CodeCommentController;
import codekoi.apiserver.domain.code.comment.service.CodeCommentQuery;
import codekoi.apiserver.domain.code.like.service.LikeCommand;
import codekoi.apiserver.domain.code.review.controller.CodeReviewController;
import codekoi.apiserver.domain.code.review.service.CodeReviewQuery;
import codekoi.apiserver.domain.skill.controller.SkillController;
import codekoi.apiserver.domain.skill.service.SkillQuery;
import codekoi.apiserver.domain.user.controller.UserController;
import codekoi.apiserver.domain.user.service.UserQuery;
import codekoi.apiserver.global.token.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
@Import(JwtTokenProvider.class)
@WebMvcTest(controllers = {
        AuthController.class,
        UserController.class,
        CodeReviewController.class,
        CodeCommentController.class,
        SkillController.class
})
public abstract class ControllerTest {

    protected MockMvc mvc;
    protected ObjectMapper mapper = new ObjectMapper();

    protected String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNjgwMDA5MjYzLCJleHAiOjE2ODYxODE5NTYzfQ.P5gzo1eNYtoog5ZtNpuqpWLqQDu2zRH1Rcdt-u_QUtQ";
    protected String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiaWF0IjoxNjg2MTE5MjYzLCJleHAiOjE2ODY3MjQwNjN9.3elRxRRR4Moa6U5TLHd2lC0yvN6TLiLu7on37Kadb2o";

    @MockBean
    protected UserQuery userQuery;
    @MockBean
    protected UserTokenQuery userTokenQuery;
    @MockBean
    protected UserTokenCommand userTokenCommand;

    @MockBean
    protected CodeReviewQuery codeReviewQuery;

    @MockBean
    protected CodeCommentQuery codeCommentQuery;

    @MockBean
    protected LikeCommand likeCommand;

    @MockBean
    protected SkillQuery skillQuery;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

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
