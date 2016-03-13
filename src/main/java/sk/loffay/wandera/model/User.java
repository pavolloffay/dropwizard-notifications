package sk.loffay.wandera.model;

import java.security.Principal;

/**
 * @author Pavol Loffay
 */
public class User implements Principal {

    private String guid;
    private String name;
    private String pass;

    public User(String guid, String name, String pass) {
        this.guid = guid;
        this.name = name;
        this.pass = pass;
    }

    public String getGuid() {
        return guid;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return !(guid != null ? !guid.equals(user.guid) : user.guid != null);
    }

    @Override
    public int hashCode() {
        return guid != null ? guid.hashCode() : 0;
    }
}
