public class SubMain {
    public static void main(String[] args) {
        NatsClient client = new NatsClient();
//        client.init("nats://127.0.0.1:4222").subscribePrint("subject.1");
        client.init("nats://192.168.0.4:4222").subscribePrint("test.1024");
    }
}
