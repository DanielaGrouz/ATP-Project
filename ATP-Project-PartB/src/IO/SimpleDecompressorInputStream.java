package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A custom InputStream that decompresses data written by SimpleCompressorOutputStream.
 * <p>
 * Reads the first 20 bytes as a header, then uses run-length decoding to restore the original data.
 */
public class SimpleDecompressorInputStream extends InputStream {
    InputStream in;
    ArrayList<Byte> matrix;
    int index;

    /**
     * Creates a new SimpleDecompressorInputStream that wraps a compressed input stream.
     *
     * @param inputStream the compressed input stream
     * @throws IOException if an I/O error occurs while reading
     */
    public SimpleDecompressorInputStream(InputStream inputStream) throws IOException {
        this.in = inputStream;
        index = 0;
        decompress();
    }

    /**
     * Decompresses the data using run-length decoding.
     * The first 20 bytes are read directly, then the remaining data is expanded based on counts.
     *
     * @throws IOException if an I/O error occurs
     */
    private void decompress() throws IOException {
        final int HEADER_SIZE = 4 * 5;
        ArrayList<Byte> byteArray = new ArrayList<>(HEADER_SIZE);
        for(int i=0;i<HEADER_SIZE;i++){
            byteArray.add((byte) in.read());
        }
        byte currentByte = 0;
        while (true){
            int currentByteCount = in.read();
            if (currentByteCount == -1){
                break;
            }
            for (int i =0;i < currentByteCount; i++){
                byteArray.add(currentByte);
            }

            if (currentByte == 0){
                currentByte = 1;
            }else{
                currentByte = 0;
            }
        }
        matrix = byteArray;
    }

    /**
     * Reads the next decompressed byte.
     *
     * @return the next byte, or -1 if end of stream
     * @throws IOException if an I/O error occurs
     */
    @Override
    public int read() throws IOException {
        if (index < matrix.size()){
            return (int) matrix.get(index++);
        }
        return -1;
    }
}
