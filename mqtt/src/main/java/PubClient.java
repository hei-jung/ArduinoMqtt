import io.nats.client.Connection;
import io.nats.client.Nats;
import org.eclipse.paho.client.mqttv3.*;

public class PubClient implements MqttCallback {

    private MqttClient client;
    private MqttConnectOptions option;
    private Connection nats;

    public PubClient init(String mqttURI, String natsURI, String clientId) {
        // Connect to MQTT server
        option = new MqttConnectOptions();
        option.setCleanSession(true);
        option.setKeepAliveInterval(30);
        try {
            client = new MqttClient(mqttURI, clientId);
            client.setCallback(this);
            client.connect(option);
        } catch (MqttException e) {
            //e.printStackTrace();
            System.out.println("[MQTT] failed to connect");
            System.exit(-1);
        }
        // Connect to NATS server
        try {
            nats = Nats.connect(natsURI);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("[NATS] failed to connect");
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
        System.out.println("connection lost");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        byte[] payload = mqttMessage.getPayload();
        String data = mqttMessage.toString();
        System.out.println(payload.length + " from " + s + ": " + data);

        /* mqtt 서버에서 받은 메시지 그대로 nats 서버로 전송 */
        //nats.publish("test.1024", data.getBytes(StandardCharsets.UTF_8));
        //nats.publish("test.1024", payload);

        /* mqtt 서버에서 받은 메시지를 double 배열 형태로 변환 */
        if (payload.length < 9) {
            System.out.println("cannot convert to double");
            return;
        }
        ConvertArray convert = new ConvertArray();
        // [1.xxx,1.xxx,1.xxx,...,1.xxx]에서 double 값만 추출해서 배열에 저장
        String[] str = data.substring(1, data.length() - 1).split(",");
        double[] d = convert.stringsToDoubles(str);
        convert.reverse(d);
        byte[] b = convert.doublesToBytes(d);
//        double[] d2 = convert.byteToDoubleArray(b);
//        convert.printDoubleArray(d2);
        nats.publish("test.1024", b);

        /* test publishing single double data */
//        //System.out.println(payload.length);
//        System.out.println("convert to double: " + value);
//        nats.publish("test.1024", payload);
//        String message = String.format("%f", value);
//        nats.publish("test.1024", message.getBytes(StandardCharsets.UTF_8));
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}