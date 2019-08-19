package edu.pkch.mvcedu.controller;

import edu.pkch.mvcedu.domain.user.domain.User;
import edu.pkch.mvcedu.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserApiControllerTest {

    private static final String TEST_URI = "/users/";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    User testUser;

    @BeforeEach
    void setUp() {
        User user = new User.Builder("pkch@woowa.com", "qwerqwer", "pkch", 27)
                .build();
        testUser = userRepository.save(user);
    }

    @Test
    @DisplayName("1번 유저 조회 테스트")
    void userReadTest() throws Exception {
        mockMvc.perform(get(TEST_URI + testUser.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("DB에 저장되어 있지 않은 2번 유저 조회시 예외 처리")
    void userReadByNoneRegisteredId() throws Exception {
        mockMvc.perform((get(TEST_URI + 2)))
                .andDo(print());
    }
}
