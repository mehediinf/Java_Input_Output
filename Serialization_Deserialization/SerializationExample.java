package Serialization_Deserialization;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
public class SerializationExample{
public static void main(String[] args) {
    
    Person person = new Person("Mehedi", 24,"CSE");
        
        try (FileOutputStream fileOut = new FileOutputStream("Serialization_Deserialization/person.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(person);
            System.out.println("Serialized data is saved in person.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

        Person person1 = null;
        try (FileInputStream fileIn = new FileInputStream("Serialization_Deserialization/person.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            person1 = (Person) in.readObject();
            
            System.out.println("Deserialized Person...");
            System.out.println("Name: " + person1.getName());
            System.out.println("Age: " + person1.getAge());
            System.out.println("Dept: "+person1.getDept());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


}
}


class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int age;
    private String dept;

    public Person(String name, int age,String dept) {
        this.name = name;
        this.age = age;
        this.dept = dept;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    public String getDept() {
        return dept;
    }
}