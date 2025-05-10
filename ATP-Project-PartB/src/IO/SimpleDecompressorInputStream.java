package IO;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SimpleDecompressorInputStream extends InputStream {
    InputStream in;
    ArrayList<Byte> matrix;
    int index;
    public SimpleDecompressorInputStream(InputStream inputStream) throws IOException {
        this.in = inputStream;
        index = 0;
        decompress();
    }
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
    @Override
    public int read() throws IOException {
        if (index < matrix.size()){
            return (int) matrix.get(index++);
        }
        return -1;
    }
}
