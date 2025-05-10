package IO;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
    OutputStream out;
    public MyCompressorOutputStream(OutputStream outputStream){
        this.out = outputStream;
    }
    @Override
    public void write(int b) throws IOException {

    }
}
