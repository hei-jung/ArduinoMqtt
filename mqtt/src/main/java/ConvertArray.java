import java.nio.ByteBuffer;

public class ConvertArray {
    public double[] stringsToDoubles(String[] strings) {
        double[] doubles = new double[strings.length];
//        System.out.println("doubles.length="+doubles.length);
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = Double.parseDouble(strings[i]);
        }
        return doubles;
    }

    public byte[] doublesToBytes(double[] doubles) {
        ByteBuffer buffer = ByteBuffer.allocate(doubles.length * 8);
        for (double d : doubles) {
            buffer.putDouble(d);
        }
        return buffer.array();
    }

    public double[] bytesToDoubles(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        double[] doubles = new double[bytes.length / 8];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = buffer.getDouble();
        }
        return doubles;
    }

    public void printDoubles(double[] doubles) {
        for (double d : doubles) {
            System.out.print(d + "\t");
        }
        System.out.println();
    }

    public void reverse(double[] doubles) {
        int end = doubles.length - 1;
        int mid = end / 2;
        for (int i = 0; i < mid; i++) {
            double temp = 0.0;
            /* swap */
            temp = doubles[end - i];
            doubles[end - i] = doubles[i];
            doubles[i] = temp;
        }
    }
}
