import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
public class DataInputStreamExample {
    public static void main(String[] args) throws FileNotFoundException, IOException {

        try (DataInputStream dis = new DataInputStream(new FileInputStream("data.txt"))){

            int intValue = dis.readInt();
            double doubleValue = dis.readDouble();
            System.out.println("Read Int: "+intValue);
            System.out.println("Read Double: "+doubleValue);

            
            
        }catch (Exception e) {
            // TODO: handle exception
        }
        
        
    }
}
