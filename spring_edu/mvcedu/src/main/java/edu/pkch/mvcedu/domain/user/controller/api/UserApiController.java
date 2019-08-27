package edu.pkch.mvcedu.domain.user.controller.api;

import edu.pkch.mvcedu.domain.user.domain.User;
import edu.pkch.mvcedu.domain.user.service.UserService;
import edu.pkch.mvcedu.domain.user.service.dto.UserResponse;
import edu.pkch.mvcedu.domain.user.service.dto.UserSignUpDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserApiController {
    private static final URI NONE_LOCATION = null;

    private final UserService userService;

    public UserApiController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> readUser(@PathVariable Long userId) {
        UserResponse userResponse = userService.fetchUserBy(userId);
        userResponse.add(linkTo(methodOn(UserApiController.class).readUser(userId)).withSelfRel());

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<User> signUp(UserSignUpDto userSignUpDto) {
        User user = userService.signUp(userSignUpDto);

        return ResponseEntity.created(NONE_LOCATION).body(user);
    }
}
