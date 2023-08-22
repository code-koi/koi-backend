package com.codekoi.apiserver.utils;

import com.codekoi.apiserver.auth.service.AuthService;
import com.codekoi.apiserver.comment.controller.CommentRestController;
import com.codekoi.apiserver.comment.service.ReviewCommentQueryService;
import com.codekoi.apiserver.like.controller.LikeRestController;
import com.codekoi.apiserver.like.service.LikeService;
import com.codekoi.apiserver.review.controller.CodeReviewRestController;
import com.codekoi.apiserver.review.service.CodeReviewQueryService;
import com.codekoi.apiserver.skill.review.service.CodeReviewSkillQueryService;
import com.codekoi.apiserver.skill.skill.controller.SkillRestController;
import com.codekoi.apiserver.skill.skill.service.SkillQueryService;
import com.codekoi.apiserver.user.controller.UserRestController;
import com.codekoi.apiserver.user.service.UserQueryService;
import com.codekoi.coreweb.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
        CommentRestController.class,
        LikeRestController.class,
        CodeReviewRestController.class,
        SkillRestController.class,
        UserRestController.class
})
public abstract class ControllerTest {

    protected MockMvc mvc;
    protected ObjectMapper mapper = new ObjectMapper();


    protected String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiaWF0IjoxNjgwMDA5MjYzLCJleHAiOjE2ODYxODE5NTYzfQ.P5gzo1eNYtoog5ZtNpuqpWLqQDu2zRH1Rcdt-u_QUtQ";
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

    @MockBean
    protected UserQueryService userQueryService;
    @MockBean
    protected AuthService authService;
    @MockBean
    protected ReviewCommentQueryService reviewCommentQueryService;
    @MockBean
    protected LikeService likeService;
    @MockBean
    protected CodeReviewQueryService codeReviewQueryService;
    @MockBean
    protected SkillQueryService skillQueryService;
    @MockBean
    protected CodeReviewSkillQueryService codeReviewSkillQueryService;
}