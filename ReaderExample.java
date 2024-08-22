import java.io.FileReader;

public class ReaderExample {
    
    public static void main(String[] args) {

        try( FileReader fileReader = new FileReader("example.txt");) {
            int data;
            while ((data = fileReader.read()) != -1) {

                System.out.print((char) data);
            }

            
        } catch (Exception e) {
            // TODO: handle exception
        }
        
       
    }
}
