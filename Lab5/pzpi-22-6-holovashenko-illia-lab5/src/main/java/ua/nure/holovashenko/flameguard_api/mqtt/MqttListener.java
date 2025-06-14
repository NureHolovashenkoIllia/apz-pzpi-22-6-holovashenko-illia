package ua.nure.holovashenko.flameguard_api.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;
import ua.nure.holovashenko.flameguard_api.dto.MeasurementDto;
import ua.nure.holovashenko.flameguard_api.service.BuildingService;
import ua.nure.holovashenko.flameguard_api.service.MeasurementService;
import ua.nure.holovashenko.flameguard_api.service.SensorService;

@Component
public class MqttListener {

    private static final String SERVER_URI = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "FireSafetySubscriberClient2";
    private static final String TOPIC = "fire_safety/measurements";

    private final MeasurementService measurementService;
    private final ObjectMapper objectMapper;
    private final BuildingService buildingService;
    private final SensorService sensorService;

    private MqttClient client;

    public MqttListener(MeasurementService measurementService, ObjectMapper objectMapper, BuildingService buildingService, SensorService sensorService) {
        this.measurementService = measurementService;
        this.objectMapper = objectMapper;
        this.buildingService = buildingService;
        this.sensorService = sensorService;
    }

    @PostConstruct
    public void connectAndSubscribe() {
        try {
            client = new MqttClient(SERVER_URI, CLIENT_ID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(30);
            options.setKeepAliveInterval(60);

            System.out.println("Connecting to MQTT broker...");
            client.connect(options);
            System.out.println("Connected to MQTT broker.");

            client.subscribe(TOPIC, (topic, message) -> {
                String payload = new String(message.getPayload());
                System.out.println("Message received: " + payload);

                try {
                    if (!payload.trim().startsWith("{") || !payload.trim().endsWith("}")) {
                        throw new IllegalArgumentException("The message is not in JSON format");
                    }

                    JsonNode jsonNode = objectMapper.readTree(payload);

                    if (payload.contains("sensorId") && payload.contains("measurementValue")) {
                        MeasurementDto measurementDto = objectMapper.readValue(payload, MeasurementDto.class);
                        measurementService.createMeasurement(measurementDto);
                        sensorService.updateLastDataReceived(measurementDto.getSensorId(), measurementDto.getDateTimeReceived());
                    } else if (payload.contains("buildingId") && payload.contains("buildingCondition")) {
                        Integer buildingId = jsonNode.get("buildingId").asInt();
                        String buildingCondition = jsonNode.get("buildingCondition").asText();
                        buildingService.updateBuildingCondition(buildingId, buildingCondition);
                    } else {
                        System.err.println("Unknown message format.");
                    }
                } catch (Exception e) {
                    System.err.println("Message processing error: " + e.getMessage());
                }
            });

            System.out.println("Subscribe to the topic: " + TOPIC);

        } catch (MqttException e) {
            System.err.println("Error connecting to MQTT broker: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error connecting to MQTT broker: " + e.getMessage(), e);
        }
    }
}
