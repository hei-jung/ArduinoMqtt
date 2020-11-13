import java.nio.ByteBuffer;

public class Convert {
    public double[] stringToDoubleArray(String[] strings) {
        double[] doubles = new double[strings.length];
//        System.out.println("doubles.length="+doubles.length);
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = Double.parseDouble(strings[i]);
        }
        return doubles;
    }

    public byte[] doubleToByteArray(double[] doubles) {
        ByteBuffer buffer = ByteBuffer.allocate(doubles.length * 8);
        for (double d : doubles) {
            buffer.putDouble(d);
        }
        return buffer.array();
    }

    public double[] byteToDoubleArray(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        double[] doubles = new double[bytes.length / 8];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i] = buffer.getDouble();
        }
        return doubles;
    }

    public void printDoubleArray(double[] doubles) {
        for (double d : doubles) {
            System.out.print(d + "\t");
        }
        System.out.println();
    }
}
