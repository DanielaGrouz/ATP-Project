package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A custom InputStream that decompresses data compressed with MyCompressorOutputStream.
 * Reads 20 header bytes as-is, then unpacks 8 bits from each following byte.
 */
public class MyDecompressorInputStream extends InputStream {
    private InputStream in;
    private ArrayList<Byte> matrix;
    private int index;

    /**
     * Creates a new decompressor that reads and decompresses the input stream.
     *
     * @param inputStream the compressed input stream
     * @throws IOException if an I/O error occurs while reading
     */
    public MyDecompressorInputStream(InputStream inputStream) throws IOException {
        this.in = inputStream;
        this.index = 0;
        decompress();
    }

    /**
     * Decompresses the input stream.
     * First reads 20 bytes (header), then unpacks the remaining bytes bit by bit.
     *
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Returns the next decompressed byte.
     *
     * @return the next byte, or -1 if end of data
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read() {
        if (index < matrix.size()){
            return (int) matrix.get(index++);
        }
        return -1;
    }
}
