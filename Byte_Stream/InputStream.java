import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class InputStream {
    
    public static void main(String[] args) {
        
        try {
            FileInputStream fileInputStream = new FileInputStream("fileDB.txt");
            
            int data;
            try {
                while((data = fileInputStream.read()) != -1){
                    System.out.print((char) data);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
