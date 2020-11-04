import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class SubscribeSync {
    public static void main(String[] args) {
        try {
            Connection nats = Nats.connect("nats://192.168.50.124:4222");
            boolean flag = true;
            while (flag) {
                Subscription sub = nats.subscribe("sensor");
                if (sub == null) {
                    flag = false;
                }
                Message message = sub.nextMessage(Duration.ZERO);
                String str = new String(message.getData(), StandardCharsets.UTF_8);
                System.out.println(str.length() + " from " + message.getSubject() + ": " + str);
            }
            nats.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
