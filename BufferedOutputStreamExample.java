import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
public class BufferedOutputStreamExample {
    public static void main(String[] args) throws IOException {

        String data = "Allah, You are a best.";
       
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("example.txt"))){
            
            bos.write(data.getBytes());

          

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }
}
