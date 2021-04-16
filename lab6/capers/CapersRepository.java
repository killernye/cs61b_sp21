package capers;

//import edu.princeton.cs.algs4.ST;

import java.io.File;
import static capers.Utils.*;

/** A repository for Capers 
 * @author Nye
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = new File(".capers");

    /** File where story writes. */
    static final File STORY = new File(".capers/story.txt");

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence() {
        if (!CWD.exists())
            CWD.mkdir();
        if (!CAPERS_FOLDER.exists())
            CAPERS_FOLDER.mkdir();
        if (!STORY.exists())
            Utils.writeContents(STORY);
        if (!Dog.DOG_FOLDER.exists())
            Dog.DOG_FOLDER.mkdir();
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        String story = Utils.readContentsAsString(STORY);
        StringBuffer sb = new StringBuffer(story);
        sb.append(text + '\n');
        Utils.writeContents(STORY, sb.toString());
        System.out.println(sb.toString());
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        Dog d = new Dog(name, breed, age);
        d.saveDog();
        System.out.println(d.toString());
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        Dog birthdayDog = Dog.fromFile(name);
        birthdayDog.haveBirthday();
        birthdayDog.saveDog();
    }
}
