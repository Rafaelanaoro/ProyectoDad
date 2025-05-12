#include <HTTPClient.h>
#include "ArduinoJson.h"
#include <WiFiUdp.h>
#include <PubSubClient.h>
#include <DHT.h>
#include <DHT_U.h>
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BusIO_Register.h>



#define DHTTYPE DHT11
#define DHTPIN 33
#define pin_ven 14
#define led_luz 26

#define BH170_ADDRESS 0x5C

DHT dht(DHTPIN, DHTTYPE);

// Replace 0 by ID of this current device
const int DEVICE_ID = 124;
int cont = 0;
int test_delay = 1000; // so we don't spam the API
boolean describe_tests = true;

// Replace 0.0.0.0 by your server local IP (ipconfig [windows] or ifconfig [Linux o MacOS] gets IP assigned to your PC)
String serverName = "http://192.168.248.210:8084/"; //192.168.197.210
HTTPClient http;
String serverPath = "/api/NFC";
String serv = "http://192.168.248.210:8084/api/NFC";

// Replace WifiName and WifiPassword by your WiFi credentials
#define STASSID "redmi6"    //"Your_Wifi_SSID"
#define STAPSK "mamamama" //"Your_Wifi_PASSWORD"



// MQTT configuration
WiFiClient espClient;
PubSubClient client(espClient);

// Server IP, where de MQTT broker is deployed
const char *MQTT_BROKER_ADRESS = "192.168.248.210"; 
const uint16_t MQTT_PORT = 1883;

// Name for this MQTT client
const char *MQTT_CLIENT_NAME = "ArduinoClient_1";

// callback a ejecutar cuando se recibe un mensaje
// en este ejemplo, muestra por serial el mensaje recibido
void OnMqttReceived(char *topic, byte *payload, unsigned int length)
{
  Serial.print("Received on ");
  Serial.print(topic);
  Serial.print(": ");

  String content = "";
  for (size_t i = 0; i < length; i++)
  {
    content.concat((char)payload[i]);
  }
  Serial.print(content);
  Serial.println();
  
  Serial.println("Hasta aqui hemos llegao");
  //Serial.println(*payload);

  if(content == "ON"){
      digitalWrite(pin_ven, HIGH);
  }
  if(content == "OFF"){
      digitalWrite(pin_ven, LOW);
  }

}

// inicia la comunicacion MQTT
// inicia establece el servidor y el callback al recibir un mensaje
void InitMqtt()
{
  client.setServer(MQTT_BROKER_ADRESS, MQTT_PORT);
  client.setCallback(OnMqttReceived);
}

// Setup
void setup()
{
  Serial.begin(9600);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(STASSID);

  /* Explicitly set the ESP32 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network. */
  WiFi.mode(WIFI_STA);
  WiFi.begin(STASSID, STAPSK);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  InitMqtt();


  dht.begin();
  
  pinMode(pin_ven, OUTPUT);
  
  digitalWrite(pin_ven, LOW);
  Serial.println("Dht begin OK");
  
  
  pinMode(led_luz, OUTPUT);
  digitalWrite(led_luz, LOW);
 // Configura el sensor BH170
  Wire.begin(21, 22); // SDA = 21, SCL = 22
  Wire.beginTransmission(BH170_ADDRESS);
  Wire.write(0x10);  // Modo de alta resolución
  Wire.endTransmission();
  Serial.println("BH170 begin OK");
  

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println("Setup!");
}

// conecta o reconecta al MQTT
// consigue conectar -> suscribe a topic y publica un mensaje
// no -> espera 5 segundos
void ConnectMqtt()
{
  Serial.print("Starting MQTT connection...");
  if (client.connect(MQTT_CLIENT_NAME))
  {
    client.subscribe("twmp");
    client.publish("twmp", "connected");
  }
  else
  {
    Serial.print("Failed MQTT connection, rc=");
    Serial.print(client.state());
    Serial.println(" try again in 5 seconds");

    delay(5000);
  }
}

// gestiona la comunicación MQTT
// comprueba que el cliente está conectado
// no -> intenta reconectar
// si -> llama al MQTT loop

void HandleMqtt()
{
  if (!client.connected())
  {
    ConnectMqtt();
  }
  client.loop();
}

String response;

//Hemos puesto un serialize para cada tipo de sensor y para cada tipo de actuador, es decir, 4 serialize

