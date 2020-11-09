import io.nats.client.Connection;
import io.nats.client.Nats;
import org.eclipse.paho.client.mqttv3.*;

import java.nio.ByteBuffer;
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
        String data = mqttMessage.toString();

        System.out.println(payload.length + " from " + s + ": " + data);

//        String[] data2 = data.split(",");
//        double[] values = new double[data2.length];
//        for (int i = 0; i < values.length; i++) {
//            values[i] = Double.parseDouble(data2[i]);
//        }
//
//        byte[] message = convertDoubleToByteArray(values);
//        double[] fe = convertByteToDoubleArray(message);
//        showArray(fe);

        // 먼저 mqtt 퍼블리셔한테서 받은 byte 배열 -> double 배열
        String[] data2 = data.split(",");
        double[] values = new double[data2.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Double.parseDouble(data2[i]);
        }
        //showArray(values);
        long[] longs = new long[values.length];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = Double.doubleToLongBits(values[i]);
        }
        byte[] bytes = new byte[longs.length * 8];
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        for (long l : longs) {
//            buf.putLong(l);
            buf.putDouble(Double.longBitsToDouble(l));
            System.out.print(Double.longBitsToDouble(l) +"\t");
        }
        System.out.println();
        nats.publish("test.1024", bytes);

        //nats.publish("test.1024", message);
        //nats.publish("test.1024", data.getBytes(StandardCharsets.UTF_8));
        //nats.publish("test.1024", payload);

        // test publishing single double data
        //System.out.println(payload.length);
        //double value = Double.parseDouble(data);
        //double value = ByteBuffer.wrap(payload).getDouble();
        //System.out.println("convert to double: " + value);
        //nats.publish("test.1024", payload);
        //String message = String.format("%f", value);
//        nats.publish("test.1024", message.getBytes(StandardCharsets.UTF_8));
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    public byte[] convertDoubleToByteArray(double[] doubles) {
        ByteBuffer bb = ByteBuffer.allocate(doubles.length * 8);
        for (double d : doubles) {
            bb.putDouble(d);
        }
        return bb.array();
    }

    public double[] convertByteToDoubleArray(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        double[] doubles = new double[bytes.length / 8];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = bb.getDouble();
        }
        return doubles;
    }

    public void showArray(double[] doubles) {
        for (double d : doubles) {
            System.out.print(d + "\t");
        }
        System.out.println();
    }
}
