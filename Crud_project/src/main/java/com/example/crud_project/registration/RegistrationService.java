package com.example.crud_project.registration;

import com.example.crud_project.registration.token.ConfirmToken;
import com.example.crud_project.registration.token.ConfirmTokenService;
import com.example.crud_project.user.User;
import com.example.crud_project.user.UserRole;
import com.example.crud_project.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private final EmailValidator emailValidator;
    private final ConfirmTokenService confirmTokenService;

    public String register(User user) {
        boolean isValidEmail = emailValidator.test(user.getEmail());
        if (!isValidEmail ){
            throw new IllegalStateException("email not valid");
        }


        String token = userService.signUpUser(
                user
        );

        return token;
    }

    @Transactional
    public void confirmToken(String token){
        ConfirmToken confirmToken = confirmTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmToken.getUser().getEmail());




    }
}
