package edu.pkch.mvcedu.api.user.controller;

import edu.pkch.mvcedu.api.user.dto.UserMyPageDto;
import edu.pkch.mvcedu.api.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserMyPageDto> readUser(@PathVariable Long userId) {
        UserMyPageDto userMyPageDto = userService.fetchUserBy(userId);

        return new ResponseEntity(userMyPageDto, HttpStatus.OK);
    }
}
