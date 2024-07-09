package org.yascode.security_jwt.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yascode.security_jwt.entity.User;
import org.yascode.security_jwt.repository.UserRepository;
import org.yascode.security_jwt.security.payload.request.ChangePasswordRequest;
import org.yascode.security_jwt.service.UserService;

import java.security.Principal;

@Service
public class UserServiceImpl implements UserService {
    private final Log logger = LogFactory.getLog(this.getClass());
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public boolean changePassword(ChangePasswordRequest changePasswordRequest, Principal connectedUser) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

            if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
                throw new IllegalStateException("Wrong password");
            }

            if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmationPassword())) {
                throw new IllegalStateException("Password are not the same");
            }

            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

            userRepository.save(user);

            return true;
        } catch (IllegalStateException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
