package sk.loffay.wandera.model;

/**
 * @author Pavol Loffay
 */
public class EventType {

    private String type;

    private String subType;

    public EventType(String type) {
        this(type, null);
    }

    public EventType(String type, String subType) {
        this.type = type;
        this.subType = subType;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }
}
