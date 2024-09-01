package Serialization_Deserialization;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DeserializationExample {
    public static void main(String[] args) throws IOException {
        
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("person.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            Animal animal = new Animal("Hasan", 25);
            oos.writeObject(animal);


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        Animal animal1 = null;
        try (FileInputStream fileIn = new FileInputStream("person.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
                animal1 = (Animal) in.readObject();

                animal1.display();

             }
        catch(Exception e){

        }
    }
}

/**
 * Animal
 */
class Animal implements Serializable{

    private String name;
    private int age;

    public Animal(String name,int age){

        this.name = name;
        this.age = age;
    }

    public void display(){

        System.out.println("Name: "+name);
        System.out.println("Age: "+age);
    }
    



}
