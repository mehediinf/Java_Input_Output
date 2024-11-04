import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class NIOExample {
    public static void main(String[] args) {
        try {
            Path path = Path.of("example.txt");
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = fileChannel.read(buffer);

            while (bytesRead != -1) {
                buffer.flip(); // Prepare buffer for reading
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear(); // Prepare buffer for writing
                bytesRead = fileChannel.read(buffer);  //byteRead = -1 
            }
            fileChannel.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
     * ব্যাখ্যা:
Import Statements:

প্রথমে, java.io.IOException, java.nio.ByteBuffer, java.nio.channels.FileChannel, java.nio.file.Path, এবং
java.nio.file.StandardOpenOption ইম্পোর্ট করা হয়েছে। এই ক্লাসগুলি ফাইল চ্যানেল এবং বাফার ব্যবহারের জন্য প্রয়োজন।

Main Method:

public static void main(String[] args) মেথড হল প্রোগ্রামের এন্ট্রি পয়েন্ট।
Path তৈরি:

Path path = Path.of("example.txt"); এখানে, example.txt নামে একটি ফাইলের পথ (Path) তৈরি করা হয়েছে। এই ফাইলটি থেকে ডেটা পড়া হবে।
FileChannel খুলুন:

FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
এই লাইনটি ফাইলটি পড়ার জন্য একটি FileChannel খুলেছে। StandardOpenOption.READ নির্দেশনা দেয় যে চ্যানেলটি শুধুমাত্র পড়ার জন্য খোলা হবে।
Buffer তৈরি:

ByteBuffer buffer = ByteBuffer.allocate(1024);
এখানে, একটি ByteBuffer তৈরি করা হয়েছে, যা 1024 বাইট পর্যন্ত ডেটা ধরে রাখতে সক্ষম। বাফার হল এমন একটি মেমোরি এলাকা যেখানে চ্যানেল থেকে ডেটা পড়া হয়।
ডেটা পড়া:

int bytesRead = fileChannel.read(buffer);
ফাইল চ্যানেলটি বাফারে ডেটা পড়ে, এবং কত বাইট পড়া হয়েছে তা bytesRead ভেরিয়েবলে সংরক্ষিত হয়। যদি কোনও বাইট না পড়া যায় তবে bytesRead এর মান -1 হবে।
While লুপ:

while (bytesRead != -1) চেক করা হয় যতক্ষণ পর্যন্ত bytesRead -1 না হয়, অর্থাৎ ডেটা পড়া শেষ না হয়।
buffer.flip();
এই লাইনটি বাফারকে পড়ার জন্য প্রস্তুত করে, যা লেখার অবস্থান থেকে পড়ার অবস্থানে বাফারকে পরিবর্তন করে।
while (buffer.hasRemaining())
যতক্ষণ বাফারে পড়ার জন্য ডেটা আছে, ততক্ষণ buffer.get() ব্যবহার করে প্রতিটি চরিত্র পড়া হয় এবং প্রিন্ট করা হয়।
buffer.clear();
বাফারকে পুনরায় লেখার জন্য প্রস্তুত করা হয়, যাতে পরবর্তী বার চ্যানেল থেকে ডেটা পড়া যায়।
FileChannel বন্ধ করা:

fileChannel.close();
ফাইল চ্যানেলটি বন্ধ করে দেওয়া হয় যখন সমস্ত ডেটা পড়া শেষ হয়।
Exception Handling:

catch (IOException e)
যদি কোনো I/O Exception ঘটে, তাহলে e.printStackTrace() এর মাধ্যমে তা প্রিন্ট করা হয়।
উপসংহার:
এই কোডটি একটি নির্দিষ্ট ফাইল থেকে ডেটা পড়ে এবং কনসোলে প্রিন্ট করে। এটি NIO এর মূল উপাদানসমূহ যেমন FileChannel, ByteBuffer, এবং Path এর ব্যবহার দেখায়।
     * 
     * 
     * 
     * 
     * 
     * 
     */
}
