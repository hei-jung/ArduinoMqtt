import io.nats.client.Connection;
import io.nats.client.Nats;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;

public class MyClient implements MqttCallback {

    private static int DATA_SIZE = 1024;
    private MqttClient client;
    private MqttConnectOptions option;
    private Connection nats;

    public MyClient init(String mqttURI, String natsURI, String clientId) {
        // Connect to MQTT server
        option = new MqttConnectOptions();
        option.setCleanSession(true);
        option.setKeepAliveInterval(30);
        try {
            client = new MqttClient(mqttURI, clientId);
            client.setCallback(this);
            client.connect(option);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        // Connect to NATS server
        try {
            nats = Nats.connect(natsURI);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean subscribe(String... topics) {
        try {
            if (topics != null) {
                for (String topic : topics) {
                    client.subscribe(topic, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void connectionLost(Throwable throwable) {

    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        byte[] payload = mqttMessage.getPayload();
        String data = mqttMessage.toString();//[1.xx,1.xx,1.xx,...]

        System.out.println(payload.length + " from " + s + ": " + data);

        //nats.publish("sensor", data.getBytes(StandardCharsets.UTF_8));
        nats.publish("sensor", payload);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
