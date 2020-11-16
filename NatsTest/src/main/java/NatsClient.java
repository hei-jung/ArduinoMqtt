import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import io.nats.client.Subscription;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class NatsClient {

    private Connection conn;

    public NatsClient init(String natsURI) {
        try {
            conn = Nats.connect(natsURI);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean subscribePrint(String subject) {
        try {
            while (true) {
                Subscription sub = conn.subscribe(subject);
                if (sub == null) {
                    conn.close();
                    break;
                }
                Message msg = sub.nextMessage(Duration.ZERO);
                byte[] data = msg.getData();//nats message type is byte[]

                //convert the byte array to a double array
                //to check subscription
                ConvertArray conv = new ConvertArray();
                double[] values = conv.bytesToDoubles(data);
                System.out.print(values.length + " from " + msg.getSubject() + ": ");
                conv.printDoubles(values);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean publishBytes(String subject) {
        //double array to publish
        double[] doubles = {1.123, 1.532, 1.324, 1.042, 1.738};//these are random numbers
//        String[] strings = {"1.123", "1.532", "1.324", "1.042", "1.738"};
//        String msg = String.join(",", strings);

        //convert to byte array
        ConvertArray conv = new ConvertArray();
        byte[] bytes = conv.doublesToBytes(doubles);

        while (true) {
            try {
                conv.printDoubles(doubles);
                conn.publish(subject, bytes);
//                conn.publish(subject, msg.getBytes());
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
