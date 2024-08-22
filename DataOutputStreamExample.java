import java.io.DataOutputStream;
import java.io.FileOutputStream;
public class DataOutputStreamExample {
    public static void main(String[] args) {

        try(DataOutputStream dos = new DataOutputStream(new FileOutputStream("data.txt"))){

            dos.writeInt(123);
            dos.writeDouble(45.67);

        }catch(Exception e){

        }
    }
}
