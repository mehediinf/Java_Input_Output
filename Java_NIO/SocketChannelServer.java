import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SocketChannelServer {
    public static void main(String[] args) {

        //এই কোডটি একাধিক ক্লায়েন্টের সাথে একসাথে নন-ব্লকিং মোডে কাজ করার ক্ষমতা রাখে।

        /*
         * প্রোগ্রামের কাজের ধাপ:
            *সার্ভার শুরু হয় এবং নির্দিষ্ট পোর্টে নতুন সংযোগ গ্রহণের জন্য অপেক্ষা করে।
            *নতুন ক্লায়েন্ট সংযোগ এলে isAcceptable ব্লক কাজ করে এবং চ্যানেলটি পড়ার জন্য প্রস্তুত হয়।
            *যখন ক্লায়েন্ট থেকে ডেটা পাওয়া যায়, তখন isReadable ব্লক কাজ করে এবং বার্তাটি পড়ে প্রিন্ট করে।
            *ক্লায়েন্ট সংযোগ বিচ্ছিন্ন হলে, চ্যানেলটি বন্ধ করে দেয়।
         * 
         */



        try {
            // সার্ভার সাইড সেক্টর তৈরি

            /*Selector: এটি একটি Selector অবজেক্ট তৈরি করে যা নন-ব্লকিং আই/ও অপারেশনের জন্য ব্যবহৃত হয়। 
            এটি একাধিক চ্যানেলের আই/ও অপারেশনগুলির ইভেন্টগুলি পর্যবেক্ষণ করে।
            ServerSocketChannel: একটি ServerSocketChannel তৈরি করে যা সার্ভার সাইডে ক্লায়েন্ট সংযোগ গ্রহণ করার জন্য ব্যবহৃত হয়।
            bind() Method: bind মেথডটি ServerSocketChannel কে নির্দিষ্ট হোস্ট (এখানে localhost) এবং পোর্ট (এখানে 8080) এ সংযোগ করে।
            configureBlocking(false): চ্যানেলটি নন-ব্লকিং মোডে সেট করা হচ্ছে যাতে সার্ভার আই/ও অপারেশনগুলি ব্লক না করে। */

            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
            serverSocketChannel.configureBlocking(false);

            // চ্যানেল রেজিস্টার করা
            /*
            register() Method: ServerSocketChannel কে Selector এর সাথে রেজিস্টার করা হচ্ছে, 
            এবং SelectionKey.OP_ACCEPT দিয়ে সাইন করা হচ্ছে যাতে সার্ভার নতুন ক্লায়েন্ট সংযোগগুলি গ্রহণ করতে পারে। 
            */
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server Start ..... ");

            //সার্ভার লুপ:
            /*
            selector.select(): এই মেথডটি Selector-এ রেজিস্টারকৃত চ্যানেলগুলির জন্য ইভেন্টগুলি চেক করে এবং যদি কোনও ইভেন্ট ঘটে থাকে (যেমন ক্লায়েন্ট সংযোগ,
            পড়া বা লেখা), এটি চেক করে। 
            selectedKeys(): selectedKeys() মেথডটি সেই চাবিগুলির একটি সেট প্রদান করে যেগুলির জন্য ইভেন্টগুলি ঘটেছে। 
             
             */

            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                //ইভেন্ট হ্যান্ডলিং:

                /*
                 * key.isAcceptable(): যদি SelectionKey.OP_ACCEPT ইভেন্ট ঘটে (অর্থাৎ, নতুন ক্লায়েন্ট সংযোগ), তখন এটি হ্যান্ডেল করে। 
                 * 
                 * নতুন SocketChannel গ্রহণ করা হয় এবং এটি নন-ব্লকিং মোডে সেট করা হয়, তারপর এটি Selector-এ OP_READ ইভেন্টের জন্য রেজিস্টার করা হয় 
                 * যাতে ক্লায়েন্টের কাছ থেকে ডেটা পড়া যায়।
                 * 
                 *key.isReadable(): যদি SelectionKey.OP_READ ইভেন্ট ঘটে (অর্থাৎ, ক্লায়েন্ট থেকে পড়ার জন্য ডেটা উপলব্ধ), তখন এটি হ্যান্ডেল করে।

                 *client.read(buffer): ক্লায়েন্ট চ্যানেল থেকে ডেটা পড়ে ByteBuffer-এ সংরক্ষণ করে।

                 *buffer.flip(): ডেটা পড়ার পরে, ByteBuffer প্রস্তুত করা হয় যাতে এটি পড়া যায়।

                 *message: বাফারের ডেটা স্ট্রিংয়ে কনভার্ট করা হয় এবং প্রাপ্ত বার্তাটি প্রিন্ট করা হয়।
                 * 
                 */
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        System.out.println("Client is a Connect");
                    }

                    if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(256);
                        int bytesRead = client.read(buffer);

                        if (bytesRead == -1) {
                            client.close();
                            System.out.println("Client is a Disconnect");
                        } else {
                            buffer.flip();
                            String message = new String(buffer.array()).trim();
                            System.out.println("Recive Msg: " + message);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
