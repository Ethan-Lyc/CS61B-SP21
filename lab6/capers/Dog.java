package capers;



import java.io.File;
import java.io.Serializable;
import static capers.Utils.*;

/** Represents a dog that can be serialized.
 * @author Yicheng Liao
*/
public class Dog implements Serializable {

    /** Folder that dogs live in. */
    static final String DOG = "dogs";
    static final File DOG_FOLDER = Utils.join(CapersRepository.CAPERS_FOLDER,DOG); //
                                         //      function in Utils)

    /** Age of dog. */
    private int age;
    /** Breed of dog. */
    private String breed;
    /** Name of dog. */
    private String name;

    /**
     * Creates a dog object with the specified parameters.
     * @param name Name of dog
     * @param breed Breed of dog
     * @param age Age of dog
     */
    public Dog(String name, String breed, int age) {
        this.age = age;
        this.breed = breed;
        this.name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
        File file = Utils.join(DOG_FOLDER,name);
        Dog d =null;
        if(!file.exists()){
            Utils.exitWithError("dog doesn't exist!!!!");
        }else{
            d = Utils.readObject(file,Dog.class);
        }
        return d;
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        age += 1;
        File file = Utils.join(DOG_FOLDER,name);
        Dog dog = fromFile(name);
        dog.age += 1;
        Utils.writeObject(file,dog);
        System.out.println(dog.toString());
        System.out.println("Happy birthday! Woof! Woof!");
    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog() {
        File file = Utils.join(Dog.DOG_FOLDER,name);
        if(file.exists()){
            Utils.exitWithError("狗已经存在了！！！");
        }else{
            Utils.writeObject(file,this);
            System.out.println(this.toString());
        }
    }

    @Override
    public String toString() {
        return String.format(
            "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
            name, breed, age);
    }

}
