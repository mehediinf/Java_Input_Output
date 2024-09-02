import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Scanner;
import java.util.Iterator;

public class NonBlockingServer {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        // সিলেক্টর তৈরি
        /*
         * Selector হল Java NIO (Non-blocking I/O) এর একটি উপাদান যা একাধিক চ্যানেলের ইভেন্ট পরিচালনা করে। এটি নন-ব্লকিং সার্ভারের মূল ভিত্তি।
         */
        Selector selector = Selector.open();

        // সার্ভার চ্যানেল তৈরি এবং সেটআপ
        /*
         * একটি ServerSocketChannel খোলা হয় এবং লোকালহোস্টের পোর্ট ৫০৫০ তে বাঁধা হয়।
         * serverSocket.configureBlocking(false); - এটি সার্ভার চ্যানেলটিকে নন-ব্লকিং মোডে সেট করে, যাতে এটি ইভেন্টের জন্য ব্লক না করে।
         */
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 5050));
        serverSocket.configureBlocking(false); // নন-ব্লকিং মোডে সেট করা

        // সিলেক্টরে সার্ভার চ্যানেল রেজিস্টার করা OP_ACCEPT ইভেন্টের জন্য
        /*
         * সার্ভার চ্যানেলটি সিলেক্টরে রেজিস্টার করা হয় SelectionKey.OP_ACCEPT ইভেন্টের জন্য, যা নির্দেশ করে যে এটি নতুন সংযোগ গ্রহণ করবে।
         */
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server is listening on port 5050...");

        /*
         * 
         * সার্ভারটি একটি ইভেন্ট লুপে চলে যেখানে এটি সিলেক্টরের ইভেন্টের জন্য অপেক্ষা করে (selector.select()).
         * selectedKeys() - এটি ইভেন্টগুলির একটি সেট প্রদান করে যা ঘটেছে, যেমন ক্লায়েন্ট সংযোগ, ডেটা পড়া, ইত্যাদি।
         * 
         */

        while (true) {
            try {
                selector.select(); // ইভেন্টের জন্য অপেক্ষা করা
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    /*
                     * key.isAcceptable() চেক করে যে একটি নতুন সংযোগ গ্রহণযোগ্য কিনা।
                     * নতুন ক্লায়েন্টের সাথে সংযোগ গ্রহণ করা হয় এবং এটিকে নন-ব্লকিং মোডে সেট করা হয়।
                     * ক্লায়েন্টটি SelectionKey.OP_READ ইভেন্টের জন্য সিলেক্টরে রেজিস্টার করা হয়, যা নির্দেশ করে যে এটি পড়ার জন্য প্রস্তুত।
                     */

                    if (key.isAcceptable()) {
                        // নতুন ক্লায়েন্ট সংযোগ গ্রহণ করা
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ); // শুধুমাত্র পড়ার জন্য রেজিস্টার করা
                        System.out.println("Connected to client");
                    }

                    /*
                     * 
                     * key.isReadable() চেক করে যে ক্লায়েন্ট থেকে ডেটা পড়ার জন্য প্রস্তুত কিনা।
                     * client.read(buffer); - ক্লায়েন্ট থেকে ডেটা পড়ে বাফারে সংরক্ষণ করা হয়।
                     */

                    if (key.isReadable()) {
                        // ক্লায়েন্ট থেকে ডেটা পড়া
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        int bytesRead = client.read(buffer);

                        /*
                         * bytesRead == -1 চেক করা: যখন client.read(buffer) কল করা হয় এবং যদি bytesRead == -1 হয়,
                           এর মানে হলো ক্লায়েন্ট সংযোগ বন্ধ করে দিয়েছে বা ডেটা পাঠানো বন্ধ করেছে।
                         * key.cancel(); - এই লাইনে সিলেক্টর থেকে এই ক্লায়েন্টের চ্যানেল সম্পর্কিত কীগুলি বাতিল করা হয়, 
                           যাতে সার্ভার আর এই ক্লায়েন্টের থেকে কোনো ইভেন্টের জন্য অপেক্ষা না করে।
                         * client.close(); - ক্লায়েন্ট চ্যানেলটি বন্ধ করা হয়, যা মূলত ক্লায়েন্টের সাথে সার্ভারের সংযোগ সম্পূর্ণভাবে বন্ধ করে।
                         * System.out.println("Client disconnected"); - এটি একটি মেসেজ প্রিন্ট করে যে ক্লায়েন্ট সংযোগ বিচ্ছিন্ন হয়েছে।
                         * 
                         */

                        if (bytesRead == -1) {  //যখন ক্লায়েন্ট থেকে পড়া হয় এবং যদি bytesRead == -1 হয়, তাহলে ক্লায়েন্ট সংযোগ বন্ধ হয়ে যায়।
                            key.cancel(); // কীগুলি বাতিল করা
                            client.close();
                            System.out.println("Client disconnected");
                        } 

                            //ক্লায়েন্ট থেকে ডেটা পড়া এবং প্রক্রিয়াকরণ:

                            /*
                             * 
                             * else if (bytesRead > 0) চেক করা: এটি চেক করে যে ডেটা সফলভাবে পড়া হয়েছে কিনা।
                             * buffer.flip(); - বাফারের অবস্থানকে রিড মোডে পরিবর্তন করে, যাতে এটি পড়া শুরু করতে পারে।
                             * new String(buffer.array(), 0, buffer.limit()) - এটি বাফারে জমা থাকা ডেটাকে একটি স্ট্রিং হিসেবে কনভার্ট করে।
                             * System.out.println("Received from client: " + receivedMessage); - সার্ভার ক্লায়েন্ট থেকে পাওয়া বার্তাটি কনসোলে প্রিন্ট করে।
                             * 
                             */

                       else if (bytesRead > 0) {
                            buffer.flip();
                            String receivedMessage = new String(buffer.array(), 0, buffer.limit());
                            System.out.println("Received from client: " + receivedMessage);

                            // ক্লায়েন্টে ডেটা পাঠানোর জন্য রেজিস্টার করা
                            /*
                             * 
                             * buffer.clear(); - বাফারকে পরিষ্কার করে, যাতে এতে নতুন ডেটা লেখা যায়।
                             * buffer.put(("Server response: " + receivedMessage).getBytes()); - ক্লায়েন্টের পাঠানো বার্তাকে সার্ভারের একটি উত্তর হিসেবে প্রস্তুত 
                               করা হয় এবং বাফারে লেখা হয়।
                             * buffer.flip(); - আবার বাফারকে রিড মোডে সেট করা হয় যাতে এটি লেখার জন্য প্রস্তুত থাকে।
                             * key.interestOps(SelectionKey.OP_WRITE); - ক্লায়েন্ট চ্যানেলটিকে লেখার জন্য প্রস্তুত করে। 
                               পরবর্তীতে সিলেক্টর যখন লেখার ইভেন্ট সনাক্ত করবে, তখন এটি ক্লায়েন্টে সার্ভারের উত্তর পাঠাতে পারবে।
                             * 
                             */
                            buffer.clear();
                            buffer.put(("Server Receive: " + receivedMessage).getBytes());
                            buffer.flip();
                            key.interestOps(SelectionKey.OP_WRITE);
                        }
                    }

                    try {
                        
                    /*
                     * key.isWritable() চেক করে যে ক্লায়েন্টে ডেটা লেখার জন্য প্রস্তুত কিনা।
                     * বাফারে বার্তাটি রাখার পর সেটি ক্লায়েন্টে পাঠানো হয়।
                     */
                    if (key.isWritable()) {
                        // ক্লায়েন্টে ডেটা পাঠানো
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        System.out.print("Server sent: ");
                        String message = in.nextLine();
                        buffer.put(message.getBytes());
                        buffer.flip();

                        while (buffer.hasRemaining()) {
                            client.write(buffer);
                        }

                        // লেখার কাজ সম্পন্ন হলে, আবার পড়ার জন্য রেজিস্টার করা
                        key.interestOps(SelectionKey.OP_READ);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
