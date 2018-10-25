package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import service.EventsListsService;
import service.ManagementService;


@Path("/eventsLists")
public class EventsListsController {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllLists() throws JsonProcessingException {
        return Response.status(201).entity(EventsListsService.getAllLists()).build();
    }

    @Path("/getFromUser")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListsFromUser(@Context UriInfo uriDetails) throws JsonProcessingException {
        Integer userId = Integer.parseInt(uriDetails.getQueryParameters().get("userId").get(0).toString());
        return Response.ok(EventsListsService.getByUserId(userId)).build();
    }

    /**
     * @param params
     * @return imprime en pantalla el json de lassss listassss de eventos
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @Path("/create")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearLista(String params) throws JsonProcessingException {
        Map<String, String> parametros = ManagementService.getPostParams(params);
        String nombreLista = parametros.get("nombreLista");
        System.out.println(nombreLista);
        Integer userId = 1;
        return Response.status(201).entity(EventsListsService.create(nombreLista, userId)).build();
    }

    @Path("/addEvent")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEvent(String params) throws IOException {
        Map<String, String> parametros = ManagementService.getPostParams(params);
        Long codigoEvento = Long.parseLong(parametros.get("codigo"));
        Integer listaId=Integer.parseInt(parametros.get("lista"));
        return Response.status(201).entity("{\"error\":" + !(EventsListsService.addEvent(listaId, codigoEvento)) + "}").build();
    }

}
