import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

public class BlockNonFullDupServer {
    private static final int PORT = 5050;
    private static final int BUFFER_SIZE = 256;

    public static void main(String[] args) throws IOException {
        try (Scanner in = new Scanner(System.in)) {
            // সিলেক্টর তৈরি
            Selector selector = Selector.open();

            // সার্ভার চ্যানেল সেটআপ
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress("localhost", PORT));
            serverSocket.configureBlocking(false);

            // সিলেক্টরে সার্ভার চ্যানেল রেজিস্টার করা
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server is listening on port " + PORT + "...");

            while (true) {
                selector.select(); // ইভেন্টের জন্য অপেক্ষা

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        // নতুন ক্লায়েন্ট সংযোগ গ্রহণ করা
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        System.out.println("Connected to client");
                    }

                    if (key.isReadable()) {
                        // ক্লায়েন্ট থেকে ডেটা পড়া
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
                        int bytesRead = client.read(buffer);

                        if (bytesRead == -1) {
                            key.cancel();
                            client.close();
                            System.out.println("Client disconnected");
                        } else if (bytesRead > 0) {
                            buffer.flip();
                            String receivedMessage = new String(buffer.array(), 0, buffer.limit());
                            System.out.println("Received from client: " + receivedMessage);

                            // ক্লায়েন্টে রেসপন্স প্রস্তুত করা
                            ByteBuffer responseBuffer = ByteBuffer.allocate(BUFFER_SIZE);
                            responseBuffer.put(("Server response: " + receivedMessage).getBytes());
                            responseBuffer.flip();

                            // ক্লায়েন্ট চ্যানেলে লেখার জন্য রেজিস্টার করা
                            key.interestOps(SelectionKey.OP_WRITE);
                            key.attach(responseBuffer); // রেসপন্স বাফার যুক্ত করা
                        }
                    }

                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment(); // যুক্ত বাফার পুনরুদ্ধার করা

                        if (buffer != null) {
                            while (buffer.hasRemaining()) {
                                client.write(buffer);
                            }

                            // লেখার কাজ শেষ হলে পুনরায় পড়ার জন্য রেজিস্টার করা
                            key.attach(null); // বাফার ক্লিয়ার করা
                            key.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
