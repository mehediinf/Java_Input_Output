import java.io.FileWriter;
import java.io.IOException;
public class WriterExample {
    public static void main(String[] args) {

        String data = "Allah is a my lord.";
        
        
        try (FileWriter fileWriter = new FileWriter("fileDB.txt")){
            

            fileWriter.write(data);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
