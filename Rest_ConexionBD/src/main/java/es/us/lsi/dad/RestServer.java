package es.us.lsi.dad;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class RestServer extends AbstractVerticle {

	private Gson gson;

	MySQLPool mySQLclient;

	public void start(Promise<Void> startFuture) {

		MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("proyecto_dad").setUser("root").setPassword("alvaro");

		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

		mySQLclient = MySQLPool.pool(vertx, connectOptions, poolOptions);

		gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

		// Defining the router object
		Router router = Router.router(vertx);
		// Handling any server startup result
		vertx.createHttpServer().requestHandler(router::handle).listen(8084, result -> {
			if (result.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(result.cause());
			}
		});

		router.route("/api/*").handler(BodyHandler.create());

		router.get("/api/NFC").handler(this::getAllSNFC);
		router.get("/api/NFC/:idNFC").handler(this::getBySensorNFC);
		router.get("/api/NFC/:idNFC").handler(this::getLastBySensorNFC);// no
		router.post("/api/NFC").handler(this::addSensorNFC);// no

		router.get("/api/actLed").handler(this::getAllALed);
		router.get("/api/actLed/:idled").handler(this::getByActLed);
		router.get("/api/actLed/:idled").handler(this::getLastByActLed);
		router.post("/api/actLed").handler(this::addALed);

		router.get("/api/actServo").handler(this::getAllAServo);
		router.get("/api/actServo/:idServo").handler(this::getByActServo);
		router.get("/api/actServo/:idServo").handler(this::getLastByActServo);
		router.post("/api/actServo").handler(this::addAServo);

	}

	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
		try {
			stopPromise.complete();
		} catch (Exception e) {
			stopPromise.fail(e);
		}
		super.stop(stopPromise);
	}