String serializeSensorNFCValueBody(int idNFC, Interger valor, long fecha, int groupId, Boolean estado )
{

  // StaticJsonObject allocates memory on the stack, it can be
  // replaced by DynamicJsonDocument which allocates in the heap.
  //
  DynamicJsonDocument doc(2048);
  // Add values in the document
  //
  doc["idNFC"] = idNFC;
  doc["valor"] = valor;
  doc["fecha"] = fecha;
  doc["groupId"] = groupId;
  doc["estado"] = estado;
  //doc["removed"] = false;
  // Generate the minified JSON and send it to the Serial port.
  //
  String output;
  serializeJson(doc, output);
  Serial.println(output);
  return output;
}

String serializeActuatorLedStatusBody(int idled, double nivel_luz, long fecha, int groupId, boolean estado )
{
  DynamicJsonDocument doc(2048);

  doc["idled"] = idled;
  doc["nivel_luz"] = nivel_luz;
  doc["fecha"] = fecha;
  doc["idP"] = idgroupIdP;
  doc["idG"] = estado;

  String output;
  serializeJson(doc, output);
  return output;
}
String serializeActuatorServoStatusBody(int idServo, Interger valor, long fecha, int groupId, boolean estado)
{
  DynamicJsonDocument doc(2048);

  doc["idServo"] = idServo;
  doc["valor"] = valor;
  doc["fecha"] = fecha;
  doc["idP"] = groupId;
  doc["idG"] = estado;

  String output;
  serializeJson(doc, output);
  return output;
}

// String serializeDeviceBody(String deviceSerialId, String name, String mqttChannel, int idGroup)
// {
//   DynamicJsonDocument doc(2048);
//   doc["deviceSerialId"] = deviceSerialId;
//   doc["name"] = name;
//   doc["mqttChannel"] = mqttChannel;
//   doc["idGroup"] = idGroup;
//   String output;
//   serializeJson(doc, output);
//   return output;
// }


/*void deserializeActuatorStatusBody(String responseJson)
{
  if (responseJson != "")
  {
    DynamicJsonDocument doc(2048);

    // Deserialize the JSON document
    DeserializationError error = deserializeJson(doc, responseJson);

    // Test if parsing succeeds.
    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    // Fetch values.
    int idActuatorState = doc["idActuatorState"];
    float status = doc["status"];
    bool statusBinary = doc["statusBinary"];
    int idActuator = doc["idActuator"];
    long timestamp = doc["timestamp"];

    Serial.println(("Actuator status deserialized: [idActuatorState: " + String(idActuatorState) + ", status: " + String(status) + ", statusBinary: " + String(statusBinary) + ", idActuator" + String(idActuator) + ", timestamp: " + String(timestamp) + "]").c_str());
  }
}
//Hacer get del device
void deserializeDeviceBody(int httpResponseCode)
{

  if (httpResponseCode > 0)
  {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    String responseJson = http.getString();
    DynamicJsonDocument doc(2048);

    DeserializationError error = deserializeJson(doc, responseJson);

    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    int idDevice = doc["idDevice"];
    String deviceSerialId = doc["deviceSerialId"];
    String name = doc["name"];
    String mqttChannel = doc["mqttChannel"];
    int idGroup = doc["idGroup"];

    Serial.println(("Device deserialized: [idDevice: " + String(idDevice) + ", name: " + name + ", deviceSerialId: " + deviceSerialId + ", mqttChannel" + mqttChannel + ", idGroup: " + idGroup + "]").c_str());
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}
  // Hacer get en los sensores
void deserializeSensorsFromDevice(int httpResponseCode)
{

  if (httpResponseCode > 0)
  {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    String responseJson = http.getString();
    // allocate the memory for the document
    DynamicJsonDocument doc(ESP.getMaxAllocHeap());

    // parse a JSON array
    DeserializationError error = deserializeJson(doc, responseJson);

    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    // extract the values
    JsonArray array = doc.as<JsonArray>();
    for (JsonObject sensor : array)
    {
      int idSensor = sensor["idSensor"];
      String name = sensor["name"];
      String sensorType = sensor["sensorType"];
      int idDevice = sensor["idDevice"];

      Serial.println(("Sensor deserialized: [idSensor: " + String(idSensor) + ", name: " + name + ", sensorType: " + sensorType + ", idDevice: " + String(idDevice) + "]").c_str());
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}

void deserializeActuatorsFromDevice(int httpResponseCode)
{

  if (httpResponseCode > 0)
  {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    String responseJson = http.getString();
    // allocate the memory for the document
    DynamicJsonDocument doc(ESP.getMaxAllocHeap());

    // parse a JSON array
    DeserializationError error = deserializeJson(doc, responseJson);

    if (error)
    {
      Serial.print(F("deserializeJson() failed: "));
      Serial.println(error.f_str());
      return;
    }

    // extract the values
    JsonArray array = doc.as<JsonArray>();
    for (JsonObject sensor : array)
    {
      int idActuator = sensor["idActuator"];
      String name = sensor["name"];
      String actuatorType = sensor["actuatorType"];
      int idDevice = sensor["idDevice"];

      Serial.println(("Actuator deserialized: [idActuator: " + String(idActuator) + ", name: " + name + ", actuatorType: " + actuatorType + ", idDevice: " + String(idDevice) + "]").c_str());
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}



void GET_tests()
{
  describe("Test GET full device info");
  String serverPath = serverName + "api/devices/" + String(DEVICE_ID);
  http.begin(serverPath.c_str());
  // test_response(http.GET());
  deserializeDeviceBody(http.GET());

  describe("Test GET sensors from deviceID");
  serverPath = serverName + "api/devices/" + String(DEVICE_ID) + "/sensors";
  http.begin(serverPath.c_str());
  deserializeSensorsFromDevice(http.GET());

  describe("Test GET actuators from deviceID");
  serverPath = serverName + "api/devices/" + String(DEVICE_ID) + "/actuators";
  http.begin(serverPath.c_str());
  deserializeActuatorsFromDevice(http.GET());

  describe("Test GET sensors from deviceID and Type");
  serverPath = serverName + "api/devices/" + String(DEVICE_ID) + "/sensors/Temperature";
  http.begin(serverPath.c_str());
  deserializeSensorsFromDevice(http.GET());

  describe("Test GET actuators from deviceID");
  serverPath = serverName + "api/devices/" + String(DEVICE_ID) + "/actuators/Relay";
  http.begin(serverPath.c_str());
  deserializeActuatorsFromDevice(http.GET());
}*/

