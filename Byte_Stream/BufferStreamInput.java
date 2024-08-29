import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class BufferStreamInput {
    public static void main(String[] args) {
        int data;

        try {
            FileInputStream fileInputStream = new FileInputStream("example.txt");
            BufferedInputStream bufferInputStream = new BufferedInputStream(fileInputStream);
            try {
                while ((data=bufferInputStream.read()) != -1) {
                    System.out.print((char)data);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("\n");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
