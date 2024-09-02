import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BlockingNonFullDupClient {
    private static final int BUFFER_SIZE = 256;
    private static final int CONNECT_TIMEOUT_MS = 100;
    private static final int READ_TIMEOUT_MS = 100;

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            // ক্লায়েন্ট চ্যানেল তৈরি এবং কনফিগার করা
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);
            client.connect(new InetSocketAddress("localhost", 5050));

            // সংযোগ প্রতিষ্ঠিত হওয়া পর্যন্ত অপেক্ষা করা
            while (!client.finishConnect()) {
                System.out.println("Connecting to server...");
                try {
                    TimeUnit.MILLISECONDS.sleep(CONNECT_TIMEOUT_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Connection interrupted.");
                    return;
                }
            }

            while (true) {
                // সার্ভারে বার্তা পাঠানো
                if (in.hasNextLine()) {
                    String message = in.nextLine();
                    ByteBuffer writeBuffer = ByteBuffer.allocate(BUFFER_SIZE);
                    writeBuffer.clear();
                    writeBuffer.put(message.getBytes());
                    writeBuffer.flip();

                    while (writeBuffer.hasRemaining()) {
                        client.write(writeBuffer);
                    }
                    System.out.println("Sent to server: " + message);
              
                } else {
                    System.out.println("No input provided.");
                    break;
                }


                // সার্ভার থেকে উত্তর গ্রহণ করা
                ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
                int bytesRead;
                while (true) {
                    readBuffer.clear(); // Ensure buffer is clear before reading
                    bytesRead = client.read(readBuffer);
                    if (bytesRead == -1) {
                        System.out.println("Server closed connection.");
                        client.close();
                        return;
                    }
                    if (bytesRead > 0) {
                        break;
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(READ_TIMEOUT_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Read interrupted.");
                        client.close();
                        return;
                    }
                }

                // সার্ভার থেকে প্রাপ্ত উত্তর প্রক্রিয়া করা
                readBuffer.flip();
                String response = new String(readBuffer.array(), 0, readBuffer.limit());
                System.out.println("Received from server: " + response);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
