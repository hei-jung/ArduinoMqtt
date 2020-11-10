import io.nats.client.Connection;
import io.nats.client.Nats;
import org.eclipse.paho.client.mqttv3.*;

import java.nio.charset.StandardCharsets;

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
        String data = mqttMessage.toString();
        System.out.println(payload.length + " from " + s + ": " + data);

        //nats.publish("test.1024", data.getBytes(StandardCharsets.UTF_8));
        nats.publish("test.1024", payload);


//        if (payload.length < 9) {
//            System.out.println("cannot convert to double");
//            return;
//        }
//        Convert convert = new Convert();
//        String[] str = data.substring(1, data.length() - 1).split(",");
//        double[] d = convert.stringToDoubleArray(str);
//        byte[] b = convert.doubleToByteArray(d);
////        double[] d2 = convert.byteToDoubleArray(b);
////        convert.showArray(d2);
//        nats.publish("test.1024", b);


        /* test publishing single double data */
        //System.out.println(payload.length);
        //double value = Double.parseDouble(data);
        //double value = ByteBuffer.wrap(payload).getDouble();
        //System.out.println("convert to double: " + value);
        //nats.publish("test.1024", payload);
        //String message = String.format("%f", value);
        //nats.publish("test.1024", message.getBytes(StandardCharsets.UTF_8));
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}