// SENSOR
	private void getAllSNFC(RoutingContext routingContext) {
		mySQLclient.query("SELECT * FROM proyecto_dad.sensorNFC;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				System.out.println(resultSet.size());
				List<sensorImpleNFC> result = new ArrayList<>();
				for (Row elem : resultSet) {
					result.add(new sensorImpleNFC(elem.getInteger("idNFC"), elem.getInteger("valor"),
							elem.getLong("fecha"), elem.getInteger("groupId"), elem.getBoolean("estado")));
				}
				System.out.println(result.toString());
				routingContext.response().setStatusCode(201).end("Datos del sensor recibidos correctamente");

			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response().setStatusCode(201).end("Datos del sensor no recibidos correctamente");
			}
		});
	}

	private void getBySensorNFC(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idNFC"));
		mySQLclient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyecto_dad.sensorNFC WHERE idNFC = ?")
						.execute(Tuple.of(id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<sensorImpleNFC> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new sensorImpleNFC(elem.getInteger("idNFC"), elem.getInteger("valor"),
											elem.getLong("fecha"), elem.getInteger("groupId"),
											elem.getBoolean("estado")));
								}
								System.out.println(result.toString());
								routingContext.response().setStatusCode(201)
										.end("Datos del sensor recibidos correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(201)
										.end("Datos del sensor no recibidos correctamente");
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}

	private void getLastBySensorNFC(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idNFC"));
		mySQLclient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result()
						.preparedQuery(
								"SELECT * FROM proyecto_dad.sensorNFC WHERE idNFC = ? ORDER BY fecha DESC LIMIT 1")
						.execute(Tuple.of(id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<sensorImpleNFC> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new sensorImpleNFC(elem.getInteger("idNFC"), elem.getInteger("valor"),
											elem.getLong("fecha"), elem.getInteger("groupId"),
											elem.getBoolean("estado")));
								}
								System.out.println(result.toString());
								routingContext.response().setStatusCode(201)
										.end("Datos del sensor recibidos correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(201)
										.end("Datos del sensor no recibidos correctamente");

							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}

	private void addSensorNFC(RoutingContext routingContext) {
		final sensorImpleNFC NFC = gson.fromJson(routingContext.getBodyAsString(), sensorImpleNFC.class);
		mySQLclient.preparedQuery("INSERT INTO sensorNFC (idNFC, valor, fecha, groupId, estado) valorS (?, ?, ?, ?, ?)")
				.execute((Tuple.of(NFC.getIdNFC(), NFC.getvalor(), NFC.getfecha(), NFC.getGroupId(), NFC.getestado())),
						res -> {
							if (res.succeeded()) {
								routingContext.response().setStatusCode(201)
										.putHeader("content-type", "application/json; charset=utf-8")
										.end("Sensor añadido correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500)
										.end("Sensor al añadir el actuador: " + res.cause().getMessage());
							}
						});
	}
	/*
	 * private void addOneS(RoutingContext routingContext) { final Sensor sensor =
	 * gson.fromJson(routingContext.getBodyAsString(), Sensor.class);
	 * mySqlClient.getConnection(connection ->{ if(connection.succeeded()) {
	 * connection.result().
	 * query("INSERT INTO bd_dad.sensor (idNFCensor, idGroup, idPlaca, tiempo, valor) valorS ("
	 * + sensor.getId() +","+sensor.getIdGroup() +","+sensor.getIdPlaca() + ","+
	 * sensor.getTiempo()+ ","+ sensor.getValor()+ ");").execute(res->{
	 * if(res.succeeded()) {
	 * routingContext.response().setStatusCode(201).putHeader("content-type",
	 * "application/json; charset=utf-8").setStatusCode(200)
	 * .end(gson.toJson(sensor)); mqttClient.publish("topic_1",
	 * Buffer.buffer("Ejemplo"), MqttQoS.AT_LEAST_ONCE, false, false); }else {
	 * System.out.println("Error: "+ res.cause().getLocalizedMessage()); }
	 * connection.result().close(); }); }else {
	 * System.out.println(connection.cause().toString()); } }
	 * 
	 * 
	 * 
	 * }
	 */

	// **************************** Actuadores ********************************
	// =============================LED =======================================
	private void getAllALed(RoutingContext routingContext) {
		mySQLclient.query("SELECT * FROM proyecto_dad.actuadorled;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				System.out.println(resultSet.size());
				List<actuadorLedImpl> result = new ArrayList<>();
				for (Row elem : resultSet) {
					result.add(new actuadorLedImpl(elem.getInteger("idled"), elem.getDouble("nivel_luz"),
							elem.getLong("fecha"), elem.getInteger("groupid"), elem.getBoolean("estado")));
				}
				System.out.println("resultado ="+ result.toString());
				routingContext.response().setStatusCode(201).end("Datos del actuador recibidos correctamente\n"+result.toString());

			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response().setStatusCode(201).end("Datos del actuador no recibidos correctamente");
			}
		});
	}

	private void getByActLed(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idled"));
		mySQLclient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyecto_dad.actuadorled WHERE idled = ?")
						.execute(Tuple.of(id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<actuadorLedImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new actuadorLedImpl(elem.getInteger("idled"),
											elem.getDouble("nivel_luz"), elem.getLong("fecha"),
											elem.getInteger("groupId"), elem.getBoolean("estado")));
								}
								System.out.println(result.toString());
								routingContext.response().setStatusCode(201)
										.end("Datos del actuador recibidos correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(201)
										.end("Datos del actuador no recibidos correctamente");
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}

	private void getLastByActLed(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idled"));
		mySQLclient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result()
						.preparedQuery(
								"SELECT * FROM proyecto_dad.actuadorled WHERE idled = ? ORDER BY fecha DESC LIMIT 1")
						.execute(Tuple.of(id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<actuadorLedImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new actuadorLedImpl(elem.getInteger("idled"),
											elem.getDouble("nivel_luz"), elem.getLong("fecha"),
											elem.getInteger("groupId"), elem.getBoolean("estado")));
								}
								System.out.println(result.toString());
								routingContext.response().setStatusCode(201)
										.end("Datos del actuador recibidos correctamente");

							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(201)
										.end("Datos del actuador no recibidos correctamente");

							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}

	private void addALed(RoutingContext routing) {
		final actuadorLedImpl led = gson.fromJson(routing.getBodyAsString(), actuadorLedImpl.class);

		mySQLclient
				.preparedQuery(
						"INSERT INTO actuadorled (idled, nivel_luz, fecha, groupId, estado) valorS (?, ?, ?, ?, ?)")
				.execute((Tuple.of(led.getIdled(), led.getNivel_luz(), led.getfecha(), led.getGroupId(),
						led.getestado())), res -> {
							if (res.succeeded()) {
								routing.response().setStatusCode(201)
										.putHeader("content-type", "application/json; charset=utf-8")
										.end("Sensor añadido");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routing.response().setStatusCode(201).end("Datos del led no recibidos");
							}
						});
	}

	// ============================Servo==================================
	private void getAllAServo(RoutingContext routingContext) {
		mySQLclient.query("SELECT * FROM proyecto_dad.actuadorServo;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				System.out.println(resultSet.size());
				List<actuadorServoImpl> result = new ArrayList<>();
				for (Row elem : resultSet) {
					result.add(new actuadorServoImpl(elem.getInteger("idServo"), elem.getInteger("valor"),
							elem.getLong("fecha"), elem.getInteger("groupId"), elem.getBoolean("estado")));
				}
				System.out.println(result.toString());
				routingContext.response().setStatusCode(201).end("Datos del actuador recibidos correctamente");

			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response().setStatusCode(201).end("Datos del actuador no recibidos correctamente");
			}
		});
	}

	private void getByActServo(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idServo"));
		mySQLclient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result().preparedQuery("SELECT * FROM proyecto_dad.actuadorServo WHERE idServo = ?")
						.execute(Tuple.of(id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<actuadorServoImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new actuadorServoImpl(elem.getInteger("idServo"),
											elem.getInteger("valor"), elem.getLong("fecha"), elem.getInteger("groupId"),
											elem.getBoolean("estado")));
								}
								System.out.println(result.toString());
								routingContext.response().setStatusCode(201)
										.end("Datos del actuador recibidos correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(201)
										.end("Datos del actuador no recibidos correctamente");
							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}

	private void getLastByActServo(RoutingContext routingContext) {
		int id = Integer.parseInt(routingContext.request().getParam("idServo"));
		mySQLclient.getConnection(connection -> {
			if (connection.succeeded()) {
				connection.result()
						.preparedQuery(
								"SELECT * FROM proyecto_dad.actuadorServo WHERE idServo = ? ORDER BY fecha DESC LIMIT 1")
						.execute(Tuple.of(id), res -> {
							if (res.succeeded()) {
								// Get the result set
								RowSet<Row> resultSet = res.result();
								System.out.println(resultSet.size());
								List<actuadorServoImpl> result = new ArrayList<>();
								for (Row elem : resultSet) {
									result.add(new actuadorServoImpl(elem.getInteger("idServo"),
											elem.getInteger("valor"), elem.getLong("fecha"), elem.getInteger("groupId"),
											elem.getBoolean("estado")));
								}
								System.out.println(result.toString());
								routingContext.response().setStatusCode(201)
										.end("Datos del actuador recibidos correctamente");

							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(201)
										.end("Datos del actuador no recibidos correctamente");

							}
							connection.result().close();
						});
			} else {
				System.out.println(connection.cause().toString());
			}
		});
	}

	private void addAServo(RoutingContext routing) {
		final actuadorServoImpl Servo = gson.fromJson(routing.getBodyAsString(), actuadorServoImpl.class);

		mySQLclient
				.preparedQuery(
						"INSERT INTO actuadorServo (idServo, valor, fecha,  groupId, estado) valorS (?, ?, ?, ?, ?)")
				.execute((Tuple.of(Servo.getIdServo(), Servo.getvalor(), Servo.getfecha(), Servo.getGroupId(),
						Servo.getestado())), res -> {
							if (res.succeeded()) {
								routing.response().setStatusCode(201)
										.putHeader("content-type", "application/json; charset=utf-8")
										.end("Actuador añadido");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routing.response().setStatusCode(201).end("Datos del Servo no recibidos");
							}
						});
	}

}
