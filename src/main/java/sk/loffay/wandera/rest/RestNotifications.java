package sk.loffay.wandera.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dropwizard.auth.Auth;
import sk.loffay.wandera.dao.NotificationStorage;
import sk.loffay.wandera.model.Notification;
import sk.loffay.wandera.model.User;

/**
 * @author Pavol Loffay
 */
@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestNotifications {
    private final Logger logger = LoggerFactory.getLogger(RestNotifications.class);

    public final NotificationStorage notificationStorage;


    public RestNotifications(NotificationStorage notificationStorage) {
        this.notificationStorage = notificationStorage;
    }

    @GET
    @Path("/")
    public Response getAll(@Auth User user) {

        List<Notification> result = notificationStorage.getAll(user.getGuid());

        return Response.ok().entity(result).build();
    }

    @GET
    @Path("/{id}")
    public Response getOne(@Auth User user, @PathParam("id") String id) {

        Notification result = notificationStorage.get(user.getGuid(), id);

        return Response.ok().entity(result).build();
    }

    @PUT
    @Path("/{id}/read")
    public Response setAsRead(@Auth User user, @PathParam("id") String id) {

        Notification result = notificationStorage.get(user.getGuid(), id);

        result.setRead();

        logger.info("User {}, Entity {} read", user.getName(), id);
        return Response.status(Response.Status.OK).entity(result).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@Auth User user, @PathParam("id") String id) {

        notificationStorage.delete(user.getGuid(), id);

        logger.info("User {}, Entity {} deleted", user.getName(), id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
