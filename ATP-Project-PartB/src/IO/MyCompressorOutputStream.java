package IO;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
    private OutputStream out;
    private int indexCount;
    private int bitBuffer;
    private int bitsInBuffer;

    public MyCompressorOutputStream(OutputStream outputStream){
        this.out = outputStream;
        indexCount = 0;
        bitBuffer = 0;
        bitsInBuffer = 0;
    }

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

    @Override
    public void close() throws IOException {
        flush();
        out.close();
    }
}
