package sk.loffay.wandera.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.loffay.wandera.model.User;

/**
 * @author Pavol Loffay
 */
public class InMemoryUserStorage implements UserStorage {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserStorage.class);


    private Map<String, User> userMap;


    public InMemoryUserStorage() {
        this.userMap = new HashMap<>();
        temporaryHack();
    }

    @Override
    public User getByName(String name) {
        User user = userMap.get(name);

        return user;
    }

    // todo
    private void temporaryHack() {

        // 1 notification
        User user2 = new User("99c6ac34-b017-4391-9ea7-c64d07263237", "user1", getHash("pass"));
        // 4 notifications
        User user3 = new User("286416b4-7eac-433c-876c-339dfd8bcd68", "user4", getHash("pass"));
        // 8 notifications
        User user1 = new User("315c0c1e-8c4c-458f-a37c-9b636d1fb80f", "user8", getHash("pass"));
        // 2 notifications
        User user4 = new User("79bcb260-7f75-49cf-b76d-41e1c2609055", "user2", getHash("pass"));

        userMap.put(user1.getName(), user1);
        userMap.put(user2.getName(), user2);
        userMap.put(user3.getName(), user3);
        userMap.put(user4.getName(), user4);
    }

    public static String getHash(String pass) {

        String generatedPassword = pass;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Unable to create MD5 hash of password");
        }

        return generatedPassword;
    }
}
