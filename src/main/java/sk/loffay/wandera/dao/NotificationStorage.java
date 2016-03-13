package sk.loffay.wandera.dao;

import java.util.List;

import sk.loffay.wandera.model.Notification;

/**
 * @author Pavol Loffay
 */
public interface NotificationStorage {

    Notification get(String userGuid, String notificationGuid);

    List<Notification> getAll(String userGuid);

    void store(Notification notification);

    void delete(String userGuid, String notificationGuid);

}
