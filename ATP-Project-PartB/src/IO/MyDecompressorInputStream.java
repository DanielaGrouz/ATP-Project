package IO;

import java.io.IOException;
import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {
    InputStream in;
    public MyDecompressorInputStream(InputStream inputStream){
        this.in = inputStream;
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}
