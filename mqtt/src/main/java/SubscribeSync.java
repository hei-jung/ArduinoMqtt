import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;

import java.time.Duration;

public class SubscribeSync {

    public static void main(String[] args) {
        try {
            Connection nats = Nats.connect("nats://192.168.0.4:4222");
            boolean flag = true;
            while (flag) {
                Subscription sub = nats.subscribe("test.1024");
                if (sub == null) {
                    flag = false;
                }
                Message message = sub.nextMessage(Duration.ZERO);

                /* MyClient.java 에서 mqtt 메시지를 double 배열로 변환해서 보낼 경우 */
                ConvertArray convert = new ConvertArray();
                byte[] data = message.getData();
                double[] values = convert.bytesToDoubles(data);
                System.out.print(values.length + " from " + message.getSubject() + ": ");
                convert.printDoubles(values);

                /* MyClient.java 에서 mqtt 메시지 그대로 보낼 경우 */
//                String str = new String(message.getData(), StandardCharsets.UTF_8);
//                System.out.println((str.length() / 8) + " from " + message.getSubject() + ": " + str);
            }
            nats.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
