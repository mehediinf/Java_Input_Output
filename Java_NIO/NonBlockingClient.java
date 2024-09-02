import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NonBlockingClient {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        try {
            // ক্লায়েন্ট চ্যানেল তৈরি ও নন-ব্লকিং মোডে কনফিগার করা
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false); // নন-ব্লকিং মোডে সেট করা
            client.connect(new InetSocketAddress("localhost", 5050));

            // সার্ভারের সাথে সংযোগ স্থাপন করা
            while (!client.finishConnect()) {
                // সার্ভার সাথে সংযোগ স্থাপন না হওয়া পর্যন্ত অপেক্ষা
                System.out.println("Connecting to server...");
                try {
                    TimeUnit.MILLISECONDS.sleep(100); // সংযোগ স্থাপনের জন্য অপেক্ষা
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }

            while (true) {
                
            
            // সার্ভারে বার্তা পাঠানো
            System.out.print("Client Sent: ");
            String message = in.nextLine();
            ByteBuffer buffer = ByteBuffer.allocate(256);
            buffer.put(message.getBytes());
            buffer.flip();

            while (buffer.hasRemaining()) {
                client.write(buffer);
            }
            

            // সার্ভার থেকে বার্তা গ্রহণ করা
            buffer.clear();
            int bytesRead = 0;
            while (bytesRead <= 0) {
                bytesRead = client.read(buffer);
                if (bytesRead == -1) {
                    System.out.println("Server closed connection.");
                    client.close();
                    return;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(100); // সার্ভার থেকে বার্তা আসার জন্য অপেক্ষা
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            buffer.flip();
            String response = new String(buffer.array(), 0, buffer.limit());
            System.out.println("Client Received : " + response);
        }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
