package edu.pkch.mvcedu.api.user.service;

import edu.pkch.mvcedu.api.user.domain.User;
import edu.pkch.mvcedu.api.user.dto.UserMyPageDto;
import edu.pkch.mvcedu.api.user.exception.NotExistUserException;
import edu.pkch.mvcedu.api.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(final UserRepository userRepository, final ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserMyPageDto fetchUserBy(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotExistUserException::new);

        return modelMapper.map(user, UserMyPageDto.class);
    }
}
