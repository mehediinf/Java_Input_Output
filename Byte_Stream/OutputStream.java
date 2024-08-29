import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class OutputStream {
    public static void main(String[] args) throws IOException {
        String data = "I am a student of UU";

        try (FileOutputStream fileOutputStream = new FileOutputStream("fileDB.txt")) {
            fileOutputStream.write(data.getBytes());
            
            


        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
        }


    }
}
