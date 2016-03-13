package sk.loffay.wandera.exception.mapper;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

/**
 * @author Pavol Loffay
 */
public class ExceptionMapperUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionMapperUtils.class);

    public static Response buildResponse(Throwable ex, Response.Status status) {
        logger.error("Rest exception: ", ex);

        ApiError apiError = new ApiError(Throwables.getRootCause(ex).getMessage());
        return Response.status(status).entity(apiError).build();
    }
}
