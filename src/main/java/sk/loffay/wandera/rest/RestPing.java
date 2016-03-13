package sk.loffay.wandera.rest;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Pavol Loffay
 */
@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestPing {

    private final String output;


    public RestPing(String template, String environment) {
        this.output = String.format(template, environment);
    }

    @GET
    public Response ping() {
        return Response.status(Response.Status.OK).entity(new Date().toString() + " " + output).build();
    }
}
