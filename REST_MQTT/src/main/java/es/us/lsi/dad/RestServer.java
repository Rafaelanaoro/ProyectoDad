package es.us.lsi.dad;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import io.vertx.core.buffer.Buffer;

public class RestServer extends AbstractVerticle {

	private Gson gson;

	MySQLPool mySQLclient;
	MqttClient mqttClient;

	public void start(Promise<Void> startFuture) {
		mqttClient = MqttClient.create(vertx, new MqttClientOptions().setAutoKeepAlive(true));

		mqttClient.connect(1883, "localhost", s -> {

			mqttClient.subscribe("twmp", MqttQoS.AT_LEAST_ONCE.value(), handler -> {
				if (handler.succeeded()) {
					System.out.println("Suscripcion " + mqttClient.clientId());
				}
			});

		});
		MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("basedatosdad").setUser("dad").setPassword("daddad");

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
		router.get("/api/NFC/:idNFC/last").handler(this::getLastBySensorNFC);// no
		router.post("/api/NFC").handler(this::addSensorNFC);// no

		router.get("/api/actLed").handler(this::getAllALed);
		router.get("/api/actLed/:idled").handler(this::getByActLed);
		router.get("/api/actLed/:idled/last").handler(this::getLastByActLed);
		router.post("/api/actLed").handler(this::addALed);

		router.get("/api/actServo").handler(this::getAllAServo);
		router.get("/api/actServo/:idServo").handler(this::getByActServo);
		router.get("/api/actServo/:idServo/last").handler(this::getLastByActServo);
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
				routingContext.response().setStatusCode(200).end("Datos del sensor recibidos correctamente");

			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response().setStatusCode(500).end("Datos del sensor no recibidos correctamente");
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
								routingContext.response().setStatusCode(200)
										.end("Datos del sensor recibidos correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500)
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
						.preparedQuery("SELECT * FROM proyecto_dad.sensorNFC WHERE idNFC = ? ORDER BY fecha DESC LIMIT 1")
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
								routingContext.response().setStatusCode(200)
										.end("Datos del sensor recibidos correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500)
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
		NFC.setfecha(Calendar.getInstance().getTimeInMillis());
		mySQLclient.preparedQuery("INSERT INTO sensorNFC (idNFC, valor, fecha, groupId, estado) VALUES (?, ?, ?, ?, ?)")
				.execute((Tuple.of(NFC.getIdNFC(), NFC.getvalor(), NFC.getfecha(), NFC.getGroupId(), NFC.getestado())),
						res -> {
							if (res.succeeded()) {
								if (mqttClient != null) {
									if (NFC.valor == 1) {
										mqttClient.publish("twmp", Buffer.buffer("ON"), MqttQoS.AT_LEAST_ONCE, false,
												false);
									} else {
										mqttClient.publish("twmp", Buffer.buffer("OFF"), MqttQoS.AT_LEAST_ONCE, false,
												false);
									}
								} else {
									System.out.println("mqttClient is null. Cannot publish message.");

								}
								routingContext.response().setStatusCode(201)
										.putHeader("content-type", "application/json; charset=utf-8")
										.end("Sensor añadido correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500)
										.end("Error al añadir el sensor " + res.cause().getMessage());
							}
						});
	}

	// **************************** Actuadores ********************************
	// =============================LED =======================================

	private void getAllALed(RoutingContext routingContext) {
		mySQLclient.query("SELECT * FROM basedatosdad.led;").execute(res -> {
			if (res.succeeded()) {
				// Get the result set
				RowSet<Row> resultSet = res.result();
				System.out.println(resultSet.size());
				List<actuadorLedImpl> result = new ArrayList<>();
				for (Row elem : resultSet) {
					result.add(new actuadorLedImpl(elem.getInteger("idled"), elem.getDouble("nivel_luz"),
							elem.getLong("fecha"), elem.getInteger("groupId"), elem.getBoolean("estado")));
				}
				System.out.println(result.toString());
				routingContext.response().setStatusCode(200).end("Datos del actuador recibidos correctamente");

			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response().setStatusCode(500).end("Datos del actuador no recibidos correctamente");
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
								routingContext.response().setStatusCode(200)
										.end("Datos del actuador recibidos correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500)
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
								routingContext.response().setStatusCode(200)
										.end("Datos del actuador recibidos correctamente");

							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500)
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
						"INSERT INTO actuadorled (idled, nivel_luz, fecha, groupId, estado) VALUES (?, ?, ?, ?, ?)")
				.execute((Tuple.of(led.getIdled(), led.getNivel_luz(), led.getfecha(), led.getGroupId(),
						led.getestado())), res -> {
							if (res.succeeded()) {
								routing.response().setStatusCode(201)
										.putHeader("content-type", "application/json; charset=utf-8")
										.end("Actuador añadido");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routing.response().setStatusCode(500).end("Datos del led no recibidos");
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
				routingContext.response().setStatusCode(200).end("Datos del actuador recibidos correctamente");

			} else {
				System.out.println("Error: " + res.cause().getLocalizedMessage());
				routingContext.response().setStatusCode(500).end("Datos del actuador no recibidos correctamente");
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
									result.add(new actuadorServoImpl(elem.getInteger("idServo"), elem.getInteger("valor"),
											elem.getLong("fecha"), elem.getInteger("groupId"),
											elem.getBoolean("estado")));
								}
								System.out.println(result.toString());
								routingContext.response().setStatusCode(200)
										.end("Datos del actuador recibidos correctamente");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500)
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
									result.add(new actuadorServoImpl(elem.getInteger("idServo"), elem.getInteger("valor"),
											elem.getLong("fecha"), elem.getInteger("groupId"),
											elem.getBoolean("estado")));
								}
								System.out.println(result.toString());
								routingContext.response().setStatusCode(200)
										.end("Datos del actuador recibidos correctamente");

							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routingContext.response().setStatusCode(500)
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
				.preparedQuery("INSERT INTO actuadorServo (idServo, valor, fecha,  groupId, estado) VALUES (?, ?, ?, ?, ?)")
				.execute((Tuple.of(Servo.getIdServo(), Servo.getvalor(), Servo.getfecha(), Servo.getGroupId(),
						Servo.getestado())), res -> {
							if (res.succeeded()) {
								routing.response().setStatusCode(201)
										.putHeader("content-type", "application/json; charset=utf-8")
										.end("Actuador añadido");
							} else {
								System.out.println("Error: " + res.cause().getLocalizedMessage());
								routing.response().setStatusCode(500).end("Datos del Servo no recibidos");
							}
						});
	}

}
