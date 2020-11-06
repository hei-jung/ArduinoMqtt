public class Main {
    public static void main(String[] args) {
        MyClient client = new MyClient();

        client.init("tcp://192.168.50.124:1883", "nats://192.168.0.4:4222", "esp32-sub")
                .subscribe(new String("sensor/#"));
    }
}
