import java.io.BufferedWriter;
import java.io.FileWriter;
public class BufferedWriterExample {
    public static void main(String[] args) {
        
        String strData = "Hi, I am a Mehedi Hasan......A B C";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("example.txt"))){

            bw.write(strData);
            
        } catch (Exception e) {
            // TODO: handle exception
        }

        


    }
}
