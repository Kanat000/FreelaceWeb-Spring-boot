package com.example.crud_project.user;

import com.example.crud_project.registration.token.ConfirmToken;
import com.example.crud_project.registration.token.ConfirmTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE =
            "user with email %s not found";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmTokenService confirmTokenService;
    public User getUSerById(Long id){
        return userRepository.getById(id);
    }
    public void update(User user, UpdateUser updateUser){
        user.setFirstname(updateUser.getFirstname());
        user.setLastname(updateUser.getLastname());
        if(updateUser.getUsername() != null){
            user.setUsername(updateUser.getUsername());
        }
        if(updateUser.getTelegram() != null){
            user.setTelegram(updateUser.getTelegram());
        }
        userRepository.save(user);
    }
    public User getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }
    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .orElseThrow( ()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE,email)) );
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public String signUpUser(User user){
        boolean userExist = userRepository.findByEmail(user.getEmail())
                .isPresent();

        if (userExist){
            throw new IllegalStateException("email already taken");
        }
        String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodePassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        // TODO: sen conf  token
        ConfirmToken confirmToken = new ConfirmToken(
            token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                    user

        );

        confirmTokenService.saveConfirmationToken(
                confirmToken);

        //  TODO: SEND EMAIL


        return token;
    }

    public int enableUser(String email){
        return userRepository.enableUser(email);
    }
}
