import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class FileChannelExample {
    public static void main(String[] args) {

        // ইউজার ইনপুট নেয়ার জন্য Scanner তৈরি করা হয়েছে
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter some text to write to the file: ");
        String userInput = scanner.nextLine();

        // ফাইলের পাথ নির্ধারণ করা হচ্ছে
        Path filePath = Path.of("input.txt");

        // ফাইল চ্যানেলের সাহায্যে ডেটা লেখা :
        /*
         * 
         * FileChannel.open(): ফাইল চ্যানেলটি খুলে এবং লেখার জন্য প্রস্তুত করে। 
         * StandardOpenOption.CREATE ফাইল তৈরি করে যদি এটি আগে না থাকে, এবং 
         * StandardOpenOption.WRITE ফাইল লেখার অনুমতি দেয়।
         * 
         */
        try (FileChannel writeChannel = FileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            // ব্যবহারকারীর ইনপুটটিকে একটি ByteBuffer এ রূপান্তর করে, যা FileChannel-এ লেখা হয়।
            ByteBuffer buffer = ByteBuffer.wrap(userInput.getBytes());

            // ফাইল চ্যানেলের মাধ্যমে ডেটা লেখা হচ্ছে
            writeChannel.write(buffer);
            System.out.println("Data written to file successfully.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ফাইল চ্যানেলের সাহায্যে ডেটা পড়া :

        /*
         * 
         * FileChannel.open(): ফাইলটি পড়ার জন্য FileChannel খোলা হয় (StandardOpenOption.READ)।
         * ByteBuffer.allocate(): একটি বাফার তৈরি করা হয় যেখানে ডেটা পড়া হবে। এখানে 1024 বাইট (1 কিলোবাইট) সাইজের বাফার তৈরি করা হয়েছে।
         * read(): ফাইল থেকে ডেটা পড়া হয় এবং বাফারে সংরক্ষণ করা হয়।
         * flip(): বাফারকে রিড মোডে রূপান্তর করা হয়, যাতে এটি পড়া যায়।
         * get(): বাফার থেকে ডেটা পড়ে একটি byte অ্যারে তে স্থানান্তর করা হয়, যা স্ট্রিংয়ে রূপান্তর করা হয় এবং প্রিন্ট করা হয়।
         * 
         */
        try (FileChannel readChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024); // ১ কিলোবাইটের বাফার
            int bytesRead = readChannel.read(buffer); // ডেটা পড়া হচ্ছে

            if (bytesRead > 0) {
                buffer.flip(); // বাফারকে রিড মোডে রূপান্তর করা হচ্ছে
                byte[] data = new byte[bytesRead];
                buffer.get(data); // বাফার থেকে ডেটা পড়া হচ্ছে
                System.out.println("Data read from file: " + new String(data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        scanner.close();
    }
}
