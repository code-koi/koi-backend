package codekoi.apiserver.utils;

import codekoi.apiserver.global.token.JwtTokenProvider;
import codekoi.apiserver.domain.auth.controller.AuthController;
import codekoi.apiserver.domain.auth.service.AuthCommand;
import codekoi.apiserver.domain.auth.service.AuthQuery;
import codekoi.apiserver.domain.user.controller.UserApiController;
import codekoi.apiserver.domain.user.service.UserQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        UserApiController.class,
        AuthController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected UserQuery userQuery;

    @MockBean
    protected AuthQuery authQuery;
    @MockBean
    protected AuthCommand authCommand;



}
