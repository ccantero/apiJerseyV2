package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;

import model.EventsList;
import model.User;
import service.EventbriteService;
import service.EventsListsService;
import service.ManagementService;
import service.UserService;


@Path("/admin")
public class AdminController {

	@RolesAllowed("ADMIN")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getRole() {
		JSONObject obj = new JSONObject();
		obj.put("role", "admin");
		Response response = Response.ok().entity(obj.toString()).build();
		return response;
	}


	@Path("/users")
	@RolesAllowed("ADMIN")
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUsers() {

		List<User> listado = ManagementService.getUserDAO().getAllNonAdminUsers();

		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();  

		for (User user : listado) 
		{
			if(user.getRole() == null || !user.getRole().equals("ADMIN"))
				arr.put(user.getUserName());
		}	

		obj.put("users", arr);
		Response response = Response.ok().entity(obj.toString()).build();
		return response;
	}

	@Path("/users/{userID}")
	@GET
	@RolesAllowed("ADMIN")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUser(@PathParam("userID") String name) {
		System.out.println(this.getClass().getName() + ":: AdminController getUser");
		User user = ManagementService.getUserDAO().getUserByName(name);

		JSONObject obj = new JSONObject();
		obj.put("username", user.getUserName());
		obj.put("cantListas", user.getEventsLists().size());
		obj.put("cantAlarmas", user.getAlarms().size());
		obj.put("lastLogin", user.getLastLogin());
		Response response = Response.ok().entity(obj.toString()).build();
		return response;

		//return Response.status(201).entity(user).build(); 
	}

	@Path("/users/{userID}/events")
	@GET
	@RolesAllowed("ADMIN")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getUserLists(@PathParam("userID") String name) {
		System.out.println(this.getClass().getName() + ":: AdminController getUserLists");
		User user = ManagementService.getUserDAO().getUserByName(name);

		List<EventsList> events = user.getEventsLists();
		JSONArray jsonEvents = new JSONArray(events);

		Response response = Response.ok().entity(jsonEvents.toString()).build();
		return response;
	}

	@Path("/compare")
	@GET
	@RolesAllowed("ADMIN")
	@Produces({MediaType.APPLICATION_JSON})
	public Response compareUserLists(@QueryParam("user1") String user1, 
			@QueryParam("list1") String list1,
			@QueryParam("user2") String user2,
			@QueryParam("list2") String list2
			) {

		System.out.println(this.getClass().getName() + ":: compareUserLists");		
		List<Long> listado1 = EventsListsService.getListOfEventsByUserAndListName(user1, list1);
		List<Long> listado2 = EventsListsService.getListOfEventsByUserAndListName(user2, list2);

		List<JSONObject> compartidos = new ArrayList<>();


		for (Long i: listado1) {
			Map<String, String> paramsEventBrite = new HashMap<String, String>();

			if (listado2.contains(i)) {
				paramsEventBrite.put("codigo", i.toString());
				String jsonString = EventbriteService.searchEvents(paramsEventBrite);
				JSONObject json = new JSONObject(jsonString);


				String string = json.get("events").toString();
				JSONArray jsonEvents = new JSONArray(string);

				String jsonEventsText = jsonEvents.get(0).toString();				
				JSONObject singleEvent = new JSONObject(jsonEventsText);			
				JSONObject jsonName = new JSONObject(singleEvent.get("name").toString());
				JSONObject jsonStart = new JSONObject(singleEvent.get("start").toString());
				JSONObject jsonEnd = new JSONObject(singleEvent.get("end").toString());

				JSONObject obj = new JSONObject();
				obj.put("id", singleEvent.get("id"));
				obj.put("name", jsonName.get("text"));
				obj.put("start", jsonStart.get("local"));
				obj.put("end", jsonEnd.get("local"));

				compartidos.add(obj);
			}
		}

		JSONArray jsonEvents = new JSONArray(compartidos);
		Response response = Response.ok().entity(jsonEvents.toString()).build();
		return response;
	}

	@Path("/events/{codigo}")
	@GET
	@RolesAllowed("ADMIN")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarEventos(@PathParam("codigo")  Long codigo) {
		System.out.println(this.getClass().getName() + ":: buscarEventos");	
		return Response.ok(EventsListsService.getCountUsersWithEvent(codigo)).build();
	}

}
