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
		router.get("/api/NFC").handler(this::getAllWithParamsNFC);
		router.get("/api/NFC/temperatura/allt").handler(this::getAllNFC);
		router.get("/api/NFC/:idNFC").handler(this::getOneNFC);
		router.post("/api/NFC").handler(this::addOneNFC);
		router.delete("/api/NFC/:idNFC").handler(this::deleteOneNFC);
		router.put("/api/NFC/:idS").handler(this::putOneNFC);
		//Actuador led
		router.route("/api/led*").handler(BodyHandler.create());
		router.get("/api/led").handler(this::getAllWithParamsLed);
		router.get("/api/led/led/allled").handler(this::getAllLed);
		router.get("/api/led/:idled").handler(this::getOneLed);
		router.post("/api/led").handler(this::addOneLed);
		//Actuador servo
		router.route("/api/servo*").handler(BodyHandler.create());
		router.get("/api/servo").handler(this::getAllWithParamsServo);
		router.get("/api/servo/servo/allservo").handler(this::getAllServo);
		router.get("/api/led/:idservo").handler(this::getOneServo);
		router.post("/api/servo").handler(this::addOneServo);
		
		
		
		
		
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
	
	private void getAllNFC(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new sensorNFCEntityWrapper(NFCs.values())));
	}
	
	private void getAllWithParamsNFC(RoutingContext routingContext) {
		
		final String NFC = routingContext.queryParams().contains("value") ? 
				routingContext.queryParam("value").get(0) : null;
		
		final String date = routingContext.queryParams().contains("date") 
				? routingContext.queryParam("date").get(0) : null;
		
		Double NFCSdouble = Double.parseDouble(NFC);
		Long Datelong = Long.parseLong(date) ;
		
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new sensorNFCEntityWrapper(NFCs.values().stream().filter(elem -> {
					boolean res = true;
					res = res && (NFCSdouble != null ? elem.getValue().equals(NFCSdouble) : true);
					res = res && (Datelong != null ? elem.getDate().equals(Datelong) : true);
					return res;
				}).collect(Collectors.toList()))));
	}
	
	private void getOneNFC(RoutingContext routingContext) {
		int id = 0;
		try {
			id = Integer.parseInt(routingContext.request().getParam("idNFC"));

			if (NFCs.containsKey(id)) {
				sensorNFCEntity ds = NFCs.get(id);
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
	
	private void addOneNFC(RoutingContext routingContext) {
		final sensorNFCEntity nfc = gson.fromJson(routingContext.getBodyAsString(), sensorNFCEntity.class);
		NFCs.put(nfc.getIdNFC(), nfc);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(nfc));
	}
	
	private void deleteOneNFC(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idNFC"));
		if (NFCs.containsKey(id)) {
			sensorNFCEntity nfc = NFCs.get(id);
			NFCs.remove(id);
			routingContext.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(NFCs));
		} else {
			routingContext.response().setStatusCode(204).putHeader("content-type", "application/json; charset=utf-8")
					.end();
		}
	}
	private void putOneNFC(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idNFC"));
		sensorNFCEntity ds = NFCs.get(id);
		final sensorNFCEntity element = gson.fromJson(routingContext.getBodyAsString(), sensorNFCEntity.class);
		ds.setValue(element.getValue());
		ds.setDate(element.getDate());
		NFCs.put(ds.getIdNFC(), ds);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(element));
	}
	
	//********Led*********//
	
	@SuppressWarnings("unused")
	private void getAllLed(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new actuadorLedEntityWrapper(leds.values())));
	}
	
	private void getAllWithParamsLed(RoutingContext routingContext) {
		final String luz = routingContext.queryParams().contains("nivel_luz") ? 
				routingContext.queryParam("nivel_luz").get(0) : null;
		
		final String date = routingContext.queryParams().contains("date") 
				? routingContext.queryParam("date").get(0) : null;
		
		Double luzdouble = Double.parseDouble(luz);
		Long datelong = Long.parseLong(date) ;
		

		routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
				.end(gson.toJson(new actuadorLedEntityWrapper(leds.values().stream().filter(elem -> {
					boolean res = true;
					res = res && (luzdouble != null ? elem.getNivel_luz().equals(luzdouble) : true);
					res = res && (datelong != null ? elem.getDate().equals(datelong) : true);
					return res;
				}).collect(Collectors.toList()))));
	}

	
	private void getOneLed(RoutingContext routingContext) {
		int id = 0;
		try {
			id = Integer.parseInt(routingContext.request().getParam("idled"));

			if (leds.containsKey(id)) {
				actuadorLedEntity ds = leds.get(id);
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
	
	private void addOneLed(RoutingContext routingContext) {
		final actuadorLedEntity led = gson.fromJson(routingContext.getBodyAsString(), actuadorLedEntity.class);
		leds.put(led.getIdled(), led);
		routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(gson.toJson(led));
	}
	
	
	
	//********Servo*********//
	
		@SuppressWarnings("unused")
		private void getAllServo(RoutingContext routingContext) {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
					.end(gson.toJson(new actuadorServoEntityWrapper(servos.values())));
		}
		
		private void getAllWithParamsServo(RoutingContext routingContext) {
			final String angulo = routingContext.queryParams().contains("value") ? 
					routingContext.queryParam("value").get(0) : null;
			
			final String date = routingContext.queryParams().contains("date") 
					? routingContext.queryParam("date").get(0) : null;
			
			Double angulodouble = Double.parseDouble(angulo);
			Long datelong = Long.parseLong(date) ;
			

			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").setStatusCode(200)
					.end(gson.toJson(new actuadorServoEntityWrapper(servos.values().stream().filter(elem -> {
						boolean res = true;
						res = res && (angulodouble != null ? elem.getValue().equals(angulodouble) : true);
						res = res && (datelong != null ? elem.getDate().equals(datelong) : true);
						return res;
					}).collect(Collectors.toList()))));
		}

		
		private void getOneServo(RoutingContext routingContext) {
			int id = 0;
			try {
				id = Integer.parseInt(routingContext.request().getParam("idServo"));

				if (servos.containsKey(id)) {
					actuadorServoEntity ds = servos.get(id);
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
		
		private void addOneServo(RoutingContext routingContext) {
			final actuadorServoEntity servo = gson.fromJson(routingContext.getBodyAsString(), actuadorServoEntity.class);
			servos.put(servo.getIdServo(), servo);
			routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
					.end(gson.toJson(servo));
		}
	/************************/
	/*****FINALIZO REST******/
	/************************/
	
}
