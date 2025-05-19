package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A custom OutputStream that compresses binary data using run-length encoding.
 * <p>
 * The first 20 bytes are written as-is (usually a header).
 * After that, sequences of 0s and 1s are counted and compressed as byte values.
 */
public class SimpleCompressorOutputStream extends OutputStream {
    OutputStream out;
    int bytesCount;
    int currentByte;
    int indexCount;

    /**
     * Creates a new SimpleCompressorOutputStream that wraps another OutputStream.
     *
     * @param outputStream the stream to write compressed data to
     */
    public SimpleCompressorOutputStream(OutputStream outputStream){
        this.out = outputStream;
        bytesCount = 0;
        currentByte = 0;
        indexCount = 0;
    }

    /**
     * Switches currentByte between 0 and 1.
     */
    private void flipByte(){
        if (currentByte == 0){
            currentByte = 1;
        }else{
            currentByte = 0;
        }
    }

    /**
     * Writes a byte to the stream. The first 20 bytes are written directly.
     * After that, it compresses sequences of the same bit using run-length encoding.
     *
     * @param b the byte to write (only 0s and 1s expected after the header)
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void write(int b) throws IOException {
        if (indexCount < 20){ // first 20 bytes are for start(i,j),end(i,j), number of rows
            out.write(b);
            indexCount++;
        }
        else{
            if (bytesCount == 256){
                out.write(bytesCount-1);
                out.write(0);
                bytesCount = 1;
            }
            if (b == currentByte) {
                bytesCount++;
            }else{
                out.write(bytesCount);
                flipByte();
                bytesCount = 1;
            }
        }
    }
}
