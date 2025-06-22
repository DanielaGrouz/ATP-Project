package IO;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
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
//    private void decompress() throws IOException {
//        int HEADER_SIZE = 4 * 5;
//        ArrayList<Byte> byteArray = new ArrayList<>(HEADER_SIZE);
//        //read and store the header bytes as-is
//        for(int i=0;i<HEADER_SIZE;i++){
//            byteArray.add((byte) in.read());
//        }
//        //read the compressed data (bit-packed)
//        int currentByte;
//        while ((currentByte = in.read()) != -1) {
//            //extract 8 bits from the byte (MSB to LSB)
//            for (int i = 7; i >= 0; i--) {
//                int bit = (currentByte >> i) & 1;
//                byteArray.add((byte) bit);
//            }
//        }
//        matrix = byteArray;
//    }
    private void decompress() throws IOException {
        final int HEADER_SIZE = 20;                 // 5 ints × 4-bytes
        ArrayList<Byte> byteArray = new ArrayList<>(HEADER_SIZE);

        /* 1. קוראים את הכותרת כפי שהיא ומעתיקים ל-byteArray */
        byte[] header = new byte[HEADER_SIZE];
        if (in.read(header) != HEADER_SIZE)
            throw new IOException("Header too short");
        for (byte b : header)
            byteArray.add(b);

        /* 2. שולפים rows (עמדה 16-19) – columns לא נמצא בכותרת */
        int rows = ByteBuffer.wrap(header).getInt(16);

        /* 3. מפענחים את גוף הקובץ בביט-פאקינג עד EOF */
        int currentByte;
        while ((currentByte = in.read()) != -1) {
            for (int bit = 7; bit >= 0; bit--)
                byteArray.add((byte) ((currentByte >> bit) & 1));
        }

        /* 4. מסיימים */
        this.matrix = byteArray;
    }

    private static byte[] toPrimitive(java.util.List<Byte> list) {
        byte[] arr = new byte[list.size()];
        for (int i = 0; i < list.size(); i++)
            arr[i] = list.get(i);      // auto-unboxing
        return arr;
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