void test_response(int httpResponseCode)
{
  delay(test_delay);
  if (httpResponseCode > 0)
  {
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    String payload = http.getString();
    Serial.println(payload);
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
  }
}

void describe(char *description)
{
  if (describe_tests)
    Serial.println(description);
}
void POST_tests()
{

  //TEST TEMPERATURA/VENTILADOR

  String actuator_states_bodyf = serializeActuatorServoStatusBody(1, 20, random(2000, 4000), 1, 1);
  describe("Test POST with actuator state");
  String serverPathtemp = serverName + "api/actServo";
  http.begin(serverPathtemp.c_str());
  test_response(http.POST(actuator_states_bodyf));

  String actuator_states_bodyle = serializeActuatorLedStatusBody(1, 20.0, random(2000, 4000), 1, 1);
  describe("Test POST with actuator state");
  String serverPathluz = serverName + "api/actLed";
  http.begin(serverPathluz.c_str());
  test_response(http.POST(actuator_states_bodyle));

  String sensor_value_bodyT = serializeSensorNFCValueBody(7, random(1, 5), random(2000, 4000), 1, 1);
  describe("Test POST with sensor value");
  serverPathtemp = serverName + "api/NFC";
  http.begin(serverPathtemp.c_str());
  test_response(http.POST(sensor_value_bodyT));


  // String device_body = serializeDeviceBody(String(DEVICE_ID), ("Name_" + String(DEVICE_ID)).c_str(), ("mqtt_" + String(DEVICE_ID)).c_str(), 12);
  // describe("Test POST with path and body and response");
  // serverPath = serverName + "api/device";
  // http.begin(serverPath.c_str());
  // test_response(http.POST(actuator_states_body));
}

// Run the tests!

void loop()
{


  //GET_tests();
  //POST_tests();

//////////////////////
//***TEMPERATURA****//
//////////////////////

  /*float temperature = dht.readTemperature();

  Serial.println("Temperatura leida: ");
  Serial.println(temperature);
  
  
  
  if(temperature > TEMPERATURE_THRESHOLD){
    digitalWrite(pin_ven, HIGH);
    //actuador = 1; 
  }else{
    //actuador = 0; 
    digitalWrite(pin_ven, LOW);
  }*/
  delay(2000);
  
  double NFC = dht.readNFC();
  String sensorValue;
  sensorValue = serializeSensorNFCValueBody(1, NFC, 5000, 1, 1);
  
  //http.begin(serverPath.c_str());
  //http.begin(serv.c_str());  
  http.begin(serv.c_str());

  test_response(http.POST(sensorValue));

  HandleMqtt();
  
  
}

