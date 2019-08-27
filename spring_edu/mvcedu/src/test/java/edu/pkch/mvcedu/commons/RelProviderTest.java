package edu.pkch.mvcedu.commons;

import edu.pkch.mvcedu.domain.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.RelProvider;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RelProviderTest {

    @Autowired
    private RelProvider relProvider;

    @Test
    void user_클래스_rel_테스트() {
        String userRel = relProvider.getItemResourceRelFor(User.class);
        assertThat(userRel).isEqualTo("user");
    }

    @Test
    void userList_클래스_rel_테스트() {
        String userRel = relProvider.getCollectionResourceRelFor(User.class);
        assertThat(userRel).isEqualTo("userList");
    }
}
