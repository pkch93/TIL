package edu.pkch.mvcedu.controller;

import edu.pkch.mvcedu.domain.user.domain.User;
import edu.pkch.mvcedu.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class UserApiControllerTest {

    private static final String TEST_URI = "/users";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    User testUser;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        User user = new User.Builder("pkch@woowa.com", "qwerqwer", "pkch", 27)
                .build();
        testUser = userRepository.save(user);
    }

    @Test
    @DisplayName("1번 유저 조회 테스트")
    void userReadTest() throws Exception {
        mockMvc.perform(get(TEST_URI + "/{userId}", testUser.getId()))
                .andDo(print())
                .andDo(document("users",
                       links(linkWithRel("self").description("해당 리소스 자원 링크")),
                        pathParameters(parameterWithName("userId").description("해당 리소스의 식별자")),
                        responseFields(
                                subsectionWithPath("_links").description("<<resources-users, User resource>>"),
                                fieldWithPath("username").description("해당 유저의 이름"),
                                fieldWithPath("email").description("해당 유저의 이메일"),
                                fieldWithPath("age").description("해당 유저의 나이"))
                ))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("DB에 저장되어 있지 않은 2번 유저 조회시 예외 처리")
    void userReadByNoneRegisteredId() throws Exception {
        mockMvc.perform((get(TEST_URI + 2)))
                .andDo(print());
    }
}
