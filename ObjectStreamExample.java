import java.io.*;

// Step 1: একটি Serializable ক্লাস তৈরি করুন
class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    int roll;

    public Student(String name, int roll) {
        this.name = name;
        this.roll = roll;
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', roll=" + roll + "}";
    }
}

public class ObjectStreamExample {
    public static void main(String[] args) {
        System.out.println("\n");
        // Step 2: একটি Student অবজেক্ট তৈরি করুন
        Student student = new Student("Rahim", 101);

        // Step 3: অবজেক্ট ফাইলের মধ্যে লেখার জন্য ObjectOutputStream ব্যবহার করুন
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("fileDB.txt"))) {
            oos.writeObject(student);
            System.out.println("The object is saved in the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 4: ObjectInputStream ব্যবহার করে অবজেক্ট ফাইল থেকে পড়ুন
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("fileDB.txt"))) {
            Student savedStudent = (Student) ois.readObject();
            System.out.println("Read Object: " + savedStudent);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
