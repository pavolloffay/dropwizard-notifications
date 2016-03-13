package sk.loffay.wandera.dao;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.loffay.wandera.exception.EntityNotFoundException;
import sk.loffay.wandera.model.EventType;
import sk.loffay.wandera.model.Notification;

/**
 * @author Pavol Loffay
 */
public class CsvNotificationStorage implements NotificationStorage {
    private static final Logger logger = LoggerFactory.getLogger(CsvNotificationStorage.class);
    private final String csvFile;

    // userGuid -> notifications
    private final Map<String, Set<Notification>> notifications;


    public CsvNotificationStorage(String fileName) {
        this.csvFile = fileName;
        this.notifications = Collections.synchronizedMap(new HashMap<>());

        loadFromFile();
    }

    @Override
    public Notification get(String userGuid, String notificationGuid) {

        synchronized (this.notifications) {
            Set<Notification> notificationsOfUser = this.notifications.get(userGuid);

            if (notificationsOfUser == null) {
                throw new EntityNotFoundException(notificationGuid, userGuid);
            }

            for (Notification notification : notificationsOfUser) {
                if (notification.getGuid().equals(notificationGuid)) {
                    return notification;
                }
            }
        }

        throw new EntityNotFoundException(notificationGuid, userGuid);
    }

    @Override
    public List<Notification> getAll(String userGuid) {

        synchronized (this.notifications) {
            Set<Notification> notificationsOfUser = this.notifications.get(userGuid);
            if (notificationsOfUser == null) {
                return Collections.emptyList();
            }

            ArrayList<Notification> sorted = notificationsOfUser.stream()
                    .sorted(Comparator.comparingLong(Notification::getEventTimestamp)).collect(Collectors
                            .toCollection(ArrayList::new));

            Map<String, List<Notification>> groupedByType = sorted.stream()
                    .collect(Collectors.groupingBy(Notification::getEventType));

            List<Notification> result = groupedByType.values().stream()
                    .flatMap(notifications -> notifications.stream())
                    .collect(Collectors.toList());

            return result;
        }
    }

    @Override
    public void store(Notification notification) {

        synchronized (this.notifications) {
            Set<Notification> notificationsOfUser = this.notifications.get(notification.getUserGuid());
            if (notificationsOfUser == null) {
                notificationsOfUser = new HashSet<>();
                notifications.put(notification.getUserGuid(), notificationsOfUser);
            }
            notificationsOfUser.add(notification);
        }
    }

    @Override
    public void delete(String userGuid, String notificationGuid) {

        synchronized (this.notifications) {
            Set<Notification> notificationsOfUser = this.notifications.get(userGuid);

            if (notificationsOfUser == null) {
                throw new EntityNotFoundException(notificationGuid, userGuid);
            }

            // todo not very nice
            Notification notificationToRemove =
                    new Notification(notificationGuid, userGuid, null, null, null, null, null,
                            null, null, null, null);

            boolean removed = notificationsOfUser.remove(notificationToRemove);
            if (!removed) {
                throw new EntityNotFoundException(notificationGuid, userGuid);
            }

            if (notificationsOfUser.isEmpty()) {
                this.notifications.remove(userGuid);
            }
        }
    }

    private void loadFromFile() {

        try {
            Reader in = new FileReader(csvFile);

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withAllowMissingColumnNames(true)
                    .withHeader()
                    .parse(in);

            for (CSVRecord record : records) {
                String notificationGuid = record.get(0);
                String deviceGuid = record.get(1);
                String userGuid = record.get(2);

                String eventTypeString = record.get(3);
                String eventSubTypeString = record.get(4);
                eventTypeString = eventTypeString.isEmpty() ? null: eventTypeString;
                eventSubTypeString = eventSubTypeString.isEmpty() ? null : eventSubTypeString;


                Double lattitude = parseDouble(record.get(5));
                Double longtitude = parseDouble(record.get(6));
                Double radius = parseDouble(record.get(7));

                String title = record.get(8);
                String content = record.get(9);

                Date eventDate = parseDateTime(record.get(10));
                Date sentDate = parseDateTime(record.get(11));

                if (notificationGuid == null || notificationGuid.isEmpty() ||
                        deviceGuid == null || deviceGuid.isEmpty() ||
                        userGuid == null || userGuid.isEmpty() ||
                        eventTypeString == null || eventTypeString.isEmpty() ||
                        eventDate == null) {
                    logger.error("Skipping entity, notificationGuid, deviceGuid, userGuid... are null or empty");
                    continue;
                }

                EventType eventType = new EventType(eventTypeString, eventSubTypeString);
                Notification notification = new Notification(notificationGuid, deviceGuid, userGuid,
                        eventType,
                        lattitude, longtitude, radius,
                        title, content, eventDate, sentDate);

                store(notification);
            }
        } catch (IOException ex) {
            logger.error("Could not load notifications from {}, setting to empty", csvFile);
        }
    }

    private Double parseDouble(String number) {

        Double doubleValue = null;
        try {
            doubleValue = Double.parseDouble(number);
        } catch (NumberFormatException ex) {
            logger.warn("Unable to parse {} to Double, setting to null", number);
        }

        return doubleValue;
    }

    private Date parseDateTime(String timestamp) {

        Long longValue = null;
        try {
            longValue = Long.parseLong(timestamp);
        } catch (NumberFormatException ex) {
            logger.warn("Unable to parse {} to Long, setting to null", timestamp);
        }

        return timestamp != null ? new Date(longValue) : null;
    }
}
