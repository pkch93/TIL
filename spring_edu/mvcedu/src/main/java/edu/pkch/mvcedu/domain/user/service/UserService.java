package edu.pkch.mvcedu.domain.user.service;

import edu.pkch.mvcedu.domain.user.domain.User;
import edu.pkch.mvcedu.domain.user.domain.repository.UserRepository;
import edu.pkch.mvcedu.domain.user.exception.NotFoundUserException;
import edu.pkch.mvcedu.domain.user.service.dto.UserMyPageDto;
import edu.pkch.mvcedu.domain.user.service.dto.UserSignUpDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(final UserRepository userRepository, final ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public UserMyPageDto fetchUserBy(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);

        return modelMapper.map(user, UserMyPageDto.class);
    }

    public User signUp(final UserSignUpDto userSignUpDto) {
        User user = new User.Builder(userSignUpDto.getEmail(), userSignUpDto.getUsername(),
                userSignUpDto.getPassword(), userSignUpDto.getAge())
                .build();

        return userRepository.save(user);
    }
}