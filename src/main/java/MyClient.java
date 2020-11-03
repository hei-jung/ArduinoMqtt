import io.nats.client.Connection;
import io.nats.client.Nats;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;

public class MyClient implements MqttCallback {

    private MqttClient client;
    private MqttConnectOptions option;
    private Connection nats;

    public MyClient init(String serverURI, String clientId) {
        // MQTT 서버 연결
        option = new MqttConnectOptions();
        option.setCleanSession(true);
        option.setKeepAliveInterval(30);
        try {
            client = new MqttClient(serverURI, clientId);
            client.setCallback(this);
            client.connect(option);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        // NATS 서버 연결
        try {
            nats = Nats.connect("nats://192.168.50.124:4222");
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
        System.out.println(s + ": " + mqttMessage.toString());
        System.out.println("total size: " + mqttMessage.toString().length());
        nats.publish("sensor", mqttMessage.getPayload());

    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
