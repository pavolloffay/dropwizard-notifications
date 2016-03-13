package sk.loffay.wandera.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import sk.loffay.wandera.exception.EntityNotFoundException;

/**
 * @author Pavol Loffay
 */
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException exception) {
        return ExceptionMapperUtils.buildResponse(exception, Response.Status.NOT_FOUND);
    }
}
