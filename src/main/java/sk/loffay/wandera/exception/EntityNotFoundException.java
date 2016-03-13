package sk.loffay.wandera.exception;

/**
 * @author Pavol Loffay
 */
public class EntityNotFoundException extends WanderaNotificationException {

    private String entityGuid;
    private String userGuid;


    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String userGuid) {
        this(null, userGuid);
    }

    public EntityNotFoundException(String entityGuid, String userGuid) {
        super();
        this.entityGuid = entityGuid;
        this.userGuid = userGuid;
    }

    @Override
    public String getMessage() {

        String msg = "Entity not found";
        if (entityGuid != null) {
            msg += ", with entity guid=" + entityGuid;
        }
        if (userGuid != null) {
            msg += ", userGuid=" + userGuid;
        }

        return msg;
    }
}
