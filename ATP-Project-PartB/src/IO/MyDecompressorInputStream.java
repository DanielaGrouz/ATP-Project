package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyDecompressorInputStream extends InputStream {
    private InputStream in;
    private ArrayList<Byte> matrix;
    private int index;

    public MyDecompressorInputStream(InputStream inputStream) throws IOException {
        this.in = inputStream;
        this.index = 0;
        decompress();
    }

    private void decompress() throws IOException {
        int HEADER_SIZE = 4 * 5;
        ArrayList<Byte> byteArray = new ArrayList<>(HEADER_SIZE);
        //read and store the header bytes as-is
        for(int i=0;i<HEADER_SIZE;i++){
            byteArray.add((byte) in.read());
        }
        //read the compressed data (bit-packed)
        int currentByte;
        while ((currentByte = in.read()) != -1) {
            //extract 8 bits from the byte (MSB to LSB)
            for (int i = 7; i >= 0; i--) {
                int bit = (currentByte >> i) & 1;
                byteArray.add((byte) bit);
            }
        }
        matrix = byteArray;
    }

    @Override
    public int read() throws IOException {
        if (index < matrix.size()){
            return (int) matrix.get(index++);
        }
        return -1;
    }
}
