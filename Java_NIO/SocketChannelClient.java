import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelClient {
    public static void main(String[] args) {
        try {
            //সার্ভারের সাথে সংযোগ স্থাপন:
            /*
             * 
             * InetSocketAddress: এটি localhost এবং 8080 পোর্ট ব্যবহার করে সার্ভারের একটি ঠিকানা তৈরি করে। এখানে localhost সার্ভারের হোস্টনাম এবং 
             * 8080 হল সার্ভারের পোর্ট নম্বর।
             * SocketChannel.open(address): এটি SocketChannel খুলে এবং সার্ভারের সাথে সংযোগ স্থাপন করে।
             * client.configureBlocking(false): চ্যানেলটি নন-ব্লকিং মোডে সেট করা হয়, যার মানে এটি অবিলম্বে রিটার্ন করবে এবং ব্লক হবে না। 
             *  অর্থাৎ, আই/ও অপারেশনগুলি ব্লক না করেই চলতে থাকবে।
             * 
             */
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            SocketChannel client = SocketChannel.open(address);
            client.configureBlocking(false);

            //বার্তা তৈরি এবং পাঠানো:
            /*
             * message: ক্লায়েন্ট যে বার্তাটি সার্ভারে পাঠাবে, সেটি একটি স্ট্রিং হিসাবে নির্ধারণ করা হয়েছে ("Hello, Server!")।
             * ByteBuffer.wrap(message.getBytes()): স্ট্রিং বার্তাটিকে byte অ্যারে তে রূপান্তর করে এবং একটি ByteBuffer তে মোড়ানো হয়। 
                এটি ডেটা সংরক্ষণ করার জন্য ব্যবহৃত একটি কনটেইনার যা SocketChannel-এ লেখা যাবে।
             * client.write(buffer): এই লাইনটি SocketChannel-এ ByteBuffer এর ডেটা (বার্তা) পাঠায়।
             * buffer.clear(): বাফারটি পরিষ্কার করা হয় যাতে এটি পুনরায় ব্যবহার করা যায়।
             * 
             */
            String message = "Hello , Server!";
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            client.write(buffer);
            buffer.clear();

            //বার্তা পাঠানোর পর ক্লায়েন্ট চ্যানেল বন্ধ করা:
            /*
             * 
             * System.out.println("Sent Msg: " + message);: সার্ভারে পাঠানো বার্তাটি কনসোলে প্রিন্ট করে।
             * client.close(): SocketChannel বন্ধ করা হয় যাতে ক্লায়েন্ট এবং সার্ভারের মধ্যে সংযোগটি বন্ধ হয়।
             * 
             */
            System.out.println("Sent Msg: " + message);
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
