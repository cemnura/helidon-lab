package com.cemnura.service;

import com.cemnura.util.Fibonacci;

import javax.enterprise.context.RequestScoped;
import javax.json.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("/")  // <1>
@RequestScoped // <2>
public class FibonacciService {

    private static final JsonBuilderFactory jsonFactory = Json.createBuilderFactory(Collections.emptyMap());

    @GET // <3>
    @Path("/fibonacci/{index}") // <4>
    @Produces(MediaType.APPLICATION_JSON) // <5>
    public JsonArray getFibonnaciSequence(@PathParam("index") int index)
    {
        List<Integer> fibonacciSequence =  Fibonacci.createSequence(index);

        JsonObjectBuilder objectBuilder = jsonFactory.createObjectBuilder();
        JsonArrayBuilder arrayBuilder = jsonFactory.createArrayBuilder();

        fibonacciSequence.stream()
                .forEach(integer ->
                        arrayBuilder.add(objectBuilder.add("seq", integer)));

        return arrayBuilder.build();
    }

    @GET
    @Path("/fibonacci/index/{index}")
    @Produces(MediaType.TEXT_PLAIN)
    public Integer getIndex(
            @PathParam("index") int index) // <6>
    {
        return Fibonacci.index(index);
    }

    /*
    * Let for lab reviewers to implement
    * */
    public Integer getRandom()
    {
        throw new UnsupportedOperationException("Not implemented!");
    }
}
