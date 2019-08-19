package edu.pkch.mvcedu.domain.user.controller.api;

import edu.pkch.mvcedu.domain.user.service.UserService;
import edu.pkch.mvcedu.domain.user.service.dto.UserMyPageDto;
import edu.pkch.mvcedu.domain.user.service.dto.UserSignUpDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/users")
public class UserApiController {
    private static final String USER_BASE_URL = "/users";

    private final UserService userService;

    public UserApiController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserMyPageDto> readUser(@PathVariable Long userId) {
        UserMyPageDto userMyPageDto = userService.fetchUserBy(userId);

        return ResponseEntity.ok(userMyPageDto);
    }

    @PostMapping
    public RedirectView signUp(UserSignUpDto userSignUpDto) {
        userService.signUp(userSignUpDto);

        return new RedirectView(USER_BASE_URL);
    }
}
