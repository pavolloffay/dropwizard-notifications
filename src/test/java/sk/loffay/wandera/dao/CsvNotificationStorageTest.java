package sk.loffay.wandera.dao;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import sk.loffay.wandera.model.EventType;
import sk.loffay.wandera.model.Notification;

/**
 * @author Pavol Loffay
 */
public class CsvNotificationStorageTest {

    public final String NOTIFICATION_FILE = "NotificationDataSheet.csv";

    static {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(ch.qos.logback.classic.Level.TRACE);
    }

    @Test
    public void testGetAll() {
        NotificationStorage storage = new CsvNotificationStorage(NOTIFICATION_FILE);

        List<Notification> allOf = storage.getAll("99c6ac34-b017-4391-9ea7-c64d07263237");
        Assert.assertTrue(allOf.size() == 1);

        allOf = storage.getAll("79bcb260-7f75-49cf-b76d-41e1c2609055");
        Assert.assertTrue(allOf.size() == 2);

        allOf = storage.getAll("286416b4-7eac-433c-876c-339dfd8bcd68");
        Assert.assertTrue(allOf.size() == 4);

        allOf = storage.getAll("315c0c1e-8c4c-458f-a37c-9b636d1fb80f");
        Assert.assertTrue(allOf.size() == 8);
    }

    @Test
    public void testDelete() {
        NotificationStorage storage = new CsvNotificationStorage(NOTIFICATION_FILE);

        storage.delete("99c6ac34-b017-4391-9ea7-c64d07263237", "532a20d3-aef0-4554-9bc1-9561dbd6151c");

        List<Notification> allOf = storage.getAll("99c6ac34-b017-4391-9ea7-c64d07263237");
        Assert.assertTrue(allOf.isEmpty());

        storage.delete("286416b4-7eac-433c-876c-339dfd8bcd68", "cad4a703-723b-49a3-aa0d-80efc82035a8");
        int size = storage.getAll("286416b4-7eac-433c-876c-339dfd8bcd68").size();
        Assert.assertTrue(size == 3);
    }

    @Test
    public void testOrder() {
        NotificationStorage storage = new CsvNotificationStorage(NOTIFICATION_FILE);

        String userGuid = "315c0c1e-8c4c-458f-a37c-9b636d1fb80f";

        EventType eventTypeAaaa = new EventType("aaaa", "bbbb");
        Notification notificationAaaa1 = new Notification("idAaaa1", "id2", userGuid, eventTypeAaaa,
                null, null, null, null, null, new Date(1), null);
        Notification notificationBbb2 = new Notification("idBbbb1", "id2", userGuid, new EventType("bbbb", "aaaa"),
                null, null, null, null, null, new Date(1), null);
        Notification notificationAaaa2 = new Notification("idAaaa2", "id2", userGuid, eventTypeAaaa,
                null, null, null, null, null, new Date(2), null);

        storage.store(notificationAaaa1);
        storage.store(notificationBbb2);
        storage.store(notificationAaaa2);

        int positionAaaa1 = 0;
        int positionAaaa2 = 0;
        List<Notification> notifications = storage.getAll(userGuid);
        for (int i = 0; i < notifications.size(); i++) {

            Notification notification = notifications.get(i);

            if (notification.getGuid().equals("idAaaa1")) {
                positionAaaa1 = i;
            } else if (notification.getGuid().equals("idAaaa2")) {
                positionAaaa2 = i;
            }
        }

        Assert.assertTrue(positionAaaa1 < positionAaaa2);
    }

    @Test
    public void testSetRead() {
        NotificationStorage storage = new CsvNotificationStorage(NOTIFICATION_FILE);

        Notification notification =
                storage.get("286416b4-7eac-433c-876c-339dfd8bcd68", "cad4a703-723b-49a3-aa0d-80efc82035a8");

        Assert.assertTrue(notification.isRead() == false);

        notification.setRead();

        notification = storage.get("286416b4-7eac-433c-876c-339dfd8bcd68", "cad4a703-723b-49a3-aa0d-80efc82035a8");
        Assert.assertTrue(notification.isRead());
    }
}
