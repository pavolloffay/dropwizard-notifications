package sk.loffay.wandera.dao;

import sk.loffay.wandera.model.User;

/**
 * @author Pavol Loffay
 */
public interface UserStorage {

    User getByName(String name);
}
