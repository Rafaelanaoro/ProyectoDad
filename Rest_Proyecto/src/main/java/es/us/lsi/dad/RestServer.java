package es.us.lsi.dad;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class RestServer extends AbstractVerticle {

	
	/************************/
	/*****INICIO REST********/
	/************************/
	
	private Map<Integer, sensorNFCEntity> NFCs = new HashMap<Integer, sensorNFCEntity>();
	private Map<Integer, actuadorLedEntity> leds= new HashMap<Integer, actuadorLedEntity>();
	private Map<Integer, actuadorServoEntity> servos= new HashMap<Integer, actuadorServoEntity>();
	private Gson gson;
	
	
	public void start(Promise<Void> startFuture) {
		// Instantiating a Gson serialize object using specific date format
		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		// Defining the router object
		Router router = Router.router(vertx);
		// Handling any server startup result
		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});

		// Defining URI paths for each method in RESTful interface, including body
		// Sensor NFC
		router.route("/api/NFC*").handler(BodyHandler.create());
		router.get("/api/NFC").handler(this::getAllWithParamsT);
		router.get("/api/NFC/temperatura/allt").handler(this::getAllT);
		router.get("/api/NFC/:idNFC").handler(this::getOneT);
		router.post("/api/NFC").handler(this::addOneT);
		router.delete("/api/NFC/:idNFC").handler(this::deleteOneT);
		router.put("/api/NFC/:idS").handler(this::putOneT);
		//Actuador led
		router.route("/api/led*").handler(BodyHandler.create());
		router.get("/api/led").handler(this::getAllWithParamsLe);
		router.get("/api/led/led/allle").handler(this::getAllLe);
		router.get("/api/led/:idla").handler(this::getOneLe);
		router.post("/api/led").handler(this::addOneLe);
		
		
		
		
		
	}
	
	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		try {
			NFCs.clear();
			stopPromise.complete();
		} catch (Exception e) {
			stopPromise.fail(e);
		}
		super.stop(stopPromise);
	}
	
	/************************/
	/*****TEMPERATURA********/
	/************************/
	
	private void getAllT(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new TemperaturaEntityListWrapper(temperaturas.values())));
	}
	
	private void getAllWithParamsT(RoutingContext routingContext) {
		
		final String temperatura = routingContext.queryParams().contains("temperatura") ? 
				routingContext.queryParam("temperatura").get(0) : null;
		
		final String timestampt = routingContext.queryParams().contains("timestampt") 
				? routingContext.queryParam("timestampt").get(0) : null;
		
		Double temperaturadouble = Double.parseDouble(temperatura);
		Long timestamptlong = Long.parseLong(timestampt) ;
		
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new TemperaturaEntityListWrapper(temperaturas.values().stream().filter(elem -> {
					boolean res = true;
					res = res && (temperaturadouble != null ? elem.getTemperatura().equals(temperaturadouble) : true);
					res = res && (timestamptlong != null ? elem.getTimestampt().equals(timestamptlong) : true);
					return res;
				}).collect(Collectors.toList()))));
	}
	
	private void getOneT(RoutingContext routingContext) {
		int id = 0;
		try {
			id = Integer.parseInt(routingContext.request().getParam("idtemp"));

			if (temperaturas.containsKey(id)) {
				SensorTemperaturaEntity ds = temperaturas.get(id);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(ds != null ? gson.toJson(ds) : "");
			} else {
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(204).end();
			}
		} catch (Exception e) {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}
	
	private void addOneT(RoutingContext routingContext) {
		final SensorTemperaturaEntity temp = gson.fromJson(routingContext.getBodyAsString(), SensorTemperaturaEntity.class);
		temperaturas.put(temp.getIdtemp(), temp);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(temp));
	}
	
	private void deleteOneT(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idtemp"));
		if (temperaturas.containsKey(id)) {
			SensorTemperaturaEntity temp = temperaturas.get(id);
			temperaturas.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(temperaturas));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}
	private void putOneT(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idtemp"));
		SensorTemperaturaEntity ds = temperaturas.get(id);
		final SensorTemperaturaEntity element = gson.fromJson(routingContext.getBodyAsString(), SensorTemperaturaEntity.class);
		ds.setTemperatura(element.getTemperatura());
		ds.setTimestampt(element.getTimestampt());
		temperaturas.put(ds.getIdtemp(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}
	
	/****************/
	/*****LUZ********/
	/****************/
	
	@SuppressWarnings("unused")
	private void getAllL(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new LuzEntityListWrapper(luces.values())));
	}
	
	private void getAllWithParamsL(RoutingContext routingContext) {
		final String luz = routingContext.queryParams().contains("nivel_luz") ? 
				routingContext.queryParam("nivel_luz").get(0) : null;
		
		final String timestampl = routingContext.queryParams().contains("timestampl") 
				? routingContext.queryParam("timestampl").get(0) : null;
		
		Double luzdouble = Double.parseDouble(luz);
		Long timestamptllong = Long.parseLong(timestampl) ;
		

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new LuzEntityListWrapper(luces.values().stream().filter(elem -> {
					boolean res = true;
					res = res && (luzdouble != null ? elem.getNivel_luz().equals(luzdouble) : true);
					res = res && (timestamptllong != null ? elem.getNivel_luz().equals(timestamptllong) : true);
					return res;
				}).collect(Collectors.toList()))));
	}

	
	private void getOneL(RoutingContext routingContext) {
		int id = 0;
		try {
			id = Integer.parseInt(routingContext.request().getParam("idl"));

			if (luces.containsKey(id)) {
				SensorLuzEntity ds = luces.get(id);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(ds != null ? gson.toJson(ds) : "");
			} else {
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(204).end();
			}
		} catch (Exception e) {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}
	
	private void addOneL(RoutingContext routingContext) {
		final SensorLuzEntity luz = gson.fromJson(routingContext.getBodyAsString(), SensorLuzEntity.class);
		luces.put(luz.getIdl(), luz);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(luz));
	}
	
	
	private void deleteOneL(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idl"));
		if (luces.containsKey(id)) {
			SensorLuzEntity user = luces.get(id);
			luces.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(user));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}
	
	private void putOneL(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idl"));
		SensorLuzEntity ds = luces.get(id);
		final SensorLuzEntity element = gson.fromJson(routingContext.getBodyAsString(), SensorLuzEntity.class);
		ds.setNivel_luz(element.getNivel_luz());
		ds.setTimestampl(element.getTimestampl());
		luces.put(ds.getIdl(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}
	
	//*****************//
	//***Actudor led***//
	//*****************//
	
	@SuppressWarnings("unused")
	private void getAllLe(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new ActLedEntityListWrapper(leds.values())));
	}
	
	private void getAllWithParamsLe(RoutingContext routingContext) {
		final String luz = routingContext.queryParams().contains("nivel_luz") ? 
				routingContext.queryParam("nivel_luz").get(0) : null;
		
		final String timestample = routingContext.queryParams().contains("timestample") 
				? routingContext.queryParam("timestample").get(0) : null;
		
		Double luzdouble = Double.parseDouble(luz);
		Long timestamplelong = Long.parseLong(timestample) ;
		

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new LuzEntityListWrapper(luces.values().stream().filter(elem -> {
					boolean res = true;
					res = res && (luzdouble != null ? elem.getNivel_luz().equals(luzdouble) : true);
					res = res && (timestamplelong != null ? elem.getNivel_luz().equals(timestamplelong) : true);
					return res;
				}).collect(Collectors.toList()))));
	}

	
	private void getOneLe(RoutingContext routingContext) {
		int id = 0;
		try {
			id = Integer.parseInt(routingContext.request().getParam("idla"));

			if (luces.containsKey(id)) {
				ActLedEntity ds = leds.get(id);
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(200).end(ds != null ? gson.toJson(ds) : "");
			} else {
				routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
						.setStatusCode(204).end();
			}
		} catch (Exception e) {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(204)
					.end();
		}
	}
	
	private void addOneLe(RoutingContext routingContext) {
		final ActLedEntity led = gson.fromJson(routingContext.getBodyAsString(), ActLedEntity.class);
		leds.put(led.getIdLA(), led);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(led));
	}
	/************************/
	/*****FINALIZO REST******/
	/************************/
	
}
