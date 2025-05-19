package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A custom OutputStream that compresses binary data.
 * <p>
 * The first 20 bytes are written as-is (usually a header).
 * The rest of the data is compressed by packing 8 bits into one byte.
 */
public class MyCompressorOutputStream extends OutputStream {
    private OutputStream out;
    private int indexCount;
    private int bitBuffer;
    private int bitsInBuffer;

    /**
     * Creates a new compressor output stream that wraps another OutputStream.
     *
     * @param outputStream the stream to write the compressed data to
     */
    public MyCompressorOutputStream(OutputStream outputStream){
        this.out = outputStream;
        indexCount = 0;
        bitBuffer = 0;
        bitsInBuffer = 0;
    }

    /**
     * Writes one byte to the stream. The first 20 bytes are written directly.
     * After that, bits are packed into bytes (8 bits per byte).
     *
     * @param b the byte to write (only the lowest bit is used after the first 20 bytes)
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void write(int b) throws IOException {
        if (indexCount < 20){ // first 20 bytes are for start(i,j),end(i,j), number of rows
            out.write(b);
            indexCount++;
        }
        else {
            //shift current bits left and add the new bit
            bitBuffer = (bitBuffer << 1) + b;
            bitsInBuffer++;
            //8 bits compressed into one byte
            if (bitsInBuffer == 8) {
                out.write(bitBuffer);
                bitBuffer = 0;
                bitsInBuffer = 0;
            }

        }
    }

    /**
     * Flushes any remaining bits in the buffer and the underlying stream.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void flush() throws IOException {
        //check if there are still bits left in the buffer that haven't been written
        if (bitsInBuffer > 0) {
            //shift the remaining bits to the left to fill the byte
            bitBuffer = bitBuffer << (8 - bitsInBuffer);
            out.write(bitBuffer);
        }
        out.flush();
    }

    /**
     * Finishes writing and closes the stream.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        flush();
        out.close();
    }
}
