package moneytransfer.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ControllerExceptionMapper implements ExceptionMapper<MoneyTransferException> {
    private static Logger logger = LoggerFactory.getLogger(ControllerExceptionMapper.class);

    @Override
    public Response toResponse(MoneyTransferException exception) {
        logger.debug(exception.getMessage());

        return Response.status(exception.errorResponseStatus())
                .entity(new ErrorResponseEntity(exception.getMessage(), exception.errorResponseStatus().getStatusCode()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
