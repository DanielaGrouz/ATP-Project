package IO;

import java.io.IOException;
import java.io.OutputStream;

public class SimpleCompressorOutputStream extends OutputStream {
    OutputStream out;
    int bytesCount;
    int currentByte;
    int indexCount;

    public SimpleCompressorOutputStream(OutputStream outputStream){
        this.out = outputStream;
        bytesCount = 0;
        currentByte = 0;
        indexCount = 0;
    }
    private void flipByte(){
        if (currentByte == 0){
            currentByte = 1;
        }else{
            currentByte = 0;
        }
    }

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
