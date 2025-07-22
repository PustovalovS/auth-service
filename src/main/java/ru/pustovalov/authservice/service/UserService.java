package ru.pustovalov.authservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.pustovalov.authservice.entity.UserEntity;
import ru.pustovalov.authservice.exception.LoginException;
import ru.pustovalov.authservice.exception.RegistrationException;
import ru.pustovalov.authservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void register(String login, String password) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new RegistrationException("User with id " + login + " already register");
        }

        val hash = BCrypt.hashpw(password, BCrypt.gensalt());
        userRepository.save(new UserEntity(login, hash));
    }

    public void checkCredentials(String login, String password) {
        val optionalUserEntity = userRepository.findByLogin(login);

        if (optionalUserEntity.isEmpty()) {
            throw new LoginException("User with id " + login + " not found");
        }

        val entity = optionalUserEntity.get();

        if (!BCrypt.checkpw(password, entity.getHash())) {
            throw new LoginException("Wrong password");
        }
    }
}
