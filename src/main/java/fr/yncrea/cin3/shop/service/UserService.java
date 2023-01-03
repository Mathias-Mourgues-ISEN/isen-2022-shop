package fr.yncrea.cin3.shop.service;

import fr.yncrea.cin3.shop.form.RegisterForm;
import fr.yncrea.cin3.shop.model.User;
import fr.yncrea.cin3.shop.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    private PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Transactional
    public void createAccount(RegisterForm form) {
        var user = new User();
        user.setName(form.getName());
        user.setFirstname(form.getFirstname());
        user.setUsername(form.getUsername());

        var hashedPassword = encoder.encode(form.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }
}
