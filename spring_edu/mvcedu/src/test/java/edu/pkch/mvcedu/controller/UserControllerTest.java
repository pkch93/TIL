package edu.pkch.mvcedu.controller;

import edu.pkch.mvcedu.api.user.domain.User;
import edu.pkch.mvcedu.api.user.repository.UserRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String TEST_URI = "/api/users/";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    User testUser;

    @BeforeEach
    void setUp() {
        User user = new User("pkch@woowa.com", "qwerqwer", "pkch", 27);
        testUser = userRepository.save(user);
    }

    @Test
    @DisplayName("1번 유저 조회 테스트")
    void userReadTest() throws Exception {
        mockMvc.perform(get(TEST_URI + testUser.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.age").value(testUser.getAge()));
    }

    @Test
    @DisplayName("DB에 저장되어 있지 않은 2번 유저 조회시 예외 처리")
    void userReadByNoneRegisteredId() throws Exception {
        mockMvc.perform((get(TEST_URI + 2)))
                .andDo(print());
    }
}
