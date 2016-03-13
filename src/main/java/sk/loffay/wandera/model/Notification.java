package sk.loffay.wandera.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Pavol Loffay
 */
public class Notification {

    private String guid;
    private String deviceGuid;
    private String userGuid;

    private EventType eventType;

    private Double geofenceLat;
    private Double geofenceLon;
    private Double geofenceRadiusMetres;

    private String title;
    private String content;

    private Date eventDate;
    private Date sentDate;

    private boolean read;

    public Notification(String guid, String deviceGuid, String userGuid,
                        EventType eventSubtype,
                        Double geofenceLat, Double geofenceLon, Double geofenceRadiusMetres,
                        String title, String content,
                        Date eventDate, Date sentDate) {
        this.guid = guid;
        this.deviceGuid = deviceGuid;
        this.userGuid = userGuid;

        this.eventType = eventSubtype;

        this.geofenceLat = geofenceLat;
        this.geofenceLon = geofenceLon;
        this.geofenceRadiusMetres = geofenceRadiusMetres;

        this.title = title;
        this.content = content;

        this.eventDate = eventDate;
        this.sentDate = sentDate;
        this.read = false;
    }


    public String getGuid() {
        return guid;
    }

    public String getDeviceGuid() {
        return deviceGuid;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public String getEventType() {
        return eventType.getType();
    }

    public String getEventSubType() {
        return eventType.getSubType();
    }

    public Double getGeofenceLat() {
        return geofenceLat;
    }

    public Double getGeofenceLon() {
        return geofenceLon;
    }

    public Double getGeofenceRadiusMetres() {
        return geofenceRadiusMetres;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getEventDate() {
        return eventDate;
    }

    @JsonIgnore
    public Long getEventTimestamp() {
        return eventDate.getTime();
    }

    public Date getSentDate() {
        return sentDate;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead() {
        this.read = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;

        Notification that = (Notification) o;

        return guid.equals(that.guid);
    }

    @Override
    public int hashCode() {
        return guid.hashCode();
    }
}
