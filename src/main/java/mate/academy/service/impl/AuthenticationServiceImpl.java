package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userFromDB = userService.findByEmail(email);
        if (userFromDB.isEmpty() || !userFromDB.get()
                .getPassword().equals(HashUtil.hashPassword(password,
                        userFromDB.get().getSalt()))) {
            throw new AuthenticationException("Can't authenticate user. "
                    + "Login or password are wrong", null);
        }
        return userFromDB.get();
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        if (email.isEmpty() || password.isEmpty()) {
            throw new RegistrationException("Email or password is incorrect", null);
        }
        if (userService.findByEmail(email).isEmpty()) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            return userService.add(user);
        }
        throw new RegistrationException("User with this email already exist. "
                + "Email: " + email, null);
    }
}