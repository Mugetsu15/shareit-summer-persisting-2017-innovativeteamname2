package edu.hm.exceptionHandling;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Exception-Handler for wrong JSON inputs.
 * @author Daniel Gabl
 *
 */
@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException > {

    @Override
    public Response toResponse(JsonMappingException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("This is an invalid request. At least one field format is not readable by the system.")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
}