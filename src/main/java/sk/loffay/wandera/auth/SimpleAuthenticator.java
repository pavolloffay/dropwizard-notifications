package sk.loffay.wandera.auth;


import com.google.common.base.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import sk.loffay.wandera.dao.InMemoryUserStorage;
import sk.loffay.wandera.dao.UserStorage;
import sk.loffay.wandera.model.User;

/**
 * @author Pavol Loffay
 */
public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {

    private UserStorage userStorage;


    public SimpleAuthenticator(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {

        User user = userStorage.getByName(credentials.getUsername());
        if (user == null) {
            return Optional.absent();
        }

        if (user.getPass().equals(InMemoryUserStorage.getHash(credentials.getPassword()))) {
            return Optional.of(user);
        }

        return Optional.absent();
    }
}
