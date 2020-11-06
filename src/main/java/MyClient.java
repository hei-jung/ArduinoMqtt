import io.nats.client.Connection;
import io.nats.client.Nats;
import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MyClient implements MqttCallback {

    //    private static final int DATA_SIZE = 1024;
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
        } catch (Exception e) {
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
        String data = mqttMessage.toString();//format: [1.xx,1.xx,1.xx,...]

        System.out.println(payload.length + " from " + s + ": " + data);

        //String[] data2 = data.split(",");
        //double[] values = new double[data2.length];
        //byte[] message = new byte[values.length];

        //for (int i = 0; i < values.length; i++) {
        //    values[i] = Double.parseDouble(data2[i]);
        //    message[i] = (byte) values[i];
        //    //message = data2[i].getBytes(StandardCharsets.UTF_8);
        //}

        //nats.publish("test.1024", message);
        //nats.publish("test.1024", data.getBytes(StandardCharsets.UTF_8));
        nats.publish("test.1024", payload);

        // test publishing single double data
//        double value = Double.parseDouble(data);
//        System.out.println("convert to double: " + value);
//        String message = String.format("%f", value);
//        nats.publish("test.1024", message.getBytes(StandardCharsets.UTF_8));
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
