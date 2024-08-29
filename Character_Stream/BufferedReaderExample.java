import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
public class BufferedReaderExample {
    public static void main(String[] args) throws IOException {
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("example.txt"))){
            
            String line;

            while ((line=bufferedReader.readLine()) !=null) {
                System.out.println(line);
            }


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
